public class Customer {
    private int id;
    private String name;
    private String address;
    private int phone;
    private String password;
    private double credit;
    private double balance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getCredit() {
        return credit;
    }

    public double getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

}
