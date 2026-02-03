# 📦 Building APK for PixelPlayer

This guide will help you build an APK file for PixelPlayer and install it on your Android device.

## 📋 Prerequisites

Before building the APK, ensure you have:

- **JDK 11 or higher** - [Download here](https://adoptium.net/)
- **Android SDK** (if building from command line without Android Studio)
- **Git** (to clone the repository)
- **Android device** with USB debugging enabled, or an **Android emulator**

## 🔧 Method 1: Building APK via Command Line (Recommended)

This is the fastest method if you don't have Android Studio installed.

### Step 1: Clone the Repository

```bash
git clone https://github.com/Dcode9/PixelPlayer.git
cd PixelPlayer
```

### Step 2: Build Debug APK

The debug APK is signed with a debug keystore and is perfect for testing.

**On Linux/Mac:**
```bash
./gradlew assembleDebug
```

**On Windows:**
```bash
gradlew.bat assembleDebug
```

The APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Step 3: Build Release APK

For a production-ready APK with optimizations:

**On Linux/Mac:**
```bash
./gradlew assembleRelease
```

**On Windows:**
```bash
gradlew.bat assembleRelease
```

The APK will be generated at:
```
app/build/outputs/apk/release/app-release.apk
```

> **Note:** The release APK is currently signed with the debug keystore (as configured in `app/build.gradle.kts`). For production releases, you should configure proper signing keys.

### Build Variants

You can build specific variants:
- `assembleDebug` - Debug build with debugging enabled
- `assembleRelease` - Release build with ProGuard optimization
- `assembleBenchmark` - Benchmark variant for performance testing

## 🎨 Method 2: Building APK via Android Studio

If you prefer using a GUI, follow these steps:

### Step 1: Open Project in Android Studio

1. Launch **Android Studio**
2. Select **File** → **Open**
3. Navigate to the PixelPlayer directory and select it
4. Wait for Gradle sync to complete

### Step 2: Build APK

1. Click **Build** in the menu bar
2. Select **Build Bundle(s) / APK(s)**
3. Click **Build APK(s)**

Android Studio will build the APK and show a notification when complete. Click **locate** to open the folder containing the APK.

### Step 3: Build Signed APK (Optional)

For a release version:

1. Click **Build** → **Generate Signed Bundle / APK**
2. Select **APK** and click **Next**
3. Create or select a keystore
4. Enter keystore credentials
5. Select **release** build variant
6. Click **Finish**

## 📱 Installing APK on Android Device

### Via USB Cable (ADB)

1. **Enable USB Debugging** on your Android device:
   - Go to **Settings** → **About Phone**
   - Tap **Build Number** 7 times to enable Developer Options
   - Go back to **Settings** → **Developer Options**
   - Enable **USB Debugging**

2. **Connect your device** via USB cable

3. **Install the APK** using ADB:

```bash
# Navigate to the APK location
cd app/build/outputs/apk/debug

# Install using ADB
adb install app-debug.apk

# Or force reinstall if already installed
adb install -r app-debug.apk
```

If you have multiple devices connected:
```bash
# List devices
adb devices

# Install on specific device
adb -s <device-id> install app-debug.apk
```

### Via File Transfer

1. **Copy the APK** to your Android device (via USB, email, cloud storage, etc.)
2. **Open the APK** file on your device using a file manager
3. **Allow installation** from unknown sources if prompted
4. **Install** the application

### Via Android Studio

If your device is connected and recognized by Android Studio:

1. Click the **Run** button (▶️) or press **Shift + F10**
2. Select your device from the list
3. Android Studio will build and install the app automatically

## 🧪 Quick Build Script

Save this as `build-apk.sh` for quick builds:

```bash
#!/bin/bash

# Build PixelPlayer APK
# Usage: ./build-apk.sh [debug|release]

BUILD_TYPE=${1:-debug}

echo "🔨 Building PixelPlayer APK ($BUILD_TYPE)..."

if [ "$BUILD_TYPE" = "debug" ]; then
    ./gradlew assembleDebug
    APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
elif [ "$BUILD_TYPE" = "release" ]; then
    ./gradlew assembleRelease
    APK_PATH="app/build/outputs/apk/release/app-release.apk"
else
    echo "❌ Invalid build type. Use 'debug' or 'release'"
    exit 1
fi

if [ -f "$APK_PATH" ]; then
    echo "✅ APK built successfully!"
    echo "📍 Location: $APK_PATH"
    echo "📦 Size: $(du -h "$APK_PATH" | cut -f1)"
    
    # Ask if user wants to install
    read -p "Install on connected device? (y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        adb install -r "$APK_PATH"
        echo "✅ APK installed!"
    fi
else
    echo "❌ Build failed! APK not found at $APK_PATH"
    exit 1
fi
```

Make it executable:
```bash
chmod +x build-apk.sh
```

Run it:
```bash
./build-apk.sh debug    # For debug build
./build-apk.sh release  # For release build
```

## 🐛 Troubleshooting

### Build Fails with "SDK not found"

**Solution:** Set the `ANDROID_HOME` environment variable:

**Linux/Mac:**
```bash
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```

**Windows:**
```cmd
set ANDROID_HOME=C:\Users\YourUsername\AppData\Local\Android\Sdk
set PATH=%PATH%;%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools
```

### Build Fails with "JDK version" Error

**Solution:** Ensure you're using JDK 11:

```bash
# Check Java version
java -version

# Set JAVA_HOME if needed
export JAVA_HOME=/path/to/jdk-11
```

### "Permission Denied" Error on Linux/Mac

**Solution:** Make gradlew executable:

```bash
chmod +x gradlew
```

### "INSTALL_FAILED_UPDATE_INCOMPATIBLE" Error

**Solution:** Uninstall the existing app first:

```bash
adb uninstall com.theveloper.pixelplay.debug  # for debug build
adb uninstall com.theveloper.pixelplay        # for release build
```

Then reinstall the APK.

### Gradle Sync Issues

**Solution:** Clean and rebuild:

```bash
./gradlew clean
./gradlew assembleDebug
```

### Out of Memory During Build

**Solution:** Increase Gradle memory in `gradle.properties`:

```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=512m
```

## 📊 Build Information

After building, you can check the APK info:

### APK Size
```bash
ls -lh app/build/outputs/apk/debug/app-debug.apk
```

### APK Information (requires aapt)
```bash
aapt dump badging app/build/outputs/apk/debug/app-debug.apk
```

### Verify Signing
```bash
jarsigner -verify -verbose -certs app/build/outputs/apk/debug/app-debug.apk
```

## 🚀 Quick Start Summary

For most users, this is all you need:

```bash
# 1. Clone
git clone https://github.com/Dcode9/PixelPlayer.git
cd PixelPlayer

# 2. Build
./gradlew assembleDebug

# 3. Install
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 📚 Additional Resources

- [Android Developer Guide - Build Your App](https://developer.android.com/studio/build)
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)
- [ADB Documentation](https://developer.android.com/studio/command-line/adb)

## 💡 Tips

- **Clean builds** help resolve many build issues: `./gradlew clean`
- **Use `--stacktrace`** for detailed error information: `./gradlew assembleDebug --stacktrace`
- **Build cache** speeds up subsequent builds (enabled by default)
- **Parallel builds** can be enabled in `gradle.properties`: `org.gradle.parallel=true`

---

For issues or questions, please open an issue on the [GitHub repository](https://github.com/Dcode9/PixelPlayer/issues).
