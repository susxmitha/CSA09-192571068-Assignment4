# Task 1 – Fee Payment GUI Design Notes

## Deficiencies in the supplied design

1. **Mixed responsibilities:** The event handler performs input processing, JDBC work, payment processing and UI feedback. The window is difficult to test and maintain.
2. **String comparison with `==`:** Java string content should be compared with `.equals()`; `==` compares object references.
3. **No input validation:** Empty registration numbers, malformed amounts, negative amounts and non-finite numbers are not handled.
4. **Blocking the Event Dispatch Thread:** Database access and `Thread.sleep(3000)` freeze repainting and user interaction.
5. **Poor feedback:** There is no clear progress, success or failure feedback for the user.
6. **Receipt button is not implemented:** The `else` branch clears fields for both Clear and Receipt.
7. **SQL injection risk:** Concatenating user input into SQL is unsafe. Use `PreparedStatement`.
8. **Resource leak:** The connection and statement are not closed using try-with-resources.
9. **Window lifecycle:** No `WindowListener` handles closing.
10. **Checked exceptions:** JDBC exceptions and interruption are not handled appropriately.

## Repaired structure

```text
FeeWindow (AWT)
  - collects registration number and amount
  - invokes FeeService
  - displays status and errors
          |
          v
FeeService
  - validates input
  - performs payment operation
  - returns success/failure
```

The included code uses a simulated payment operation. A real database version should use a DAO with prepared statements and background execution so that the Event Dispatch Thread remains responsive.

## Design defence (under 100 words)

The redesign separates presentation from business logic. `FeeWindow` is responsible only for collecting input and displaying feedback, while `FeeService` validates the values and performs the payment operation. The service can be tested independently without creating an AWT window. Changes to the interface therefore do not require rewriting payment rules, and changes to payment processing do not require modifying the event-handling code. This improves testability, readability and maintainability.
