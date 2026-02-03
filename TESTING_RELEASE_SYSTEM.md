# 🧪 Testing the APK Release System

This document explains how to test the automated APK build and release system.

## ✅ What Has Been Set Up

The repository now has an automated APK build and release system with:

1. **GitHub Actions Workflow** (`.github/workflows/build-apk.yml`)
   - Builds APKs on every push to main
   - Builds APKs on every pull request
   - Creates releases automatically when tags are pushed
   - Can be triggered manually via GitHub UI

2. **Release Creation Script** (`create-release.sh`)
   - Interactive script to bump version and create releases
   - Handles git tagging and pushing

3. **Documentation**
   - `RELEASE_GUIDE.md` - Comprehensive guide for creating releases
   - Updated `README.md` with download links
   - Updated `DOCS_INDEX.md` with new documentation

## 🧪 How to Test

### Test 1: Verify Workflow Syntax

The workflow file has been validated for:
- ✅ YAML syntax correctness
- ✅ No trailing spaces
- ✅ Proper job dependencies
- ✅ Correct artifact upload/download

You can verify this yourself:
```bash
python3 -c "import yaml; yaml.safe_load(open('.github/workflows/build-apk.yml'))"
```

### Test 2: Trigger Manual Workflow Run (Recommended First Test)

1. **Go to GitHub Actions**
   - Navigate to: https://github.com/Dcode9/PixelPlayer/actions
   
2. **Select the Workflow**
   - Click on "Build Android APK" in the left sidebar
   
3. **Run Workflow**
   - Click the "Run workflow" button
   - Select the branch (e.g., `copilot/build-apk-file-release`)
   - Click "Run workflow"

4. **Monitor Progress**
   - The workflow should complete in 5-10 minutes
   - Watch for any errors in the build logs

5. **Download APK Artifacts**
   - Once complete, scroll down to "Artifacts"
   - Download `PixelPlayer-debug-apk` and/or `PixelPlayer-release-apk`
   - Extract the ZIP file to get the APK

6. **Install on Device**
   - Transfer APK to your Android device
   - Install and verify it works

### Test 3: Test Release Creation (Creates Actual Release)

⚠️ **Warning:** This will create a real release on GitHub. Only do this when you're ready to publish.

**Option A: Using the Script (Easier)**

```bash
# Run the interactive script
./create-release.sh

# Follow the prompts:
# 1. Enter new version name (e.g., 0.5.1-beta)
# 2. Enter new version code (e.g., 8)
# 3. Confirm the changes
# 4. Push to GitHub
```

**Option B: Manual Process**

```bash
# 1. Update version in gradle.properties
# Edit gradle.properties and change:
#   APP_VERSION_NAME=0.5.1-beta
#   APP_VERSION_CODE=8

# 2. Commit the change
git add gradle.properties
git commit -m "Bump version to 0.5.1-beta"

# 3. Create tag
git tag -a v0.5.1-beta -m "Release version 0.5.1-beta"

# 4. Push to GitHub
git push origin main
git push origin v0.5.1-beta
```

**What Should Happen:**
1. GitHub Actions will start automatically
2. Build job will compile both debug and release APKs
3. Release job will create a GitHub Release
4. APKs will be attached to the release with proper naming
5. Release notes will be auto-generated

**Verify the Release:**
1. Go to: https://github.com/Dcode9/PixelPlayer/releases
2. Find your new release (e.g., v0.5.1-beta)
3. Check that it has:
   - Proper title and description
   - Two APK files attached
   - Correct version information
   - Marked as pre-release (if beta/alpha)

4. Download an APK and test installation

### Test 4: Test APK Download from Release

1. **Navigate to Releases**
   - Go to: https://github.com/Dcode9/PixelPlayer/releases
   - Click on the latest release

2. **Download APK**
   - Click on the release APK file (e.g., `PixelPlayer-0.5.1-beta-release.apk`)
   - Save to your computer

3. **Transfer to Android Device**
   - Via USB cable
   - Or via cloud storage/email

4. **Install**
   - Open the APK file on your Android device
   - Allow installation from unknown sources if prompted
   - Complete installation

