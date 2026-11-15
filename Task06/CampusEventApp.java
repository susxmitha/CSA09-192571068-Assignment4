import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/** Layered Swing/JDBC campus event registration application. */
public class CampusEventApp extends JFrame {
    private static final int PAGE_SIZE = 25;
    private final RegistrationDAO dao = new RegistrationDAO();
    private final RegistrationService service = new RegistrationService(dao);

    private final JTextField studentId = new JTextField(10);
    private final JTextField studentName = new JTextField(12);
    private final JComboBox<EventItem> eventChoice = new JComboBox<>();
    private final JComboBox<EventItem> filterChoice = new JComboBox<>();
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Student ID", "Student Name", "Event", "Registered At"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JLabel status = new JLabel("Ready");
    private int currentOffset = 0;

    public CampusEventApp() {
        super("Campus Event Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        setJMenuBar(createMenuBar());

        JPanel registration = new JPanel(new FlowLayout(FlowLayout.LEFT));
        registration.add(new JLabel("Student ID:"));
        registration.add(studentId);
        registration.add(new JLabel("Name:"));
        registration.add(studentName);
        registration.add(new JLabel("Event:"));
        registration.add(eventChoice);
        JButton register = new JButton("Register");
        registration.add(register);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filters.add(new JLabel("Filter by event:"));
        filters.add(filterChoice);
        JButton applyFilter = new JButton("Apply Filter");
        JButton previous = new JButton("Previous");
        JButton next = new JButton("Next");
        JButton export = new JButton("Export CSV");
        filters.add(applyFilter);
        filters.add(previous);
        filters.add(next);
        filters.add(export);

        JPanel north = new JPanel(new GridLayout(2, 1));
        north.add(registration);
        north.add(filters);
        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        register.addActionListener(e -> registerStudent());
        applyFilter.addActionListener(e -> {
            currentOffset = 0;
            loadPage();
        });
        previous.addActionListener(e -> {
            currentOffset = Math.max(0, currentOffset - PAGE_SIZE);
            loadPage();
        });
        next.addActionListener(e -> {
            currentOffset += PAGE_SIZE;
            loadPage();
        });
        export.addActionListener(e -> exportCsv());

        setSize(1050, 560);
        setLocationRelativeTo(null);
        loadEvents();
        loadPage();
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem refresh = new JMenuItem("Refresh");
        JMenuItem exit = new JMenuItem("Exit");
        refresh.addActionListener(e -> {
            loadEvents();
            loadPage();
        });
        exit.addActionListener(e -> dispose());
        file.add(refresh);
        file.add(exit);
        bar.add(file);
        return bar;
    }

    private void loadEvents() {
        new SwingWorker<List<EventItem>, Void>() {
            @Override protected List<EventItem> doInBackground() throws Exception {
                return dao.findEvents();
            }
            @Override protected void done() {
                try {
                    List<EventItem> events = get();
                    eventChoice.removeAllItems();
                    filterChoice.removeAllItems();
                    filterChoice.addItem(new EventItem(0, "All events"));
                    for (EventItem event : events) {
                        eventChoice.addItem(event);
                        filterChoice.addItem(event);
                    }
                    status.setText("Events loaded: " + events.size());
                } catch (Exception ex) {
                    showError("Unable to load events: " + rootMessage(ex));
                }
            }
        }.execute();
    }

    private void registerStudent() {
        String id = studentId.getText().trim();
        String name = studentName.getText().trim();
        EventItem event = (EventItem) eventChoice.getSelectedItem();

        if (id.isEmpty() || name.isEmpty() || event == null) {
            showError("Enter student ID, student name and select an event.");
            return;
        }

        status.setText("Registering...");
        setFormEnabled(false);
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                service.register(id, name, event.id);
                return null;
            }
            @Override protected void done() {
                setFormEnabled(true);
                try {
                    get();
                    status.setText("Registration successful.");
                    JOptionPane.showMessageDialog(CampusEventApp.this,
                            "Registration completed successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadPage();
                } catch (Exception ex) {
                    showError("Registration failed: " + rootMessage(ex));
                }
            }
        }.execute();
    }

    private void setFormEnabled(boolean enabled) {
        studentId.setEnabled(enabled);
        studentName.setEnabled(enabled);
        eventChoice.setEnabled(enabled);
    }

    private void loadPage() {
        EventItem selected = (EventItem) filterChoice.getSelectedItem();
        int eventId = selected == null ? 0 : selected.id;
        status.setText("Loading registrations...");
        new SwingWorker<List<RegistrationRow>, Void>() {
            @Override protected List<RegistrationRow> doInBackground() throws Exception {
                return dao.findPage(eventId, PAGE_SIZE, currentOffset);
            }
            @Override protected void done() {
                try {
                    List<RegistrationRow> rows = get();
                    model.setRowCount(0);
                    for (RegistrationRow row : rows) {
                        model.addRow(new Object[]{row.id, row.studentId, row.studentName,
                                row.eventName, row.registeredAt});
                    }
                    status.setText("Loaded " + rows.size() + " registration(s). Offset: "
                            + currentOffset);
                } catch (Exception ex) {
                    showError("Unable to load registrations: " + rootMessage(ex));
                }
            }
        }.execute();
    }

