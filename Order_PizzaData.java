import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Order_PizzaData {
    Connection conn;
    private static Order_PizzaData data;

    private Order_PizzaData(Connection conn, boolean newTable) {
        this.conn = conn;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Order_Pizza(orderId INT, PizzaId INT,quantity INT, PRIMARY KEY (orderId,PizzaId), FOREIGN KEY (orderId) REFERENCES Orders(orderId) ON DELETE CASCADE, FOREIGN KEY (PizzaId) REFERENCES Pizzas(PizzaId) ON DELETE CASCADE)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        data = this;
    }

    public static Order_PizzaData getInstance(Connection conn) {
        if (data == null) {
            return new Order_PizzaData(conn, true);
        } else {
            return data;
        }
    }

    public List<Pizza> findAllPizzas(int orderId) {
        List<Pizza> Pizzas = new ArrayList<Pizza>();
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("SELECT PizzaId FROM Order_Pizza WHERE orderId = ?;");
            pstmt.setInt(1, orderId);
            PizzaData data = PizzaData.getInstance(conn);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Integer PizzaId = rs.getInt(1);
                Pizzas.add(data.find(PizzaId));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Pizzas;
    }

    public void insert(Order order, List<Pizza> Pizzas,List<Integer> quantities) throws SQLException {
        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn
                    .prepareStatement("INSERT INTO Order_Pizza(orderId, PizzaId, quantity) VALUES (?, ?, ?);");
            for (int i = 0; i < Pizzas.size(); i++) {
                pstmt.setInt(1, order.getOrderId());
                pstmt.setInt(2, Pizzas.get(i).getPizzaId());
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
                    .prepareStatement("DELETE FROM Order_Pizza WHERE orderId = ?;");
            pstmt.setInt(1, order.getOrderId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteALL() {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Order_Pizza;");
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public double getPizzasPrice(int orderId) {
        List<Pizza> pizzaList = findAllPizzas(orderId);
        double price = 0;
        for (int i = 0; i < pizzaList.size(); i++) {
            price += Pizza_IngridientData.getInstance(conn).getPizzaPrice(pizzaList.get(i).getPizzaId());
        }
        return price;
    }
}
