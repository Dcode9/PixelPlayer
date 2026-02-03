@echo off
REM PixelPlayer APK Build Script for Windows
REM This script automates the APK building process for PixelPlayer
REM Usage: build-apk.bat [debug|release|clean]

setlocal enabledelayedexpansion

REM Set colors
set "GREEN=[32m"
set "RED=[31m"
set "YELLOW=[33m"
set "BLUE=[34m"
set "NC=[0m"

REM Get build type (default to debug)
set BUILD_TYPE=%1
if "%BUILD_TYPE%"=="" set BUILD_TYPE=debug

REM Print header
echo.
echo ================================================================
echo   PixelPlayer APK Build Script
echo ================================================================
echo.

REM Check if this is a help command
if "%BUILD_TYPE%"=="help" goto :show_help
if "%BUILD_TYPE%"=="--help" goto :show_help
if "%BUILD_TYPE%"=="-h" goto :show_help

REM Check prerequisites
echo %BLUE%Checking prerequisites...%NC%

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo %RED%ERROR: Java is not installed. Please install JDK 11 or higher.%NC%
    exit /b 1
)
echo %GREEN%✓ Java is installed%NC%

REM Check if gradlew exists
if not exist "gradlew.bat" (
    echo %RED%ERROR: gradlew.bat not found. Are you in the project root directory?%NC%
    exit /b 1
)
echo %GREEN%✓ Gradle wrapper found%NC%
echo.

REM Handle clean command
if "%BUILD_TYPE%"=="clean" (
    echo %BLUE%Cleaning previous builds...%NC%
    call gradlew.bat clean
    if errorlevel 1 (
        echo %RED%ERROR: Clean failed%NC%
        exit /b 1
    )
    echo %GREEN%✓ Clean completed%NC%
    exit /b 0
)

REM Validate build type
if not "%BUILD_TYPE%"=="debug" if not "%BUILD_TYPE%"=="release" (
    echo %RED%ERROR: Invalid build type '%BUILD_TYPE%'. Use 'debug' or 'release'%NC%
    echo.
    goto :show_help
)

REM Set paths based on build type
if "%BUILD_TYPE%"=="debug" (
    set GRADLE_TASK=assembleDebug
    set APK_PATH=app\build\outputs\apk\debug\app-debug.apk
) else (
    set GRADLE_TASK=assembleRelease
    set APK_PATH=app\build\outputs\apk\release\app-release.apk
)

REM Build APK
echo %BLUE%Building PixelPlayer APK ^(%BUILD_TYPE%^)...%NC%
echo.
call gradlew.bat %GRADLE_TASK%

if errorlevel 1 (
    echo.
    echo %RED%ERROR: Build failed!%NC%
    echo %YELLOW%Try running: gradlew.bat clean%NC%
    exit /b 1
)

REM Check if APK was created
if not exist "%APK_PATH%" (
    echo %RED%ERROR: APK not found at %APK_PATH%%NC%
    exit /b 1
)

echo.
echo %GREEN%✓ APK built successfully!%NC%
echo.
echo %BLUE%Location: %APK_PATH%%NC%

REM Get file size
for %%A in ("%APK_PATH%") do set APK_SIZE=%%~zA
set /a APK_SIZE_MB=APK_SIZE/1024/1024
echo %BLUE%Size: %APK_SIZE_MB% MB%NC%
echo.

REM Ask if user wants to install
set /p INSTALL_CHOICE="Do you want to install the APK on a connected device? [y/N]: "
if /i "%INSTALL_CHOICE%"=="y" goto :install_apk
if /i "%INSTALL_CHOICE%"=="yes" goto :install_apk

echo.
echo %BLUE%You can manually install the APK from:%NC%
echo %BLUE%  %APK_PATH%%NC%
goto :end

:install_apk
echo.
echo %BLUE%Installing APK...%NC%

REM Check if ADB is available
adb version >nul 2>&1
if errorlevel 1 (
    echo %YELLOW%WARNING: ADB is not available.%NC%
    echo %YELLOW%Please install Android SDK Platform Tools or manually install the APK.%NC%
    goto :end
)

REM Check for connected devices
adb devices | find "device" >nul
if errorlevel 1 (
    echo %YELLOW%WARNING: No Android devices connected.%NC%
    echo %YELLOW%Please connect a device with USB debugging enabled.%NC%
    goto :end
)

REM Install APK
adb install -r "%APK_PATH%"
if errorlevel 1 (
    echo %YELLOW%WARNING: Installation may have failed.%NC%
    echo %YELLOW%Try uninstalling the app first: adb uninstall com.theveloper.pixelplay.debug%NC%
    goto :end
)

echo %GREEN%✓ APK installed successfully!%NC%

REM Ask if user wants to launch
set /p LAUNCH_CHOICE="Do you want to launch the app? [y/N]: "
if /i "%LAUNCH_CHOICE%"=="y" (
    adb shell am start -n com.theveloper.pixelplay.debug/com.theveloper.pixelplay.MainActivity
    echo %GREEN%✓ App launched!%NC%
)

goto :end

:show_help
echo Usage: build-apk.bat [OPTION]
echo.
echo Options:
echo   debug       Build debug APK ^(default^)
echo   release     Build release APK with optimizations
echo   clean       Clean build artifacts
echo   help        Show this help message
echo.
echo Examples:
echo   build-apk.bat              Build debug APK
echo   build-apk.bat release      Build release APK
echo   build-apk.bat clean        Clean build artifacts
echo.
echo The script will:
echo   1. Check prerequisites ^(Java, Gradle^)
echo   2. Build the APK
echo   3. Show APK location and size
echo   4. Offer to install on connected device ^(if ADB available^)
echo.
exit /b 0

:end
echo.
echo ================================================================
echo   Build Process Complete!
echo ================================================================
echo.
exit /b 0