    private void exportCsv() {
        JFileChooserHelper.save(this, model);
    }

    private void showError(String message) {
        status.setText(message);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static String rootMessage(Throwable error) {
        Throwable current = error;
        while (current.getCause() != null) current = current.getCause();
        return current.getMessage() == null ? current.toString() : current.getMessage();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new CampusEventApp().setVisible(true));
    }

    static class EventItem {
        final int id;
        final String name;
        EventItem(int id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return name; }
    }

    static class RegistrationRow {
        final long id;
        final String studentId, studentName, eventName, registeredAt;
        RegistrationRow(long id, String studentId, String studentName,
                        String eventName, String registeredAt) {
            this.id = id; this.studentId = studentId; this.studentName = studentName;
            this.eventName = eventName; this.registeredAt = registeredAt;
        }
    }

    static class RegistrationService {
        private final RegistrationDAO dao;
        RegistrationService(RegistrationDAO dao) { this.dao = dao; }
        void register(String id, String name, int eventId) throws Exception {
            if (id == null || !id.matches("[A-Za-z0-9_-]{3,30}"))
                throw new IllegalArgumentException("Student ID must contain 3–30 letters, digits, _ or -.");
            if (name == null || name.trim().length() < 2)
                throw new IllegalArgumentException("Enter a valid student name.");
            if (eventId <= 0) throw new IllegalArgumentException("Select a valid event.");
            dao.insert(id.trim(), name.trim(), eventId);
        }
    }

    static class RegistrationDAO {
        private String url() {
            return System.getenv().getOrDefault("DB_URL",
                    "jdbc:mysql://localhost:3306/campus_events");
        }
        private String user() {
            return System.getenv().getOrDefault("DB_USER", "root");
        }
        private String password() {
            return System.getenv().getOrDefault("DB_PASSWORD", "");
        }
        private Connection connect() throws SQLException {
            return DriverManager.getConnection(url(), user(), password());
        }

        List<EventItem> findEvents() throws SQLException {
            List<EventItem> result = new ArrayList<>();
            String sql = "SELECT event_id, event_name FROM event ORDER BY event_name";
            try (Connection c = connect();
                 PreparedStatement ps = c.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(new EventItem(
                        rs.getInt("event_id"), rs.getString("event_name")));
            }
            return result;
        }

        void insert(String studentId, String studentName, int eventId) throws SQLException {
            String sql = "INSERT INTO registration(student_id, student_name, event_id) VALUES (?, ?, ?)";
            try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, studentId);
                ps.setString(2, studentName);
                ps.setInt(3, eventId);
                ps.executeUpdate();
            } catch (SQLException ex) {
                if ("23000".equals(ex.getSQLState())) {
                    throw new SQLException("This student is already registered for this event.", ex);
                }
                throw ex;
            }
        }

        List<RegistrationRow> findPage(int eventId, int limit, int offset) throws SQLException {
            List<RegistrationRow> result = new ArrayList<>();
            String base = "SELECT r.registration_id, r.student_id, r.student_name, "
                    + "e.event_name, r.registered_at FROM registration r "
                    + "JOIN event e ON e.event_id = r.event_id ";
            String sql = (eventId > 0 ? base + "WHERE r.event_id = ? " : base)
                    + "ORDER BY r.registration_id DESC LIMIT ? OFFSET ?";
            try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
                int index = 1;
                if (eventId > 0) ps.setInt(index++, eventId);
                ps.setInt(index++, limit);
                ps.setInt(index, offset);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) result.add(new RegistrationRow(
                            rs.getLong("registration_id"), rs.getString("student_id"),
                            rs.getString("student_name"), rs.getString("event_name"),
                            rs.getString("registered_at")));
                }
            }
            return result;
        }
    }

    static class JFileChooserHelper {
        static void save(JFrame parent, DefaultTableModel model) {
            javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
            chooser.setSelectedFile(new java.io.File("registrations.csv"));
            if (chooser.showSaveDialog(parent) != javax.swing.JFileChooser.APPROVE_OPTION) return;
            try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    if (col > 0) writer.append(',');
                    writer.append(escape(model.getColumnName(col)));
                }
                writer.append('\n');
                for (int row = 0; row < model.getRowCount(); row++) {
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        if (col > 0) writer.append(',');
                        writer.append(escape(String.valueOf(model.getValueAt(row, col))));
                    }
                    writer.append('\n');
                }
                JOptionPane.showMessageDialog(parent, "CSV exported successfully.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent, "CSV export failed: " + ex.getMessage(),
                        "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        private static String escape(String value) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
    }
}
