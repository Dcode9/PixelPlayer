# 🎯 How to Create and Run PixelPlayer APK on Android

**Quick Answer:** Use our automated build script or Gradle commands to create an APK, then install it on your Android device.

---

## 🚀 Fastest Method (5 Minutes)

### Step 1: Get the Code
```bash
git clone https://github.com/Dcode9/PixelPlayer.git
cd PixelPlayer
```

### Step 2: Build APK

**Linux/Mac:**
```bash
./build-apk.sh debug
```

**Windows:**
```cmd
build-apk.bat debug
```

### Step 3: Install & Run

The script will ask if you want to install. If you say yes and have a device connected, it installs automatically!

**Manual Installation:**
- APK location: `app/build/outputs/apk/debug/app-debug.apk`
- Copy to your phone and tap to install

---

## 📱 What You Get

After installation:
- **App Name:** PixelPlayer
- **Package:** com.theveloper.pixelplay.debug
- **Size:** ~25MB
- **Permissions:** Storage access, Notifications

---

## 🎓 Learning Path

Choose your experience level:

### 🟢 Beginner
**→ Start here:** [VISUAL_BUILD_GUIDE.md](VISUAL_BUILD_GUIDE.md)
- Step-by-step with screenshots
- All methods explained
- Troubleshooting included

### 🟡 Intermediate  
**→ Quick Reference:** [QUICK_BUILD.md](QUICK_BUILD.md)
- Essential commands only
- Common issues and fixes
- One-page cheatsheet

### 🔴 Advanced
**→ Full Documentation:** [BUILD_APK.md](BUILD_APK.md)
- Complete build options
- Signing configurations
- Build variants
- Performance optimization

---

## 🛠️ Prerequisites

### Required:
- ✅ **JDK 11+** - [Download](https://adoptium.net/)
- ✅ **Git** - For cloning the repository

### Optional (for installation):
- 📱 **Android device** with USB debugging enabled
- 🔌 **USB cable**
- 💻 **ADB (Android Debug Bridge)** - Usually included with Android SDK

---

## 📖 Available Documentation

| Document | Purpose | Best For |
|----------|---------|----------|
| **THIS FILE** | Quick overview | First-time users |
| [VISUAL_BUILD_GUIDE.md](VISUAL_BUILD_GUIDE.md) | Step-by-step visual guide | Beginners |
| [BUILD_APK.md](BUILD_APK.md) | Comprehensive manual | Complete reference |
| [QUICK_BUILD.md](QUICK_BUILD.md) | Quick commands | Quick lookups |
| [README.md](README.md) | Project overview | Understanding the app |

---

## ⚡ Quick Commands

### Build Commands
```bash
# Using automated script
./build-apk.sh debug          # Linux/Mac
build-apk.bat debug           # Windows

# Using Gradle directly
./gradlew assembleDebug       # Linux/Mac
gradlew.bat assembleDebug     # Windows
```

### Install Commands
```bash
# Via ADB
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Launch after install
adb shell am start -n com.theveloper.pixelplay.debug/com.theveloper.pixelplay.MainActivity
```

---

## 🎯 Build Options

| Command | Output | Use Case |
|---------|--------|----------|
| `assembleDebug` | `app-debug.apk` | Development & Testing |
| `assembleRelease` | `app-release.apk` | Production (optimized) |
| `assembleBenchmark` | `app-benchmark.apk` | Performance testing |

---

## 📍 APK Locations

After building, find your APK here:

```
PixelPlayer/
└── app/
    └── build/
        └── outputs/
            └── apk/
                ├── debug/
                │   └── app-debug.apk           ← Debug build
                ├── release/
                │   └── app-release.apk         ← Release build
                └── benchmark/
                    └── app-benchmark.apk       ← Benchmark build
```

---

## 🔧 Installation Methods

### Method 1: Using ADB (Recommended)
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

**Pros:** Fast, reliable, can reinstall over existing app
**Cons:** Requires USB debugging enabled

### Method 2: Manual Transfer
1. Copy APK to device
2. Tap to install
3. Grant permissions

**Pros:** No cables needed
**Cons:** Must enable "Unknown sources"

### Method 3: Android Studio
1. Connect device
2. Click Run (▶️)
3. App installs and launches

**Pros:** One-click solution
**Cons:** Requires Android Studio

---

## 💡 Common Scenarios

### Scenario 1: I just want to try the app
```bash
git clone https://github.com/Dcode9/PixelPlayer.git
cd PixelPlayer
./build-apk.sh debug
# Copy app-debug.apk to your phone and install
```

### Scenario 2: I'm a developer wanting to modify code
1. Open project in Android Studio
2. Make your changes
3. Click Run (▶️)
4. Test on device/emulator

### Scenario 3: I want to build for production
```bash
./gradlew assembleRelease
# APK at: app/build/outputs/apk/release/app-release.apk
# Configure signing in app/build.gradle.kts
```

### Scenario 4: Build failed, need to start fresh
```bash
./gradlew clean
./gradlew assembleDebug
```

---

## 🎬 Video Tutorial Checklist

If you're watching a video or following along:

**Phase 1: Setup**
- [ ] Clone repository
- [ ] Open terminal in project folder
- [ ] Check Java is installed: `java -version`

**Phase 2: Build**
- [ ] Run build command
- [ ] Wait for "BUILD SUCCESSFUL"
- [ ] Note APK location

**Phase 3: Install**
- [ ] Enable USB debugging on phone
- [ ] Connect phone to computer
- [ ] Run install command OR
- [ ] Copy APK to phone and tap to install

**Phase 4: Launch**
- [ ] Find PixelPlayer icon
- [ ] Grant permissions
- [ ] Enjoy!

---

## 🐛 Having Issues?

### Can't build?
→ Check [BUILD_APK.md](BUILD_APK.md) Troubleshooting section

### Can't install?
→ See [VISUAL_BUILD_GUIDE.md](VISUAL_BUILD_GUIDE.md) Installation Methods

### App crashes?
→ Enable USB debugging and check logs: `adb logcat | grep PixelPlay`

### Need help?
→ Open an issue on [GitHub](https://github.com/Dcode9/PixelPlayer/issues)

---

## 📊 Summary Table

| Task | Command | Output |
|------|---------|--------|
| **Build** | `./build-apk.sh debug` | Creates APK file |
| **Install** | `adb install -r <apk-path>` | Installs on device |
| **Launch** | Tap app icon or use ADB | Opens app |
| **Clean** | `./gradlew clean` | Removes old builds |
| **Help** | `./build-apk.sh help` | Shows options |

---

## 🎓 Next Steps

After you have the app running:

1. **Explore Features** - Check out [README.md](README.md) for feature list
2. **Customize** - Modify source code if you want
3. **Share** - Tell others about PixelPlayer
4. **Contribute** - Submit improvements via Pull Request

---

## 📞 Support

- 📖 **Documentation:** You're reading it!
- 🐛 **Bug Reports:** [GitHub Issues](https://github.com/Dcode9/PixelPlayer/issues)
- 💬 **Discussions:** [GitHub Discussions](https://github.com/Dcode9/PixelPlayer/discussions)
- ⭐ **Like the app?** Give us a star on GitHub!

---

## ✅ Success Checklist

You're done when:
- [ ] APK file exists on your computer
- [ ] APK installed on Android device
- [ ] PixelPlayer icon visible in app drawer
- [ ] App opens and shows music library
- [ ] You're listening to music! 🎵

---

**That's it!** You now know how to create an APK and run it on Android. Choose your preferred documentation from the list above for more details.

Happy listening! 🎵
