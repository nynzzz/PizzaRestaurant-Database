import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.sql.*;

public class ConnectionToDatabase {

    public static Connection start(boolean update) throws SQLException {
        final String URL = "jdbc:mysql://localhost";
        final String USER = "root";
        final String PASSWORD = "gabrijeliscool";
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.createStatement();
            stmt.execute("CREATE DATABASE IF NOT EXISTS Menu");
            stmt.execute("USE Menu");
            if (update)
                stmt.execute("DROP TABLES IF EXISTS deserts,drinks,pizza_ingridient,pizzas,ingridients,customers,deliverers,order_desert,order_pizza,order_drink,orders,Customer_Discount");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (update) {
            createDeserts(conn);
            createDrinks(conn);
            createIngridients(conn);
            createPizzas(conn);
            createDeliverers(conn);
        }
        return conn;

    }

    public static void createDeserts(Connection conn) {
        Desert desert1 = new Desert("tiramisu", 4.75);
        Desert desert2 = new Desert("panna cotta", 3.75);
        DesertData desertData = DesertData.getInstance(conn);
        desertData.insert(desert1);
        desertData.insert(desert2);
    }

    public static void createDrinks(Connection conn) {
        Drink CocaCola = new Drink("CocaCola", 2.80);
        Drink Fanta = new Drink("Fanta", 2.90);
        Drink juice = new Drink("juice", 2.70);
        Drink vodka = new Drink("vodka", 3.60);
        DrinkData drinkData = DrinkData.getInstance(conn);
        drinkData.insert(CocaCola);
        drinkData.insert(Fanta);
        drinkData.insert(juice);
        drinkData.insert(vodka);
    }

    public static void createIngridients(Connection conn) {
        Ingridient ingridient1 = new Ingridient("olives", 0.30, "true");
        Ingridient ingridient2 = new Ingridient("cheese", 1.50, "true");
        Ingridient ingridient3 = new Ingridient("tomato sauce", 0.50, "true");
        Ingridient ingridient4 = new Ingridient("origano", 0.10, "true");
        Ingridient ingridient5 = new Ingridient("salami", 0.70, "false");
        Ingridient ingridient6 = new Ingridient("tuna", 1.50, "false");
        Ingridient ingridient8 = new Ingridient("cream", 0.50, "true");
        Ingridient ingridient7 = new Ingridient("ham", 1.10, "false");
        Ingridient ingridient9 = new Ingridient("ketchup", 0.50, "true");
        Ingridient ingridient10 = new Ingridient("onion", 0.30, "true");

        IngridientData ingridientData = IngridientData.getInstance(conn);
        ingridientData.insert(ingridient1);
        ingridientData.insert(ingridient2);
        ingridientData.insert(ingridient3);
        ingridientData.insert(ingridient4);
        ingridientData.insert(ingridient5);
        ingridientData.insert(ingridient6);
        ingridientData.insert(ingridient7);
        ingridientData.insert(ingridient8);
        ingridientData.insert(ingridient9);
        ingridientData.insert(ingridient10);
    }

