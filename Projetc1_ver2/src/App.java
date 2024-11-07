import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    private static App instance;

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    private DataAccess dataAccess;

    private InternalUser currentUser = null;
    private Customer customer = null;

    public InternalUser getCurrentUser() { return currentUser; }

    public void setCurrentUser(InternalUser user) {
        this.currentUser = user;
    }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    private CartView cartView = new CartView();
    public CartView getCartView() {
        return cartView;
    }

    private CustomerCartView customerCartView = new CustomerCartView();
    public CustomerCartView getCustomerCartView() {
        return customerCartView;
    }

    private StaffView staffView = new StaffView();
    public StaffView getStaffView() {return staffView;}

    private ManagerView managerView = new ManagerView();
    public ManagerView getManagerView() {return managerView;}

    public LoginScreen internalLoginScreen = new LoginScreen.InternalLoginScreen();
    public LoginScreen customerLoginScreen = new LoginScreen.CustomerLoginScreen();

    public LoginScreen getInternalLoginScreen() {
        return internalLoginScreen;
    }
    public LoginScreen getCustomerLoginScreen() {return customerLoginScreen;}

    public LoginController loginController1;
    public LoginController loginController2;
    public CartController cartController1;
    public CartController cartController2;
    public InventoryController inventoryController;

    public EmployeeController employeeController;


    public DataAccess getDataAccess() {
        return dataAccess;
    }


    private App() {
        // create SQLite database connection here!
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String URL = "jdbc:mysql://localhost:3306/retailstore";
            String USER = "root";
            String PASSWORD = "Pkd@0604";

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            dataAccess = new DataAccess(connection);

        }
        catch (ClassNotFoundException ex) {
            System.out.println("SQLite is not installed. System exits with error!");
            ex.printStackTrace();
            System.exit(1);
        }

        catch (SQLException ex) {
            System.out.println("SQLite database is not ready. System exits with error!" + ex.getMessage());

            System.exit(2);
        }

        // Create data adapter here!

        loginController1 = new LoginController(customerLoginScreen, dataAccess);
        loginController2 = new LoginController(internalLoginScreen, dataAccess);
        cartController1 = new CartController(cartView, dataAccess);
        cartController2 = new CartController(customerCartView, dataAccess);
        inventoryController = new InventoryController(staffView, managerView, dataAccess);
        employeeController = new EmployeeController(managerView, dataAccess);

    }

    public void runInternalApp() {
        App.getInstance().getInternalLoginScreen().setVisible(true); //For internal users
        App.getInstance().getCustomerLoginScreen().setVisible(false); //For customers
    }

    public void runCustomerApp() {
        App.getInstance().getInternalLoginScreen().setVisible(false); //For internal users
        App.getInstance().getCustomerLoginScreen().setVisible(true); //For customers
    }



    public static void main(String[] args) {

        App.getInstance().runInternalApp();
//        App.getInstance().runCustomerApp();

    }


}

