import java.sql.*;
import java.util.Date;

public class OrderData {
    Connection conn;
    private static OrderData data;

    private OrderData(Connection conn, boolean newTable) {
        this.conn = conn;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Orders (OrderId INT NOT NULL AUTO_INCREMENT,customerId VARCHAR(30),delivererId VARCHAR(30),status VARCHAR(20),timeOfOrder TIMESTAMP,total Double, VAT Double,PRIMARY KEY (OrderId), FOREIGN KEY (customerId) REFERENCES Customers(phoneNumber), FOREIGN KEY (delivererId) REFERENCES Deliverers(phoneNumber))");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        data = this;
    }
    public static OrderData getInstance(Connection conn){
        if(data == null){
            return new OrderData(conn, true);
        }
        else{
            return data;
        }
    }

    public Order find(String DelivererId) {
        Order newOrder = null;
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT customerId,delivererId,status,timeOfOrder,total,VAT FROM Orders WHERE delivererId = ?;");
            pstmt.setString(1, DelivererId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                newOrder = new Order(rs.getString(1),rs.getString(2),rs.getString(3),rs.getDouble(4),rs.getDouble(5));
                newOrder.setOrderId(rs.getInt(DelivererId));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return newOrder;
    }

    public void insert(Order newOrder) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Orders (customerId, delivererId, status,timeOfOrder,total,VAT) VALUES (?, ?, ?, ?, ?, ?);",Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, newOrder.getCustomerId());
            pstmt.setString(2, newOrder.getDelivererId());
            pstmt.setString(3, newOrder.getStatus());
            pstmt.setTimestamp(4, new Timestamp(new Date().getTime()));
            pstmt.setDouble(5, newOrder.getTotal());
            pstmt.setDouble(6, newOrder.getVAT());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int key = -1;
            if(rs.next())
                key = rs.getInt(1);
            newOrder.setOrderId(key);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Order oldOrder) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Orders WHERE OrderId = ?;");
            pstmt.setInt(1, oldOrder.getOrderId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void deleteALL() {
        try {
            Statement stmt = conn.createStatement();
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Orders;");
            stmt.executeUpdate("ALTER TABLE Orders AUTO_INCREMENT = 1");
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int differenceInTime(Order order) throws SQLException{
        int difference = 0;
        PreparedStatement pstmt = conn.prepareStatement("SELECT TIMESTAMPDIFF(SECOND, timeOfOrder, CURRENT_TIMESTAMP) FROM Orders WHERE OrderId = ?;");
            pstmt.setInt(1, order.getOrderId());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                difference = rs.getInt(1)/60;
            }
            return difference;
    }

}
