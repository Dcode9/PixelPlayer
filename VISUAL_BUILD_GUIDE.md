# 🎯 Visual APK Build Guide

This visual guide shows exactly how to build and install the PixelPlayer APK on your Android device.

## 📋 Table of Contents
- [Method 1: Using Build Script (Easiest)](#method-1-using-build-script-easiest)
- [Method 2: Manual Gradle Build](#method-2-manual-gradle-build)
- [Method 3: Android Studio](#method-3-android-studio)
- [Installing the APK](#installing-the-apk)

---

## Method 1: Using Build Script (Easiest)

### Step 1: Open Terminal in Project Directory

**On Windows:**
- Open Command Prompt or PowerShell
- Navigate to project: `cd path\to\PixelPlayer`

**On Linux/Mac:**
- Open Terminal
- Navigate to project: `cd path/to/PixelPlayer`

### Step 2: Run the Build Script

**Linux/Mac:**
```bash
./build-apk.sh debug
```

**Windows:**
```cmd
build-apk.bat debug
```

### Expected Output:
```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  🔍 Checking Prerequisites
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

✅ Java version: openjdk version "11.0.x"
✅ Gradle wrapper found

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  🔨 Building PixelPlayer APK (debug)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

> Task :app:assembleDebug
BUILD SUCCESSFUL in 45s

✅ APK built successfully!

📍 Location: app/build/outputs/apk/debug/app-debug.apk
📦 Size: 25M
📱 Version: 1.0.0

Do you want to install the APK on a connected device? [y/N]:
```

### Step 3: Install (Optional)

If you have a device connected:
- Type `y` and press Enter
- The script will install the APK automatically

If no device connected:
- The script shows the APK location for manual installation

---

## Method 2: Manual Gradle Build

### Step 1: Open Terminal

Navigate to the project directory:
```bash
cd /path/to/PixelPlayer
```

### Step 2: Build Debug APK

**Linux/Mac:**
```bash
./gradlew assembleDebug
```

**Windows:**
```cmd
gradlew.bat assembleDebug
```

### Step 3: Find Your APK

The APK is located at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Step 4: Install with ADB

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## Method 3: Android Studio

### Step 1: Open Project

1. Launch Android Studio
2. Click **File** → **Open**
3. Select the PixelPlayer project folder
4. Wait for Gradle sync to complete

### Step 2: Build APK

1. Click **Build** in the top menu
2. Select **Build Bundle(s) / APK(s)**
3. Click **Build APK(s)**

### Step 3: Locate APK

Android Studio will show a notification:
```
APK(s) generated successfully.
Locate or analyze the APK.
```

Click **locate** to open the folder containing the APK.

### Step 4: Run on Device (Alternative)

Instead of building APK, you can directly run:
1. Connect your Android device
2. Click the **Run** button (▶️) or press **Shift + F10**
3. Select your device from the list
4. Android Studio will install and launch the app

---

## Installing the APK

### Method A: Using ADB (Recommended)

#### Prerequisites:
1. **Enable USB Debugging** on your device:
   - Go to **Settings** → **About Phone**
   - Tap **Build Number** 7 times
   - Go back to **Settings** → **Developer Options**
   - Enable **USB Debugging**

2. **Connect device** via USB cable

#### Install Command:
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

#### Verify Installation:
```bash
adb shell pm list packages | grep pixelplay
```

You should see:
```
package:com.theveloper.pixelplay.debug
```

### Method B: Manual Installation

1. **Copy APK to device:**
   - Via USB: Copy to Downloads folder
   - Via cloud: Upload to Google Drive/Dropbox
   - Via email: Email to yourself

2. **Enable Unknown Sources:**
   - Go to **Settings** → **Security**
   - Enable **Install unknown apps**
   - Allow your file manager or browser

3. **Install:**
   - Open the APK file with your file manager
   - Tap **Install**
   - Tap **Open** when installation completes

### Method C: Wireless ADB (Advanced)

If your device supports wireless debugging:

```bash
# Connect via USB first
adb tcpip 5555
adb connect <device-ip>:5555

# Now you can disconnect USB
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 🎉 Success!

After installation, you should see the PixelPlayer icon on your home screen or app drawer.

### First Launch:
1. Grant necessary permissions (Storage, Notifications)
2. Select music folders to scan
3. Start enjoying your music!

---

## 🔍 Verification Steps

### Check App Info
```bash
adb shell dumpsys package com.theveloper.pixelplay.debug | grep version
```

### Launch App via ADB
```bash
adb shell am start -n com.theveloper.pixelplay.debug/com.theveloper.pixelplay.MainActivity
```

### View Logs
```bash
adb logcat | grep PixelPlay
```

---

## 📊 Build Types Comparison

| Build Type | Size | Speed | Debug Info | Use Case |
|------------|------|-------|------------|----------|
| **Debug** | ~30MB | Slower | ✅ Yes | Development & Testing |
| **Release** | ~20MB | Faster | ❌ No | Production |
| **Benchmark** | ~20MB | Fastest | ❌ No | Performance Testing |

### Build Commands:
```bash
# Debug
./gradlew assembleDebug

# Release
./gradlew assembleRelease

# Benchmark
./gradlew assembleBenchmark
```

---

## 🐛 Troubleshooting

### Problem: "Permission Denied" on Linux/Mac

**Solution:**
```bash
chmod +x gradlew
chmod +x build-apk.sh
```

### Problem: "SDK not found"

**Solution:**
```bash
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```

### Problem: Build fails with memory error

**Solution:** Edit `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=512m
```

### Problem: "INSTALL_FAILED_UPDATE_INCOMPATIBLE"

**Solution:** Uninstall existing app:
```bash
adb uninstall com.theveloper.pixelplay.debug
```

Then install again:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Problem: Device not recognized

**Solution:**
```bash
# List devices
adb devices

# Restart ADB server
adb kill-server
adb start-server
adb devices
```

---

## 💡 Pro Tips

### 1. Speed Up Builds
Add to `gradle.properties`:
```properties
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true
```

### 2. Clean Build
If build fails mysteriously:
```bash
./gradlew clean
./gradlew assembleDebug
```

### 3. Build All Variants
```bash
./gradlew assemble
```

### 4. Check APK Size
```bash
ls -lh app/build/outputs/apk/debug/app-debug.apk
```

### 5. Analyze APK
```bash
# Using Android Studio
Build → Analyze APK...
# Select your APK file
```

---

## 📱 Quick Reference Card

```
┌─────────────────────────────────────────────────────┐
│           PIXELPLAYER APK BUILD CARD               │
├─────────────────────────────────────────────────────┤
│                                                     │
│  BUILD:                                            │
│    ./build-apk.sh debug        (Linux/Mac)         │
│    build-apk.bat debug         (Windows)           │
│                                                     │
│  MANUAL:                                           │
│    ./gradlew assembleDebug                         │
│                                                     │
│  INSTALL:                                          │
│    adb install -r app/build/outputs/apk/          │
│                   debug/app-debug.apk              │
│                                                     │
│  LOCATION:                                         │
│    app/build/outputs/apk/debug/app-debug.apk       │
│                                                     │
│  CLEAN:                                            │
│    ./gradlew clean                                 │
│                                                     │
└─────────────────────────────────────────────────────┘
```

---

## 📚 Additional Resources

- [BUILD_APK.md](BUILD_APK.md) - Comprehensive build guide
- [QUICK_BUILD.md](QUICK_BUILD.md) - Quick reference
- [README.md](README.md) - Project overview
- [Android Developer Docs](https://developer.android.com/studio/build)

---

## ✅ Checklist

Before building:
- [ ] JDK 11+ installed
- [ ] Android SDK installed (or Android Studio)
- [ ] Project cloned from GitHub
- [ ] Terminal/Command Prompt open in project directory

After building:
- [ ] APK file exists at expected location
- [ ] APK size is reasonable (~20-30MB)
- [ ] Device connected with USB debugging enabled
- [ ] APK installed successfully
- [ ] App launches without errors

---

**Need help?** Check [BUILD_APK.md](BUILD_APK.md) for detailed troubleshooting or open an issue on GitHub.
