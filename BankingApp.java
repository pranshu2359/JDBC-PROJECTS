package BankingSystem;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "235900";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try{
            Connection conn = DriverManager.getConnection(url, username, password);
            Scanner sc=new Scanner(System.in);
            User user=new User(conn,sc);
            Accounts accounts=new Accounts(conn,sc);
            AccountsManager accountManager=new AccountsManager(conn,sc);

            String email;
            long account_number;

            while(true){
                System.out.println("****Welcome to Banking System ****");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice=sc.nextInt();
                switch (choice) {
                    case 1:
                        user.register();
                        System.out.print("\033[H\033[2J");
                        System.out.flush();

                        System.out.flush();
                        break;
                    case 2:
                        email=user.login();
                        if(email!=null){
                            System.out.println();
                            System.out.println("user logged in");
                            if(!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1.open a new bank Account :");
                                System.out.println("2.Exit");
                                if(sc.nextInt()==1){
                                    account_number=accounts.open_account(email);
                                    System.out.println("Account Created Succesfully");
                                    System.out.println("your account number is : "+account_number);

                                }else{
                                    break;
                                }
                            }
                            account_number=accounts.getAccount_Number(email);
                            int choice2=0;
                            while(choice2!=5){
                                System.out.println();
                                System.out.println("1.Debit Money :");
                                System.out.println("2.Credit Money :");
                                System.out.println("3.Transfer Money :");
                                System.out.println("4.Check Balance :");
                                System.out.println("5.Log out :");
                                System.out.println("Enter your choice");
                                choice2=sc.nextInt();
                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Invalid choice");
                                        break;
                                }
                            }
                        }
                        else{
                            System.out.println("incorrect email and password :");
                        }
                    case 3:
                        System.out.println("----THANK YOU FOR USING BANKING SYSTEM ----" );
                        System.out.println("Existing System");
                        return;
                    default:
                        System.out.println("enter valid choice");
                        break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

