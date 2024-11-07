import java.sql.*;
import java.util.ArrayList;

public class DataAccess {
    private Connection connection;

//
//    public Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(URL, USER, PASSWORD);
//    }

    public DataAccess(Connection connection) {
        this.connection = connection;
    }

    /*------LOG IN SCREEN-------------*/
    public InternalUser loadUser(String userName, String password) {
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Internal_Users WHERE username = ? AND password = ?");
            statement.setString(1, userName);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                InternalUser user = new InternalUser();
                user.setUserID(resultSet.getString("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getString("user_type"));
                resultSet.close();
                statement.close();

                return user;
            }

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }

    /*------PRODUCT-------------*/
    public Product loadProduct(int id) {
        try {
            String query = "SELECT * FROM Products WHERE Product_id = " + id;

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                Product product = new Product();
                product.setID(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setPrice(resultSet.getDouble(3));
                product.setQuantity(resultSet.getInt(4));
                resultSet.close();
                statement.close();

                return product;
            }

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> loadAllCategories() {
        ArrayList<String> categories = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT* FROM Product_category");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                categories.add(resultSet.getString("category_name"));
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return categories;
    }

    public ArrayList<Product> loadProductsByCategory(String category) {
        ArrayList<Product> products = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Products WHERE product_category = (SELECT category_id FROM Product_category WHERE category_name = ?)");
            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setID(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setPrice(resultSet.getDouble(3));
                product.setQuantity(resultSet.getInt(4));
                products.add(product);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return products;
    }

    public Product getProductByName(String name){
        try {
            String query = "SELECT * FROM Products WHERE product_name =  ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Product product = new Product();
                product.setID(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setPrice(resultSet.getDouble(3));
                product.setQuantity(resultSet.getInt(4));
                resultSet.close();
                statement.close();
                return product;
            }
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }

    public Product getProductByID(int id) {
        try {
            String query = "SELECT * FROM Products WHERE product_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Product product = new Product();
                product.setID(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setPrice(resultSet.getDouble(3));
                product.setQuantity(resultSet.getInt(4));
                resultSet.close();
                statement.close();
                return product;
            }
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Product> loadAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        try {
            String query = "SELECT * FROM Products";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Product product = new Product();
                product.setID(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setPrice(resultSet.getDouble(3));
                product.setQuantity(resultSet.getInt(4));
                products.add(product);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return products;
    }

    public ArrayList<Product> loadOutOfStockProducts() {
        ArrayList<Product> products = new ArrayList<>();
        try {
            String query = "SELECT * FROM Products WHERE product_quantity = 0";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Product product = new Product();
                product.setID(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setPrice(resultSet.getDouble(3));
                product.setQuantity(resultSet.getInt(4));
                products.add(product);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return products;
    }

    public void updateProduct(Product product) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Products SET product_name = ?, product_price = ?, product_quantity = ? WHERE product_id = ?");
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setInt(4, product.getID());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
    }

    public void addProduct(Product product) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Products (product_name, product_price, product_category, product_quantity) VALUES (?, ?, ?, ?)");
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setString(3, product.getCategory());
            statement.setInt(4, product.getQuantity());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
    }

    public void removeProduct(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Products WHERE product_id = ?");
            statement.setInt(1, id);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
    }

    /*------EMPLOYEE-------------*/
    public ArrayList<Employee> loadAllEmployees() {
        ArrayList<Employee> employees = new ArrayList<>();
        try {
            String query = "SELECT * FROM Internal_Users where user_type = 'Cashier' or user_type = 'Staff'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setID(resultSet.getInt("user_id"));
                employee.setUsername(resultSet.getString("username"));
                employee.setPassword(resultSet.getString("password"));
                employee.setRole(resultSet.getString("user_type"));
                employees.add(employee);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public Employee loadEmployee(int id) {
        try {
            String query = "SELECT * FROM Internal_Users WHERE user_id = " + id;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                Employee employee = new Employee();
                employee.setID(resultSet.getInt("user_id"));
                employee.setUsername(resultSet.getString("username"));
                employee.setPassword(resultSet.getString("password"));
                employee.setRole(resultSet.getString("user_type"));
                resultSet.close();
                statement.close();
                return employee;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Employee> loadEmployeesByRole(String role) {
        ArrayList<Employee> employees = new ArrayList<>();
        try {
            String query = "SELECT * FROM Internal_Users WHERE user_type = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, role);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setID(resultSet.getInt("user_id"));
                employee.setUsername(resultSet.getString("username"));
                employee.setPassword(resultSet.getString("password"));
                employee.setRole(resultSet.getString("user_type"));
                employees.add(employee);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public void addEmployee(Employee employee) {
        try {
            String query = "INSERT INTO Internal_Users (username, password, user_type) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, employee.getUsername());
            statement.setString(2, employee.getPassword());
            statement.setString(3, employee.getRole());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee(Employee employee) {
        try {
            String query = "UPDATE Internal_Users SET username = ?, password = ?, user_type = ? WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, employee.getUsername());
            statement.setString(2, employee.getPassword());
            statement.setString(3, employee.getRole());
            statement.setInt(4, employee.getID());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeEmployee(int id) {
        try {
            String query = "DELETE FROM Internal_Users WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer loadCustomer(int phoneNumber, String password){
        try {
            String query = "SELECT * FROM Customers WHERE phone_number = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, phoneNumber);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setPhone(resultSet.getInt("phone_number"));
                customer.setName(resultSet.getString("full_name"));
                customer.setAddress(resultSet.getString("address"));
                customer.setPassword(resultSet.getString("password"));
                customer.setCredit(resultSet.getDouble("credit_score"));
                customer.setBalance(resultSet.getDouble("balance"));
                resultSet.close();
                statement.close();
                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Load customer by ID
    public Customer loadCustomerByID(int id) {
        try {
            String query = "SELECT * FROM Customers WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setPhone(resultSet.getInt("phone_number"));
                customer.setName(resultSet.getString("full_name"));
                customer.setAddress(resultSet.getString("address"));
                customer.setPassword(resultSet.getString("password"));
                customer.setCredit(resultSet.getDouble("credit_score"));
                customer.setBalance(resultSet.getDouble("balance"));
                resultSet.close();
                statement.close();
                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Customer loadCustomerByPhone(int phoneNumber) {
        try {
            String query = "SELECT * FROM Customers WHERE phone_number = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, phoneNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setPhone(resultSet.getInt("phone_number"));
                customer.setName(resultSet.getString("full_name"));
                customer.setAddress(resultSet.getString("address"));
                customer.setPassword(resultSet.getString("password"));
                customer.setCredit(resultSet.getDouble("credit_score"));
                customer.setBalance(resultSet.getDouble("balance"));
                resultSet.close();
                statement.close();
                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addOrder(Order order) {
        try {
            String query = "INSERT INTO Orders (customer_id, order_date, order_total_price, payment_method) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, order.getCustomerID());
            statement.setString(2, order.getDate());
            statement.setDouble(3, order.getTotalPrice());
            statement.setString(4, order.getPaymentMethod());

            statement.executeUpdate();
            statement.close();

            //Get the last receipt number
            String query1 = "SELECT MAX(order_id) FROM Orders";
            PreparedStatement statement1 = connection.prepareStatement(query1);
            ResultSet resultSet = statement1.executeQuery();
            if (resultSet.next()) {
                order.setReceiptNo(resultSet.getInt(1));
            } else {
                order.setReceiptNo(1);
            }
            resultSet.close();

            //Add each item into Order_Items table
            for (Product product : order.getProducts()) {
                String query2 = "INSERT INTO Order_Items (order_id, product_id, order_item_quantity) VALUES (?, ?, ?)";
                PreparedStatement statement2 = connection.prepareStatement(query2);
                statement2.setInt(1, order.getReceiptNo());
                statement2.setInt(2, product.getID());
                statement2.setInt(3, product.getQuantity());
                statement2.executeUpdate();
                statement2.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order loadOrder(int receiptNo) {
        try {
            String query = "SELECT * FROM Orders WHERE order_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, receiptNo);
            ResultSet resultSet = statement.executeQuery();
            Order order = new Order();
            if (resultSet.next()) {
                order.setReceiptNo(resultSet.getInt("order_id"));
                order.setCustomerID(resultSet.getInt("customer_id"));
                order.setDate(resultSet.getString("order_date"));
                order.setTotalPrice(resultSet.getDouble("order_total_price"));
                order.setPaymentMethod(resultSet.getString("payment_method"));
                resultSet.close();
                statement.close();
            }
            //Get all products in the order
            String query1 = "SELECT * FROM Order_Items WHERE order_id = ?";
            PreparedStatement statement1 = connection.prepareStatement(query1);
            statement1.setInt(1, receiptNo);
            ResultSet resultSet1 = statement1.executeQuery();
            ArrayList<Product> products = new ArrayList<>();
            while (resultSet1.next()) {
                Product product = getProductByID(resultSet1.getInt("product_id"));
                product.setQuantity(resultSet1.getInt("order_item_quantity"));
                products.add(product);
            }
            order.setProducts(products);
            return order;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String loadOrderDetails(int orderId) {
        try {
            String query = "SELECT c.full_name, c.address, c.phone_number " +
                    "FROM customers c " +
                    "JOIN orders o ON o.customer_id = c.id " +
                    "WHERE o.order_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            Order order = loadOrder(orderId);

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
                return details.toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


//
//    /*------VIEW ORDER SCREEN-------------*/
//
//    public Order getOrderByID(String orderID) {
//        try {
//            PreparedStatement getOrderStatement = connection.prepareStatement("SELECT * FROM Orders WHERE order_id = ?");
//            PreparedStatement getItemStatement = connection.prepareStatement("SELECT * FROM Order_Items WHERE order_id = ?");
//            getOrderStatement.setString(1, orderID);
//            ResultSet orderSet = getOrderStatement.executeQuery();
//            ResultSet itemSet = getItemStatement.executeQuery();
//
//            Order order = new Order();
//            while (itemSet.next()) {
//                Product product = getProductByID(itemSet.getString("product_id"));
//                product.setQuantity(itemSet.getInt("order_item_quantity"));
//                order.addProduct(product);
//            }
//
//            while (orderSet.next()) {
//                order.setOrderID(orderSet.getInt("order_id"));
//                order.setCustomerID(orderSet.getString("customer_id"));
//                order.setOrderDate(orderSet.getDate("order_date"));
//                order.setOrderStatus(orderSet.getString("order_status"));
//                order.setOrderTotalPrice(orderSet.getDouble("order_total_price"));
//                order.setPaymentStatus(orderSet.getString("payment_status"));
//                orderSet.close();
//                getOrderStatement.close();
//
//                return order;
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Database access error!");
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public ArrayList<Order> getOrdersByCustomerID(String customerID) {
//        ArrayList<Order> orders = new ArrayList<>();
//
//        try {
//            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Orders WHERE customer_id = ?");
//            statement.setString(1, customerID);
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                Order order = new Order();
//                order.setOrderID(resultSet.getInt("order_id"));
//                order.setCustomerID(resultSet.getString("customer_id"));
//                order.setOrderDate(resultSet.getDate("order_date"));
//                order.setOrderStatus(resultSet.getString("order_status"));
//                order.setOrderTotalPrice(resultSet.getDouble("order_total_price"));
//                order.setPaymentStatus(resultSet.getString("payment_status"));
//
//                orders.add(order);
//            }
//            resultSet.close();
//            statement.close();
//
//        } catch (SQLException e) {
//            System.out.println("Database access error!");
//            e.printStackTrace();
//        }
//        return orders;
//    }
//
//    public ArrayList<Order> getOrdersByStatus(String status) {
//        ArrayList<Order> orders = new ArrayList<>();
//
//        try {
//            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Orders WHERE order_status = ?");
//            statement.setString(1, status);
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                Order order = new Order();
//                order.setOrderID(resultSet.getInt("order_id"));
//                order.setCustomerID(resultSet.getString("customer_id"));
//                order.setOrderDate(resultSet.getDate("order_date"));
//                order.setOrderStatus(resultSet.getString("order_status"));
//                order.setOrderTotalPrice(resultSet.getDouble("order_total_price"));
//                order.setPaymentStatus(resultSet.getString("payment_status"));
//
//                orders.add(order);
//            }
//            resultSet.close();
//            statement.close();
//
//        } catch (SQLException e) {
//            System.out.println("Database access error!");
//            e.printStackTrace();
//        }
//        return orders;
//    }
//
//    public ArrayList<Order> getOrdersByDate(Date orderDate) {
//        ArrayList<Order> orders = new ArrayList<>();
//
//        try {
//            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Orders WHERE order_date = ?");
//            statement.setDate(1, orderDate);
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                Order order = new Order();
//                order.setOrderID(resultSet.getInt("order_id"));
//                order.setCustomerID(resultSet.getString("customer_id"));
//                order.setOrderDate(resultSet.getDate("order_date"));
//                order.setOrderStatus(resultSet.getString("order_status"));
//                order.setOrderTotalPrice(resultSet.getDouble("order_total_price"));
//                order.setPaymentStatus(resultSet.getString("payment_status"));
//
//                orders.add(order);
//            }
//            resultSet.close();
//            statement.close();
//
//        } catch (SQLException e) {
//            System.out.println("Database access error!");
//            e.printStackTrace();
//        }
//        return orders;
//    }
//
//
//    public ArrayList<Order> getOrdersByPaymentStatus(String paymentStatus) {
//        ArrayList<Order> orders = new ArrayList<>();
//
//        try {
//            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Orders WHERE payment_status = ?");
//            statement.setString(1, paymentStatus);
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                Order order = new Order();
//                order.setOrderID(resultSet.getInt("order_id"));
//                order.setCustomerID(resultSet.getString("customer_id"));
//                order.setOrderDate(resultSet.getDate("order_date"));
//                order.setOrderStatus(resultSet.getString("order_status"));
//                order.setOrderTotalPrice(resultSet.getDouble("order_total_price"));
//                order.setPaymentStatus(resultSet.getString("payment_status"));
//
//                orders.add(order);
//            }
//            resultSet.close();
//            statement.close();
//
//        } catch (SQLException e) {
//            System.out.println("Database access error!");
//            e.printStackTrace();
//        }
//        return orders;
//    }
}








