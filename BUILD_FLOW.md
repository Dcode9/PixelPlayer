# 🔄 APK Build Process Flow

## Visual Process Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                    PIXELPLAYER APK BUILD FLOW                   │
└─────────────────────────────────────────────────────────────────┘

START
  │
  ├─ Prerequisites Check
  │  ├─ JDK 11+ installed? ───────────────┐
  │  ├─ Git installed? ────────────────────┤
  │  └─ Android SDK (optional) ────────────┤
  │                                         │
  ↓                                         ↓
                                         [INSTALL]
                                            │
  ├─ Clone Repository                       │
  │  └─ git clone <repo-url>                │
  │                                         │
  ↓                                         ↓
                                      [RETRY WITH TOOLS]
  ├─ Choose Build Method                    
  │  │                                      
  │  ├─ Option 1: Automated Script         
  │  │  └─ ./build-apk.sh debug           
  │  │                                     
  │  ├─ Option 2: Gradle Command           
  │  │  └─ ./gradlew assembleDebug        
  │  │                                     
  │  └─ Option 3: Android Studio           
  │     └─ Build → Build APK(s)           
  │                                        
  ↓                                        
                                          
  ├─ Build Process                         
  │  ├─ Sync dependencies                  
  │  ├─ Compile code                       
  │  ├─ Process resources                  
  │  ├─ Generate DEX files                 
  │  └─ Package APK                        
  │                                        
  ↓                                        
                                          
  ├─ APK Generated                         
  │  └─ Location: app/build/outputs/apk/  
  │     └─ debug/app-debug.apk            
  │                                        
  ↓                                        
                                          
  ├─ Installation Method                   
  │  │                                     
  │  ├─ Option A: ADB                      
  │  │  ├─ Enable USB debugging            
  │  │  ├─ Connect device                  
  │  │  └─ adb install -r <apk>           
  │  │                                     
  │  ├─ Option B: Manual                   
  │  │  ├─ Copy APK to device             
  │  │  ├─ Enable Unknown Sources          
  │  │  └─ Tap to install                 
  │  │                                     
  │  └─ Option C: Android Studio           
  │     ├─ Connect device                  
  │     └─ Click Run (▶️)                  
  │                                        
  ↓                                        
                                          
  ├─ App Installed                         
  │  └─ Package: com.theveloper.pixelplay  
  │                                        
  ↓                                        
                                          
  ├─ First Launch                          
  │  ├─ Grant permissions                  
  │  ├─ Scan music folders                 
  │  └─ Start playing! 🎵                  
  │                                        
  ↓                                        
                                          
END (Success!)                             
```

## Time Estimates

| Stage | Time | Description |
|-------|------|-------------|
| **Prerequisites** | 5-30 min | One-time setup |
| **Clone Repository** | 1-5 min | Download code |
| **Build APK** | 2-10 min | First build is slower |
| **Install APK** | 1-2 min | Via ADB or manual |
| **First Launch** | 1-2 min | Grant permissions |
| **Total** | ~10-50 min | Depending on setup |

## Quick Decision Tree

```
Need APK?
  │
  ├─ Have Android Studio?
  │  ├─ YES → Use Android Studio method
  │  └─ NO → Use command line
  │          │
  │          ├─ Want automation?
  │          │  ├─ YES → Use build-apk.sh/bat
  │          │  └─ NO → Use gradlew directly
  │          │
  │          └─ Continue to build...
  │
  └─ Want to install?
     │
     ├─ Have ADB?
     │  ├─ YES → Use adb install
     │  └─ NO → Manual transfer
     │
     └─ Launch and enjoy!
```

## Error Handling Flow

```
Build Failed?
  │
  ├─ Check Prerequisites
  │  ├─ Java version correct?
  │  ├─ Gradle wrapper exists?
  │  └─ Disk space available?
  │
  ↓
  │
  ├─ Try Clean Build
  │  └─ ./gradlew clean
  │     ./gradlew assembleDebug
  │
  ↓
  │
  ├─ Check Error Message
  │  ├─ SDK not found → Set ANDROID_HOME
  │  ├─ Permission denied → chmod +x gradlew
  │  ├─ Out of memory → Increase heap size
  │  └─ Network error → Check connection
  │
  ↓
  │
  └─ Still failing?
     └─ Check detailed docs or ask for help
```

## Platform-Specific Paths

### Linux/Mac

```
PixelPlayer/
│
├─ build-apk.sh              ← Use this
├─ gradlew                   ← Or this
│
└─ app/build/outputs/apk/
   └─ debug/
      └─ app-debug.apk       ← Generated here
```

### Windows

```
PixelPlayer\
│
├─ build-apk.bat             ← Use this
├─ gradlew.bat               ← Or this
│
└─ app\build\outputs\apk\
   └─ debug\
      └─ app-debug.apk       ← Generated here
```

## Success Indicators

✅ **Build Successful:**
```
BUILD SUCCESSFUL in 45s
127 actionable tasks: 127 executed
```

✅ **APK Generated:**
```
$ ls -lh app/build/outputs/apk/debug/
-rw-r--r-- 1 user user 25M Feb 3 app-debug.apk
```

✅ **Installation Successful:**
```
$ adb install -r app-debug.apk
Performing Streamed Install
Success
```

✅ **App Running:**
```
$ adb shell pm list packages | grep pixelplay
package:com.theveloper.pixelplay.debug
```

## Common Issues Quick Fix

| Issue | Quick Fix |
|-------|-----------|
| Build fails | `./gradlew clean` |
| Permission denied | `chmod +x gradlew` |
| SDK not found | Set ANDROID_HOME |
| Install fails | `adb uninstall <package>` |
| Device not found | `adb kill-server && adb start-server` |

## Support Resources

```
┌──────────────────────────────────────┐
│  Need Help? Check These Docs:        │
├──────────────────────────────────────┤
│                                      │
│  🟢 Beginner   → VISUAL_BUILD_GUIDE  │
│  🟡 Reference  → BUILD_APK.md        │
│  🔴 Quick Look → QUICK_BUILD.md      │
│  📘 Overview   → HOW_TO_BUILD.md     │
│                                      │
└──────────────────────────────────────┘
```

---

**Remember:** The automated scripts (`build-apk.sh` / `build-apk.bat`) handle most of this automatically! 🚀
