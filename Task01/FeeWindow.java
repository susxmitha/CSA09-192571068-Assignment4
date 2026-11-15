import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/** AWT window: collects input and displays service results only. */
public class FeeWindow extends Frame {
    private final TextField regNo = new TextField(18);
    private final TextField amount = new TextField(18);
    private final Label status = new Label("Enter payment details.");
    private final FeeService feeService = new FeeService();

    public FeeWindow() {
        super("Student Fee Payment");
        setLayout(new BorderLayout(8, 8));

        Panel fields = new Panel(new GridLayout(2, 2, 8, 8));
        fields.add(new Label("Registration number:"));
        fields.add(regNo);
        fields.add(new Label("Amount (Rs.):"));
        fields.add(amount);

        Button pay = new Button("Pay");
        Button clear = new Button("Clear");
        Button receipt = new Button("Receipt");

        Panel actions = new Panel(new FlowLayout(FlowLayout.CENTER));
        actions.add(pay);
        actions.add(clear);
        actions.add(receipt);

        add(fields, BorderLayout.NORTH);
        add(actions, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        pay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePayment();
            }
        });

        clear.addActionListener(e -> {
            regNo.setText("");
            amount.setText("");
            status.setText("Fields cleared.");
        });

        receipt.addActionListener(e ->
                status.setText("Receipt is available after a successful payment."));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(430, 190);
        setLocationRelativeTo(null);
    }

    private void handlePayment() {
        try {
            String message = feeService.payFee(regNo.getText(), amount.getText());
            status.setText(message);
        } catch (FeeService.FeeException ex) {
            status.setText("Payment failed: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new FeeWindow().setVisible(true));
    }
}
