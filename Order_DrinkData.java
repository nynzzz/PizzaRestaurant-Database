import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Order_DrinkData {
    Connection conn;
    private static Order_DrinkData data;

    private Order_DrinkData(Connection conn, boolean newTable) {
        this.conn = conn;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Order_Drink(orderId INT, drinkId INT,quantity INT, PRIMARY KEY (orderId,drinkId), FOREIGN KEY (orderId) REFERENCES Orders(orderId) ON DELETE CASCADE, FOREIGN KEY (drinkId) REFERENCES Drinks(drinkId) ON DELETE CASCADE)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        data = this;
    }

    public static Order_DrinkData getInstance(Connection conn) {
        if (data == null) {
            return new Order_DrinkData(conn, true);
        } else {
            return data;
        }
    }

    public List<Drink> findAllDrinks(int orderId) {
        List<Drink> Drinks = new ArrayList<Drink>();
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("SELECT drinkId FROM Order_Drink WHERE orderId = ?;");
            pstmt.setInt(1, orderId);
            DrinkData data = DrinkData.getInstance(conn);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Integer DrinkId = rs.getInt(1);
                Drinks.add(data.find(DrinkId));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Drinks;
    }

    public void insert(Order order, List<Drink> Drinks,List<Integer> quantities) throws SQLException {
        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn
                    .prepareStatement("INSERT INTO Order_Drink(orderId, drinkId, quantity) VALUES (?, ?, ?);");
            for (int i = 0; i < Drinks.size(); i++) {
                pstmt.setInt(1, order.getOrderId());
                pstmt.setInt(2, Drinks.get(i).getDrinkId());
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
                    .prepareStatement("DELETE FROM Order_Drink WHERE orderId = ?;");
            pstmt.setInt(1, order.getOrderId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteALL() {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Order_Drink;");
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public double getDrinksPrice(int orderId) {
        List<Drink> drinkList = findAllDrinks(orderId);
        double price = 0;
        for (int i = 0; i < drinkList.size(); i++) {
            price += drinkList.get(i).getPrice();
        }
        return price;
    }
}
