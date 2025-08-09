package com.hichem.soft.tree.view;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TreeUtils {

	/** Get all nodes in the tree (recursive) */
	public static <T> List<TreeNode<T>> getAllNodes(TreeRoot<T> root) {
		List<TreeNode<T>> result = new ArrayList<>();
		for (TreeNode<T> node : root) {
			result.add(node);
			result.addAll(getAllNodes(node.getChildren()));
		}
		return result;
	}

	/** Get all leaf nodes (no children) */
	public static <T> List<TreeNode<T>> getLeafNodes(TreeRoot<T> root) {
		List<TreeNode<T>> result = new ArrayList<>();
		for (TreeNode<T> node : root) {
			if (node.isEmpty()) {
				result.add(node);
			} else {
				result.addAll(getLeafNodes(node.getChildren()));
			}
		}
		return result;
	}

	/** Get all expanded (visible) nodes */
	public static <T> List<TreeNode<T>> getExpandedNodes(TreeRoot<T> root) {
		List<TreeNode<T>> result = new ArrayList<>();
		for (TreeNode<T> node : root) {
			if (node.expanded) {
				result.add(node);
				result.addAll(getExpandedNodes(node.getChildren()));
			}
		}
		return result;
	}

	/** Find node by data */
	public static <T> TreeNode<T> findNodeByData(TreeRoot<T> root, T data) {
		for (TreeNode<T> node : root) {
			if (node.data.equals(data))
				return node;
			TreeNode<T> found = findNodeByData(node.getChildren(), data);
			if (found != null)
				return found;
		}
		return null;
	}

	/** Get depth/level of a node (root = 0) */
	public static <T> int getDepth(TreeNode<T> node) {
		int depth = 0;
		TreeNode<T> current = node.parent;
		while (current != null) {
			depth++;
			current = current.parent;
		}
		return depth;
	}

	/** Get full path from root to node */
	public static <T> List<TreeNode<T>> getPath(TreeNode<T> node) {
		List<TreeNode<T>> path = new ArrayList<>();
		TreeNode<T> current = node;
		while (current != null) {
			path.add(0, current);
			current = current.parent;
		}
		return path;
	}

	/** Get siblings of a node */
	public static <T> List<TreeNode<T>> getSiblings(TreeNode<T> node) {
		List<TreeNode<T>> siblings = new ArrayList<>();
		if (node.parent != null) {
			for (TreeNode<T> child : node.parent.getChildren()) {
				if (child != node)
					siblings.add(child);
			}
		}
		return siblings;
	}

	/** Is root node? */
	public static <T> boolean isRoot(TreeNode<T> node) {
		return node.parent == null;
	}

	/** Is leaf node? */
	public static <T> boolean isLeaf(TreeNode<T> node) {
		return node.isEmpty();
	}

	/** Is parent node (has children)? */
	public static <T> boolean isParent(TreeNode<T> node) {
		return !node.isEmpty();
	}

	/** Convert tree to JSON string */
	public static <T> String toJson(TreeRoot<T> root) {
		try {
			return toJsonArray(root).toString(2); // pretty print with indent 2
		} catch (Exception e) {
			return "[]";
		}
	}

	/** Convert tree to JSONArray */
	public static <T> JSONArray toJsonArray(TreeRoot<T> root) {
		JSONArray arr = new JSONArray();
		for (TreeNode<T> node : root) {
			arr.put(toJsonObject(node));
		}
		return arr;
	}

	/** Convert single node to JSONObject (recursively) */
	private static <T> JSONObject toJsonObject(TreeNode<T> node) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("data", String.valueOf(node.data));
			obj.put("expanded", node.expanded);
			obj.put("iconResId", node.iconResId);
			obj.put("barColor", node.barColor);

			if (!node.isEmpty()) {
				obj.put("children", toJsonArray(node.getChildren()));
			} else {
				obj.put("children", new JSONArray());
			}
		} catch (Exception ignored) {
		}
		return obj;
	}

	public static TreeRoot<String> fromJson(String json) {
		try {
			json = json.trim();
			if (json.startsWith("[")) {
				// Root is an array
				return fromJsonArray(new JSONArray(json), null);
			} else if (json.startsWith("{")) {
				// Root is a single object
				TreeNode<String> rootNode = fromJsonObject(new JSONObject(json), null);
				TreeRoot<String> root = new TreeRoot<>();
				root.add(rootNode);
				return root;
			} else {
				throw new IllegalArgumentException("JSON must start with '[' or '{'");
			}
		} catch (JSONException e) {
			throw new IllegalArgumentException("Invalid JSON structure for TreeRoot", e);
		}
	}

	private static TreeRoot<String> fromJsonArray(JSONArray arr, TreeNode<String> parent) throws JSONException {
		TreeRoot<String> root = new TreeRoot<>();
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			TreeNode<String> node = fromJsonObject(obj, parent);
			root.add(node);
		}
		return root;
	}

	private static TreeNode<String> fromJsonObject(JSONObject obj, TreeNode<String> parent) throws JSONException {
		TreeNode<String> node = new TreeNode<>(obj.getString("data"));
		node.expanded = obj.optBoolean("expanded", false);
		node.iconResId = obj.optInt("iconResId", -1);
		node.barColor = obj.optInt("barColor", -1);
		node.parent = parent;

		if (obj.has("children")) {
			JSONArray childrenArr = obj.getJSONArray("children");
			TreeRoot<String> children = fromJsonArray(childrenArr, node);
			node.getChildren().addAll(children);
		}
		return node;
	}

	// Save the expanded/collapsed state of the tree to a JSON string
	public static String saveTreeState(TreeRoot<?> root) {
		try {
			JSONObject obj = new JSONObject();
			saveNodeStateArray(root, obj);
			return obj.toString();
		} catch (JSONException e) {
			return "{}"; // fallback
		}
	}

	private static void saveNodeStateArray(TreeRoot<?> root, JSONObject obj) throws JSONException {
		for (TreeNode<?> node : root) {
			obj.put(node.data.toString(), node.expanded);
			if (!node.isEmpty()) {
				saveNodeStateArray(node.getChildren(), obj);
			}
		}
	}

	// Load the expanded/collapsed state from a JSON string into the tree
	public static void loadTreeState(TreeRoot<?> root, String stateJson) {
		try {
			JSONObject obj = new JSONObject(stateJson);
			applyNodeStateArray(root, obj);
		} catch (JSONException e) {
			// ignore invalid state
		}
	}

	private static void applyNodeStateArray(TreeRoot<?> root, JSONObject obj) throws JSONException {
		for (TreeNode<?> node : root) {
			if (obj.has(node.data.toString())) {
				node.expanded = obj.getBoolean(node.data.toString());
			}
			if (!node.isEmpty()) {
				applyNodeStateArray(node.getChildren(), obj);
			}
		}
	}

	public static TreeNode<Object> fromAnyJson(String json) {
		try {
			json = json.trim();
			if (json.startsWith("{")) {
				JSONObject jsonObject = new JSONObject(json);
				return fromJsonObject(null, jsonObject);
			} else if (json.startsWith("[")) {
				JSONArray jsonArray = new JSONArray(json);
				return fromJsonArray(null, jsonArray);
			} else {
				// Not JSON, treat as single data node
				return new TreeNode<>(json);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return new TreeNode<>("Invalid JSON");
		}
	}

	private static TreeNode<Object> fromJsonObject(String name, JSONObject obj) {
		TreeNode<Object> node = new TreeNode<>(name != null ? name : "root");
		JSONArray keys = obj.names();
		if (keys != null) {
			for (int i = 0; i < keys.length(); i++) {
				String key = keys.optString(i);
				Object value = obj.opt(key);
				if (value instanceof JSONObject) {
					node.addChild(fromJsonObject(key, (JSONObject) value));
				} else if (value instanceof JSONArray) {
					node.addChild(fromJsonArray(key, (JSONArray) value));
				} else {
					node.addChild(new TreeNode<>(key + ": " + String.valueOf(value)));
				}
			}
		}
		return node;
	}

	private static TreeNode<Object> fromJsonArray(String name, JSONArray arr) {
		TreeNode<Object> node = new TreeNode<>(name != null ? name : "array");
		for (int i = 0; i < arr.length(); i++) {
			Object value = arr.opt(i);
			if (value instanceof JSONObject) {
				node.addChild(fromJsonObject("[" + i + "]", (JSONObject) value));
			} else if (value instanceof JSONArray) {
				node.addChild(fromJsonArray("[" + i + "]", (JSONArray) value));
			} else {
				node.addChild(new TreeNode<>("[" + i + "]: " + String.valueOf(value)));
			}
		}
		return node;
	}

	public static String toAnyJson(TreeNode<Object> node) {
		try {
			// Wrap the node into a JSON object with its name as key if it has children
			Object result;
			if (node.isLeaf()) {
				result = parseValue(node.data);
			} else {
				JSONObject obj = new JSONObject();
				String key = extractKey(node.data);
				obj.put(key, toJsonValue(node));
				result = obj;
			}

			if (result instanceof JSONObject) {
				return ((JSONObject) result).toString(2);
			} else if (result instanceof JSONArray) {
				return ((JSONArray) result).toString(2);
			} else {
				return String.valueOf(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{}";
		}
	}

	private static Object toJsonValue(TreeNode<Object> node) {
		// If node has no children → it's a leaf value
		try {
			if (node.isLeaf()) {
				return parseValue(node.data);
			}

			// Detect if children are array-like
			boolean isArray = true;
			for (TreeNode<Object> child : node.getChildren()) {
				if (!isArrayKey(child.data)) {
					isArray = false;
					break;
				}
			}

			if (isArray) {
				JSONArray arr = new JSONArray();
				for (TreeNode<Object> child : node.getChildren()) {
					arr.put(toJsonValue(child));
				}
				return arr;
			} else {
				JSONObject obj = new JSONObject();
				for (TreeNode<Object> child : node.getChildren()) {
					String key = extractKey(child.data);
					obj.put(key, toJsonValue(child));
				}
				return obj;
			}
		} catch (Exception z) {
			return null;
		}
	}

	private static boolean isArrayKey(Object data) {
		if (!(data instanceof String))
			return false;
		String s = (String) data;
		return s.matches("^\\[\\d+\\]$");
	}

	private static String extractKey(Object data) {
		if (!(data instanceof String))
			return String.valueOf(data);
		String s = (String) data;
		int colonIndex = s.indexOf(":");
		if (colonIndex > 0) {
			return s.substring(0, colonIndex).trim();
		}
		return s;
	}

	private static Object parseValue(Object data) {
		if (data == null)
			return JSONObject.NULL;
		if (!(data instanceof String))
			return data;

		String s = ((String) data).trim();

		// Detect primitives
		if ("true".equalsIgnoreCase(s))
			return true;
		if ("false".equalsIgnoreCase(s))
			return false;
		if ("null".equalsIgnoreCase(s))
			return JSONObject.NULL;
		try {
			if (s.contains("."))
				return Double.parseDouble(s);
			return Integer.parseInt(s);
		} catch (NumberFormatException ignored) {
		}

		// If contains "key: value" format, extract only the value
		int colonIndex = s.indexOf(":");
		if (colonIndex > 0) {
			return parseValue(s.substring(colonIndex + 1).trim());
		}

		return s; // treat as string
	}

}