import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CartController {
    private CartView cartView;

    private CustomerCartView customerCartView;
    private DataAccess dataAccess;
    private double totalPrice = 0;



    public CartController(CartView view, DataAccess dataAccess) {
        this.cartView = view;
        this.dataAccess = dataAccess;
        view.getAddBtn().addActionListener(e -> addProduct(view, view.getTable()));
        view.getRemoveBtn().addActionListener(e -> removeProduct(view));
        view.getCheckoutBtn().addActionListener(e -> moveToCheckout(view));
    }

    public CartController(CustomerCartView view, DataAccess dataAccess) {
        this.customerCartView = view;
        this.dataAccess = dataAccess;
        InventoryController inventoryController = new InventoryController(dataAccess);
        view.getAddToCartButton().addActionListener(e -> addProduct(view, view.getCartTableModel()));
        view.getGoToCheckoutButton().addActionListener(e -> moveToCheckout(view));
        view.getRemoveBtn().addActionListener(e -> removeProduct(view));
        view.getCategoryMenu().addActionListener(e -> inventoryController.getProductByCategory(view.getCategoryMenu(), view.getMenuTable()));
        inventoryController.populateProductTable(view.getMenuTable(), dataAccess.loadAllProducts());
        inventoryController.populateCategoryMenu(view.getCategoryMenu(), dataAccess);
        view.getSearchBtn().addActionListener(e -> inventoryController.getProductByName(view.getProductNameSearchTxt(), view.getMenuTable()));
    }

    public void addProduct(CartView view, DefaultTableModel table) {
        Product product;
        int qty;
        if (view instanceof CustomerCartView) {
            String productName = ((CustomerCartView) view).getProductNameTxt().getText();
            String quantityStr = ((CustomerCartView) view).getQuantityTxt().getText();
            qty = Integer.parseInt(quantityStr);
            product = dataAccess.getProductByName(productName);

            if (productName.isEmpty() || quantityStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields");
                return;
            }
        } else {
            String productIDStr = view.getIdTxt().getText();
            String quantityStr = view.getQuantityTxt().getText();
            int productID = Integer.parseInt(productIDStr);
            qty = Integer.parseInt(quantityStr);
            product = dataAccess.loadProduct(productID);

            if (productIDStr.isEmpty() || quantityStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields");
                return;
            }

        }


        try {

            if (product == null) {
                JOptionPane.showMessageDialog(null, "Product not found");
                return;
            }
            if (qty <= 0) {
                JOptionPane.showMessageDialog(null, "Invalid quantity");
                return;
            }
            if (qty > product.getQuantity()) {
                JOptionPane.showMessageDialog(null, "Not enough in stock");
                return;
            }

            double sum = qty * product.getPrice();
            totalPrice += sum;
            view.setTotalPriceTxt(String.format("%.2f", totalPrice));
            Object[] productTuple = {product.getID(), product.getName(), String.valueOf(qty), String.format("%.2f", sum)};
            table.addRow(productTuple);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input");
        }
    }

    //get all items in table
    public ArrayList<Product> getAllItems(CartView view) {
        ArrayList<Product> items = new ArrayList<>();
        DefaultTableModel model;
        if (view instanceof CustomerCartView) {
            model = ((CustomerCartView) view).getCartTableModel();
        } else {
            model = view.getTable();
        }
        int rowCount = model.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            int id =  (Integer) model.getValueAt(i, 0);
            Product product = dataAccess.getProductByID(id);
            product.setName((String) model.getValueAt(i, 1));
            product.setQuantity(Integer.parseInt((String) model.getValueAt(i, 2)));
            product.setPrice(Double.parseDouble((String) model.getValueAt(i, 3)));
            items.add(product);
        }
        return items;
    }


    public void removeProduct(CartView view) {
        JTable jtable;
        DefaultTableModel model;
        if (view instanceof CustomerCartView) {
            jtable = view.getCartTable();
            model = ((CustomerCartView) view).getCartTableModel();
        } else {
            jtable = view.getCartTable();
            model = view.getTable();
        }
        int[] selectedRows = jtable.getSelectedRows(); // Get all selected rows
        if (selectedRows.length > 0) {
            for (int i = selectedRows.length - 1; i >= 0; i--) { // Iterate in reverse order
                double price = Double.parseDouble((String) model.getValueAt(selectedRows[i], 3));
                totalPrice -= price;
                model.removeRow(selectedRows[i]);
            }
            view.setTotalPriceTxt(String.format("%.2f", totalPrice));
        } else {
            JOptionPane.showMessageDialog(null, "Please select rows to delete");
        }
    }

    public void moveToCheckout(CartView view) {
        ArrayList<Product> itemList = getAllItems(view);
        if (totalPrice == 0) {
            JOptionPane.showMessageDialog(null, "Please add products to the cart");
            return;
        }
        int phoneNumberInt;
        if (view instanceof CustomerCartView) phoneNumberInt = App.getInstance().getCustomer().getPhone();
        else{
            String phoneNumber = view.getPhoneNumberTxt().getText();

            if (!phoneNumber.isEmpty()) {
                phoneNumberInt = Integer.parseInt(phoneNumber);
            }
            else phoneNumberInt = -1;
        }
        Customer customer;
        if (cartView instanceof CustomerCartView) {
            customer = App.getInstance().getCustomer();
        }
        else {
            customer = dataAccess.loadCustomerByPhone(phoneNumberInt);
            if (customer == null) {
                JOptionPane.showMessageDialog(null, "Customer not found");
                return;
            }
        }
        System.out.println(phoneNumberInt);
        System.out.println(customer);
        ReceiptView receiptView = new ReceiptView(view, customer, totalPrice, itemList);
        receiptView.setVisible(true);
        view.dispose();
    }

    public static void main(String[] args) {
        DataAccess dataAccess = new DataAccess(App.getInstance().getConnection());
        CustomerCartView view = new CustomerCartView();
        CartController controller = new CartController(view, dataAccess);
        view.setVisible(true);
    }
}
