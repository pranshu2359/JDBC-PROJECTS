package BankingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountsManager {

    private Connection connection;
    private Scanner scanner;

    public AccountsManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void credit_money(long account_number) {

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter security pin: ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);

            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM Accounts WHERE account_number=? AND security_pin=?");
            ps.setLong(1, account_number);
            ps.setString(2, security_pin);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PreparedStatement ps2 = connection.prepareStatement(
                        "UPDATE Accounts SET balance = balance + ? WHERE account_number=?");
                ps2.setDouble(1, amount);
                ps2.setLong(2, account_number);

                int rows = ps2.executeUpdate();
                if (rows > 0) {
                    connection.commit();
                    System.out.println("Rs. " + amount + " credited successfully.");
                } else {
                    connection.rollback();
                    System.out.println("Transaction failed.");
                }
            } else {
                System.out.println("Invalid security pin.");
            }

        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) {}
        }
    }

    // ---------------- DEBIT MONEY ----------------
    public void debit_money(long account_number) {

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter security pin: ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);

            PreparedStatement ps = connection.prepareStatement(
                    "SELECT balance FROM Accounts WHERE account_number=? AND security_pin=?");
            ps.setLong(1, account_number);
            ps.setString(2, security_pin);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");

                if (amount <= balance) {
                    PreparedStatement ps2 = connection.prepareStatement(
                            "UPDATE Accounts SET balance = balance - ? WHERE account_number=?");
                    ps2.setDouble(1, amount);
                    ps2.setLong(2, account_number);

                    ps2.executeUpdate();
                    connection.commit();
                    System.out.println("Rs. " + amount + " debited successfully.");
                } else {
                    System.out.println("Insufficient balance.");
                }
            } else {
                System.out.println("Invalid security pin.");
            }

        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) {}
        }
    }

    // ---------------- TRANSFER MONEY ----------------
    public void transfer_money(long sender_account_number) {

        System.out.print("Enter receiver account number: ");
        long receiver_account_number = scanner.nextLong();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter security pin: ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);

            PreparedStatement senderPs = connection.prepareStatement(
                    "SELECT balance FROM Accounts WHERE account_number=? AND security_pin=?");
            senderPs.setLong(1, sender_account_number);
            senderPs.setString(2, security_pin);

            ResultSet rs = senderPs.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");

                if (amount <= balance) {
                    PreparedStatement debitPs = connection.prepareStatement(
                            "UPDATE Accounts SET balance = balance - ? WHERE account_number=?");
                    debitPs.setDouble(1, amount);
                    debitPs.setLong(2, sender_account_number);

                    PreparedStatement creditPs = connection.prepareStatement(
                            "UPDATE Accounts SET balance = balance + ? WHERE account_number=?");
                    creditPs.setDouble(1, amount);
                    creditPs.setLong(2, receiver_account_number);

                    int d = debitPs.executeUpdate();
                    int c = creditPs.executeUpdate();

                    if (d > 0 && c > 0) {
                        connection.commit();
                        System.out.println("Rs. " + amount + " transferred successfully.");
                    } else {
                        connection.rollback();
                        System.out.println("Transfer failed.");
                    }
                } else {
                    System.out.println("Insufficient balance.");
                }
            } else {
                System.out.println("Invalid security pin.");
            }

        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) {}
        }
    }

    // ---------------- GET BALANCE ----------------
    public void getBalance(long account_number) {

        System.out.print("Enter security pin: ");
        String security_pin = scanner.nextLine();

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT balance FROM Accounts WHERE account_number=? AND security_pin=?");
            ps.setLong(1, account_number);
            ps.setString(2, security_pin);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Balance: Rs. " + rs.getDouble("balance"));
            } else {
                System.out.println("Invalid security pin.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
