import javax.swing.*;

public class LoginScreen extends JFrame {
    private JTextField userNameTxt;
    private JTextField passwordTxt;
    private JButton loginBtn;
    private JButton clearBtn;
    private JLabel usernameLabel;
    private JLabel pwdLabel;

    private JPanel loginPanel1;

    public LoginScreen() {
        setContentPane(loginPanel1);
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public JButton getBtnLogin() {
        return loginBtn;
    }

    public JTextField getTxtPassword() {
        return passwordTxt;
    }

    public JTextField getTxtUserName() {
        return userNameTxt;
    }


//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            InternalLoginScreen internalLoginScreen = new InternalLoginScreen();
//        });
//    }



    public static class CustomerLoginScreen extends LoginScreen {
        public CustomerLoginScreen() {
            super();
        }
    }

    public static class InternalLoginScreen extends LoginScreen {
        public InternalLoginScreen() {
            super();
        }
    }


}
