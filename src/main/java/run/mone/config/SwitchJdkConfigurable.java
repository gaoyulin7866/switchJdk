package run.mone.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.ui.Gray;
import com.intellij.util.ui.JBUI;
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
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 添加提示标签
        JLabel label = new JLabel("提示：设置成功后需重启IDEA");
        label.setBorder(new EmptyBorder(0, 0, 12, 0));
        label.setForeground(new Color(255, 165, 0));
        panel.add(label);

        // 添加复选框
        switchSystemJavaCheckBox = new JCheckBox("同时切换系统java版本", false);
        switchSystemJavaCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        switchSystemJavaCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                switchSystemJava = switchSystemJavaCheckBox.isSelected();
            }
        });
        panel.add(switchSystemJavaCheckBox);

        // 在复选框下方添加提示信息
        JLabel infoLabel = new JLabel("<html>1).目前只支持mac系统电脑。<br> " +
                "2). java环境变量配置必须在~/.bash_profile中。<br>" +
                "3). java环境变量格式必须为：<br>" +
                "&emsp;export JAVA_HOME=/xxxx/xxx/xxx<br>" +
                "&emsp;export PATH=$JAVA_HOME/bin:$PATH<br>" +
                "4). JAVA_HOME的值会替换为下面设置的对应值，所以jdk的存储路径可以任意。<br>" +
                "5). 勾选并切换jdk版本时会运行命令行工具，执行source ~/.bash_profile，以便java版本生效。<br>" +
                "6). 如以上有一个条件不满足，建议不要勾选此配置。</html>");
        infoLabel.setBorder(JBUI.Borders.empty(4, 24, 12, 0)); // 添加一些上边距
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoLabel.setForeground(Gray._159);
        infoLabel.setFont(new Font(infoLabel.getFont().getName(), Font.PLAIN, 12)); // 设置字体大小为12px
        panel.add(infoLabel);

        // 添加Jdk Path标签
        JLabel jdkPathLabel = new JLabel("Jdk Paths:");
        jdkPathLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        jdkPathLabel.setBorder(JBUI.Borders.emptyBottom(8));
        panel.add(jdkPathLabel);

        jdkPathsArea = new JTextArea(1,1);
        jdkPathsArea.setLineWrap(true); // 自动换行
        jdkPathsArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(jdkPathsArea);

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