# TreeViewX

[![Buy Me A Coffee](https://img.shields.io/badge/☕-Buy%20me%20a%20coffee-orange)](https://buymeacoffee.com/hichem_soft_dev)

TreeViewX is a **pure Java Android TreeView component** with full programmatic customization and **no XML or AndroidX dependencies**. It supports **any object type** as node data, expandable/collapsible hierarchy, custom icons, colored lines between parent & child, and JSON import/export.

## ✨ Features
- **Generic type support** – works with any Java object (`TreeNode<T>`).
- **Fully programmatic** – no XML required.
- **Expandable/collapsible nodes** with click listeners.
- **Optional icons** for each node.
- **Colored connector lines** between parent and child nodes.
- **CardView-like row styling** (no CardView dependency).
- **Customizable text rendering** via a `LabelProvider`.
- **From/To JSON conversion**:
  - `fromAnyJson(String json)` → Build a tree from any JSON.
  - `toAnyJson(TreeNode<Object> root)` → Export tree to JSON without custom metadata.
- **Tree state save/restore** – store expanded/collapsed states as a string.
- **Utility helpers** like `isRootNode()`, `isLeaf()`, `getLevel()`, etc.

## 📦 Installation
Copy the `treeview/` package into your project:
```
com.hichem.soft.tree.view
 ├── TreeNode.java
 ├── TreeRoot.java
 ├── ObjectTreeAdapter.java
 ├── TreeViewListener.java
 ├── TreeUtils.java
```
No extra dependencies required.

## 🚀 Quick Start
**1. Create your tree data**
```java
TreeRoot<Object> nodes = new TreeRoot<>();
TreeNode<Object> main = new TreeNode<>("MainActivity");
TreeNode<Object> vars = new TreeNode<>("var");
vars.addChild(new TreeNode<>("x:int", Color.CYAN));
vars.addChild(new TreeNode<>("y:String", Color.RED));
main.addChild(vars);
nodes.add(main);
TreeNode<Object> second = new TreeNode<>("SecondActivity");
second.addChild(new TreeNode<>("onStart"));
nodes.add(second);
```

**2. Create and attach the adapter**
```java
LinearLayout container = new LinearLayout(this);
container.setOrientation(LinearLayout.VERTICAL);
ObjectTreeAdapter<Object> adapter = new ObjectTreeAdapter<>(
    this,
    nodes,
    data -> data.toString() // LabelProvider: how to display each node's data
);
adapter.setListener(new TreeViewListener<Object>() {
    @Override
    public void onNodeClick(TreeNode<Object> node) {
        Toast.makeText(MainActivity.this, "Clicked: " + node.getData(), Toast.LENGTH_SHORT).show();
    }
});
adapter.attachTo(container);
new AlertDialog.Builder(this)
    .setView(container)
    .show();
```

**3. Import JSON into TreeView**
```java
String json = "{ \"name\": \"Hichem\", \"skills\": [\"Java\", \"Bread\"] }";
TreeNode<Object> root = TreeUtils.fromAnyJson(json);
TreeRoot<Object> nodes = new TreeRoot<>();
nodes.add(root);
```

**4. Export TreeView to JSON**
```java
String jsonOut = TreeUtils.toAnyJson(root);
Log.d("TreeJSON", jsonOut);
```

## 🔧 Helper Methods
### `TreeNode`
- `isRootNode()` → Node has no parent.
- `isParent()` → Node has children.
- `isChild()` → Node has a parent.
- `isLeaf()` → Node has no children.
- `getLevel()` → Depth of node (0 = root).
- `getData()` / `setData()` → Access node data.
- `toDisplayString()` → String used for rendering.

### `TreeUtils`
- `fromAnyJson(String)` → Build tree from standard JSON.
- `toAnyJson(TreeNode)` → Export to standard JSON.
- `saveState(TreeRoot)` → Serialize expanded/collapsed state.
- `loadState(TreeRoot, String)` → Restore expanded/collapsed state.

## 🎯 Example Output
**Tree Example:**
```
MainActivity
 └── var
      ├── x:int
      └── y:String
SecondActivity
 └── onStart
```
**JSON Export:**
```json
{
  "MainActivity": {
    "var": {
      "x": "int",
      "y": "String"
    }
  },
  "SecondActivity": {
    "onStart": ""
  }
}
```

## 📜 License
This project is licensed under the MIT License. You are free to use it in commercial or personal projects.

## ☕ Support
If you like this library and want to support its development:
[**Buy me a coffee**](https://buymeacoffee.com/hichem_soft_dev)