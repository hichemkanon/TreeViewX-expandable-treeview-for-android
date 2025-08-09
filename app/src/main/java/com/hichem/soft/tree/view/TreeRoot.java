package com.hichem.soft.tree.view;

import java.util.ArrayList;

public class TreeRoot<T> extends ArrayList<TreeNode<T>> {

	public TreeRoot() {
		super();
	}

	public TreeNode<T> addNode(T data) {
		TreeNode<T> node = new TreeNode<>(data);
		add(node);
		return node;
	}

	public TreeNode<T> findNodeByData(T data) {
		for (TreeNode<T> node : this) {
			TreeNode<T> result = findInTree(node, data);
			if (result != null) return result;
		}
		return null;
	}

	private TreeNode<T> findInTree(TreeNode<T> node, T data) {
		if (node.data.equals(data)) return node;
		for (TreeNode<T> child : node.getChildren()) {
			TreeNode<T> result = findInTree(child, data);
			if (result != null) return result;
		}
		return null;
	}

	public boolean removeNode(TreeNode<T> target) {
		for (TreeNode<T> node : this) {
			if (node == target) {
				return remove(node);
			}
			if (removeFromChildren(node, target)) return true;
		}
		return false;
	}

	private boolean removeFromChildren(TreeNode<T> parent, TreeNode<T> target) {
		if (parent.getChildren().remove(target)) return true;
		for (TreeNode<T> child : parent.getChildren()) {
			if (removeFromChildren(child, target)) return true;
		}
		return false;
	}
}