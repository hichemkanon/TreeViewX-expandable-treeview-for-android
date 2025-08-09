package com.hichem.soft.tree.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import java.util.List;

public abstract class TreeViewAdapter<T> {
	
	protected Context context;
	protected TreeRoot<T> roots;
	protected LinearLayout container;
	protected TreeViewListener<T> listener;

	public void setListener(TreeViewListener<T> listener) {
		this.listener = listener;
	}
	
	public TreeViewAdapter(Context ctx, TreeRoot roots) {
		this.context = ctx;
		this.roots = roots;
	}

	public void attachTo(LinearLayout container) {
		this.container = container;
		refresh();
	}

	public void refresh() {
		if (container == null)
			return;
		container.removeAllViews();
		for (TreeNode<T> rootNode : roots) {
			renderNode(rootNode, 0);
		}
	}

	private void renderNode(TreeNode<T> node, int level) {
		View view = createView(context, node, level);

		// Add default click handlers if view is not null
		if (view != null) {
			view.setOnClickListener(v -> {
				if (!node.isEmpty()) {
					node.expanded = !node.expanded;
					if (listener != null) {
						if (node.expanded)
							listener.onNodeExpand(node);
						else
							listener.onNodeCollapse(node);
					}
					refresh();
				}
				if (listener != null)
					listener.onNodeClick(node);
			});

			view.setOnLongClickListener(v -> {
				if (listener != null) {
					listener.onNodeLongClick(node);
					return true;
				}
				return false;
			});
		}

		container.addView(view);
		
		if (node.expanded) {
			for (TreeNode<T> child : node.getChildren()) {
				renderNode(child, level + 1);
			}
		}
	}

	protected abstract View createView(Context ctx, TreeNode<T> node, int level);
}