import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class InventoryController {
    private StaffView staffView;

    private ManagerView managerView;
    private DataAccess dataAccess;
    private String changesLog = "";
    ArrayList<Product> changedProducts = new ArrayList<>();

    public InventoryController(StaffView staffView, ManagerView managerView, DataAccess dataAccess) {
        this.staffView = staffView;
        this.managerView = managerView;
        this.dataAccess = dataAccess;

        JTable staffViewProductTable = staffView.getProductTable();
        JTable managerViewProductTable = managerView.getProductTable();

        //For the staff view
        this.staffView.getSubmitBtn().addActionListener(e -> getProductByID(staffView.getIDTxt() ,staffViewProductTable));
        this.staffView.getCategoryMenu().addActionListener(e -> getProductByCategory(staffView.getCategoryMenu() ,staffViewProductTable));
        this.staffView.getViewOutOfStockButton().addActionListener(e -> getOutOfStockProducts(staffViewProductTable));
        this.staffView.getSaveBtn().addActionListener(e -> saveChanges());
        populateProductTable(staffViewProductTable,dataAccess.loadAllProducts());
        populateCategoryMenu(staffView.getCategoryMenu(), dataAccess);
        addTableModelListener();

        //For the manager view
        populateProductTable(managerViewProductTable, dataAccess.loadAllProducts());
        populateCategoryMenu(managerView.getCategoryMenu(), dataAccess);
        this.managerView.getSubmitPButton().addActionListener(e -> getProductByID(managerView.getProductIDTxt(), managerViewProductTable));
        this.managerView.getCategoryMenu().addActionListener(e -> getProductByCategory(managerView.getCategoryMenu(),managerViewProductTable));
        this.managerView.getCreatePButton().addActionListener(e -> addProduct());
        this.managerView.getDeleteProductButton().addActionListener(e -> removeProduct());
    }

    public InventoryController(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }


    //FOR STAFF VIEW
    public void populateProductTable(JTable jtable, ArrayList<Product> products) {
        DefaultTableModel tableModel = new DefaultTableModel(new Object[] {"ID", "Name", "Price", "Quantity"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Make ID column uneditable
            }
        };
        jtable.setModel(tableModel);
        tableModel.setRowCount(0); // Clear existing rows

        for (Product product : products) {
            Object[] row = {product.getID(), product.getName(), product.getPrice(), product.getQuantity()};
            tableModel.addRow(row);
        }
    }

    public void populateCategoryMenu(JComboBox<String> categoryMenu, DataAccess dataAccess){
        ArrayList<String> categories = dataAccess.loadAllCategories();
        categoryMenu.addItem("All");
        for (String category : categories) {
            categoryMenu.addItem(category);
        }
    }

    private void getProductByID(JTextField IDTxt, JTable jtable) {
        if (IDTxt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(staffView, "Please enter a product ID!");
            populateProductTable(jtable, dataAccess.loadAllProducts());
            return;
        }
        String[] productIDs = IDTxt.getText().split(" ");
        ArrayList<Product> products = new ArrayList<>();
        for (String productID : productIDs){
            Product product = dataAccess.loadProduct(Integer.parseInt(productID));
            products.add(product);
        }
        populateProductTable(jtable, products);
    }

    public void getProductByName(JTextField nameTxt, JTable jtable) {
        if (nameTxt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(staffView, "Please enter a product name!");
            populateProductTable(jtable, dataAccess.loadAllProducts());
            return;
        }
        String[] productNames = nameTxt.getText().split(" ");
        ArrayList<Product> products = new ArrayList<>();
        for (String productName : productNames){
            Product product = dataAccess.getProductByName(productName);
            products.add(product);
        }
        populateProductTable(jtable, products);
    }

    public void getProductByCategory(JComboBox<String> categoryMenu ,JTable jtable) {
        String category = (String) categoryMenu.getSelectedItem();
        if (category.equals("All") || category.isEmpty()) {
            populateProductTable(jtable, dataAccess.loadAllProducts());
            return;
        }
        ArrayList<Product> products = dataAccess.loadProductsByCategory(category);
        populateProductTable(jtable, products);
    }

    public void getOutOfStockProducts(JTable jtable) {
        ArrayList<Product> products = dataAccess.loadOutOfStockProducts();
        populateProductTable(jtable, products);
    }

    private void addTableModelListener() {
        DefaultTableModel tableModel = (DefaultTableModel) staffView.getProductTable().getModel();
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (column >= 0) { // Ignore row insert/delete events
                    DefaultTableModel model = (DefaultTableModel) e.getSource();
                    int id = (int) model.getValueAt(row, 0);
                    String oldValue = "";
                    String newValue = model.getValueAt(row, column).toString();
                    String columnName = model.getColumnName(column);

                    Product product = dataAccess.loadProduct(id);
                    switch (column) {
                        case 1:
                            oldValue = product.getName();
                            product.setName(newValue);
                            break;
                        case 2:
                            oldValue = String.valueOf(product.getPrice());
                            product.setPrice(Double.parseDouble(newValue));
                            break;
                        case 3:
                            oldValue = String.valueOf(product.getQuantity());
                            product.setQuantity(Integer.parseInt(newValue));
                            break;
                    }
                    if (oldValue.equals(newValue)) {
                        return;
                    }
                    changedProducts.add(product);
                    changesLog += String.format("Product %d: %s changed from %s to %s\n", id, columnName, oldValue, newValue);

                    System.out.println("Changes log: " + changesLog);
                }
            }
        });
    }

    private void saveChanges() {
        if (changedProducts.isEmpty()) {
            JOptionPane.showMessageDialog(staffView, "No changes to save.");
            return;
        }
        JTextArea textArea = new JTextArea(changesLog);
        textArea.setColumns(30);
        textArea.setRows(10);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);

        int response = JOptionPane.showConfirmDialog(null, scrollPane, "Confirm Changes", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (Product product : changedProducts) {
                dataAccess.updateProduct(product);
            }
            changesLog = "";
            changedProducts.clear();
            JOptionPane.showMessageDialog(staffView, "Changes saved successfully!");
        } else {
            JOptionPane.showMessageDialog(staffView, "Changes not saved.");
        }

    }

    private void addProduct() {
        String name = managerView.getProductNameTxt().getText();
        double price = Double.parseDouble(managerView.getProductPriceTxt().getText());
        String category = (String) managerView.getProductCatTxt().getText();
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);
        product.setQuantity(0);
        dataAccess.addProduct(product);
        JOptionPane.showMessageDialog(managerView, "Product added successfully!");
        populateProductTable(managerView.getProductTable(), dataAccess.loadAllProducts());
    }
    private void removeProduct(){
        int[] selectedRows = managerView.getProductTable().getSelectedRows(); // Get all selected rows

        //Display confirmation dialog
        int response = JOptionPane.showConfirmDialog(managerView, "Are you sure you want to delete the selected products?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.NO_OPTION) {
            return;
        }
        if (selectedRows.length > 0) {
            for (int i = selectedRows.length - 1; i >= 0; i--) { // Iterate in reverse order
                int id = (int) managerView.getProductTable().getValueAt(selectedRows[i], 0);
                dataAccess.removeProduct(id);
            }
            JOptionPane.showMessageDialog(managerView, "Product(s) deleted successfully!");
        }
        populateProductTable(managerView.getProductTable(), dataAccess.loadAllProducts());
    }

//    public static void main(String[] args) {
//        DataAccess dataAccess = new DataAccess(InternalApp.getInstance().getConnection());
//        StaffView staffView = new StaffView();
//        ManagerView managerView = new ManagerView();
//        InventoryController inventoryController = new InventoryController(staffView, managerView, dataAccess);
//        staffView.setVisible(true);
//        managerView.setVisible(true);
//    }
}