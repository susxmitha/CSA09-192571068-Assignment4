# Task 3 – Campus Library Search Screen Layout

## Layout choices

- **Frame:** `BorderLayout` places the title at the top, the main content in the centre and the status message at the bottom.
- **Search row:** `FlowLayout(LEFT)` keeps the text field, category selector and Search button in one compact row.
- **Main content:** Nested `BorderLayout` places the search row at NORTH, scrollable results at CENTER and navigation buttons at SOUTH.
- **Bottom buttons:** `FlowLayout(CENTER)` keeps Prev, Next and Reserve at their preferred sizes.
- **Results:** `JScrollPane` occupies the centre region and receives additional space when the frame is resized.

## Nested container structure

```text
JFrame – BorderLayout
+-- JLabel title – NORTH
+-- JPanel main – CENTER (BorderLayout)
|   +-- JPanel search – NORTH (FlowLayout)
|   +-- JScrollPane results – CENTER
|   +-- JPanel navigation – SOUTH (FlowLayout)
+-- JLabel status – SOUTH
```

`GridBagLayout` offers more precise alignment but requires more constraints. Absolute positioning is not recommended because fixed coordinates do not adapt reliably when the window is resized or fonts differ.

The number of layout-managed components is linear in the number of components, `O(c)` for `c` child components. The nested-panel approach adds a small number of containers but provides predictable resizing. Exact layout-manager implementation costs depend on component count and hierarchy.
