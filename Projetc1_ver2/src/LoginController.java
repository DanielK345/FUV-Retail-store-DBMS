import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController implements ActionListener {
    private LoginScreen loginScreen;
    private DataAccess dataAccess;


    public LoginController(LoginScreen loginScreen, DataAccess dataAccess) {
        this.loginScreen = loginScreen;
        this.dataAccess = dataAccess;
        this.loginScreen.getBtnLogin().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginScreen.getBtnLogin()) {
            String username = loginScreen.getTxtUserName().getText().trim();
            String password = loginScreen.getTxtPassword().getText().trim();

            InternalUser internalUser = null;
            Customer customer = null;

            System.out.println("Login with username = " + username + " and password = " + password);
            if (loginScreen instanceof LoginScreen.InternalLoginScreen) {
                internalUser = dataAccess.loadUser(username, password);
                if (internalUser == null) {
                    JOptionPane.showMessageDialog(null, "This user does not exist!");
                }
                if (internalUser.getRole().equals("Cashier")) {
                    App.getInstance().setCurrentUser(internalUser);
                    this.loginScreen.setVisible(false);
                    App.getInstance().getCartView().setVisible(true);
                }
                if (internalUser.getRole().equals("Staff")) {
                    App.getInstance().setCurrentUser(internalUser);
                    this.loginScreen.setVisible(false);
                    App.getInstance().getStaffView().setVisible(true);
                }
                if (internalUser.getRole().equals("Manager")) {
                    App.getInstance().setCurrentUser(internalUser);
                    this.loginScreen.setVisible(false);
                    App.getInstance().getManagerView().setVisible(true);
                }
            }
            if (loginScreen instanceof LoginScreen.CustomerLoginScreen) {
                customer = dataAccess.loadCustomer(Integer.parseInt(username), password);
                if (customer == null) {
                    JOptionPane.showMessageDialog(null, "This customer does not exist!");
                }
                else {
                    loginScreen.setVisible(false);
                    App.getInstance().setCustomer(customer);
                    CustomerCartView customerCartView = App.getInstance().getCustomerCartView();
                    customerCartView.getWelcomePanel().setText("<html>Welcome " + customer.getName() + "<br><br>Have a nice shopping!</html>");
                    customerCartView.setVisible(true);
                }
            }
        }
    }

}
