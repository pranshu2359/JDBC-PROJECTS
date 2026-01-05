package BankingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    private Connection connection;
    private Scanner scanner;

    public User(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register() {

        System.out.print("Enter full name: ");
        scanner.nextLine();
        String full_name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (user_exist(email)) {
            System.out.println("Email already exists!");
            return;
        }

        String query = "INSERT INTO User (full_name, email, password) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, full_name);
            ps.setString(2, email);
            ps.setString(3, password);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("User registered successfully!");
            } else {
                System.out.println("Registration failed!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String login() {

        System.out.print("Enter email: ");
        scanner.nextLine();
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String query = "SELECT * FROM User WHERE email=? AND password=?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Login successful!");
                return email;
            } else {
                System.out.println("Invalid email or password.");
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean user_exist(String email) {

        String query = "SELECT email FROM User WHERE email=?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
