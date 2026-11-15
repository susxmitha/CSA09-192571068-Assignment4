import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Four equivalent event-handling styles for a small INR currency converter.
 * Rates are illustrative fixed values for assignment demonstration only.
 */
public class ConverterStyles {
    private static double rate(String currency) {
        return currency.equals("USD") ? 0.012 : 0.011;
    }

    private static void showResult(JFrame parent, JTextField amount,
                                   JComboBox<String> target) {
        try {
            double inr = Double.parseDouble(amount.getText().trim());
            if (!Double.isFinite(inr) || inr < 0) {
                throw new NumberFormatException("Amount must be non-negative.");
            }
            String currency = (String) target.getSelectedItem();
            double converted = inr * rate(currency);
            JOptionPane.showMessageDialog(parent,
                    String.format("INR %.2f = %s %.2f", inr, currency, converted),
                    "Conversion Result", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "Invalid amount",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void configure(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
    }

    private static JPanel form(JFrame frame, JTextField amount,
                               JComboBox<String> target, JButton convert) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("INR"));
        panel.add(amount);
        panel.add(target);
        panel.add(convert);
        return panel;
    }

    // (a) The frame itself implements ActionListener.
    static class FrameListenerVersion extends JFrame implements ActionListener {
        private final JTextField amount = new JTextField(10);
        private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});

        FrameListenerVersion() {
            super("Converter – Frame Listener");
            JButton convert = new JButton("Convert");
            add(form(this, amount, target, convert));
            convert.addActionListener(this);
            amount.addActionListener(this);
            configure(this);
            pack();
            setLocationRelativeTo(null);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            showResult(this, amount, target);
        }
    }

    // (b) Named inner class.
    static class NamedInnerVersion extends JFrame {
        private final JTextField amount = new JTextField(10);
        private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});

        NamedInnerVersion() {
            super("Converter – Named Inner Class");
            JButton convert = new JButton("Convert");
            add(form(this, amount, target, convert));
            ConvertHandler handler = new ConvertHandler();
            convert.addActionListener(handler);
            amount.addActionListener(handler);
            configure(this);
            pack();
            setLocationRelativeTo(null);
        }

        private class ConvertHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                showResult(NamedInnerVersion.this, amount, target);
            }
        }
    }

    // (c) Anonymous inner class.
    static class AnonymousVersion extends JFrame {
        private final JTextField amount = new JTextField(10);
        private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});

        AnonymousVersion() {
            super("Converter – Anonymous Inner Class");
            JButton convert = new JButton("Convert");
            add(form(this, amount, target, convert));
            ActionListener handler = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showResult(AnonymousVersion.this, amount, target);
                }
            };
            convert.addActionListener(handler);
            amount.addActionListener(handler);
            configure(this);
            pack();
            setLocationRelativeTo(null);
        }
    }

    // (d) One shared lambda listener for both triggers.
    static class LambdaVersion extends JFrame {
        private final JTextField amount = new JTextField(10);
        private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});

        LambdaVersion() {
            super("Converter – Lambda");
            JButton convert = new JButton("Convert");
            add(form(this, amount, target, convert));
            ActionListener handler = e -> showResult(this, amount, target);
            convert.addActionListener(handler);
            amount.addActionListener(handler);
            configure(this);
            pack();
            setLocationRelativeTo(null);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] choices = {
                "Frame implements ActionListener",
                "Named inner class",
                "Anonymous inner class",
                "Lambda expression"
            };
            String selected = (String) JOptionPane.showInputDialog(
                    null, "Choose an implementation:", "Currency Converter",
                    JOptionPane.QUESTION_MESSAGE, null, choices, choices[3]);

            if (selected == null) return;
            JFrame frame;
            switch (selected) {
                case "Frame implements ActionListener":
                    frame = new FrameListenerVersion();
                    break;
                case "Named inner class":
                    frame = new NamedInnerVersion();
                    break;
                case "Anonymous inner class":
                    frame = new AnonymousVersion();
                    break;
                default:
                    frame = new LambdaVersion();
            }
            frame.setVisible(true);
        });
    }
}
