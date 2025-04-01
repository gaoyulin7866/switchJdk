## SwitchJdk - Quick JDK Version Switcher

This plugin allows you to quickly switch between different JDK versions in IntelliJ IDEA.

### Configuration Steps:

1. Go to **Settings > Other Settings > Switch Sdk**
2. Configure your JDK paths in the following format:
   ```properties
   jdk17=/path/to/jdk17/home
   jdk21=/path/to/jdk21/home
   jdk11=/path/to/jdk11/home
   jdk8=/path/to/jdk8/home
   jdk19=/path/to/jdk19/home
   
3. Restart idea

### Example Configuration:
   ```properties
    jdk17=/Library/Java/JavaVirtualMachines/jdk-17.0.3.1.jdk/Contents/Home
    jdk21=/Library/Java/JavaVirtualMachines/jdk-21.0.2.jdk/Contents/Home
    jdk11=/Library/Java/JavaVirtualMachines/jdk-11.0.24.jdk/Contents/Home
    jdk8=/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home
    jdk19=/Library/Java/JavaVirtualMachines/jdk-19.0.2.jdk/Contents/Home
```

### After configuration, you can quickly switch JDK versions using the status bar widget.

### Illustration
![SwitchJdk Preview](https://downloads.marketplace.jetbrains.com/files/26964/preview_cd66d166-9fdc-4a3b-8c78-5ca3d2ebbf96)

![SwitchJdk Preview](https://downloads.marketplace.jetbrains.com/files/26964/preview_645f8fdb-1015-4004-98cd-730a6ca0f563)