5. **Test the App**
   - Launch PixelPlayer
   - Verify it works correctly
   - Check version in app settings (if available)

## 🔍 What to Look For

### Successful Build Indicators

✅ **GitHub Actions should show:**
- All steps completed with green checkmarks
- Build job completed successfully
- Release job completed successfully (when tag is pushed)
- Artifacts uploaded successfully

✅ **Release should have:**
- Correct version number in title
- Two APK files attached
- Auto-generated description with installation instructions
- Proper pre-release flag (for beta/alpha versions)

✅ **APK should:**
- Install successfully on Android device
- Launch without crashing
- Show correct version (if visible in app)
- Function as expected

### Common Issues and Solutions

❌ **Build fails with "SDK not found"**
- This should not happen in GitHub Actions as SDK is installed
- If it does, check the SDK installation step in the workflow

❌ **Release not created**
- Verify the tag starts with 'v' (e.g., v0.5.0)
- Check that the build job completed successfully
- Verify GitHub Actions has write permissions

❌ **APK not attached to release**
- Check artifact upload/download steps in workflow
- Verify the APK path is correct
- Check for errors in the release job logs

❌ **Can't install APK on device**
- Enable "Install from unknown sources" in Android settings
- Verify the APK is for the correct architecture
- Check that you're not trying to downgrade versions

## 📊 Expected Workflow Duration

- **Build Job:** 5-8 minutes
  - Checkout: ~10 seconds
  - Setup JDK: ~15 seconds
  - Setup Android SDK: ~1-2 minutes
  - Build debug APK: ~2-3 minutes
  - Build release APK: ~2-3 minutes
  - Upload artifacts: ~10 seconds

- **Release Job:** 1-2 minutes
  - Download artifacts: ~5 seconds
  - Create release: ~5 seconds
  - Upload assets: ~30 seconds

**Total:** ~7-10 minutes per release

## 📝 Test Checklist

Before merging this PR, verify:

- [ ] Workflow file syntax is valid
- [ ] Manual workflow run completes successfully
- [ ] Both debug and release APKs are generated
- [ ] APK artifacts can be downloaded from GitHub Actions
- [ ] APKs install successfully on Android device
- [ ] APKs launch and function correctly
- [ ] Release script (`create-release.sh`) is executable
- [ ] Documentation is clear and accurate

Optional (can be done after merge):

- [ ] Create a test release with a tag
- [ ] Verify release is created automatically
- [ ] Verify APKs are attached to release
- [ ] Verify release notes are properly formatted
- [ ] Test download from GitHub Releases page
- [ ] Test installation from GitHub Releases

## 🎯 Recommended Testing Approach

### For Initial Testing (Before Creating Real Release)

1. **Merge this PR** to main branch
2. **Run manual workflow** from GitHub Actions UI
3. **Download and test artifacts**
4. **Verify everything works**

### For Creating First Real Release

1. **Update CHANGELOG.md** with changes
2. **Run release script** or manually bump version
3. **Push tag** to trigger release
4. **Verify release creation**
5. **Test download and installation**
6. **Announce release** to users

## 📚 Additional Resources

- **GitHub Actions Docs:** https://docs.github.com/en/actions
- **Android APK Building:** https://developer.android.com/studio/build
- **Release Management:** See `RELEASE_GUIDE.md`

## 🆘 Getting Help

If you encounter issues:

1. **Check GitHub Actions logs** for detailed error messages
2. **Review the workflow file** for configuration issues
3. **Consult RELEASE_GUIDE.md** for release creation help
4. **Open an issue** on GitHub with:
   - Description of the problem
   - Link to failed workflow run
   - Error messages
   - Steps to reproduce

## ✨ Next Steps

After successful testing:

1. **Merge this PR** to enable automated releases
2. **Create first release** when ready to publish
3. **Share download link** with users
4. **Update README** with latest release badge
5. **Announce on social media** or relevant channels

---

**Note:** The workflow is configured to:
- Build APKs on every push to main (for testing)
- Create releases only when tags are pushed (for distribution)
- Allow manual triggering anytime (for testing)

This gives you flexibility to test builds without creating releases, and create releases only when you're ready to publish.
