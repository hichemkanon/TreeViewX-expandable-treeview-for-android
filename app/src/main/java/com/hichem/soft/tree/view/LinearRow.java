package com.hichem.soft.tree.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import java.util.List;

public class LinearRow extends LinearLayout {

	private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final TreeNode<?> node;
	private final int level;

	private static final int BASE_PADDING = 24;
	private static final int LEVEL_SPACING = 20;
	public boolean lineEnabled = true;
	private int lineColor = Color.LTGRAY;

	public LinearRow(Context context, TreeNode<?> node, int level) {
		super(context);
		this.node = node;
		this.level = level;
		init();
	}

	public LinearRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.node = null;
		this.level = 0;
		init();
	}

	private void init() {
		setWillNotDraw(false);
		linePaint.setColor(Color.parseColor("#BBBBBB"));
		linePaint.setStrokeWidth(4f);
	}

	public LinearRow setLineColor(int clr) {
		lineColor = clr;
		linePaint.setColor(lineColor);
		return this;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (node == null || !lineEnabled)
			return;

		// Current and parent line positions based on padding
		int currentX = BASE_PADDING + level * LEVEL_SPACING;
		int nextX = BASE_PADDING + level + 1 * LEVEL_SPACING;
		int parentX = BASE_PADDING + (level - 1) * LEVEL_SPACING;
		// Get Y center of this row
		int centerY = (getHeight() / 2) + ((int) 2.2f);

		// Draw horizontal line from parentX to currentX
		
		
		
		if (!node.isRootNode()) {
			canvas.drawLine(parentX, centerY, currentX - 3, centerY, linePaint);
			// Draw vertical line from top to centerY
			canvas.drawLine(parentX, 0, parentX, centerY, linePaint);

		}
		
		if (node.expanded){
			canvas.drawLine(currentX, centerY, currentX+8, centerY, linePaint);
		}

		if (!node.isRootNode() && isNotLastInParent(node) && node.isEmpty()) {
			canvas.drawLine(nextX - 2, centerY, nextX - 2, getHeight(), linePaint);
		}
		if (isNotLastInParent(node) && !node.isRootNode() && node.expanded) {
			canvas.drawLine(nextX - 1, centerY - 2, nextX - 1, getHeight(), linePaint);
		}
		if (node.isRootNode() && node.expanded) {
			canvas.drawLine(currentX, centerY - 2, currentX, getHeight(), linePaint);
		}
	}

	private boolean isNotLastInParent(TreeNode<?> node) {
		if (node == null || node.parent == null)
			return true;
		if (!node.isEmpty())
			return true;
		TreeRoot root = node.parent.getChildren();
		if (node == root.get(root.size() - 1)) {
			return false;
		}
		return true;
	}
}