    public static void createPizzas(Connection conn) throws SQLException {
        Pizza pizza1 = new Pizza("margarita");
        Pizza pizza2 = new Pizza("vesuvio");
        Pizza pizza3 = new Pizza("marinara");
        Pizza pizza4 = new Pizza("red pizza");
        Pizza pizza5 = new Pizza("tuna pizza");
        Pizza pizza6 = new Pizza("onion pizza");
        Pizza pizza7 = new Pizza("white pizza");
        Pizza pizza8 = new Pizza("salami, tuna, ham pizza");
        Pizza pizza9 = new Pizza("all in one pizza");
        Pizza pizza10 = new Pizza("margarita with olives");

        PizzaData pizzaData = PizzaData.getInstance(conn);
        pizzaData.insert(pizza1);
        pizzaData.insert(pizza2);
        pizzaData.insert(pizza3);
        pizzaData.insert(pizza4);
        pizzaData.insert(pizza5);
        pizzaData.insert(pizza6);
        pizzaData.insert(pizza7);
        pizzaData.insert(pizza8);
        pizzaData.insert(pizza9);
        pizzaData.insert(pizza10);
        Pizza_IngridientData pizza_ingridientData = Pizza_IngridientData.getInstance(conn);
        IngridientData ingridientData = IngridientData.getInstance(conn);
        List<Ingridient> ingridientsPizza1 = ingridientData.getAllIngridients().subList(1, 4);
        List<Ingridient> ingridientsPizza2 = ingridientData.getAllIngridients().subList(1, 5);
        List<Ingridient> ingridientsPizza3 = ingridientData.getAllIngridients().subList(6, 9);
        List<Ingridient> ingridientsPizza4 = ingridientData.getAllIngridients().subList(2, 5);
        List<Ingridient> ingridientsPizza5 = ingridientData.getAllIngridients().subList(1, 6);
        List<Ingridient> ingridientsPizza6 = ingridientData.getAllIngridients().subList(7, 10);
        List<Ingridient> ingridientsPizza7 = ingridientData.getAllIngridients().subList(4, 7);
        List<Ingridient> ingridientsPizza8 = ingridientData.getAllIngridients().subList(3, 8);
        List<Ingridient> ingridientsPizza9 = ingridientData.getAllIngridients().subList(0, 6);
        List<Ingridient> ingridientsPizza10 = ingridientData.getAllIngridients().subList(0, 5);
        pizza_ingridientData.insert(pizza1, ingridientsPizza1);
        pizza_ingridientData.insert(pizza2, ingridientsPizza2);
        pizza_ingridientData.insert(pizza3, ingridientsPizza3);
        pizza_ingridientData.insert(pizza4, ingridientsPizza4);
        pizza_ingridientData.insert(pizza5, ingridientsPizza5);
        pizza_ingridientData.insert(pizza6, ingridientsPizza6);
        pizza_ingridientData.insert(pizza7, ingridientsPizza7);
        pizza_ingridientData.insert(pizza8, ingridientsPizza8);
        pizza_ingridientData.insert(pizza9, ingridientsPizza9);
        pizza_ingridientData.insert(pizza10, ingridientsPizza10);
    }
    public static void createDeliverers(Connection conn){
        Deliverer deliverer1 = new Deliverer("0976828124", "Gabrijel", "6224", true);
        Deliverer deliverer2 = new Deliverer("274983749027", "Eduard", "6214", true);
        Deliverer deliverer3 = new Deliverer("9824790280894", "Niko", "6111", true);
        Deliverer deliverer4 = new Deliverer("3873297929", "Ivan", "3929", true);
        Deliverer deliverer5 = new Deliverer("907984237894", "Egor", "4222", true);
        Deliverer deliverer6 = new Deliverer("84793983476", "Marko", "1311", true);
        Deliverer deliverer7 = new Deliverer("1292789112", "Alex", "2000", true);
        Deliverer deliverer8 = new Deliverer("12798213498749", "Gijs", "2002", true);
        Deliverer deliverer9 = new Deliverer("00387483783478", "Gibi", "6224", true);
        Deliverer deliverer10 = new Deliverer("8479787439879", "Kralj", "6224", true);
        Deliverer deliverer11 = new Deliverer("973479847", "Car", "6214", true);
        Deliverer deliverer12 = new Deliverer("3485786728378", "Luka", "6227", true);
        Deliverer deliverer13 = new Deliverer("7437568736823", "Josip", "6229", true);
        Deliverer deliverer14 = new Deliverer("87465786834", "Gabrijel", "6229", true);
        Deliverer deliverer15 = new Deliverer("2454243643637", "Tome", "2000", true);
        Deliverer deliverer16 = new Deliverer("45378864327453", "Lovre", "6111", true);
        Deliverer deliverer17 = new Deliverer("6378465443775", "Karlo", "3111", true);
        Deliverer deliverer18 = new Deliverer("2749898479438", "Viktorija", "2311", true);

        DelivererData data = DelivererData.getInstance(conn);
        data.insert(deliverer1);
        data.insert(deliverer2);
        data.insert(deliverer3);
        data.insert(deliverer4);
        data.insert(deliverer5);
        data.insert(deliverer6);
        data.insert(deliverer7);
        data.insert(deliverer8);
        data.insert(deliverer9);
        data.insert(deliverer10);
        data.insert(deliverer11);
        data.insert(deliverer12);
        data.insert(deliverer13);
        data.insert(deliverer14);
        data.insert(deliverer15);
        data.insert(deliverer16);
        data.insert(deliverer17);
        data.insert(deliverer18);


    }

}
