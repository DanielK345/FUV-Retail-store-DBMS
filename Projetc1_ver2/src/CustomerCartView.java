import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CustomerCartView extends CartView{
    private JPanel panel1;
    private JTable menuTable;
    private JTextField productNameSearchTxt;

    public JTable getMenuTable() {
        return menuTable;
    }

    public JTextField getProductNameTxt() {
        return productNameTxt;
    }

    public JButton getAddToCartButton() {
        return addToCartButton;
    }

    public JButton getGoToCheckoutButton() {
        return goToCheckoutButton;
    }


    public JTextField getQuantityTxt() {
        return quantityTxt;
    }

    public JComboBox<String> getCategoryMenu() {
        return categoryMenu;
    }

    public JTextField getProductNameSearchTxt() {
        return productNameSearchTxt;
    }

    public JButton getSearchBtn() {
        return searchBtn;
    }

    public JTable getCartTable() {
        return cartTable;
    }

    public DefaultTableModel getMenuTableModel() {
        return menuTableModel;
    }

    public DefaultTableModel getCartTableModel() {
        return cartTableModel;
    }

    private JButton addToCartButton;
    private JButton goToCheckoutButton;
    private JTextField quantityTxt;
    private JComboBox<String> categoryMenu;
    private JTable cartTable;
    private JButton searchBtn;
    private JTextField productNameTxt;
    private JButton deleteButton;
    private JLabel welcomePanel;
    private JLabel totalPriceTxt;

    @Override
    public JButton getRemoveBtn() {
        return deleteButton;
    }

    @Override
    public JLabel getTotalPriceTxt() {
        return totalPriceTxt;
    }
    @Override
    public void setTotalPriceTxt(String totalPriceTxt) {
        this.totalPriceTxt.setText(totalPriceTxt);
    }

    public JLabel getWelcomePanel() {
        return welcomePanel;
    }

    private DefaultTableModel menuTableModel;
    private DefaultTableModel cartTableModel;

    public CustomerCartView() {
        setContentPane(panel1);
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Customer Cart");

        menuTableModel = new DefaultTableModel(new Object[] {"ID", "Name", "Price", "Quantity"}, 0);
        cartTableModel = new DefaultTableModel(new Object[] {"ID", "Name", "Quantity", "Price"}, 0);
        menuTable.setModel(menuTableModel);
        cartTable.setModel(cartTableModel);
    }

    public static void main(String[] args) {
        CustomerCartView view = new CustomerCartView();
        view.setVisible(true);
    }


}
