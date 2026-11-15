# Task 5 – Event-handling Styles Comparison

| Style | Readability | Access to frame fields | Reuse | Ease of change |
|---|---|---|---|---|
| Frame implements `ActionListener` | Simple for tiny windows; frame can become crowded | Direct | Limited; frame itself is the listener | Adding more actions can create a long conditional handler |
| Named inner class | Clear separation and meaningful name | Direct access to enclosing instance fields | Good within the enclosing class | Good when handler logic grows |
| Anonymous inner class | Local and explicit, but verbose | Direct access | Usually limited to registration site | More verbose to change |
| Lambda expression | Concise for a single-method listener | Captures enclosing fields | Can be shared by multiple triggers | Very easy for small actions |

The supplied implementation creates one `ActionListener` lambda and registers it with both the Convert button and the amount field. Pressing Enter in the text field therefore invokes the same conversion logic.

A method reference such as `this::convert` is suitable when the handler delegates directly to a method with a compatible signature. A shared Swing `Action` is preferable when the same command must be used by a button, menu item and keyboard shortcut, because it centralises action behaviour and enabled state.

For a large application, a named handler or shared `Action` is often easier to test, reuse and maintain than putting all logic in the frame.
