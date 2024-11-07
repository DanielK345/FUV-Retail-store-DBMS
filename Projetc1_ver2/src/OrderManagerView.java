
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderManagerView extends JFrame {
    private JTable orderTable;
    private JTextArea orderDetailPane;
    private JButton markAsCompleteButton;
    private JPanel panel1;
    private JButton backBtn;
    private final DefaultTableModel tableModel;
    private Connection connection = App.getInstance().getConnection();
    private final DataAccess dataAccess = new DataAccess(connection);


    public OrderManagerView() {
        setContentPane(panel1);
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Order Manager");

        tableModel = new DefaultTableModel(new Object[]{"Order ID", "Customer Name", "Total Amount"}, 0);
        orderTable.setModel(tableModel);

        loadOrdersToTable();

        orderTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = orderTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int orderId = (int) tableModel.getValueAt(selectedRow, 0);
                        System.out.println(orderId);
                        loadOrderDetails(orderId);
                    }
                }
            }
        });

        markAsCompleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow != -1) {
                    int orderId = (int) tableModel.getValueAt(selectedRow, 0);
                    deleteOrder(orderId);
                    tableModel.removeRow(selectedRow);
                    orderDetailPane.setText("");

                    JOptionPane.showMessageDialog(null, "Order marked as complete");
                }
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                App.getInstance().getStaffView().setVisible(true);
            }
        });

    }

    public void loadOrdersToTable() {
        try {
            String query = "SELECT o.order_id, c.full_name, o.order_total_price " +
                    "FROM orders o " +
                    "Join customers c ON o.customer_id = c.id";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                String customerName = resultSet.getString("full_name");
                double totalAmount = resultSet.getDouble("order_total_price");

                tableModel.addRow(new Object[]{orderId, customerName, totalAmount});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadOrderDetails(int orderId) {
        try {
            String query = "SELECT c.full_name, c.address, c.phone_number " +
                    "FROM customers c " +
                    "JOIN orders o ON o.customer_id = c.id " +
                    "WHERE o.order_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            Order order = dataAccess.loadOrder(orderId);

            StringBuilder details = new StringBuilder();

            if (resultSet.next()) {
                String customerName = resultSet.getString("full_name");
                String customerAddress = resultSet.getString("address");
                String customerPhone = resultSet.getString("phone_number");

                String orderDetails = order.toString();


                details.append("Customer info: ").append("\n\n");
                details.append("Name: ").append(customerName).append("\n");
                details.append("Address: ").append(customerAddress).append("\n");
                details.append("Phone: ").append(customerPhone).append("\n");
                details.append("\n--------------------------------------\n");
                details.append("\nOrder Details: ").append("\n\n");
                details.append(orderDetails);

                // Add more details as needed
                orderDetailPane.setText(details.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void deleteOrder(int orderId) {
        try {
            // First, delete from order_items table where order_id matches
            String deleteOrderItemsQuery = "DELETE FROM order_items WHERE order_id = ?";
            PreparedStatement deleteOrderItemsStmt = connection.prepareStatement(deleteOrderItemsQuery);
            deleteOrderItemsStmt.setInt(1, orderId);
            deleteOrderItemsStmt.executeUpdate();

            // Then, delete from orders table
            String deleteOrderQuery = "DELETE FROM orders WHERE order_id = ?";
            PreparedStatement deleteOrderStmt = connection.prepareStatement(deleteOrderQuery);
            deleteOrderStmt.setInt(1, orderId);
            deleteOrderStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setConnection(Connection connection) {
        this.connection = connection;
    }

//    public static void main(String[] args) {
//        Connection connection = App.getInstance().getConnection();
//        OrderManagerView orderManagerView = new OrderManagerView();
//        orderManagerView.setVisible(true);
//    }
}
