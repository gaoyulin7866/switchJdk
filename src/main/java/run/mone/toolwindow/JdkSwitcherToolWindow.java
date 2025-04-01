package run.mone.toolwindow;

import com.intellij.ProjectTopics;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootEvent;
import com.intellij.openapi.roots.ModuleRootListener;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import org.jetbrains.annotations.NotNull;

public class JdkSwitcherToolWindow implements StatusBarWidget {
    private final Project project;
    private JdkSwitcherPresentation presentation;

    public JdkSwitcherToolWindow(Project project) {
        this.project = project;
    }

    @Override
    @NotNull
    public String ID() {
        return "JdkSwitcher";
    }

    @Override
    public WidgetPresentation getPresentation() {
        if (presentation == null) {
            presentation = new JdkSwitcherPresentation(project);
        }
        return presentation;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        // 添加 SDK 变化监听器
        project.getMessageBus().connect().subscribe(ProjectJdkTable.JDK_TABLE_TOPIC,
                new ProjectJdkTable.Listener() {
                    @Override
                    public void jdkAdded(@NotNull Sdk jdk) {
                        statusBar.updateWidget(ID());
                    }

                    @Override
                    public void jdkRemoved(@NotNull Sdk jdk) {
                        statusBar.updateWidget(ID());
                    }

                    @Override
                    public void jdkNameChanged(@NotNull Sdk jdk, @NotNull String previousName) {
                        statusBar.updateWidget(ID());
                    }
                });

        // 监听项目根变化（包括 SDK 变化）
        project.getMessageBus().connect().subscribe(ProjectTopics.PROJECT_ROOTS, 
            new ModuleRootListener() {
                @Override
                public void rootsChanged(@NotNull ModuleRootEvent event) {
                    statusBar.updateWidget(ID());
                }
            });
    }

    @Override
    public void dispose() {
        presentation = null;
    }
}