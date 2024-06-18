package spesePersonali;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseSetup {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "spese_personali";
    private static String user;
    private static String password;

    public static void main(String[] args) {
        user = args[0];
        password = args[1];
        createDatabase();
        createTables();
    }
    private static void createDatabase() {
        String createDbQuery = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
        try (Connection connection = DriverManager.getConnection(URL, user, password);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createDbQuery);
            System.out.println("Database created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void createTables() {
        String createSpeseTable = "CREATE TABLE IF NOT EXISTS spese (" +
                "id_spese INT AUTO_INCREMENT PRIMARY KEY, "
                +"nome_spesa VARCHAR(100) NOT NULL, " +
                "importo DECIMAL(10,2) NOT NULL, " +
                "data DATE NOT NULL, "
                + "id_categoria INT"
                + ")";
        
        String createCategorieTable = "CREATE TABLE IF NOT EXISTS categorie (" +
                "id_categoria INT AUTO_INCREMENT PRIMARY KEY, " +
                "nome_categoria VARCHAR(100) NOT NULL, " +
                "descrizione TEXT"
                + ")";        
        try (Connection connection = DriverManager.getConnection(URL + DB_NAME, user, password);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createSpeseTable);
            stmt.executeUpdate(createCategorieTable);
            System.out.println("Tables created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
