package com.hichem.soft.tree.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

public class ObjectTreeAdapter<T> extends TreeViewAdapter<T> {

	public interface Formatter<T> {
		String getDisplayText(T data);
	}

	private final Formatter<T> formatter;

	public ObjectTreeAdapter(Context ctx, TreeRoot<T> root, Formatter<T> formatter) {
		super(ctx, root);
		this.formatter = formatter;
	}

	@Override
	protected View createView(Context ctx, TreeNode<T> node, int level) {

		LinearCard card = new LinearCard(ctx);
		card.setCornerRadius(16f);
		card.setCardElevation(6f);
		card.setCardBackgroundColor(Color.WHITE);
		card.setPadding(0, 0, 0, 0);

		AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(150);
		card.startAnimation(anim);

		LinearRow row = new LinearRow(ctx, node, level);
		row.setOrientation(LinearLayout.HORIZONTAL);
		row.setPadding(24 + level * 20, 16, 24, 16);
		card.addView(row);

		View leftBar = new View(ctx);
		LinearLayout.LayoutParams barParams = new LinearLayout.LayoutParams(8, LinearLayout.LayoutParams.MATCH_PARENT);
		barParams.setMarginEnd(8);
		leftBar.setLayoutParams(barParams);
		leftBar.setBackgroundColor(
				node.barColor != -1 ? node.barColor : (node.isEmpty() ? Color.GRAY : Color.TRANSPARENT));
		//row.addView(leftBar);

		if (node.barColor != -1) {
			if (node.isEmpty()) {
				//	row.setLineColor(node.barColor);
			}
		}

		//  content.addView(leftBar 

		TextView arrowTv = new TextView(context);
		arrowTv.setTextSize(16);
		arrowTv.setTypeface(null, Typeface.BOLD);
		arrowTv.setTextColor(node.expanded ? Color.LTGRAY : Color.DKGRAY);

		ImageView icon = new ImageView(ctx);
		if (node.iconResId != -1) {
			icon.setImageResource(node.iconResId);
			icon.setVisibility(View.VISIBLE);
		} else {
			icon.setVisibility(View.GONE);
		}
		icon.setLayoutParams(new LinearLayout.LayoutParams(48, 48));

		PrettyTextView label = new PrettyTextView(ctx);

		String display = formatter != null ? formatter.getDisplayText(node.data) : String.valueOf(node.data);

		label.setText(display);

		arrowTv.setText(node.isEmpty() ? " " : (node.expanded ? "  -" : "  +"));

		//content.addView(arrowTv);
		//content.addView(label);

		row.addView(leftBar);
		row.addView(icon);
		row.addView(label);
		row.addView(arrowTv);

		View divider = new View(ctx);
		LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				1);
		dividerParams.setMargins(16, 0, 16, 0);
		divider.setLayoutParams(dividerParams);
		divider.setBackgroundColor(Color.parseColor("#CCCCCC"));

		//  card.addView(divider);

		card.setOnClickListener(v -> {
			node.expanded = !node.expanded;
			refresh();
			if (listener != null)
				listener.onNodeClick(node);
		});

		card.setOnLongClickListener(v -> {
			if (listener != null) {
				listener.onNodeLongClick(node);
				return true;
			}
			return false;
		});

		return card;
	}

	public class PrettyTextView extends TextView {

		public PrettyTextView(Context context) {
			super(context);
			init();
		}

		private void init() {
			// Appearance
			setTextColor(Color.BLACK);
			setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
			setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);

			// Padding
			int hPadding = dp(6);
			int vPadding = dp(3);
			setPadding(hPadding, vPadding, hPadding, vPadding);

			// Background with rounded corners
			GradientDrawable bg = new GradientDrawable();
			bg.setColor(Color.parseColor("#F5F5F5")); // Light background
			bg.setCornerRadius(dp(12));
			bg.setStroke(dp(1), Color.parseColor("#DDDDDD")); // Light border
			setBackground(bg);

			// Center vertically for good layout with icons
			setGravity(Gravity.CENTER_VERTICAL);
		}

		private int dp(int px) {
			float density = getResources().getDisplayMetrics().density;
			return Math.round(px * density);
		}
	}

}