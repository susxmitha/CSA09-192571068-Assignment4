# Programming in Java – Assignment 4 (CO4)
## Event-Driven GUI Design

**Student:** S. Susmitha  
**Register Number:** 192571068  
**Course:** Programming in Java (CSA09)  
**Course Outcome:** CO4  
**Nature:** Individual assignment  
**Java environment:** OpenJDK 21.0.13 (Windows 11)  
**Database:** MySQL Server 9.4 with MySQL Connector/J

## Repository contents
- `Task01/` – Fee-payment GUI and separate service class.
- `Task02/` – Hospital appointment desk event model, component hierarchy, pseudocode and event trace.
- `Task03/` – Resizable campus library search screen.
- `Task04/` – Drawing pad with mouse event handling and persistent points.
- `Task05/` – Currency converter demonstrating four event-handling styles.
- `Task06/` – Campus event registration Swing application and MySQL schema.
- `docs/` – design notes and comparisons.
- `evidence/Q1` … `evidence/Q6` – screenshots of executed interactions with evidence indexes.

## Compile and run
Run commands from the relevant task directory in Windows CMD.

### Task 1
```bat
cd Task01
javac FeeService.java FeeWindow.java
java FeeWindow
```
The service validates input and records a simulated payment result. It is not connected to a live bank or payment gateway.

### Task 2
Open `Task02/AppointmentDeskDesign.md`. This is a design exercise covering the event model, component hierarchy, handler pseudocode, event trace, applet comparison and complexity analysis.

### Task 3
```bat
cd Task03
javac LibrarySearchScreen.java
java LibrarySearchScreen
```

### Task 4
```bat
cd Task04
javac DrawPad.java
java DrawPad
```
Click inside the drawing area. The program stores points and redraws them in `paint()`.

### Task 5
```bat
cd Task05
javac ConverterStyles.java
java ConverterStyles
```
Choose one of the four implementation styles. Enter an INR amount, select USD or EUR, and click Convert or press Enter in the amount field.

### Task 6 – MySQL/JDBC
1. Start MySQL Server.
2. Execute `Task06/schema.sql` against your MySQL instance.
3. Obtain MySQL Connector/J and note the full path to its JAR file.
4. From `Task06`, compile and run:
```bat
javac -cp ".;C:\path\to\mysql-connector-j.jar" CampusEventApp.java
java -cp ".;C:\path\to\mysql-connector-j.jar" CampusEventApp
```
5. If credentials are not defaults, set `DB_URL`, `DB_USER` and `DB_PASSWORD` in the same CMD session. Do not commit passwords.

Default JDBC URL: `jdbc:mysql://localhost:3306/campus_events`. The application uses prepared statements, paginated reads and a background worker. A unique database constraint prevents duplicate registration for the same student/event pair.

## Evidence summary
- Q1: initial form, receipt control, clear action, invalid amount, negative amount, empty registration number and successful simulated payment.
- Q2: event model, component hierarchy, handler pseudocode, event trace and complexity analysis.
- Q3: initial screen and three window sizes.
- Q4: initial state and multiple mouse-click drawings.
- Q5: listener styles, valid conversion and invalid input.
- Q6: database records, pagination, filtering, CSV export, empty-input validation and duplicate prevention.

## Important notes
- Task 1 payment is simulated.
- Task 2 is submitted as a design artefact.
- Task 6 requires MySQL and MySQL Connector/J at runtime.
- Excel may show long IDs in scientific notation or timestamps as `#######`; widen columns and format Student ID as Text to inspect the CSV correctly.
- The assignment requires progressive commits (at least one per task). Create task-wise commits where possible; do not claim commits that were not made.

## Result
All six tasks have been implemented or designed as applicable and demonstrated with the screenshots stored in `evidence/`. The final report is supplied separately.
