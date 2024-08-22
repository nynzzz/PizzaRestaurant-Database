import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrinkData {
    private Connection conn;
    private static DrinkData data;
    private DrinkData(Connection conn, boolean newTable) {
        this.conn = conn;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Drinks (drinkId INT NOT NULL AUTO_INCREMENT,name VARCHAR(64), price Double, PRIMARY KEY (drinkId))");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        data = this;
    }
    public static DrinkData getInstance(Connection conn){
        if(data == null){
            return new DrinkData(conn, true);
        }
        else{
            return data;
        }
    }

    public Drink find(int drinkId) {
        Drink newDrink = null;
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT name,price FROM Drinks WHERE drinkId = ?;");
            pstmt.setInt(1, drinkId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                newDrink = new Drink(rs.getString(1), rs.getDouble(2));
                newDrink.setDrinkId(drinkId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return newDrink;
    }

    public void update(Drink newDrink) {
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("UPDATE Drinks SET name = ?, price = ? WHERE drinkId = ?;");
            pstmt.setString(1, newDrink.getName());
            pstmt.setDouble(2, newDrink.getPrice());
            pstmt.setInt(3, newDrink.getDrinkId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insert(Drink newDrink) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Drinks (name, price) VALUES (?, ?);",Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, newDrink.getName());
            pstmt.setDouble(2, newDrink.getPrice());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int key = -1;
            if(rs.next())
                key = rs.getInt(1);
            newDrink.setDrinkId(key);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Drink oldDrink) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Drinks WHERE drinkId = ?;");
            pstmt.setInt(1, oldDrink.getDrinkId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Drink> getAllDrinks() {
        List<Drink> drinks = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Drinks");
            while (rs.next()) {
                Drink drink = new Drink(rs.getString(2), rs.getDouble(3));
                drink.setDrinkId(rs.getInt(1));
                drinks.add(drink);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return drinks;
    }

    public void deleteALL() {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Drinks;");
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
