package sketchlib.sketch.dialog.material;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class MaterialDialog extends Dialog {
	
	public enum Theme { LIGHT, DARK, AUTO }
	public enum Animation { NONE, ZOOM, FADE, SLIDE_BOTTOM }
	public enum ProgressStyle { NONE, SPINNER, HORIZONTAL, CIRCULAR }
	
	private static Theme defaultTheme = Theme.AUTO;
	private static Animation defaultAnimation = Animation.ZOOM;
	private static Integer defaultBackgroundColor = null;
	private static Integer defaultPrimaryColor = null;
	
	private LinearProgressIndicator mLinearProgress;
	private CircularProgressIndicator mCircularProgress;
	private TextView mProgressText;
	
	public static void setDefaultTheme(Theme theme) { defaultTheme = theme; }
	public static void setDefaultAnimation(Animation animation) { defaultAnimation = animation; }
	public static void setDefaultBackgroundColor(int color) { defaultBackgroundColor = color; }
	public static void setDefaultPrimaryColor(int color) { defaultPrimaryColor = color; }
	
	private MaterialDialog(Context context) {
		super(context);
	}
	
	public void setProgress(int progress) {
		if (mLinearProgress != null) {
			mLinearProgress.setProgressCompat(progress, true);
		}
		if (mCircularProgress != null) {
			mCircularProgress.setProgressCompat(progress, true);
		}
		if (mProgressText != null) {
			mProgressText.setText(progress + "%");
		}
	}
	
	public static class Builder {
		private final Context context;
		private String title;
		private String message;
		private String positiveText;
		private String negativeText;
		private int iconResId = -1;
		private boolean cancelable = true;
		private ProgressStyle progressStyle = ProgressStyle.NONE;
		
		private Theme theme = defaultTheme;
		private Animation animation = defaultAnimation;
		private Integer primaryColor = null;
		private Integer backgroundColor = null;
		private Integer iconTintColor = null;
		
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
		public Builder setIconTint(int color) { this.iconTintColor = color; return this; }
		public Builder setCancelable(boolean cancelable) { this.cancelable = cancelable; return this; }
		public Builder setPrimaryColor(int color) { this.primaryColor = color; return this; }
		public Builder setBackgroundColor(int color) { this.backgroundColor = color; return this; }
		public Builder setTheme(Theme theme) { this.theme = theme; return this; }
		public Builder setAnimation(Animation animation) { this.animation = animation; return this; }
		
		public Builder setLoading(boolean isLoading) {
			if (isLoading) this.progressStyle = ProgressStyle.SPINNER;
			return this;
		}
		public Builder setHorizontalProgress(boolean isHorizontal) {
			if (isHorizontal) this.progressStyle = ProgressStyle.HORIZONTAL;
			return this;
		}
		public Builder setCircularProgress(boolean isCircular) {
			if (isCircular) this.progressStyle = ProgressStyle.CIRCULAR;
			return this;
		}
		
		public MaterialDialog build() {
			MaterialDialog dialog = new MaterialDialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			if (dialog.getWindow() != null) {
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			}
			dialog.setCancelable(cancelable);
			
			boolean isDark = false;
			if (theme == Theme.DARK) {
				isDark = true;
			} else if (theme == Theme.AUTO) {
				isDark = (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
			}
			
			int finalPrimaryColor;
			if (primaryColor != null) {
				finalPrimaryColor = primaryColor;
			} else if (defaultPrimaryColor != null) {
				finalPrimaryColor = defaultPrimaryColor;
			} else {
				finalPrimaryColor = Color.parseColor("#6750A4");
			}
			
			int finalBgColor;
			if (backgroundColor != null) {
				finalBgColor = backgroundColor;
			} else if (defaultBackgroundColor != null) {
				finalBgColor = defaultBackgroundColor;
			} else {
				finalBgColor = isDark ? Color.parseColor("#2B2930") : Color.parseColor("#F3EDF7");
			}
			
			int titleColor = isDark ? Color.parseColor("#E6E0E9") : Color.parseColor("#1D1B20");
			int msgColor = isDark ? Color.parseColor("#CAC4D0") : Color.parseColor("#49454F");
			int trackColor = Color.argb(40, Color.red(finalPrimaryColor), Color.green(finalPrimaryColor), Color.blue(finalPrimaryColor));
			int btnNegPressedBg = isDark ? Color.parseColor("#1FFFFFFF") : Color.parseColor("#1F000000");
			
			LinearLayout rootLayout = new LinearLayout(context);
			rootLayout.setOrientation(LinearLayout.VERTICAL);
			rootLayout.setGravity(Gravity.CENTER_HORIZONTAL);
			int padding = dpToPx(context, 24);
			rootLayout.setPadding(padding, padding, padding, padding);
			
			GradientDrawable bgDrawable = new GradientDrawable();
			bgDrawable.setColor(finalBgColor);
			bgDrawable.setCornerRadius(dpToPx(context, 28));
			rootLayout.setBackground(bgDrawable);
			
			if (progressStyle == ProgressStyle.HORIZONTAL) {
				Context themeContext = context;
				int wavyStyle = context.getResources().getIdentifier("Widget.Material3Expressive.LinearProgressIndicator.Wavy", "style", context.getPackageName());
				if (wavyStyle != 0) themeContext = new ContextThemeWrapper(context, wavyStyle);
				
				LinearProgressIndicator progressBar = new LinearProgressIndicator(themeContext);
				progressBar.setMax(100);
				progressBar.setProgressCompat(0, false);
				progressBar.setIndicatorColor(finalPrimaryColor);
				progressBar.setTrackColor(trackColor);
				progressBar.setTrackCornerRadius(dpToPx(context, 4));
				
				LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				progressParams.bottomMargin = dpToPx(context, 8);
				rootLayout.addView(progressBar, progressParams);
				
				TextView progressText = new TextView(context);
				progressText.setText("0%");
				progressText.setTextColor(msgColor);
				progressText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				progressText.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
				progressText.setGravity(Gravity.END);
				LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				textParams.bottomMargin = dpToPx(context, 16);
				rootLayout.addView(progressText, textParams);
				
				dialog.mLinearProgress = progressBar;
				dialog.mProgressText = progressText;
			} else if (progressStyle == ProgressStyle.CIRCULAR) {
				FrameLayout frameLayout = new FrameLayout(context);
				LinearLayout.LayoutParams frameParams = new LinearLayout.LayoutParams(dpToPx(context, 72), dpToPx(context, 72));
				frameParams.bottomMargin = dpToPx(context, 16);
				rootLayout.addView(frameLayout, frameParams);
				
				Context themeContext = context;
				int wavyStyle = context.getResources().getIdentifier("Widget.Material3Expressive.CircularProgressIndicator.Wavy", "style", context.getPackageName());
				if (wavyStyle != 0) themeContext = new ContextThemeWrapper(context, wavyStyle);
				
				CircularProgressIndicator circularView = new CircularProgressIndicator(themeContext);
				circularView.setMax(100);
				circularView.setProgressCompat(0, false);
				circularView.setIndicatorColor(finalPrimaryColor);
				circularView.setTrackColor(trackColor);
				circularView.setIndicatorSize(dpToPx(context, 64));
				circularView.setTrackThickness(dpToPx(context, 4));
				
				FrameLayout.LayoutParams circParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				circParams.gravity = Gravity.CENTER;
				frameLayout.addView(circularView, circParams);
				
				TextView progressText = new TextView(context);
				progressText.setText("0%");
				progressText.setTextColor(msgColor);
				progressText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
				progressText.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
				progressText.setGravity(Gravity.CENTER);
				
				FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				textParams.gravity = Gravity.CENTER;
				frameLayout.addView(progressText, textParams);
				
				dialog.mCircularProgress = circularView;
				dialog.mProgressText = progressText;
			} else if (progressStyle == ProgressStyle.SPINNER) {
				Context themeContext = context;
				int wavyStyle = context.getResources().getIdentifier("Widget.Material3Expressive.CircularProgressIndicator.Wavy", "style", context.getPackageName());
				if (wavyStyle != 0) themeContext = new ContextThemeWrapper(context, wavyStyle);
				
				CircularProgressIndicator spinner = new CircularProgressIndicator(themeContext);
				spinner.setIndeterminate(true);
				spinner.setIndicatorColor(finalPrimaryColor);
				spinner.setIndicatorSize(dpToPx(context, 48));
				spinner.setTrackThickness(dpToPx(context, 4));
				
				LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				progressParams.bottomMargin = dpToPx(context, 16);
				rootLayout.addView(spinner, progressParams);
			} else if (iconResId != -1) {
				ImageView iconView = new ImageView(context);
				iconView.setImageResource(iconResId);
				int tint = iconTintColor != null ? iconTintColor : finalPrimaryColor;
				iconView.setColorFilter(tint, PorterDuff.Mode.SRC_IN);
				LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dpToPx(context, 24), dpToPx(context, 24));
				iconParams.bottomMargin = dpToPx(context, 16);
				rootLayout.addView(iconView, iconParams);
			}
			
			if (title != null) {
				TextView titleView = new TextView(context);
				titleView.setText(title);
				titleView.setTextColor(titleColor);
				titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
				titleView.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
				titleView.setGravity(Gravity.CENTER);
				rootLayout.addView(titleView);
			}
			
			if (message != null) {
				TextView msgView = new TextView(context);
				msgView.setText(message);
				msgView.setTextColor(msgColor);
				msgView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				msgView.setGravity(Gravity.CENTER);
				msgView.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, context.getResources().getDisplayMetrics()), 1.0f);
				LinearLayout.LayoutParams msgParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				msgParams.topMargin = dpToPx(context, 16);
				rootLayout.addView(msgView, msgParams);
			}
			
			if (positiveText != null || negativeText != null) {
				LinearLayout btnLayout = new LinearLayout(context);
				btnLayout.setOrientation(LinearLayout.HORIZONTAL);
				btnLayout.setGravity(Gravity.END);
				LinearLayout.LayoutParams btnLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				btnLayoutParams.topMargin = dpToPx(context, 24);
				rootLayout.addView(btnLayout, btnLayoutParams);
				
				boolean isSingleButton = (positiveText == null || negativeText == null);
				
				if (negativeText != null) {
					TextView negBtn = createM3Button(context, negativeText, Color.TRANSPARENT, btnNegPressedBg, finalPrimaryColor);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					isSingleButton ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT, 
					dpToPx(context, 40));
					if (!isSingleButton) params.rightMargin = dpToPx(context, 8);
					negBtn.setOnClickListener(v -> {
						if (negativeListener != null) negativeListener.onClick(v);
						dialog.dismiss();
					});
					btnLayout.addView(negBtn, params);
				}
				
				if (positiveText != null) {
					TextView posBtn = createM3Button(context, positiveText, finalPrimaryColor, manipulateColor(finalPrimaryColor, 0.8f), Color.WHITE);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					isSingleButton ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT, 
					dpToPx(context, 40));
					posBtn.setOnClickListener(v -> {
						if (positiveListener != null) positiveListener.onClick(v);
						dialog.dismiss();
					});
					btnLayout.addView(posBtn, params);
				}
			}
			
			dialog.setContentView(rootLayout);
			
			if (dialog.getWindow() != null) {
				int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
				dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
			}
			
			dialog.setOnShowListener(d -> {
				if (animation == Animation.NONE) return;
				
				rootLayout.setAlpha(0f);
				if (animation == Animation.ZOOM) {
					rootLayout.setScaleX(0.8f);
					rootLayout.setScaleY(0.8f);
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
		
		public MaterialDialog show() {
			MaterialDialog dialog = build();
			dialog.show();
			return dialog;
		}
		
		private TextView createM3Button(Context context, String text, int normalColor, int pressedColor, int textColor) {
			TextView button = new TextView(context);
			button.setText(text);
			button.setTextColor(textColor);
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
			button.setGravity(Gravity.CENTER);
			int paddingH = dpToPx(context, 24);
			button.setPadding(paddingH, 0, paddingH, 0);
			
			GradientDrawable normal = new GradientDrawable();
			normal.setColor(normalColor);
			normal.setCornerRadius(dpToPx(context, 20));
			
			GradientDrawable pressed = new GradientDrawable();
			pressed.setColor(pressedColor);
			pressed.setCornerRadius(dpToPx(context, 20));
			
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
