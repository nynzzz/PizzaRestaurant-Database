import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PizzaData {
    Connection conn;
    private static PizzaData data;

    private PizzaData(Connection conn, boolean newTable) {
        this.conn = conn;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Pizzas (pizzaId INT NOT NULL AUTO_INCREMENT,name VARCHAR(64),PRIMARY KEY (pizzaId))");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        data = this;
    }
    public static PizzaData getInstance(Connection conn){
        if(data == null){
            return new PizzaData(conn, true);
        }
        else{
            return data;
        }
    }

    public Pizza find(int pizzaId) {
        Pizza newPizza = null;
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT name FROM Pizzas WHERE pizzaId = ?;");
            pstmt.setInt(1, pizzaId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                newPizza = new Pizza(rs.getString(1));
                newPizza.setPizzaId(pizzaId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return newPizza;
    }

    public void update(Pizza newPizza) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("UPDATE Pizzas SET name = ? WHERE pizzaId = ?;");
            pstmt.setString(1, newPizza.getName());
            pstmt.setInt(2, newPizza.getPizzaId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insert(Pizza newPizza) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Pizzas (name) VALUES (?);",Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, newPizza.getName());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int key = -1;
            if(rs.next())
                key = rs.getInt(1);
            newPizza.setPizzaId(key);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Pizza oldPizza) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Pizzas WHERE pizzaId = ?;");
            pstmt.setInt(1, oldPizza.getPizzaId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void deleteALL() {
        try {
            Statement stmt = conn.createStatement();
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Pizzas;");
            stmt.executeUpdate("ALTER TABLE Pizzas AUTO_INCREMENT = 1");
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Pizza> getAllPizzas() {
        List<Pizza> Pizzas = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Pizzas");
            while (rs.next()) {
                Pizza pizza = new Pizza(rs.getString(2));
                pizza.setPizzaId(rs.getInt(1));
                Pizzas.add(pizza);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Pizzas;
    }

}
