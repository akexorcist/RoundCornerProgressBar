/*

Copyright [yyyy] [name of copyright owner]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/

package com.akexorcist.roundcornerprogressbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RoundCornerProgressBar extends LinearLayout {	
	private final static int DEFAULT_PROGRESS_BAR_HEIGHT = 30;
	
	private LinearLayout layoutBackground;
	private LinearLayout layoutProgress;
	private int backgroundWidth = 0;
	private int backgroundHeight = 0;
	
	private boolean isProgressBarCreated = false;
	private boolean isProgressSetBeforeDraw = false;
	private boolean isMaxProgressSetBeforeDraw = false;
	private boolean isBackgroundColorSetBeforeDraw = false;
	private boolean isProgressColorSetBeforeDraw = false;
	
	private float max = 100;
	private float progress = 0;
	private int radius = 10;
	private int padding = 5;
	private int progressColor = Color.parseColor("#8a8a8a");
	private int backgroundColor = Color.parseColor("#595959");

	@SuppressLint("NewApi")
	public RoundCornerProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode()) {
			isProgressBarCreated = false;
			isProgressSetBeforeDraw = false;
			isMaxProgressSetBeforeDraw = false;
			isBackgroundColorSetBeforeDraw = false;
			isProgressColorSetBeforeDraw = false;
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.round_corner_layout, this);
			setup(context, attrs);
			isProgressBarCreated = true;
		} else {
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				setBackground(new ColorDrawable(Color.parseColor("#CCCCCC")));
	    	} else {
				setBackgroundColor(Color.parseColor("#CCCCCC"));
	    	}
			setGravity(Gravity.CENTER);
	        
			TextView tv = new TextView(context);
			tv.setText("RoundCornerProgressBar");
			addView(tv);
		}
	}
	
	@SuppressLint("NewApi")
	private void setup(Context context, AttributeSet attrs) {
		int color;
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerProgress);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
		radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, metrics);
		radius = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_backgroundRadius, radius);
		
		layoutBackground = (LinearLayout) findViewById(R.id.round_corner_progress_background);
		padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, padding, metrics);
		padding = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_backgroundPadding, padding);
		layoutBackground.setPadding(padding, padding, padding, padding);
		if(!isBackgroundColorSetBeforeDraw) {
			color = typedArray.getColor(R.styleable.RoundCornerProgress_backgroundColor, backgroundColor);
			setBackgroundColor(color);
		}
		ViewTreeObserver observer = layoutBackground.getViewTreeObserver(); 
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
		    @Override 
		    public void onGlobalLayout() { 
		    	layoutBackground.getViewTreeObserver().removeOnGlobalLayoutListener(this); 
		    	int height = 0;
		    	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    		backgroundWidth = layoutBackground.getMeasuredWidth();
		    		height = layoutBackground.getMeasuredHeight();
		    	} else {
			    	backgroundWidth = layoutBackground.getWidth();
		    		height = layoutBackground.getHeight();
		    	}
	    		backgroundHeight = (height == 0) ? (int) dp2px(DEFAULT_PROGRESS_BAR_HEIGHT) : height ;
				LayoutParams params = (LayoutParams)layoutBackground.getLayoutParams();
                params.height = backgroundHeight;
				layoutBackground.setLayoutParams(params);
				setProgress(progress);
		    } 
		});

		layoutProgress = (LinearLayout) findViewById(R.id.round_corner_progress_progress);
		if(!isProgressColorSetBeforeDraw) {
			color = typedArray.getColor(R.styleable.RoundCornerProgress_progressColor, progressColor);
			setProgressColor(color);
		}

		if(!isMaxProgressSetBeforeDraw) {
			max = (int) typedArray.getInt(R.styleable.RoundCornerProgress_max, 0);
		}
		if(!isProgressSetBeforeDraw) {
			progress = (int) typedArray.getInt(R.styleable.RoundCornerProgress_progress, 0);
		}

		typedArray.recycle();
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void setProgressColor(int color) {
		progressColor = color;
		int radius = this.radius - padding / 2;
		GradientDrawable gradient = new GradientDrawable();
		gradient.setShape(GradientDrawable.RECTANGLE);
		gradient.setColor(progressColor);
		gradient.setCornerRadii(new float [] { radius, radius, radius, radius, radius, radius, radius, radius});
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    		layoutProgress.setBackground(gradient);
    	} else {
			layoutProgress.setBackgroundDrawable(gradient);
    	}
		
		if(!isProgressBarCreated) {
			isProgressColorSetBeforeDraw = true;
		}
	}
		
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void setBackgroundColor(int color) {
		backgroundColor = color;
		GradientDrawable gradient = new GradientDrawable();
		gradient.setShape(GradientDrawable.RECTANGLE);
		gradient.setColor(backgroundColor);
		gradient.setCornerRadius(radius);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			layoutBackground.setBackgroundDrawable(gradient);
    	} else {
			layoutBackground.setBackground(gradient);
    	}
		
		if(!isProgressBarCreated) {
			isBackgroundColorSetBeforeDraw = true;
		}
	}
	
	public int getBackgroundColor() {
		return backgroundColor;
	}
	
	public int getProgressColor() {
		return progressColor;
	}

	public void setProgress(float progress) {
		this.progress = progress;
		progress = (progress > max) ? max : progress;
		float ratio = max / progress;
		
		LayoutParams params = (LayoutParams)layoutProgress.getLayoutParams();
		params.width = (int)((backgroundWidth - (padding * 2)) / ratio);
		layoutProgress.setLayoutParams(params);
		
		if(!isProgressBarCreated) {
			isProgressSetBeforeDraw = true;
		}
	}
	
	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		if(!isProgressBarCreated) {
			isMaxProgressSetBeforeDraw = true;
		}
		this.max = max;
	}
	
	public float getProgress() {
		return progress;
	}
	
	@SuppressLint("NewApi")
	private float dp2px(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));	
	}
}
