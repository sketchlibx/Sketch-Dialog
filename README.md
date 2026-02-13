Ah, samajh gaya! Toh "SketchLib" aapka main brand/app name hai (sketchlib.store), aur yeh specific repository sirf Sketch Dialog ke liye hai.
Maine aapka README.md puri tarah se update kar diya hai. Isme se ScratchView hata diya hai aur sirf Dialog par focus kiya hai, aur aapka exact dependency link bhi add kar diya hai.
Ye raha aapka updated aur perfect README.md:
# Sketch Dialog 💬

[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Website](https://img.shields.io/badge/Website-sketchlib.store-purple.svg)](https://sketchlib.fun)

**Sketch Dialog** (by SketchLib) is a premium, lightweight, and highly customizable UI dialog library for Android. Built with 100% Java, it provides beautiful alert dialogs, popups, and confirmation screens that are incredibly easy to integrate with zero XML layout boilerplate needed!

---

## ✨ Features

* 🌓 **Smart Theming:** Fully supports Auto, Light, and Dark themes.
* 🎬 **Built-in Animations:** Smooth entry animations including Zoom, Fade, and Slide Bottom.
* 📏 **Adaptive Layouts:** Automatically expands to `MATCH_PARENT` if only a single button is provided.
* 🖼️ **Icon Support:** Easily attach native Android system icons (or your own drawables).
* ⚡ **100% Java/Kotlin:** No need to mess with complex XML layout files.

---

## 📦 Installation

**Step 1:** Add the JitPack repository to your root `build.gradle` (or `settings.gradle`):

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url '[https://jitpack.io](https://jitpack.io)' }
    }
}
```

Step 2: Add the dependency to your app-level build.gradle:

```xml
dependencies {
    implementation 'com.github.sketchlibx:Sketch-Dialog:v1.0.0'
}
```

🚀 Usage
1. Global Configuration (Optional but Recommended)
Set your default theme and animation once in your Application class or main onCreate():

```java
// Set defaults for all dialogs
SketchDialog.setDefaultTheme(SketchDialog.Theme.AUTO);
SketchDialog.setDefaultAnimation(SketchDialog.Animation.SLIDE_BOTTOM);

3. Standard Alert / Exit Dialog (Two Buttons)
new SketchDialog.Builder(this)
    .setIcon(android.R.drawable.ic_menu_close_clear_cancel)
    .setTitle("Exit Application?")
    .setMessage("Are you sure you want to close the app?")
    .setPrimaryColor(Color.parseColor("#582C8E")) //Set Custom Color
    .setPositiveButton("Yes, Exit", v -> finishAffinity())
    .setNegativeButton("Cancel", null) // Pass null to simply dismiss
    .show();

4. Update / Success Dialog (Single Button)
When you provide only one button, Sketch Dialog intelligently makes it full-width (MATCH_PARENT).
new SketchDialog.Builder(this)
    .setIcon(android.R.drawable.ic_popup_sync)
    .setTitle("Update Required!")
    .setMessage("A new version of the app is available with amazing new features.")
    .setCancelable(false) // Prevents user from dismissing the dialog
    .setPrimaryColor(Color.parseColor("#2196F3")) 
    .setPositiveButton("Update Now", v -> openPlayStore())
    .show();

5. Forced Dark Mode & Custom Animation Override
You can easily override global settings for a specific dialog.
new SketchDialog.Builder(this)
    .setTheme(SketchDialog.Theme.DARK) // Forces Dark Mode regardless of system settings
    .setAnimation(SketchDialog.Animation.ZOOM) // Overrides global animation
    .setIcon(android.R.drawable.ic_dialog_alert)
    .setTitle("Connection Lost")
    .setMessage("Please check your internet connection and try again.")
    .setPrimaryColor(Color.parseColor("#E53935")) 
    .setPositiveButton("Retry", v -> retryConnection())
    .show(); 
```
🤝 Contribution
Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.
📄 License
This project is licensed under the MIT License - see the LICENSE file for details.

---
