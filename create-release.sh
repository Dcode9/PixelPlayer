#!/bin/bash

# PixelPlayer Release Creation Script
# This script helps create a new release by tagging and pushing to GitHub
# Usage: ./create-release.sh

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_message() {
    local color=$1
    shift
    echo -e "${color}$@${NC}"
}

print_header() {
    echo ""
    print_message "$BLUE" "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    print_message "$BLUE" "  $1"
    print_message "$BLUE" "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
}

print_header "🚀 PixelPlayer Release Creator"

# Check if we're in a git repository
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    print_message "$RED" "❌ Error: Not in a git repository"
    exit 1
fi

# Get current version from gradle.properties
if [ ! -f "gradle.properties" ]; then
    print_message "$RED" "❌ Error: gradle.properties not found"
    exit 1
fi

CURRENT_VERSION=$(grep "APP_VERSION_NAME" gradle.properties | cut -d'=' -f2)
CURRENT_VERSION_CODE=$(grep "APP_VERSION_CODE" gradle.properties | cut -d'=' -f2)

print_message "$BLUE" "Current version: $CURRENT_VERSION (code: $CURRENT_VERSION_CODE)"
echo ""

# Check for uncommitted changes
if ! git diff-index --quiet HEAD --; then
    print_message "$YELLOW" "⚠️  Warning: You have uncommitted changes"
    git status --short
    echo ""
    read -p "$(echo -e ${YELLOW}Do you want to continue anyway? [y/N]:${NC} )" -n 1 -r
    echo ""
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_message "$BLUE" "ℹ️  Release cancelled. Please commit your changes first."
        exit 0
    fi
fi

# Ask for new version
echo ""
print_message "$YELLOW" "Enter new version name (e.g., 0.5.1-beta, 1.0.0):"
read -p "> " NEW_VERSION

if [ -z "$NEW_VERSION" ]; then
    print_message "$RED" "❌ Error: Version cannot be empty"
    exit 1
fi

# Ask for new version code
print_message "$YELLOW" "Enter new version code (integer, current: $CURRENT_VERSION_CODE):"
read -p "> " NEW_VERSION_CODE

if [ -z "$NEW_VERSION_CODE" ]; then
    print_message "$RED" "❌ Error: Version code cannot be empty"
    exit 1
fi

# Validate version code is a number
if ! [[ "$NEW_VERSION_CODE" =~ ^[0-9]+$ ]]; then
    print_message "$RED" "❌ Error: Version code must be an integer"
    exit 1
fi

# Check if version code is greater than current
if [ "$NEW_VERSION_CODE" -le "$CURRENT_VERSION_CODE" ]; then
    print_message "$YELLOW" "⚠️  Warning: New version code ($NEW_VERSION_CODE) should be greater than current ($CURRENT_VERSION_CODE)"
    read -p "$(echo -e ${YELLOW}Continue anyway? [y/N]:${NC} )" -n 1 -r
    echo ""
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 0
    fi
fi

echo ""
print_header "📝 Summary"
print_message "$BLUE" "Current version: $CURRENT_VERSION (code: $CURRENT_VERSION_CODE)"
print_message "$GREEN" "New version:     $NEW_VERSION (code: $NEW_VERSION_CODE)"
echo ""

read -p "$(echo -e ${YELLOW}Proceed with release? [y/N]:${NC} )" -n 1 -r
echo ""
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    print_message "$BLUE" "ℹ️  Release cancelled"
    exit 0
fi

# Update gradle.properties
print_message "$BLUE" "📝 Updating gradle.properties..."
sed -i "s/APP_VERSION_NAME=.*/APP_VERSION_NAME=$NEW_VERSION/" gradle.properties
sed -i "s/APP_VERSION_CODE=.*/APP_VERSION_CODE=$NEW_VERSION_CODE/" gradle.properties
print_message "$GREEN" "✅ Updated gradle.properties"

# Commit the change
print_message "$BLUE" "💾 Committing version bump..."
git add gradle.properties
git commit -m "Bump version to $NEW_VERSION"
print_message "$GREEN" "✅ Committed version bump"

# Create tag
TAG_NAME="v$NEW_VERSION"
print_message "$BLUE" "🏷️  Creating tag $TAG_NAME..."
git tag -a "$TAG_NAME" -m "Release version $NEW_VERSION"
print_message "$GREEN" "✅ Created tag $TAG_NAME"

# Push to remote
echo ""
print_message "$YELLOW" "Ready to push to GitHub!"
print_message "$BLUE" "This will:"
print_message "$BLUE" "  1. Push the version bump commit"
print_message "$BLUE" "  2. Push the tag $TAG_NAME"
print_message "$BLUE" "  3. Trigger GitHub Actions to build and release APKs"
echo ""

read -p "$(echo -e ${YELLOW}Push to GitHub now? [y/N]:${NC} )" -n 1 -r
echo ""
if [[ $REPLY =~ ^[Yy]$ ]]; then
    print_message "$BLUE" "📤 Pushing to GitHub..."
    
    # Get current branch
    CURRENT_BRANCH=$(git branch --show-current)
    
    # Push commit
    git push origin "$CURRENT_BRANCH"
    
    # Push tag
    git push origin "$TAG_NAME"
    
    print_message "$GREEN" "✅ Pushed to GitHub!"
    echo ""
    print_header "🎉 Release Created Successfully!"
    echo ""
    print_message "$GREEN" "Tag: $TAG_NAME"
    print_message "$GREEN" "Version: $NEW_VERSION (code: $NEW_VERSION_CODE)"
    echo ""
    print_message "$BLUE" "📊 Monitor the build progress at:"
    print_message "$BLUE" "   https://github.com/Dcode9/PixelPlayer/actions"
    echo ""
    print_message "$BLUE" "📦 Once complete, the release will be available at:"
    print_message "$BLUE" "   https://github.com/Dcode9/PixelPlayer/releases/tag/$TAG_NAME"
    echo ""
else
    print_message "$BLUE" "ℹ️  Push cancelled. You can push manually later with:"
    print_message "$BLUE" "   git push origin $CURRENT_BRANCH"
    print_message "$BLUE" "   git push origin $TAG_NAME"
fi

echo ""
