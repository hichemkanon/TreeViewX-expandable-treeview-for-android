package com.hichem.soft.tree.view;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {
	
	public T data;
	public boolean expanded = false;
	private final TreeRoot<T> children = new TreeRoot<>();
	public TreeNode<T> parent;
	
	// Optional visual elements
	public int iconResId = -1;
	public int barColor = -1;
	
	public TreeNode(T data) {
		this.data = data;
	}
	
	public TreeNode(T data, int barColor) {
		this.data = data;
		this.barColor = barColor;
	}
	
	// === Core ===
	
	public void addChild(TreeNode<T> child) {
		child.parent = this;
		children.add(child);
	}
	
	public void addChildren(List<TreeNode<T>> list) {
		for (TreeNode<T> child : list) {
			child.parent = this;
			children.add(child);
		}
	}
	
	public boolean isEmpty() {
		return children.isEmpty();
	}
	
	public TreeRoot<T> getChildren() {
		return children;
	}
	
	// === Helper methods ===
	
	/** Returns the underlying data */
	public T getData() {
		return data;
	}
	
	/** Sets the node's data */
	public void setData(T data) {
		this.data = data;
	}
	
	/** Returns true if this node has no parent */
	public boolean isRootNode() {
		return parent == null;
	}
	
	/** Returns true if this node has children */
	public boolean isParent() {
		return !children.isEmpty();
	}
	
	/** Returns true if this node has a parent */
	public boolean isChild() {
		return parent != null;
	}
	
	/** Returns true if this is a leaf (no children) */
	public boolean isLeaf() {
		return children.isEmpty();
	}
	
	/** Returns the depth level of this node (0 = root) */
	public int getLevel() {
		int level = 0;
		TreeNode<T> current = this.parent;
		while (current != null) {
			level++;
			current = current.parent;
		}
		return level;
	}
	
	/** Returns the root node of this tree */
	public TreeNode<T> getRoot() {
		TreeNode<T> current = this;
		while (current.parent != null) {
			current = current.parent;
		}
		return current;
	}
	
	/** Returns the full path from root to this node as a list */
	public List<TreeNode<T>> getPath() {
		List<TreeNode<T>> path = new ArrayList<>();
		TreeNode<T> current = this;
		while (current != null) {
			path.add(0, current);
			current = current.parent;
		}
		return path;
	}
	
	/** Returns the number of direct children */
	public int getChildCount() {
		return children.size();
	}
	
	/** Returns the number of all descendants */
	public int getDescendantCount() {
		int count = children.size();
		for (TreeNode<T> child : children) {
			count += child.getDescendantCount();
		}
		return count;
	}
	
	/** Finds the first child with matching data (equals) */
	public TreeNode<T> findChildByData(T target) {
		for (TreeNode<T> child : children) {
			if ((child.data == null && target == null) ||
			(child.data != null && child.data.equals(target))) {
				return child;
			}
		}
		return null;
	}
	
	/** String representation for display */
	@Override
	public String toString() {
		return data != null ? data.toString() : "(null)";
	}
}