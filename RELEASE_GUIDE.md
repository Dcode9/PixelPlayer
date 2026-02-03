# 🚀 Release Guide for PixelPlayer

This guide explains how to create releases and make APKs available for download.

## 📦 Automatic Release Creation

The repository is configured to automatically build and release APK files when you create a version tag.

### Creating a Release

1. **Update Version Information**
   
   Edit `gradle.properties` and update the version:
   ```properties
   APP_VERSION_NAME=0.5.0-beta
   APP_VERSION_CODE=7
   ```
   
   - `APP_VERSION_NAME`: Human-readable version (e.g., "1.0.0", "1.0.0-beta")
   - `APP_VERSION_CODE`: Integer version code (increment for each release)

2. **Commit the Version Change**
   
   ```bash
   git add gradle.properties
   git commit -m "Bump version to 0.5.0-beta"
   ```

3. **Create and Push a Git Tag**
   
   ```bash
   # Create a tag matching the version (must start with 'v')
   git tag -a v0.5.0-beta -m "Release version 0.5.0-beta"
   
   # Push the tag to GitHub
   git push origin v0.5.0-beta
   ```

4. **Wait for GitHub Actions**
   
   - GitHub Actions will automatically:
     - Build both debug and release APKs
     - Create a new GitHub Release
     - Upload the APK files to the release
   
   - You can monitor the progress at:
     `https://github.com/Dcode9/PixelPlayer/actions`

5. **Release is Ready!**
   
   - The release will be available at:
     `https://github.com/Dcode9/PixelPlayer/releases`
   
   - APK files will be named:
     - `PixelPlayer-{version}-release.apk`
     - `PixelPlayer-{version}-debug.apk`

### Release Types

The workflow automatically determines the release type based on the version name:

- **Pre-release** (beta/alpha): If version contains "beta" or "alpha"
  - Example: `v0.5.0-beta`, `v1.0.0-alpha`
  - Marked as pre-release on GitHub

- **Stable release**: Any other version
  - Example: `v1.0.0`, `v2.1.3`
  - Marked as latest release on GitHub

## 📥 Downloading APK

Users can download APK files in two ways:

### Method 1: From GitHub Releases (Recommended)

1. Go to the [Releases page](https://github.com/Dcode9/PixelPlayer/releases)
2. Find the desired version
3. Download either:
   - `PixelPlayer-{version}-release.apk` (recommended for users)
   - `PixelPlayer-{version}-debug.apk` (for testing/debugging)

### Method 2: From GitHub Actions Artifacts

APKs are also available as artifacts from each workflow run:

1. Go to [Actions](https://github.com/Dcode9/PixelPlayer/actions)
2. Click on a successful workflow run
3. Download artifacts:
   - `PixelPlayer-debug-apk`
   - `PixelPlayer-release-apk`

Note: Artifacts are available for 90 days and require GitHub login.

## 🛠️ Manual Release Creation

If you prefer to create releases manually:

1. **Build APKs Locally**
   ```bash
   ./build-apk.sh release
   ```

2. **Create a Release on GitHub**
   - Go to https://github.com/Dcode9/PixelPlayer/releases/new
   - Choose a tag or create a new one
   - Add release notes
   - Upload the APK files manually

3. **Publish the Release**

## 📱 APK Signing

### Debug Signing (Current)

Currently, both debug and release APKs are signed with the debug keystore:
- Good for testing and development
- Can be installed alongside other debug versions
- **Not suitable for Google Play Store**

### Production Signing (Future)

For production releases to Google Play Store, you'll need to:

1. Create a release keystore:
   ```bash
   keytool -genkey -v -keystore release.keystore -alias pixelplayer \
           -keyalg RSA -keysize 2048 -validity 10000
   ```

2. Configure GitHub Secrets:
   - `RELEASE_KEYSTORE`: Base64 encoded keystore file
   - `RELEASE_KEYSTORE_PASSWORD`: Keystore password
   - `RELEASE_KEY_ALIAS`: Key alias
   - `RELEASE_KEY_PASSWORD`: Key password

3. Update `app/build.gradle.kts` to use the release keystore

## 🔍 Troubleshooting

### Release Not Created

- **Check if the tag starts with 'v'**: Tags must start with 'v' (e.g., `v1.0.0`)
- **Check GitHub Actions**: Ensure the workflow completed successfully
- **Check permissions**: The workflow needs write permissions for contents

### APK Not Building

- **Check Java version**: Requires JDK 17
- **Check Android SDK**: Requires Android SDK 35
- **Check build logs**: View detailed logs in GitHub Actions

### Version Conflicts

- **Increment version code**: Always increment `APP_VERSION_CODE` when releasing
- **Use semantic versioning**: Follow [semver](https://semver.org/) for `APP_VERSION_NAME`

## 📋 Release Checklist

Before creating a release:

- [ ] Update `APP_VERSION_NAME` in `gradle.properties`
- [ ] Update `APP_VERSION_CODE` in `gradle.properties`
- [ ] Update `CHANGELOG.md` with changes
- [ ] Test the app locally
- [ ] Commit all changes
- [ ] Create and push version tag
- [ ] Verify GitHub Actions workflow succeeds
- [ ] Test downloaded APK on device
- [ ] Add release notes if needed

## 📚 Additional Resources

- [GitHub Releases Documentation](https://docs.github.com/en/repositories/releasing-projects-on-github)
- [Android App Signing](https://developer.android.com/studio/publish/app-signing)
- [Semantic Versioning](https://semver.org/)

## 🎯 Quick Commands

```bash
# Update version in gradle.properties (edit manually)
# Then commit and tag:
git add gradle.properties
git commit -m "Bump version to X.Y.Z"
git tag -a vX.Y.Z -m "Release version X.Y.Z"
git push origin main
git push origin vX.Y.Z

# Check release status
# Visit: https://github.com/Dcode9/PixelPlayer/actions
```

---

For questions or issues, please open an issue on the [GitHub repository](https://github.com/Dcode9/PixelPlayer/issues).
