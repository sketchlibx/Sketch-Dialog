Sketch Dialog 💬
Sketch Dialog (by SketchLib) is a premium, lightweight, and highly customizable UI dialog library for Android. Built with 100% Java, it provides beautiful alert dialogs, popups, progress dialogs, and confirmation screens that are incredibly easy to integrate with zero XML layout boilerplate needed!
Now featuring a Multi-Module architecture, you can choose between our Simple design or the new Material Design 3 (M3), along with a stunning new Glassmorphism effect!
<p align="center">
<img src="preview.gif" width="250" alt="Demo GIF"/>
</p>

✨ Features

 * 🎨 Two Design Languages: Choose between simple and material modules based on your app's UI.
 * 🪞 Glassmorphism (Blur Effect): Premium semi-transparent glassy look with true background blur (Android 12+).
 * 🌓 Smart Theming: Fully supports Auto, Light, and Dark themes.
 * 🌈 Full Color Control: Customize Background, Primary, and Icon colors globally or per dialog.
 * 🎬 Built-in Animations: Smooth entry animations including Zoom, Fade, and Slide Bottom.
 * ⏳ Progress Indicators: Built-in support for Spinners, Horizontal, and Circular progress bars (with M3 Wavy styles).
 * 📏 Adaptive Layouts: Automatically expands to MATCH_PARENT if only a single button is provided.
 * 🖼️ Icon Support: Easily attach native Android system icons with custom tinting.
 * ⚡ 100% Java/Kotlin: No need to mess with complex XML layout files.
📦 Installation
Step 1: Add the JitPack repository to your root build.gradle (or settings.gradle):

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2: Add the dependencies to your app-level build.gradle. You can choose one or both depending on your needs:
```gradle
dependencies {
    // For Material Design 3 (M3) Dialogs
    implementation 'com.github.sketchlibx.Sketch-Dialog:material:v1.0.3'

    // For Simple/Classic Dialogs
    implementation 'com.github.sketchlibx.Sketch-Dialog:simple:v1.0.3'
}
```

🚀 Usage
> 💡 Note: Both MaterialDialog and SketchDialog share the exact same Builder API! Simply swap the class name based on the design you want.
> 
1. Global Configuration (Optional but Recommended)
Set your default theme, animation, colors, and glassy mode once in your Application class or main onCreate(). This applies to all dialogs unless overridden.

```java
// Configure Material Dialogs
MaterialDialog.setDefaultTheme(MaterialDialog.Theme.AUTO);
MaterialDialog.setDefaultAnimation(MaterialDialog.Animation.SLIDE_BOTTOM);
MaterialDialog.setDefaultPrimaryColor(Color.parseColor("#6750A4")); // Optional Global Primary
MaterialDialog.setDefaultBackgroundColor(Color.WHITE); // Optional Global Background
MaterialDialog.setDefaultGlassyMode(true); // Enable Global Glassmorphism

// Configure Simple Dialogs
SketchDialog.setDefaultTheme(SketchDialog.Theme.AUTO);
SketchDialog.setDefaultAnimation(SketchDialog.Animation.SLIDE_BOTTOM);
SketchDialog.setDefaultGlassyMode(true);
```

2. Standard Alert / Exit Dialog (Two Buttons)
```java
new MaterialDialog.Builder(this)
    .setIcon(android.R.drawable.ic_menu_close_clear_cancel)
    .setTitle("Exit Application?")
    .setMessage("Are you sure you want to close the app?")
    .setPrimaryColor(Color.parseColor("#6750A4")) // Custom Primary Color
    .setPositiveButton("Yes, Exit", v -> finishAffinity())
    .setNegativeButton("Cancel", null) // Pass null to simply dismiss
    .show();
```

3. Update / Success Dialog (Single Button)
When you provide only one button, the library intelligently makes it full-width (MATCH_PARENT).
```java
new SketchDialog.Builder(this)
    .setIcon(android.R.drawable.ic_popup_sync)
    .setTitle("Update Required!")
    .setMessage("A new version of the app is available with amazing new features.")
    .setCancelable(false) // Prevents user from dismissing the dialog
    .setPrimaryColor(Color.parseColor("#2196F3"))
    .setPositiveButton("Update Now", v -> openPlayStore())
    .show();
```

4. Progress & Loading Dialogs
You can easily show spinners or trackable progress bars without creating extra layouts.
```java
// MaterialDialog uses Native M3 Progress Indicators.
MaterialDialog progressDialog = new MaterialDialog.Builder(this)
    .setCircularProgress(true) // Or use .setHorizontalProgress(true) / .setLoading(true)
    .setTitle("Uploading File")
    .setMessage("Please wait while we upload your file.")
    .setPrimaryColor(Color.parseColor("#2196F3"))
    .setCancelable(false)
    .build();
    
progressDialog.show();

// Dynamically update progress later in your code:
progressDialog.setProgress(45);
```

5. Advanced Customization (Colors, Icons & Glassy Mode)
You can override global settings for specific dialogs, including the background color, icon tint, and blur effects.
```java
new MaterialDialog.Builder(this)
    .setTheme(MaterialDialog.Theme.DARK) // Force Dark Mode
    .setAnimation(MaterialDialog.Animation.ZOOM) // Override Animation
    .setGlassyMode(true) // Enable Glassmorphism/Blur just for this dialog
    
    // Icon Customization
    .setIcon(android.R.drawable.ic_dialog_alert)
    .setIconTint(Color.YELLOW)
    
    // Color Overrides
    .setBackgroundColor(Color.parseColor("#121212")) // Custom Background
    .setPrimaryColor(Color.parseColor("#E53935")) // Custom Primary
    
    .setTitle("Connection Lost")
    .setMessage("Please check your internet connection and try again.")
    .setPositiveButton("Retry", v -> retryConnection())
    .show();
```

🤝 Contribution
Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.
📄 License
This project is licensed under the MIT License - see the LICENSE file for details.
