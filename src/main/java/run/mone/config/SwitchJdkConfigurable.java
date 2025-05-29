package run.mone.config;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SwitchJdkConfigurable implements Configurable {
    private JPanel panel;
    private JTextArea jdkPathsArea;
    private JCheckBox switchSystemJavaCheckBox;
    private boolean switchSystemJava = false;

    @Nls
    @Override
    public String getDisplayName() {
        return "Switch JDK";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        panel = new JPanel(new BorderLayout());

        // 添加提示标签
        JLabel label = new JLabel("设置成功后需重启IDEA");
        // 添加复选框
        switchSystemJavaCheckBox = new JCheckBox("同时切换系统java版本", false);
        switchSystemJavaCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                switchSystemJava = switchSystemJavaCheckBox.isSelected();
            }
        });
        panel.add(switchSystemJavaCheckBox, BorderLayout.SOUTH);
        
        label.setBorder(new EmptyBorder(0, 0, 24, 0));
        label.setForeground(new Color(255, 165, 0));
        panel.add(label, BorderLayout.NORTH);

        jdkPathsArea = new JTextArea(10, 50);
        panel.add(new JScrollPane(jdkPathsArea), BorderLayout.CENTER);

        return panel;
    }

    @Override
    public boolean isModified() {
        SwitchJdkConfig config = SwitchJdkConfig.getInstance();
        Map<String, String> currentPaths = parseTextArea();
        boolean pathsModified = !config.jdkPaths.equals(currentPaths);
        boolean switchModified = config.switchSystemJava != switchSystemJava;
        return pathsModified || switchModified;
    }

    @Override
    public void apply() {
        SwitchJdkConfig config = SwitchJdkConfig.getInstance();
        config.jdkPaths = parseTextArea();
        config.switchSystemJava = switchSystemJava;
    }

    @Override
    public void reset() {
        SwitchJdkConfig config = SwitchJdkConfig.getInstance();
        jdkPathsArea.setText(config.jdkPaths.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("\n")));
        switchSystemJavaCheckBox.setSelected(config.switchSystemJava);
    }

    private Map<String, String> parseTextArea() {
        return jdkPathsArea.getText().lines()
                .map(line -> line.split("=", 2))
                .filter(parts -> parts.length == 2)
                .collect(Collectors.toMap(parts -> parts[0].trim(), parts -> parts[1].trim()));
    }
}