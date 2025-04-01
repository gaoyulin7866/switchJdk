package run.mone.toolwindow;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.externalSystem.service.execution.ExternalSystemJdkUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.JavaSdkImpl;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.StatusBar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.execution.MavenRunner;
import org.jetbrains.idea.maven.execution.MavenRunnerSettings;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.jetbrains.idea.maven.project.MavenImportingSettings;
import run.mone.config.SwitchJdkConfig;

import java.util.Map;

public class JdkSwitcherPopup {
    private final Project project;
    private final Map<String, String> jdkPaths;

    public JdkSwitcherPopup(Project project) {
        this.project = project;
        this.jdkPaths = SwitchJdkConfig.getInstance().jdkPaths;
    }

    public ListPopup createPopup() {
        DefaultActionGroup group = new DefaultActionGroup();

        if (jdkPaths.isEmpty()) {
            // 如果没有配置 JDK 路径，显示当前项目的 SDK
            Sdk currentSdk = ProjectRootManager.getInstance(project).getProjectSdk();
            String sdkName = currentSdk != null ? currentSdk.getName() : "No SDK configured";
            group.add(new AnAction("Current SDK: " + sdkName) {
                @Override
                public void actionPerformed(@NotNull AnActionEvent e) {
                    // 显示提示消息
                    Messages.showMessageDialog(
                            project,
                            "请在Settings > Other Settings中配置Switch JDK",
                            "提示",
                            Messages.getInformationIcon()
                    );
                }
            });
        } else {
            for (Map.Entry<String, String> entry : jdkPaths.entrySet()) {
                group.add(new SwitchJdkAction(entry.getKey(), entry.getValue()));
            }
        }

        return JBPopupFactory.getInstance()
                .createActionGroupPopup(
                        "Switch JDK",
                        group,
                        DataManager.getInstance().getDataContext(),
                        JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                        true
                );
    }

    private class SwitchJdkAction extends AnAction {
        private final String jdkName;
        private final String jdkPath;

        public SwitchJdkAction(String jdkName, String jdkPath) {
            super("Switch to " + jdkName);
            this.jdkName = jdkName;
            this.jdkPath = jdkPath;
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            ApplicationManager.getApplication().invokeLater(() -> {
                WriteAction.run(() -> {
                    ProjectJdkTable jdkTable = ProjectJdkTable.getInstance();
                    ProjectRootManager rootManager = ProjectRootManager.getInstance(project);

                    // 先检查是否已存在相同路径的 SDK
                    Sdk existingSdk = jdkTable.findJdk(jdkName);

                    if (existingSdk != null) {
                        jdkTable.removeJdk(existingSdk);
                    }

                    // 创建新的 SDK
                    JavaSdkImpl javaSdk = (JavaSdkImpl) JavaSdkImpl.getInstance();
                    Sdk newJdk = javaSdk.createJdk(jdkName, jdkPath);

                    // 添加到 JDK 表中
                    jdkTable.addJdk(newJdk);

                    // 设置为项目 SDK
                    rootManager.setProjectSdk(newJdk);

                    // 设置Maven Importing的SDK为项目默认
                    MavenProjectsManager mavenProjectsManager = MavenProjectsManager.getInstance(project);
                    MavenImportingSettings importingSettings = mavenProjectsManager.getImportingSettings();
                    importingSettings.setJdkForImporter(ExternalSystemJdkUtil.USE_PROJECT_JDK);

                    // 设置Runner的JDK
                    MavenRunner mavenRunner = MavenRunner.getInstance(project);
                    MavenRunnerSettings runnerSettings = mavenRunner.getState();
                    runnerSettings.setJreName(ExternalSystemJdkUtil.USE_PROJECT_JDK);

                    // 更新状态栏
                    ApplicationManager.getApplication().invokeLater(() -> {
                        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
                        if (statusBar != null) {
                            statusBar.updateWidget("JdkSwitcher");
                        }
                    });
                });
            });
        }

        @Override
        public void update(@NotNull AnActionEvent e) {
            e.getPresentation().setEnabled(true);
        }
    }
}