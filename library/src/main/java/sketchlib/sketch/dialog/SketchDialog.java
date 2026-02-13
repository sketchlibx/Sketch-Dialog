package sketchlib.sketch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SketchDialog extends Dialog {

    public enum Theme { LIGHT, DARK, AUTO }
    public enum Animation { NONE, ZOOM, FADE, SLIDE_BOTTOM }

    // --- GLOBAL DEFAULTS ---
    private static Theme defaultTheme = Theme.AUTO;
    private static Animation defaultAnimation = Animation.ZOOM;

    // Global setters: Ise app start hote hi ek baar call karein
    public static void setDefaultTheme(Theme theme) { defaultTheme = theme; }
    public static void setDefaultAnimation(Animation animation) { defaultAnimation = animation; }

    private SketchDialog(Context context) {
        super(context);
    }

    public static class Builder {
        private final Context context;
        private String title;
        private String message;
        private String positiveText;
        private String negativeText;
        private int iconResId = -1;
        private boolean cancelable = true;

        // Initialize with Global Defaults
        private Theme theme = defaultTheme;
        private Animation animation = defaultAnimation;
        private int primaryColor = Color.parseColor("#582C8E"); // Purple default

        private View.OnClickListener positiveListener;
        private View.OnClickListener negativeListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) { this.title = title; return this; }
        public Builder setMessage(String message) { this.message = message; return this; }
        public Builder setPositiveButton(String text, View.OnClickListener listener) {
            this.positiveText = text; this.positiveListener = listener; return this;
        }
        public Builder setNegativeButton(String text, View.OnClickListener listener) {
            this.negativeText = text; this.negativeListener = listener; return this;
        }
        public Builder setIcon(int iconResId) { this.iconResId = iconResId; return this; }
        public Builder setCancelable(boolean cancelable) { this.cancelable = cancelable; return this; }
        public Builder setPrimaryColor(int color) { this.primaryColor = color; return this; }
        
        // Override global settings for a specific dialog
        public Builder setTheme(Theme theme) { this.theme = theme; return this; }
        public Builder setAnimation(Animation animation) { this.animation = animation; return this; }

        public SketchDialog build() {
            SketchDialog dialog = new SketchDialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            dialog.setCancelable(cancelable);

            // --- Determine Colors based on Theme ---
            boolean isDark = theme == Theme.DARK || 
                (theme == Theme.AUTO && (context.getResources().getConfiguration().uiMode 
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES);

            int bgColor = isDark ? Color.parseColor("#1C1C22") : Color.parseColor("#FFFFFF");
            int titleColor = isDark ? Color.parseColor("#FFFFFF") : Color.parseColor("#1A1A1A");
            int msgColor = isDark ? Color.parseColor("#A0A0A5") : Color.parseColor("#666666");
            int btnNegBg = isDark ? Color.parseColor("#2C2C35") : Color.parseColor("#F0F0F0");
            int btnNegText = isDark ? Color.parseColor("#FFFFFF") : Color.parseColor("#333333");

            // --- Create Dynamic Layout ---
            LinearLayout rootLayout = new LinearLayout(context);
            rootLayout.setOrientation(LinearLayout.VERTICAL);
            rootLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            int padding = dpToPx(context, 24);
            rootLayout.setPadding(padding, padding, padding, padding);

            // Background with Rounded Corners
            GradientDrawable bgDrawable = new GradientDrawable();
            bgDrawable.setColor(bgColor);
            bgDrawable.setCornerRadius(dpToPx(context, 16));
            rootLayout.setBackground(bgDrawable);

            // 1. Icon
            if (iconResId != -1) {
                ImageView iconView = new ImageView(context);
                iconView.setImageResource(iconResId);
                LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dpToPx(context, 56), dpToPx(context, 56));
                iconParams.bottomMargin = dpToPx(context, 16);
                rootLayout.addView(iconView, iconParams);
            }

            // 2. Title
            if (title != null) {
                TextView titleView = new TextView(context);
                titleView.setText(title);
                titleView.setTextColor(titleColor);
                titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                titleView.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titleView.setGravity(Gravity.CENTER);
                rootLayout.addView(titleView);
            }

            // 3. Message
            if (message != null) {
                TextView msgView = new TextView(context);
                msgView.setText(message);
                msgView.setTextColor(msgColor);
                msgView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                msgView.setGravity(Gravity.CENTER);
                msgView.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics()), 1.0f);
                LinearLayout.LayoutParams msgParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                msgParams.topMargin = dpToPx(context, 8);
                rootLayout.addView(msgView, msgParams);
            }

            // 4. Buttons Layout
            if (positiveText != null || negativeText != null) {
                LinearLayout btnLayout = new LinearLayout(context);
                btnLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams btnLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                btnLayoutParams.topMargin = dpToPx(context, 24);
                rootLayout.addView(btnLayout, btnLayoutParams);

                // Check if only one button is provided
                boolean isSingleButton = (positiveText == null || negativeText == null);

                // Negative Button
                if (negativeText != null) {
                    TextView negBtn = createButton(context, negativeText, btnNegBg, btnNegText);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            isSingleButton ? ViewGroup.LayoutParams.MATCH_PARENT : 0, 
                            dpToPx(context, 48), 
                            isSingleButton ? 0f : 1f);
                    
                    if (!isSingleButton) params.rightMargin = dpToPx(context, 8);
                    
                    negBtn.setOnClickListener(v -> {
                        if (negativeListener != null) negativeListener.onClick(v);
                        dialog.dismiss();
                    });
                    btnLayout.addView(negBtn, params);
                }

                // Positive Button
                if (positiveText != null) {
                    TextView posBtn = createButton(context, positiveText, primaryColor, Color.WHITE);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            isSingleButton ? ViewGroup.LayoutParams.MATCH_PARENT : 0, 
                            dpToPx(context, 48), 
                            isSingleButton ? 0f : 1f);
                    
                    if (!isSingleButton) params.leftMargin = dpToPx(context, 8);
                    
                    posBtn.setOnClickListener(v -> {
                        if (positiveListener != null) positiveListener.onClick(v);
                        dialog.dismiss();
                    });
                    btnLayout.addView(posBtn, params);
                }
            }

            dialog.setContentView(rootLayout);

            // Set Dialog Width to 85% of screen
            if (dialog.getWindow() != null) {
                int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.85);
                dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            // --- Setup Animations ---
            dialog.setOnShowListener(d -> {
                if (animation == Animation.NONE) return;

                rootLayout.setAlpha(0f);
                if (animation == Animation.ZOOM) {
                    rootLayout.setScaleX(0.7f);
                    rootLayout.setScaleY(0.7f);
                    rootLayout.animate().alpha(1f).scaleX(1f).scaleY(1f)
                            .setDuration(300).setInterpolator(new OvershootInterpolator()).start();
                } else if (animation == Animation.FADE) {
                    rootLayout.animate().alpha(1f).setDuration(250).start();
                } else if (animation == Animation.SLIDE_BOTTOM) {
                    rootLayout.setTranslationY(150f);
                    rootLayout.animate().alpha(1f).translationY(0f)
                            .setDuration(300).setInterpolator(new DecelerateInterpolator()).start();
                }
            });

            return dialog;
        }

        public SketchDialog show() {
            SketchDialog dialog = build();
            dialog.show();
            return dialog;
        }

        private TextView createButton(Context context, String text, int bgColor, int textColor) {
            TextView button = new TextView(context);
            button.setText(text);
            button.setTextColor(textColor);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            button.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            button.setGravity(Gravity.CENTER);

            GradientDrawable normal = new GradientDrawable();
            normal.setColor(bgColor);
            normal.setCornerRadius(dpToPx(context, 12));

            GradientDrawable pressed = new GradientDrawable();
            pressed.setColor(manipulateColor(bgColor, 0.8f));
            pressed.setCornerRadius(dpToPx(context, 12));

            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{android.R.attr.state_pressed}, pressed);
            states.addState(new int[]{}, normal);

            button.setBackground(states);
            button.setClickable(true);
            button.setFocusable(true);
            return button;
        }

        private int dpToPx(Context context, float dp) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        }

        private int manipulateColor(int color, float factor) {
            int a = Color.alpha(color);
            int r = Math.round(Color.red(color) * factor);
            int g = Math.round(Color.green(color) * factor);
            int b = Math.round(Color.blue(color) * factor);
            return Color.argb(a, Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));
        }
    }
}
