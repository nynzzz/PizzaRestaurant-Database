import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DesertData{
    Connection conn;
    private static DesertData data;
    private DesertData(Connection conn, boolean newTable) {
        this.conn = conn;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Deserts (desertId INT NOT NULL AUTO_INCREMENT,name VARCHAR(64), price Double, PRIMARY KEY (desertId))");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        data = this;
    }

    public static DesertData getInstance(Connection conn){
        if(data == null){
            return new DesertData(conn, true);
        }
        else{
            return data;
        }
    }

    public Desert find(int desertId) {
        Desert newDesert = null;
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT name,price FROM Deserts WHERE desertId = ?;");
            pstmt.setInt(1, desertId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                newDesert = new Desert(rs.getString(1), rs.getDouble(2));
                newDesert.setDesertId(desertId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return newDesert;
    }

    public void update(Desert newDesert) {
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("UPDATE Deserts SET name = ?, price = ? WHERE desertId = ?;");
            pstmt.setString(1, newDesert.getName());
            pstmt.setDouble(2, newDesert.getPrice());
            pstmt.setInt(3, newDesert.getDesertId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insert(Desert newDesert) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Deserts (name, price) VALUES (?, ?);",Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, newDesert.getName());
            pstmt.setDouble(2, newDesert.getPrice());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int key = -1;
            if(rs.next())
                key = rs.getInt(1);
            newDesert.setDesertId(key);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Desert oldDesert) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Deserts WHERE desertId = ?;");
            pstmt.setInt(1, oldDesert.getDesertId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Desert> getAllDeserts() {
        List<Desert> Deserts = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Deserts");
            while (rs.next()) {
                Desert desert = new Desert(rs.getString(2), rs.getDouble(3));
                desert.setDesertId(rs.getInt(1));
                Deserts.add(desert);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Deserts;
    }

    public void deleteALL() {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Deserts;");
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
