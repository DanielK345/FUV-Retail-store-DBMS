import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StaffView extends JFrame {
    private JButton submitBtn;
    private JButton viewOutOfStockButton;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton manageOnlineOrdersButton;
    private JButton saveBtn;
    private JPanel staffPanel;
    private JTextField IDTxt;
    private JComboBox<String> categoryMenu;
    private JScrollPane scrollPane;


    public JButton getSubmitBtn() {
        return submitBtn;
    }

    public JButton getViewOutOfStockButton() {
        return viewOutOfStockButton;
    }

    public JTable getProductTable() {
        return productTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getManageOnlineOrdersButton() {
        return manageOnlineOrdersButton;
    }

    public JButton getSaveBtn() {
        return saveBtn;
    }

    public JPanel getStaffPanel() {
        return staffPanel;
    }

    public JTextField getIDTxt() {
        return IDTxt;
    }

    public JComboBox<String> getCategoryMenu() {
        return categoryMenu;
    }


    public StaffView() {
        setContentPane(staffPanel);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Inventory View");

        tableModel = new DefaultTableModel(new Object[] {"ID", "Name", "Price", "Quantity"}, 0);
        productTable.setModel(tableModel);



        manageOnlineOrdersButton.addActionListener(e -> {
            OrderManagerView onlineOrderManagerView = new OrderManagerView();
            onlineOrderManagerView.setVisible(true);
        });
    }
}
