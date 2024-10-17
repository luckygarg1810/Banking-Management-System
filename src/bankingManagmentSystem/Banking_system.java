package bankingManagmentSystem;

import java.sql.*;
import java.util.Scanner;

public class Banking_system {

    private static String url = "jdbc:mysql://localhost:3306/banking_system";
    private static String username = "root";
    private static String password = "garg.lucky";

    public static void main(String[] args) throws  ClassNotFoundException, SQLException{

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        }
        catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

      try {
          Connection connection = DriverManager.getConnection(url, username, password);
          Scanner sc = new Scanner(System.in);

          User user = new User(connection, sc);
          accounts_manager ac_manager = new accounts_manager(connection, sc);
          Accounts accounts = new Accounts(connection, sc);

          String email;
          long account_no;

          while (true) {
              System.out.println("*** Welcome to Banking System ***");
              System.out.println("1. Register");
              System.out.println("2. Login");
              System.out.println("3. Exit");
              System.out.println();
              System.out.println("Enter your choice");
              int choice = sc.nextInt();

              switch (choice) {
                  case 1:
                      user.register();
                      break;
                  case 2:
                    email =  user.login();
                    if (email!= null){
                        System.out.println("Login Successfully");
                        if (!accounts.account_exist(email)){
                            System.out.println("1. Open Bank Account");
                            System.out.println("2. Exit");
                            int select = sc.nextInt();
                            if (select == 1){
                               account_no = accounts.openAccount(email);
                                System.out.println("Account created successfully");
                                System.out.println("Your Account no. is " + account_no);
                            }
                            else {
                                System.out.println("Exiting....");
                                break;
                            }
                        }
                        else {
                         account_no = accounts.getAccountNo(email);
                         int choice2 = 0;
                         while (choice2!=5){
                             System.out.println("1. Debit Money");
                             System.out.println("2. Credit Money");
                             System.out.println("3. Transfer Money");
                             System.out.println("4. check balance");
                             System.out.println("5. Logout");
                             System.out.println();
                             System.out.println("Enter your choice");
                              choice2 = sc.nextInt();
                              switch (choice2) {
                                  case 1:
                                      ac_manager.debit_money(account_no);
                                      break;
                                  case 2:
                                      ac_manager.credit_money(account_no);
                                      break;
                                  case 3:
                                      ac_manager.transferMoney(account_no);
                                      break;
                                  case 4:
                                      ac_manager.getBalance(account_no);
                                      break;
                                  case 5:
                                      break;
                                  default:
                                      System.out.println("Invalid choice");
                                      break;
                              }
                         }
                        }
                    }
                    else {
                        System.out.println("Wrong Email or password");
                    }
                  case 3:
                      System.out.println("Thanks for using Banking Management System");
                      System.out.println("Exiting...");
                      return;
                  default:
                      System.out.println("Please enter valid choice");
                      break;
              }
          }
      }
      catch (SQLException e){
          System.out.println(e.getMessage());
      }

    }
}