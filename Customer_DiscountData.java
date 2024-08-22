import java.sql.*;

public class Customer_DiscountData {
    Connection conn;
    private static Customer_DiscountData data;

    private Customer_DiscountData(Connection conn, boolean newTable) {
        this.conn = conn;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Customer_Discount(customerId VARCHAR(30), discountCode VARCHAR(10),PRIMARY KEY(customerId, discountCode), FOREIGN KEY (customerId) REFERENCES Customers(phoneNumber) ON DELETE CASCADE)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        data = this;
    }

    public static Customer_DiscountData getInstance(Connection conn) {
        if (data == null) {
            return new Customer_DiscountData(conn, true);
        } else {
            return data;
        }
    }

    public String findDiscount(Customer customer) {
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("SELECT discountCode FROM Customer_Discount WHERE customerId = ?;");
            pstmt.setString(1, customer.getPhoneNumber());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println(rs.getString(1));
                return rs.getString(1);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void insert(Customer customer, String discount){
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("INSERT INTO Customer_Discount(customerId, discountCode) VALUES (?, ?);");
            pstmt.setString(1, customer.getPhoneNumber());
            pstmt.setString(2, discount);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
    }

    public void delete(String discount) {
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("DELETE FROM Customer_Discount WHERE discountCode = ?;");
            pstmt.setString(1, discount);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public boolean validateDiscount(Customer customer, String discount) {
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("SELECT * FROM Customer_Discount WHERE customerId = ? and discountCode = ?;");
            pstmt.setString(1, customer.getPhoneNumber());
            pstmt.setString(2, discount);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
