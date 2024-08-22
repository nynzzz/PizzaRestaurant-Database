import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DelivererData {
    Connection conn;
    private static DelivererData data;

    private DelivererData(Connection conn, boolean newTable) {
        this.conn = conn;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Deliverers (phoneNumber VARCHAR(30) NOT NULL, name VARCHAR(30), postalCode VARCHAR(4),available VARCHAR(10),PRIMARY KEY (phoneNumber))");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        data = this;
    }

    public static DelivererData getInstance(Connection conn) {
        if (data == null) {
            return new DelivererData(conn, true);
        } else {
            return data;
        }
    }

    public Deliverer find(String phoneNumber) {
        Deliverer newDeliverer = null;
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement("SELECT name,postalCode,available FROM Deliverers WHERE phoneNumber = ?;");
            pstmt.setString(1, phoneNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                boolean available = false;
                if(rs.getString(3).equals("true")){
                    available = true;
                }
                newDeliverer = new Deliverer(phoneNumber, rs.getString(1), rs.getString(2),available);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return newDeliverer;
    }

    public void update(Deliverer newDeliverer) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE Deliverers SET name = ?, postalCode = ?, available = ? WHERE phoneNumber = ?;");
            pstmt.setString(1, newDeliverer.getName());
            pstmt.setString(2, newDeliverer.getPostalCode());
            pstmt.setString(3, ""+ newDeliverer.getAvailable());
            pstmt.setString(4, newDeliverer.getPhoneNumber());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insert(Deliverer Deliverer) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO Deliverers (phoneNumber, name, postalCode, available) VALUES (?, ?, ?, ?);");
            pstmt.setString(1, Deliverer.getPhoneNumber());
            pstmt.setString(2, Deliverer.getName());
            pstmt.setString(3, Deliverer.getPostalCode());
            pstmt.setString(4, Deliverer.getAvailable()+"");
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Deliverer> getAllDeliverers() {
        List<Deliverer> Deliverers = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Deliverers");
            while (rs.next()) {
                boolean available = false;
                if(rs.getString(3).equals("true")){
                    available = true;
                }
                Deliverer Deliverer = new Deliverer(rs.getString(1), rs.getString(2), rs.getString(3),available);
                Deliverers.add(Deliverer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Deliverers;
    }
    public Deliverer getAvailableAndPostalCodeDeliverer(String postalCode) throws SQLException {
        Deliverer Deliverer = null;
        for (int i = 0; i < getAllDeliverers().size(); i++) {
            Order order = OrderData.getInstance(conn).find(getAllDeliverers().get(i).getPhoneNumber());
            if(!getAllDeliverers().get(i).getAvailable()&&order!=null&&OrderData.getInstance(conn).differenceInTime(order)>=30){
                order.setStatus("delivered");
                getAllDeliverers().get(i).setAvailable(true);
                update(getAllDeliverers().get(i));
            }
        }
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Deliverers WHERE available = 'true' and postalCode = ? LIMIT 1");
            stmt.setString(1,postalCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Deliverer = new Deliverer(rs.getString(1), rs.getString(2), rs.getString(3),true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Deliverer;
    }
}
