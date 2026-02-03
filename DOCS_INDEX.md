# 📖 Documentation Index

Welcome to the PixelPlayer build documentation! This index helps you find the right guide for your needs.

## 🎯 Start Here

**New to building Android apps?**
→ [HOW_TO_BUILD.md](HOW_TO_BUILD.md) - Quick overview and links to detailed guides

## 📚 Complete Documentation Library

### By Skill Level

| Skill Level | Document | Description |
|-------------|----------|-------------|
| 🟢 **Beginner** | [VISUAL_BUILD_GUIDE.md](VISUAL_BUILD_GUIDE.md) | Step-by-step instructions with examples |
| 🟡 **Intermediate** | [QUICK_BUILD.md](QUICK_BUILD.md) | Essential commands and quick fixes |
| 🔴 **Advanced** | [BUILD_APK.md](BUILD_APK.md) | Complete technical reference |

### By Purpose

| Need | Document | Use When |
|------|----------|----------|
| 📖 **Overview** | [HOW_TO_BUILD.md](HOW_TO_BUILD.md) | First time building |
| 👀 **Visual Guide** | [VISUAL_BUILD_GUIDE.md](VISUAL_BUILD_GUIDE.md) | Want detailed steps |
| ⚡ **Quick Lookup** | [QUICK_BUILD.md](QUICK_BUILD.md) | Know what to do, need command |
| 📊 **Process Flow** | [BUILD_FLOW.md](BUILD_FLOW.md) | Understand the workflow |
| 🔧 **Complete Ref** | [BUILD_APK.md](BUILD_APK.md) | Need all options/details |
| 🚀 **Release Guide** | [RELEASE_GUIDE.md](RELEASE_GUIDE.md) | Creating official releases |
| 🏠 **Project Info** | [README.md](README.md) | About PixelPlayer app |

## 🛠️ Build Tools

### Automated Scripts (Recommended)

**Linux/Mac:**
```bash
./build-apk.sh [debug|release|clean]
```

**Windows:**
```cmd
build-apk.bat [debug|release|clean]
```

### Manual Gradle Commands

**Debug Build:**
```bash
./gradlew assembleDebug    # Linux/Mac
gradlew.bat assembleDebug  # Windows
```

**Release Build:**
```bash
./gradlew assembleRelease    # Linux/Mac
gradlew.bat assembleRelease  # Windows
```

## 🚀 Quick Commands

```bash
# Clone repository
git clone https://github.com/Dcode9/PixelPlayer.git
cd PixelPlayer

# Build APK (easiest)
./build-apk.sh debug

# Build APK (manual)
./gradlew assembleDebug

# Install APK
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Launch app
adb shell am start -n com.theveloper.pixelplay.debug/com.theveloper.pixelplay.MainActivity
```

## 📱 APK Locations

| Build Type | APK Path |
|------------|----------|
| Debug | `app/build/outputs/apk/debug/app-debug.apk` |
| Release | `app/build/outputs/apk/release/app-release.apk` |
| Benchmark | `app/build/outputs/apk/benchmark/app-benchmark.apk` |

## 🆘 Getting Help

### Common Issues

| Issue | Solution Document | Section |
|-------|------------------|---------|
| Build fails | [BUILD_APK.md](BUILD_APK.md) | Troubleshooting |
| Can't install | [VISUAL_BUILD_GUIDE.md](VISUAL_BUILD_GUIDE.md) | Installation Methods |
| Permission error | [QUICK_BUILD.md](QUICK_BUILD.md) | Common Issues |
| SDK not found | [BUILD_APK.md](BUILD_APK.md) | Prerequisites |

### Support Channels

- 📖 **Documentation:** This index!
- 🐛 **Bug Reports:** [GitHub Issues](https://github.com/Dcode9/PixelPlayer/issues)
- 💬 **Questions:** [GitHub Discussions](https://github.com/Dcode9/PixelPlayer/discussions)

## 🎓 Learning Path

Follow this path for best results:

```
1. HOW_TO_BUILD.md           ← Start here (5 min read)
   └─ Get overview and choose your path

2. VISUAL_BUILD_GUIDE.md     ← Follow detailed steps (15 min)
   └─ Learn by doing

3. QUICK_BUILD.md            ← Bookmark for future (2 min)
   └─ Quick reference when needed

4. BUILD_APK.md              ← Read when needed (reference)
   └─ Deep dive into advanced topics
```

## 📋 Checklists

### Before Building

- [ ] JDK 11+ installed
- [ ] Git installed
- [ ] Repository cloned
- [ ] Terminal/Command Prompt open in project directory

### After Building

- [ ] APK file exists at expected location
- [ ] APK size is reasonable (~20-30MB)
- [ ] No build errors in console

### Before Installing

- [ ] USB debugging enabled on device
- [ ] Device connected (or APK copied to device)
- [ ] Unknown sources enabled (for manual install)

### After Installing

- [ ] App icon visible in app drawer
- [ ] App opens without crashing
- [ ] Permissions granted
- [ ] Music library loads

## 📊 Documentation Statistics

| Metric | Value |
|--------|-------|
| Total Documents | 7 files |
| Total Size | ~43 KB |
| Code Examples | 50+ snippets |
| Build Methods | 3 (Script, Gradle, Studio) |
| Install Methods | 3 (ADB, Manual, Studio) |
| Troubleshooting Items | 15+ issues covered |
| Platform Support | Windows, Linux, Mac |

## 🎯 Quick Decision Guide

**I want to...**

- ✅ **Build APK quickly** → Use `./build-apk.sh debug`
- ✅ **Understand the process** → Read [VISUAL_BUILD_GUIDE.md](VISUAL_BUILD_GUIDE.md)
- ✅ **Find a command** → Check [QUICK_BUILD.md](QUICK_BUILD.md)
- ✅ **Solve a problem** → See [BUILD_APK.md](BUILD_APK.md) Troubleshooting
- ✅ **Learn about the app** → Read [README.md](README.md)

## 📝 Version Information

This documentation covers:
- **PixelPlayer:** Latest version
- **Android SDK:** API 29+ (Android 11+)
- **Java/JDK:** Version 11+
- **Gradle:** 8.3+ (wrapper included)

## 🔄 Keep Updated

Documentation matches the current codebase. If you're reading this after a major update, ensure you have the latest version:

```bash
git pull origin main
```

---

## 🎉 Ready to Build?

**Start with:** [HOW_TO_BUILD.md](HOW_TO_BUILD.md)

**Questions?** All answers are in these docs! Use the index above to find what you need.

**Happy Building!** 🚀
