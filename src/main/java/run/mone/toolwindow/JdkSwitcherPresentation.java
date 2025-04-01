package run.mone.toolwindow;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;

public class JdkSwitcherPresentation implements StatusBarWidget.MultipleTextValuesPresentation {
    private final Project project;

    public JdkSwitcherPresentation(Project project) {
        this.project = project;
    }

    @NotNull
    @Override
    public String getTooltipText() {
        return "Click to switch JDK version";
    }

    @Nullable
    @Override
    public ListPopup getPopupStep() {
        return new JdkSwitcherPopup(project).createPopup();
    }

    @Override
    public @Nullable Consumer<MouseEvent> getClickConsumer() {
        // 返回 null 因为我们使用 getPopupStep() 来处理点击事件
        return null;
    }

    @NotNull
    @Override
    public String getSelectedValue() {
        ProjectRootManager rootManager = ProjectRootManager.getInstance(project);
        Sdk projectSdk = rootManager.getProjectSdk();
        return projectSdk != null ? projectSdk.getVersionString() : "No JDK";
    }

    @NotNull
    @Override
    public String getMaxValue() {
        return "JDK 21.0.2";
    }
}