import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CartView extends JFrame {
    private JLabel inputIDLabel;
    private JTextField IdTxt;
    private JLabel inputQuantityLabel;
    private JTextField qtyTxt;
    private JList itemList;

    public JLabel getInputIDLabel() {
        return inputIDLabel;
    }

    public JTextField getIdTxt() {
        return IdTxt;
    }

    public JLabel getInputQuantityLabel() {
        return inputQuantityLabel;
    }

    public JTextField getQuantityTxt() {
        return qtyTxt;
    }

    public JList getItemList() {
        return itemList;
    }

    public JPanel getCashierPanel() {
        return receiptPanel;
    }

    public JButton getAddBtn() {
        return addBtn;
    }

    public JLabel getTotalPriceTxt() {
        return totalPriceLabel;
    }

    private JPanel receiptPanel;
    private JButton addBtn;

    private JLabel totalPriceLabel;
    public void setTotalPriceTxt(String totalPrice) {
        this.totalPriceTxt.setText(totalPrice);
    }

    public JLabel getTotalPrice() {
        return totalPriceLabel;
    }

    private JScrollPane scrollPane;
    private JTable cartTable;

    private DefaultTableModel itemTable;
    private JLabel totalPriceTxt;
    private JButton checkoutBtn;

    public JButton getCheckoutBtn() {
        return checkoutBtn;
    }

    public JButton getRemoveBtn() {
        return removeBtn;
    }

    private JButton removeBtn;
    private JLabel enterPhoneLabel;
    private JTextField phoneNumberTxt;
    public JTextField getPhoneNumberTxt() {
        return phoneNumberTxt;
    }

    public DefaultTableModel getTable(){
        return itemTable;
    }

    public JTable getCartTable() { // Getter for JTable instance
        return cartTable;
    }




    public CartView() {
        setContentPane(receiptPanel);
        setTitle("Receipt view");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);

        itemTable = new DefaultTableModel(new Object[]{"ID", "Name", "Quantity", "Total price"}, 0);
        cartTable.setModel(itemTable);

    }


//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new ReceiptView();
//        });
//    }

}
