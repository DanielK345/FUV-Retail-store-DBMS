import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReceiptView extends JFrame {
    private final Connection connection = App.getInstance().getConnection();
    private JTextArea textArea1;
    private JLabel totalPriceTxt, totalPaymentTxt, changeTxt;
    private JTextField taxTxt, receivedTxt, creditAppliedTxt;
    private JComboBox<String> paymentMenu;
    private JCheckBox checkBox1;
    private JPanel receiptFinal, CashOptionPane, registeredCustomerPanel;
    private JButton getReceiptBtn, goBackBtn, finishButton;
    private JTextArea customerInfoTxt;

    private int receiptNo = 0;
    private double totalPayment, receivedAmount, change, creditsApplied, creditsEarned, tax;
    private String paymentMethod = "Cash";

    private String date = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));


    private CartView view;
    private Customer customer;
    private final ArrayList<Product> itemList;


    public ReceiptView(CartView view, Customer customer, double totalPrice, ArrayList<Product> itemList) {
        this.view = view;
        this.customer = customer;
        this.itemList = itemList;
        setupFrame();
        initializeComponents(totalPrice);
        setupEventHandlers(totalPrice, itemList);
    }

    private void setupFrame() {
        setContentPane(receiptFinal);
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Checkout");
    }

    private void initializeComponents(double totalPrice) {
        customerInfoTxt.append(String.format("Name: %s\nPhone Number: %s\nAddress: %s\nCredit: %.2f", customer.getName(), customer.getPhone(), customer.getAddress(), customer.getCredit()));

        paymentMenu.addItem("Cash");
        paymentMenu.addItem("Credit Card");
        paymentMenu.addItem("Debit Card");
        paymentMenu.addItem("Mobile Payment");

        if (customer == null) {
            registeredCustomerPanel.setVisible(false);
        }
        totalPriceTxt.setText(String.format("%.2f", totalPrice));
        updateTotalPayment(totalPrice);
    }

    private void setupEventHandlers(double totalPrice, ArrayList<Product> items) {
        paymentMenu.addActionListener(e -> updatePaymentMethod());
        addDocumentListener(creditAppliedTxt, () -> updateTotalPayment(totalPrice));
        addDocumentListener(receivedTxt, this::updateChange);

        //calculate credits earned: 1% of total payment
        checkBox1.addActionListener(e -> {
            if (checkBox1.isSelected()) {
                creditsEarned = totalPayment / 100;
            } else {
                creditsEarned = 0;
            }
        });

        getReceiptBtn.addActionListener(e -> {
            if (receivedAmount < totalPayment) {
                JOptionPane.showMessageDialog(this, "Insufficient payment!");
                return;
            }
            if (customer != null && creditsApplied > customer.getCredit()) {
                JOptionPane.showMessageDialog(this, "Insufficient credits!");
                return;
            }
            populateReceiptTextArea(items, totalPrice);
            System.out.println(receivedAmount + " " + totalPayment);
        });

        finishButton.addActionListener(e -> {
            //Remove products from database
            for (Product item : items) {
                String name = item.getName();
                int qty = item.getQuantity();
                try {
                    PreparedStatement statement = connection.prepareStatement("UPDATE Products SET product_quantity = product_quantity - ? WHERE product_name = ?");
                    statement.setInt(1, qty);
                    statement.setString(2, name);
                    statement.execute();
                    statement.close();
                } catch (SQLException exception) {
                    System.out.println("Database access error!");
                    exception.printStackTrace();
                }
            }

            //Update customer credit
            try {
                int phoneNumber;
                if (view instanceof CustomerCartView ) phoneNumber = App.getInstance().getCustomer().getPhone();
                else phoneNumber = Integer.parseInt(view.getPhoneNumberTxt().getText());

                PreparedStatement statement = connection.prepareStatement("UPDATE Customers SET credit_score = credit_score - ? + ? WHERE phone_number = ?");
                statement.setDouble(1, creditsApplied);
                statement.setDouble(2, creditsEarned);
                statement.setInt(3, phoneNumber);
                statement.execute();
                statement.close();
            } catch (SQLException exception) {
                System.out.println("Database access error!");
                exception.printStackTrace();
            }

            //
            JOptionPane.showMessageDialog(this, "Payment successful");
            if (view instanceof CustomerCartView){
                goBackToCustomerView();
                //Create new Order object and add to database
                Order order = new Order(customer.getId(), items, date, totalPrice, paymentMethod);
                App.getInstance().getDataAccess().addOrder(order);
            } else {
                goBackToCartView();
            }

            for (Product item : items) {
                System.out.println(item.getID() + " " + item.getName() + " " + item.getQuantity());
            }


        });

        goBackBtn.addActionListener(e -> {
            setVisible(false);
            view.setVisible(true);
        });
    }

    private void updatePaymentMethod() {
        paymentMethod = (String) paymentMenu.getSelectedItem();
        boolean isCash = "Cash".equals(paymentMethod);
        CashOptionPane.setVisible(isCash);
        if (!isCash) {
            receivedTxt.setText(String.format("%.2f", totalPayment));
        }

        System.out.println("Received " + receivedTxt.getText());
    }

    private void updateTotalPayment(double totalPrice) {
        try {
            creditsApplied = parseDouble(creditAppliedTxt.getText());
            tax = parseDouble(taxTxt.getText());
            totalPayment = totalPrice + totalPrice * tax / 100 - creditsApplied;
            totalPayment = Math.round(totalPayment * 100.0) / 100.0;
            totalPaymentTxt.setText(String.format("%.2f", totalPayment));
        } catch (NumberFormatException ex) {
            totalPaymentTxt.setText("");
        }
    }

    private void updateChange() {
        try {
            receivedAmount = parseDouble(receivedTxt.getText());
            change = receivedAmount - totalPayment;
            changeTxt.setText(String.format("%.2f", change));
        } catch (NumberFormatException ex) {
            changeTxt.setText("");
        }
    }

    private void populateReceiptTextArea(ArrayList<Product> items, double totalPrice) {
        textArea1.setText(String.format("Receipt No: %d\nDate: %s\n-----------------------------------------\n", receiptNo, date));
        textArea1.append("Items: \n");
        for (Product item : itemList) {
            textArea1.append(String.format("%s x%s $%s\n", item.getName(), item.getQuantity(), item.getPrice()));
        }
        textArea1.append("-----------------------------------------\n");
        textArea1.append(String.format("Total Price: $%.2f\nTax: %.2f%%\nDiscount: $%.2f\nTotal Payment: $%.2f\nPayment Method: %s\nReceived Amount: $%.2f\nChange: $%.2f\nCredits Earned: %.2f\n",
                totalPrice, tax, creditsApplied, totalPayment, paymentMethod, receivedAmount, change, creditsEarned));
    }

    private void goBackToCartView() {
        setVisible(false);
        new CartView().setVisible(true);
    }

    private void goBackToCustomerView() {
        setVisible(false);
        App.getInstance().getCustomerCartView().setVisible(true);
    }

    private void addDocumentListener(JTextField textField, Runnable updateMethod) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateMethod.run(); }
            public void removeUpdate(DocumentEvent e) { updateMethod.run(); }
            public void changedUpdate(DocumentEvent e) { updateMethod.run(); }
        });
    }

    private double parseDouble(String text) {
        return text.isEmpty() ? 0 : Double.parseDouble(text);
    }
}
