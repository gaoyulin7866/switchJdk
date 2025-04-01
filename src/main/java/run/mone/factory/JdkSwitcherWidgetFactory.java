package run.mone.factory;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import org.jetbrains.annotations.NotNull;
import run.mone.toolwindow.JdkSwitcherToolWindow;

public class JdkSwitcherWidgetFactory implements StatusBarWidgetFactory {
    @Override
    @NotNull
    public String getId() {
        return "JdkSwitcher";
    }

    @Override
    @NotNull
    public String getDisplayName() {
        return "JDK Switcher";
    }

    @Override
    public boolean isAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public StatusBarWidget createWidget(@NotNull Project project) {
        return new JdkSwitcherToolWindow(project);
    }

    @Override
    public void disposeWidget(@NotNull StatusBarWidget widget) {
        widget.dispose();
    }

    @Override
    public boolean canBeEnabledOn(@NotNull StatusBar statusBar) {
        return true;
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }
}