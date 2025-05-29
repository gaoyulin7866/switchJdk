package run.mone.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.application.ApplicationManager;

import java.util.HashMap;
import java.util.Map;

@State(name = "SwitchJdkConfig", storages = @Storage("switchJdkConfig.xml"))
public class SwitchJdkConfig implements PersistentStateComponent<SwitchJdkConfig> {
    public Map<String, String> jdkPaths = new HashMap<>();
    public boolean switchSystemJava = false;

    @Nullable
    @Override
    public SwitchJdkConfig getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SwitchJdkConfig state) {
        this.jdkPaths = state.jdkPaths;
        this.switchSystemJava = state.switchSystemJava;
    }

    public static SwitchJdkConfig getInstance() {
        return ApplicationManager.getApplication().getService(SwitchJdkConfig.class);
    }
} 