import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerData {
    Connection conn;
    private static CustomerData data;

    private CustomerData(Connection conn, boolean newTable) {
        this.conn = conn;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Customers (name VARCHAR(30), address VARCHAR(30),phoneNumber VARCHAR(30) NOT NULL,numberOfPizzas INT, PRIMARY KEY (phoneNumber))");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        data = this;
    }

    public static CustomerData getInstance(Connection conn) {
        if (data == null) {
            return new CustomerData(conn, true);
        } else {
            return data;
        }
    }

    public Customer find(String phoneNumber) {
        Customer newCustomer = null;
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("SELECT name,address FROM Customers WHERE phoneNumber = ?;");
            pstmt.setString(1, phoneNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                newCustomer = new Customer(rs.getString(1), rs.getString(2),phoneNumber);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return newCustomer;
    }

    public void update(Customer newCustomer) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE Customers SET name = ?, address = ?, numberOfPizzas = ? WHERE phoneNumber = ?;");
            pstmt.setString(1, newCustomer.getName());
            pstmt.setString(2, newCustomer.getAddress());
            pstmt.setInt(3, newCustomer.getNumberOfPizzasOrdered());
            pstmt.setString(4, newCustomer.getPhoneNumber());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insert(Customer customer) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO Customers (name, address, phoneNumber,numberOfPizzas) VALUES (?, ?, ?, ?);");
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getAddress());
            pstmt.setString(3, customer.getPhoneNumber());
            pstmt.setInt(4, customer.getNumberOfPizzasOrdered());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Customer> getAllCustomers() {
        List<Customer> Customers = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customers");
            while (rs.next()) {
                Customer customer = new Customer(rs.getString(1), rs.getString(2), rs.getString(3));
                Customers.add(customer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Customers;
    }

    public void deleteALL() {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Customers;");
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean checkIsNumberOfPizzas10(Customer customer) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT numberOfPizzas FROM Customers WHERE phoneNumber = ?;");
        pstmt.setString(1, customer.getPhoneNumber());
        ResultSet rs = pstmt.executeQuery();
        int numberOfPizzas = 0;
        if (rs.next()) {
            numberOfPizzas = rs.getInt(1);
        }
        if (numberOfPizzas >= 10) {
            customer.setNumberOfPizzasOrdered(numberOfPizzas-10);
            update(customer);
            return true;
        }

        return false;
    }
    public void delete(String customerId){
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Customers WHERE PhoneNumber = ?;");
            pstmt.setString(1, customerId);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
