import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Order_DesertData {
    Connection conn;
    private static Order_DesertData data;

    private Order_DesertData(Connection conn, boolean newTable) {
        this.conn = conn;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Order_Desert(orderId INT, desertId INT,quantity INT, PRIMARY KEY(orderId, desertId), FOREIGN KEY (orderId) REFERENCES Orders(orderId) ON DELETE CASCADE, FOREIGN KEY (desertId) REFERENCES Deserts(desertId) ON DELETE CASCADE)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        data = this;
    }

    public static Order_DesertData getInstance(Connection conn) {
        if (data == null) {
            return new Order_DesertData(conn, true);
        } else {
            return data;
        }
    }

    public List<Desert> findAllDeserts(int orderId) {
        List<Desert> deserts = new ArrayList<Desert>();
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("SELECT desertId FROM Order_Desert WHERE orderId = ?;");
            pstmt.setInt(1, orderId);
            DesertData data = DesertData.getInstance(conn);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Integer desertId = rs.getInt(1);
                deserts.add(data.find(desertId));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return deserts;
    }

    public void insert(Order order, List<Desert> deserts,List<Integer> quantities) throws SQLException {
        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn
                    .prepareStatement("INSERT INTO Order_Desert(orderId, desertId, quantity) VALUES (?, ?, ?);");
            for (int i = 0; i < deserts.size(); i++) {
                pstmt.setInt(1, order.getOrderId());
                pstmt.setInt(2, deserts.get(i).getDesertId());
                pstmt.setInt(3, quantities.get(i));
                pstmt.addBatch();
                ;
            }
            pstmt.executeBatch();
            conn.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public void delete(Order order) {
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("DELETE FROM Order_Desert WHERE orderId = ?;");
            pstmt.setInt(1, order.getOrderId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteALL() {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Order_Desert;");
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public double getDesertsPrice(int orderId) {
        List<Desert> ingridientIdList = findAllDeserts(orderId);
        double price = 0;
        for (int i = 0; i < ingridientIdList.size(); i++) {
            price += ingridientIdList.get(i).getPrice();
        }
        return price;
    }
}
