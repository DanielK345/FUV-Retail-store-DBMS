import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class EmployeeController {
    private final ManagerView managerView;
    private final DataAccess dataAccess;

    public EmployeeController(ManagerView managerView, DataAccess dataAccess) {
        this.managerView = managerView;
        this.dataAccess = dataAccess;

        // Populate the employee table
        populateEmployeeTable(dataAccess.loadAllEmployees());
        populateRoleMenu();

        // Add action listeners for buttons
        this.managerView.getCreateEButton().addActionListener(e -> addEmployee());
        this.managerView.getSaveEmpBtn().addActionListener(e -> updateEmployee());
        this.managerView.getSubmitEButton().addActionListener(e -> getEmployeeByID());
        this.managerView.getRoleMenu().addActionListener(e -> getEmployeeByRole());
        this.managerView.getDeleteEmployeeButton().addActionListener(e -> removeEmployee());
    }

    private void populateEmployeeTable(ArrayList<Employee> employees) {
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Username", "Email", "Phone number ","Role"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Make ID column uneditable
            }
        };
        managerView.getEmployeeTable().setModel(tableModel);
        tableModel.setRowCount(0); // Clear existing rows

        for (Employee employee : employees) {
            Object[] row = {employee.getID(), employee.getUsername(), null, null, employee.getRole()};
            tableModel.addRow(row);
        }
    }

    private void populateRoleMenu() {
        JComboBox<String> roleMenu = managerView.getRoleMenu();
        roleMenu.addItem("Cashier");
        roleMenu.addItem("Staff");

    }

    private void getEmployeeByID() {
        String idText = managerView.getEmpIDTxt().getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(managerView, "Please enter an employee ID.");
            populateEmployeeTable(dataAccess.loadAllEmployees());
            return;
        }
        int id = Integer.parseInt(idText);
        Employee employee = dataAccess.loadEmployee(id);
        if (employee != null) {
            DefaultTableModel tableModel = (DefaultTableModel) managerView.getEmployeeTable().getModel();
            tableModel.setRowCount(0); // Clear existing rows
            Object[] row = {employee.getID(), employee.getUsername(), employee.getEmail(), employee.getPhoneNumber(), employee.getRole()};
            tableModel.addRow(row);
        } else {
            JOptionPane.showMessageDialog(managerView, "Employee not found.");
        }
    }

    private void getEmployeeByRole() {
        String role = (String) managerView.getRoleMenu().getSelectedItem();
        ArrayList<Employee> employees = dataAccess.loadEmployeesByRole(role);
        populateEmployeeTable(employees);
    }

    private void addEmployee() {
        String username = managerView.getNewUsNTxt().getText();
        String password = managerView.getNewPwdTxt().getText();
        String role = managerView.getNewRoleTxt().getText();
        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setPassword(password);
        employee.setRole(role);
        dataAccess.addEmployee(employee);
        JOptionPane.showMessageDialog(managerView, "Employee added successfully!");
        populateEmployeeTable(dataAccess.loadAllEmployees());
    }

    private void updateEmployee() {
        int selectedRow = managerView.getEmployeeTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(managerView, "Please select an employee to update.");
            return;
        }
        int id = (int) managerView.getEmployeeTable().getValueAt(selectedRow, 0);
        String username = (String) managerView.getEmployeeTable().getValueAt(selectedRow, 1);
        String role = (String) managerView.getEmployeeTable().getValueAt(selectedRow, 2);

        Employee employee = new Employee();
        employee.setID(id);
        employee.setUsername(username);
        employee.setRole(role);
        dataAccess.updateEmployee(employee);
        JOptionPane.showMessageDialog(managerView, "Employee updated successfully!");
        populateEmployeeTable(dataAccess.loadAllEmployees());
    }

    private void removeEmployee() {
        int[] selectedRows = managerView.getEmployeeTable().getSelectedRows(); // Get all selected rows

        // Display confirmation dialog
        int response = JOptionPane.showConfirmDialog(managerView, "Are you sure you want to delete the selected employees?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.NO_OPTION) {
            return;
        }
        if (selectedRows.length > 0) {
            for (int i = selectedRows.length - 1; i >= 0; i--) { // Iterate in reverse order
                int id = (int) managerView.getEmployeeTable().getValueAt(selectedRows[i], 0);
                dataAccess.removeEmployee(id);
            }
            populateEmployeeTable(dataAccess.loadAllEmployees());
            JOptionPane.showMessageDialog(managerView, "Employee(s) deleted successfully!");
        }
    }

//    public static void main(String[] args) {
//        DataAccess dataAccess = new DataAccess(InternalApp.getInstance().getConnection());
//        ManagerView managerView = new ManagerView();
//        EmployeeController employeeController = new EmployeeController(managerView, dataAccess);
//        managerView.setVisible(true);
//    }
}