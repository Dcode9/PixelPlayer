# Quick APK Build Reference

## 🚀 Fastest Way to Build APK

### Linux/Mac
```bash
./build-apk.sh debug
```

### Windows
```cmd
build-apk.bat debug
```

## 📦 Output Locations

| Build Type | APK Location |
|-----------|-------------|
| Debug | `app/build/outputs/apk/debug/app-debug.apk` |
| Release | `app/build/outputs/apk/release/app-release.apk` |

## 🔧 Manual Build Commands

### Debug Build
```bash
# Linux/Mac
./gradlew assembleDebug

# Windows
gradlew.bat assembleDebug
```

### Release Build
```bash
# Linux/Mac
./gradlew assembleRelease

# Windows
gradlew.bat assembleRelease
```

## 📱 Install APK

### Using ADB
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Manually
1. Copy APK to your Android device
2. Open with file manager
3. Tap to install

## 🐛 Common Issues

### "Permission Denied" on Linux/Mac
```bash
chmod +x gradlew
```

### "SDK not found"
```bash
export ANDROID_HOME=$HOME/Android/Sdk
```

### Clean Build
```bash
./gradlew clean
./gradlew assembleDebug
```

## 📚 Full Documentation

See [BUILD_APK.md](BUILD_APK.md) for complete instructions, troubleshooting, and advanced options.
