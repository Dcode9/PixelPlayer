#!/bin/bash

# PixelPlayer APK Build Script
# This script automates the APK building process for PixelPlayer
# Usage: ./build-apk.sh [debug|release|clean]

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Print colored message
print_message() {
    local color=$1
    shift
    echo -e "${color}$@${NC}"
}

# Print header
print_header() {
    echo ""
    print_message "$BLUE" "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    print_message "$BLUE" "  $1"
    print_message "$BLUE" "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
}

# Check prerequisites
check_prerequisites() {
    print_header "🔍 Checking Prerequisites"
    
    # Check if Java is installed
    if ! command -v java &> /dev/null; then
        print_message "$RED" "❌ Java is not installed. Please install JDK 11 or higher."
        exit 1
    fi
    
    # Check Java version
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d. -f1)
    if [ "$JAVA_VERSION" -lt 11 ]; then
        print_message "$RED" "❌ Java version 11 or higher is required. Current version: $JAVA_VERSION"
        exit 1
    fi
    print_message "$GREEN" "✅ Java version: $(java -version 2>&1 | head -n 1)"
    
    # Check if gradlew exists
    if [ ! -f "./gradlew" ]; then
        print_message "$RED" "❌ gradlew not found. Are you in the project root directory?"
        exit 1
    fi
    print_message "$GREEN" "✅ Gradle wrapper found"
    
    # Make gradlew executable
    chmod +x ./gradlew
    
    echo ""
}

# Clean build
clean_build() {
    print_header "🧹 Cleaning Previous Builds"
    ./gradlew clean
    print_message "$GREEN" "✅ Clean completed"
}

# Build APK
build_apk() {
    local build_type=$1
    local gradle_task=""
    local apk_path=""
    
    if [ "$build_type" = "debug" ]; then
        gradle_task="assembleDebug"
        apk_path="app/build/outputs/apk/debug/app-debug.apk"
    elif [ "$build_type" = "release" ]; then
        gradle_task="assembleRelease"
        apk_path="app/build/outputs/apk/release/app-release.apk"
    else
        print_message "$RED" "❌ Invalid build type. Use 'debug' or 'release'"
        exit 1
    fi
    
    print_header "🔨 Building PixelPlayer APK ($build_type)"
    
    # Build with progress
    ./gradlew $gradle_task --console=plain
    
    # Check if APK was created
    if [ -f "$apk_path" ]; then
        print_message "$GREEN" "✅ APK built successfully!"
        echo ""
        print_message "$BLUE" "📍 Location: $apk_path"
        print_message "$BLUE" "📦 Size: $(du -h "$apk_path" | cut -f1)"
        
        # Get APK info if aapt is available
        if command -v aapt &> /dev/null; then
            APP_VERSION=$(aapt dump badging "$apk_path" 2>/dev/null | grep "versionName" | sed -n "s/.*versionName='\([^']*\)'.*/\1/p")
            if [ ! -z "$APP_VERSION" ]; then
                print_message "$BLUE" "📱 Version: $APP_VERSION"
            fi
        fi
        
        echo ""
        return 0
    else
        print_message "$RED" "❌ Build failed! APK not found at $apk_path"
        exit 1
    fi
}

# Install APK
install_apk() {
    local apk_path=$1
    
    print_header "📱 Installing APK"
    
    # Check if ADB is available
    if ! command -v adb &> /dev/null; then
        print_message "$YELLOW" "⚠️  ADB is not available. Please install Android SDK Platform Tools."
        print_message "$YELLOW" "    You can manually install the APK from: $apk_path"
        return 1
    fi
    
    # Check for connected devices
    DEVICES=$(adb devices | grep -v "List" | grep "device$" | wc -l)
    
    if [ "$DEVICES" -eq 0 ]; then
        print_message "$YELLOW" "⚠️  No Android devices connected."
        print_message "$YELLOW" "    Please connect a device with USB debugging enabled."
        print_message "$YELLOW" "    Or manually install the APK from: $apk_path"
        return 1
    fi
    
    print_message "$BLUE" "📱 Found $DEVICES device(s) connected"
    
    # Install APK
    print_message "$BLUE" "Installing APK..."
    if adb install -r "$apk_path" 2>&1 | grep -q "Success"; then
        print_message "$GREEN" "✅ APK installed successfully!"
        
        # Try to launch the app
        read -p "$(echo -e ${YELLOW}Do you want to launch the app? [y/N]:${NC} )" -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            adb shell am start -n com.theveloper.pixelplay.debug/com.theveloper.pixelplay.MainActivity
            print_message "$GREEN" "✅ App launched!"
        fi
    else
        print_message "$YELLOW" "⚠️  Installation may have failed. Try uninstalling the app first:"
        print_message "$YELLOW" "    adb uninstall com.theveloper.pixelplay.debug"
    fi
}

# Show help
show_help() {
    cat << EOF
PixelPlayer APK Build Script

Usage: ./build-apk.sh [OPTION]

Options:
    debug       Build debug APK (default)
    release     Build release APK with optimizations
    clean       Clean build artifacts
    help        Show this help message

Examples:
    ./build-apk.sh              # Build debug APK
    ./build-apk.sh release      # Build release APK
    ./build-apk.sh clean        # Clean build artifacts

The script will:
1. Check prerequisites (Java, Gradle)
2. Build the APK
3. Show APK location and size
4. Offer to install on connected device (if ADB available)

EOF
}

# Main script
main() {
    local build_type=${1:-debug}
    
    # Handle special commands
    case "$build_type" in
        help|--help|-h)
            show_help
            exit 0
            ;;
        clean)
            check_prerequisites
            clean_build
            exit 0
            ;;
        debug|release)
            # Continue with build
            ;;
        *)
            print_message "$RED" "❌ Invalid option: $build_type"
            echo ""
            show_help
            exit 1
            ;;
    esac
    
    # Run build process
    check_prerequisites
    build_apk "$build_type"
    
    # Determine APK path
    if [ "$build_type" = "debug" ]; then
        APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
    else
        APK_PATH="app/build/outputs/apk/release/app-release.apk"
    fi
    
    # Ask if user wants to install
    echo ""
    read -p "$(echo -e ${YELLOW}Do you want to install the APK on a connected device? [y/N]:${NC} )" -n 1 -r
    echo ""
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        install_apk "$APK_PATH"
    else
        print_message "$BLUE" "ℹ️  You can manually install the APK from:"
        print_message "$BLUE" "   $APK_PATH"
    fi
    
    print_header "🎉 Build Process Complete!"
}

# Run main function
main "$@"
