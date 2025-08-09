package com.hichem.soft.tree.view;

public interface TreeViewListener<T> {
	default void onNodeClick(TreeNode<T> node){}
	default void onNodeLongClick(TreeNode<T> node){}
	default void onNodeExpand(TreeNode<T> node){}
	default void onNodeCollapse(TreeNode<T> node){}
}