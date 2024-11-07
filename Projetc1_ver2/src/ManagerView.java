import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManagerView extends JFrame {
    // Panels
    private JPanel panel1, SelectionPane, cardPanel, employeesPanel, newEmployeePane, inventoryPanel;

    // Buttons
    private JButton employeeBtn, inventoryBtn, finanBtn, saveEmpButton, deleteEmployeeButton, createEButton, saveProductBtn, deleteProductButton;

    // Tables
    private JTable employeeTable, productTable;

    // Text Fields
    private JTextField empIDTxt, newUsNTxt, newPwdTxt, productIDTxt, productNameTxt, productCatTxt, productPriceTxt;

    // Combo Boxes
    private JComboBox<String> roleMenu, categoryMenu;
    private JButton submitEButton;
    private JButton submitPButton;
    private JButton createPButton;
    private JScrollPane scrollPaneProduct;
    private JScrollPane scrollPaneEmp;
    private JTextField newRoleTxt;

    public JTable getProductTable() {
        return productTable;
    }

    private DefaultTableModel tableModel;

    public JButton getCreateEButton() {
        return createEButton;
    }

    public JButton getSubmitEButton() {
        return submitEButton;
    }

    public JButton getSubmitPButton() {
        return submitPButton;
    }

    public JButton getCreatePButton() {
        return createPButton;
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public JPanel getSelectionPane() {
        return SelectionPane;
    }

    public JPanel getCardPanel() {
        return cardPanel;
    }

    public JPanel getEmployeesPanel() {
        return employeesPanel;
    }

    public JPanel getNewEmployeePane() {
        return newEmployeePane;
    }

    public JPanel getInventoryPanel() {
        return inventoryPanel;
    }

    public JButton getEmployeeBtn() {
        return employeeBtn;
    }

    public JButton getInventoryBtn() {
        return inventoryBtn;
    }

    public JTextField getNewRoleTxt() {
        return newRoleTxt;
    }

    public JButton getFinanBtn() {
        return finanBtn;
    }

    public JButton getSaveEmpBtn() {
        return saveEmpButton;
    }

    public JButton getDeleteEmployeeButton() {
        return deleteEmployeeButton;
    }

    public JButton getSaveProductBtn() {
        return saveProductBtn;
    }

    public JButton getDeleteProductButton() {
        return deleteProductButton;
    }

    public JTable getEmployeeTable() {
        return employeeTable;
    }


    public JTextField getEmpIDTxt() {
        return empIDTxt;
    }

    public JTextField getNewUsNTxt() {
        return newUsNTxt;
    }

    public JTextField getNewPwdTxt() {
        return newPwdTxt;
    }

    public JTextField getProductIDTxt() {
        return productIDTxt;
    }

    public JTextField getProductNameTxt() {
        return productNameTxt;
    }

    public JTextField getProductCatTxt() {
        return productCatTxt;
    }

    public JTextField getProductPriceTxt() {
        return productPriceTxt;
    }

    public JComboBox<String> getRoleMenu() {
        return roleMenu;
    }

    public JComboBox<String> getCategoryMenu() {
        return categoryMenu;
    }

    public ManagerView() {
        setContentPane(panel1);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Manager View");

        cardPanel.add(employeesPanel, "employeesPanel");
        cardPanel.add(inventoryPanel, "inventoryPanel");

        tableModel = new DefaultTableModel(new Object[] {"ID", "Name", "Price", "Quantity"}, 0);
        productTable.setModel(tableModel);

        employeeBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "employeesPanel");
        });

        inventoryBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "inventoryPanel");
        });
    }
}