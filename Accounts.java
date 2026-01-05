package BankingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Accounts {

    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public long open_account(String email) {

        if (account_exist(email)) {
            throw new RuntimeException("Account already exists");
        }

        String query = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES (?, ?, ?, ?, ?)";

        System.out.print("Enter full name: ");
        scanner.nextLine();
        String full_name = scanner.nextLine();

        System.out.print("Enter initial balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter security pin: ");
        String security_pin = scanner.nextLine();

        try {
            long account_number = generateAccountNumber();

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, account_number);
            ps.setString(2, full_name);
            ps.setString(3, email);
            ps.setDouble(4, balance);
            ps.setString(5, security_pin);

            ps.executeUpdate();
            System.out.println("Account created successfully.");
            return account_number;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Failed to open account");
    }

    // ---------------- CLOSE ACCOUNT ----------------
    public void close_account(String email) {

        if (!account_exist(email)) {
            throw new RuntimeException("Account does not exist");
        }

        String query = "DELETE FROM Accounts WHERE email=?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Account closed successfully.");
            } else {
                throw new RuntimeException("Failed to close account");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- GET ACCOUNT NUMBER ----------------
    public long getAccount_Number(String email) {

        String query = "SELECT account_number FROM Accounts WHERE email=?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("account_number");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Account not found");
    }

    // ---------------- GENERATE ACCOUNT NUMBER ----------------
    private long generateAccountNumber() {

        String query = "SELECT account_number FROM Accounts ORDER BY account_number DESC LIMIT 1";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                return rs.getLong("account_number") + 1;
            } else {
                return 10000100;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 10000100;
    }

    // ---------------- CHECK ACCOUNT EXISTS ----------------
    public boolean account_exist(String email) {

        String query = "SELECT account_number FROM Accounts WHERE email=?";

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
