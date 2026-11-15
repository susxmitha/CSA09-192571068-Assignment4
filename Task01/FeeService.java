/**
 * Service layer for the fee-payment example.
 * The payment operation is simulated so the example runs without a bank API.
 */
public class FeeService {
    public static class FeeException extends Exception {
        public FeeException(String message) {
            super(message);
        }
    }

    public String payFee(String registrationNumber, String amountText)
            throws FeeException {
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            throw new FeeException("Registration number cannot be empty.");
        }

        final double amount;
        try {
            amount = Double.parseDouble(amountText.trim());
        } catch (NumberFormatException ex) {
            throw new FeeException("Enter a valid numeric amount.");
        }

        if (!Double.isFinite(amount) || amount <= 0) {
            throw new FeeException("Amount must be greater than zero.");
        }

        // Demonstration only: replace with a payment gateway/JDBC service in production.
        return String.format("Payment of Rs. %.2f recorded for %s.",
                amount, registrationNumber.trim());
    }
}
