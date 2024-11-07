import java.util.ArrayList;
import java.util.Date;

public class Order {
    private int receiptNo;
    private int customerID;
    private ArrayList<Product> products;
    private String date;
    private double totalPrice;
    private String paymentMethod;
    private String address;

    public void setReceiptNo(int receiptNo) {
        this.receiptNo = receiptNo;
    }

    public int getReceiptNo() {
        return receiptNo;
    }

    public String getAddress() {
        return address;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Order(){

    }

    public Order(int customerID, ArrayList<Product> products, String date, double totalPrice, String paymentMethod) {
        this.customerID = customerID;
        this.products = products;
        this.date = date;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
    }

    public String toString() {
        StringBuilder order = new StringBuilder();
        order.append((String.format("Receipt No: %d\nDate: %s\n-----------------------------------------\n", receiptNo, date)));
        order.append("Items: \n");
        for (Product product : products) {
            order.append(String.format("%s x%s $%s\n", product.getName(), product.getQuantity(), product.getPrice()));
        }
        order.append("-----------------------------------------\n");
        order.append(String.format("Total Price: $%.2f\nPayment Method: %s\n",
                totalPrice, paymentMethod));
        return order.toString();
    }

}
