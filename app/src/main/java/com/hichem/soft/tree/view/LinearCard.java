package com.hichem.soft.tree.view;

package com.sketchware.doctor.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class LinearCard extends LinearLayout {

	private final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final RectF rect = new RectF();

	private int cardColor = Color.WHITE;
	private float cornerRadius = 24f;
	private float elevation = 6f;
	private int shadowColor = Color.argb(80, 0, 0, 0);

	public LinearCard(Context context) {
		super(context);
		init();
	}

	private void init() {
		setWillNotDraw(true); // Let us draw the background and shadow
		setOrientation(VERTICAL);
		setPadding(32, 24, 32, 24);
		setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		setBackgroundColor(Color.TRANSPARENT);
	}

	public void setCardBackgroundColor(int color) {
		this.cardColor = color;
		invalidate();
	}

	public void setCornerRadius(float radius) {
		this.cornerRadius = radius;
		invalidate();
	}

	public void setCardElevation(float elevation) {
		this.elevation = elevation;
		invalidate();
	}

	public void setShadowColor(int color) {
		this.shadowColor = color;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		rect.set(
			elevation,
			elevation,
			getWidth() - elevation,
			getHeight() - elevation
		);

		// Shadow
		shadowPaint.setColor(shadowColor);
		shadowPaint.setStyle(Paint.Style.FILL);
		canvas.drawRoundRect(rect, cornerRadius, cornerRadius, shadowPaint);

		// Background
		backgroundPaint.setColor(cardColor);
		canvas.drawRoundRect(rect, cornerRadius, cornerRadius, backgroundPaint);
	}
}