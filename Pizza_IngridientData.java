import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Pizza_IngridientData {
    Connection conn;
    private static Pizza_IngridientData data;

    private Pizza_IngridientData(Connection conn, boolean newTable) {
        this.conn = conn;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Pizza_Ingridient (pizzaId INT, ingridientId INT, PRIMARY KEY (pizzaId,ingridientId), FOREIGN KEY (pizzaId) REFERENCES Pizzas(pizzaId) ON DELETE CASCADE, FOREIGN KEY (ingridientId) REFERENCES Ingridients(ingridientId) ON DELETE CASCADE)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        data = this;
    }

    public static Pizza_IngridientData getInstance(Connection conn) {
        if (data == null) {
            return new Pizza_IngridientData(conn, true);
        } else {
            return data;
        }
    }

    public List<Ingridient> findAllIngridients(int pizzaId) {
        List<Ingridient> ingridients = new ArrayList<Ingridient>();
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("SELECT ingridientId FROM Pizza_Ingridient WHERE pizzaId = ?;");
            pstmt.setInt(1, pizzaId);
            IngridientData data = IngridientData.getInstance(conn);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Integer ingridientId = rs.getInt(1);
                ingridients.add(data.find(ingridientId));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ingridients;
    }

    public List<Pizza> findAllPizzas(int ingridientId) {
        List<Pizza> pizzas = new ArrayList<Pizza>();
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("SELECT pizzaId FROM Pizza_Ingridient WHERE ingridientId = ?;");
            pstmt.setInt(1, ingridientId);
            ResultSet rs = pstmt.executeQuery();
            PizzaData data = PizzaData.getInstance(conn);
            if (rs.next()) {
                Integer pizzaId = rs.getInt(1);
                pizzas.add(data.find(pizzaId));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pizzas;
    }

    public void insert(Pizza pizza, List<Ingridient> ingridients) throws SQLException {
        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn
                    .prepareStatement("INSERT INTO Pizza_Ingridient (pizzaId, ingridientId) VALUES (?, ?);");
            for (int i = 0; i < ingridients.size(); i++) {
                pstmt.setInt(1, pizza.getPizzaId());
                pstmt.setInt(2, ingridients.get(i).getIngridientId());
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

    public void delete(Pizza pizza, Ingridient ingridient) {
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("DELETE FROM Pizza_Ingridient WHERE pizzaId = ? and ingridientId = ?;");
            pstmt.setInt(1, pizza.getPizzaId());
            pstmt.setInt(2, ingridient.getIngridientId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteALL() {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Pizza_Ingridient;");
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public double getPizzaPrice(int pizzaId) {
        List<Ingridient> ingridientIdList = findAllIngridients(pizzaId);
        double price = 0;
        for (int i = 0; i < ingridientIdList.size(); i++) {
            price += ingridientIdList.get(i).getPrice();
        }
        return price * 1.4;
    }

    public boolean isVegeterian(int pizzaId) {
        List<Ingridient> ingridientIdList = findAllIngridients(pizzaId);
        for (int i = 0; i < ingridientIdList.size(); i++) {
            if(ingridientIdList.get(i).getVegeterian().equals("false")){
                return false;
            }
        }
        return true;
    }
}
