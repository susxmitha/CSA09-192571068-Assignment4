# Task 2 – Hospital Outpatient Appointment Desk

## 2.1 Event Model

| Event source | Event object | Listener interface | Action |
|---|---|---|---|
| Department combo box | `ItemEvent` | `ItemListener` | Load the doctors mapped to the selected department and refresh the doctor combo box. |
| Doctor combo box | `ItemEvent` | `ItemListener` | Update the selected doctor state and available slot information. |
| Book button | `ActionEvent` | `ActionListener` | Validate patient ID, department, doctor and time slot; then request booking. |
| Patient ID field | `ActionEvent` | `ActionListener` | Optionally trigger validation or move focus to the next field. |
| Window close button | `WindowEvent` | `WindowListener` / `WindowAdapter` | Ask for confirmation if a booking is in progress; otherwise close. |

## 2.2 Component Containment Hierarchy

```text
JFrame: AppointmentDeskFrame
|
+-- JMenuBar
|   +-- JMenu: File
|       +-- JMenuItem: Exit
|
+-- JPanel (BorderLayout: mainPanel)
    |
    +-- JPanel (GridBagLayout: formPanel)
    |   +-- JLabel: Patient ID
    |   +-- JTextField: patientIdField
    |   +-- JLabel: Department
    |   +-- JComboBox<String>: departmentCombo
    |   +-- JLabel: Doctor
    |   +-- JComboBox<Doctor>: doctorCombo
    |   +-- JLabel: Time Slot
    |   +-- JComboBox<String>: slotCombo
    |   +-- JButton: Book Appointment
    |
    +-- JLabel: statusLabel (SOUTH)
```

A `JOptionPane` is used for confirmation and error messages. The top-level window uses `JFrame`; the form is grouped in a `JPanel`.

## 2.3 Handler Pseudocode

### (a) Refresh doctors when department changes

```text
ALGORITHM RefreshDoctors
INPUT: selectedDepartment
BEGIN
    CLEAR doctorCombo
    IF selectedDepartment is empty THEN
        DISABLE doctorCombo
        RETURN
    END IF

    doctorList <- doctorMap.get(selectedDepartment)
    FOR EACH doctor IN doctorList
        ADD doctor TO doctorCombo
    END FOR

    ENABLE doctorCombo
    SELECT first doctor IF doctorList is not empty
END
```

### (b) Validate and book appointment

```text
ALGORITHM BookAppointment
INPUT: patientId, department, doctor, timeSlot
BEGIN
    IF patientId is empty THEN
        DISPLAY "Enter patient ID"
        RETURN
    END IF

    IF department, doctor or timeSlot is not selected THEN
        DISPLAY "Complete all appointment selections"
        RETURN
    END IF

    SET bookingInProgress = TRUE
    TRY
        result <- AppointmentService.book(patientId, department,
                                          doctor, timeSlot)
        DISPLAY confirmation result
    CATCH validation or booking error
        DISPLAY clear error message
    FINALLY
        SET bookingInProgress = FALSE
    END TRY
END
```

## 2.4 Event Sequence and Applet Comparison

1. `main` schedules GUI creation on the Swing Event Dispatch Thread using `SwingUtilities.invokeLater`.
2. The frame constructor creates components, layouts and listeners.
3. `setVisible(true)` displays the frame.
4. User input generates events such as `ItemEvent` and `ActionEvent`.
5. The Event Dispatch Thread invokes the registered listener.
6. A successful booking updates the status or displays a confirmation dialog.
7. On close, `windowClosing` checks `bookingInProgress`. If true, it asks for confirmation. If false, it disposes the frame.
8. `windowClosed` may be used for final cleanup after disposal.

Unlike an applet, a standalone Swing frame does not use the applet lifecycle methods `init`, `start`, `paint`, `stop` and `destroy`. Its construction, visibility, event callbacks and window events govern the interaction.

## Alternative Concept and Complexity Analysis

Let `d` be the number of departments and `m` the average number of doctors per department.

**Preloaded map:** Loading the data once costs approximately `O(d × m)` time and `O(d × m)` space. A department change performs a map lookup in expected `O(1)` time plus `O(m)` to populate the doctor combo box.

**Database fetch on each change:** Each department change incurs a database round trip and query cost, approximately `O(m)` result processing plus network/database latency. It can reduce the amount of cached data, but repeated changes are slower and can make the GUI unresponsive if performed on the Event Dispatch Thread. Database access should run in a background worker.

A `Map<String, List<Doctor>>` is preferable when the doctor directory is reasonably small and changes infrequently. For frequently changing data, use asynchronous database retrieval and update the combo box on the Event Dispatch Thread.
