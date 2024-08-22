import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngridientData {
    Connection conn;
    private static IngridientData data;

    private IngridientData(Connection conn, boolean newTable) {
        this.conn = conn;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Ingridients (ingridientId INT NOT NULL AUTO_INCREMENT,name VARCHAR(64), price Double, vegeterian VARCHAR(30), PRIMARY KEY (ingridientId));");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        data = this;
    }

    public static IngridientData getInstance(Connection conn){
        if(data == null){
            return new IngridientData(conn, true);
        }
        else{
            return data;
        }
    }


    public Ingridient find(int ingridientId) {
        Ingridient newIngridient = null;
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT name,price,vegeterian FROM Ingridients WHERE ingridientId = ?;");
            pstmt.setInt(1, ingridientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                newIngridient = new Ingridient(rs.getString(1), rs.getDouble(2),rs.getString(3));
                newIngridient.setIngridientId(ingridientId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return newIngridient;
    }

    public void update(Ingridient newIngridient) {
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("UPDATE Ingridients SET name = ?, price = ?, vegeterian = ? WHERE ingridientId = ?;");
            pstmt.setString(1, newIngridient.getName());
            pstmt.setDouble(2, newIngridient.getPrice());
            pstmt.setString(3, newIngridient.getVegeterian());
            pstmt.setInt(4, newIngridient.getIngridientId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insert(Ingridient newIngridient) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Ingridients (name, price, vegeterian) VALUES (?, ?, ?);",Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, newIngridient.getName());
            pstmt.setDouble(2, newIngridient.getPrice());
            pstmt.setString(3, newIngridient.getVegeterian());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int key = -1;
            if(rs.next())
                key = rs.getInt(1);
            newIngridient.setIngridientId(key);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Ingridient oldIngridient) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Ingridients WHERE ingridientId = ?;");
            pstmt.setInt(1, oldIngridient.getIngridientId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Ingridient> getAllIngridients() {
        List<Ingridient> ingridients = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Ingridients");
            while (rs.next()) {
                Ingridient ingridient = new Ingridient(rs.getString(2), rs.getDouble(3), rs.getString(4));
                ingridient.setIngridientId(rs.getInt(1));
                ingridients.add(ingridient);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ingridients;
    }
    public void deleteALL() {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Ingridients;");
            pstmt.executeUpdate();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("ALTER TABLE Pizzas AUTO_INCREMENT = 1");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
