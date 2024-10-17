package bankingManagmentSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class accounts_manager {

    private Connection connection;
    private Scanner sc;

    public accounts_manager(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    public void debit_money(long account_no) throws SQLException {
        sc.nextLine();
        System.out.println("Enter Amount");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security Pin");
        String security_pin = sc.nextLine();
        try {
            connection.setAutoCommit(false);
            if (account_no != 0) {

                PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where " +
                        "account_no = ? and security_pin = ?");

                preparedStatement.setLong(1, account_no);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double balance = resultSet.getDouble("balance");
                    if (amount <= balance) {
                        PreparedStatement preparedStatement1 = connection.prepareStatement("update accounts set " +
                                "balance = balance -? where account_no = ?");

                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, account_no);
                        int rowsAffected = preparedStatement1.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("Rs. " + amount + " Debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                        } else {
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Not Enough Balance in your Account");
                    }
                } else {
                    System.out.println("Invalid Pin");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);
    }

    public void credit_money(long account_no) throws SQLException {
        sc.nextLine();
        System.out.println("Enter Amount");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security Pin");
        String security_pin = sc.nextLine();

        try {
            connection.setAutoCommit(false);
            if (account_no != 0) {

                PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where " +
                        "account_no = ? and security_pin = ?");

                preparedStatement.setLong(1, account_no);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String credit_query = "update accounts set balance = balance + ? where account_no = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_no);
                    int rowsAffected = preparedStatement1.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Rs." + amount + " Credited Successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                    } else {
                        System.out.println("Transaction Failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Invalid Security Pin");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);
    }

    public void transferMoney(long sender_accountNo) throws SQLException {

        sc.nextLine();
        System.out.println("Enter Receiver's Account No. ");
        long ReceiverAccountNo = sc.nextLong();
        System.out.println("Enter Amount");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security Pin");
        String security_pin = sc.nextLine();

        try {

            connection.setAutoCommit(false);
            if (sender_accountNo != 0 && ReceiverAccountNo != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where" +
                        " account_no =? and security_pin = ?");
                preparedStatement.setLong(1, sender_accountNo);
                preparedStatement.setString(2, security_pin);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    double balance = resultSet.getDouble("balance");

                    if (balance >= amount) {
                      String debit_query = "update accounts set balance = balance - ? where account_no = ?";
                      String credit_query = "update accounts set balance = balance + ? where account_no = ?";

                      PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                      PreparedStatement preparedStatement2 = connection.prepareStatement(credit_query);

                      preparedStatement1.setDouble(1, amount);
                      preparedStatement1.setLong(2, sender_accountNo);

                      preparedStatement2.setDouble(1, amount);
                      preparedStatement2.setLong(2, ReceiverAccountNo);

                      int rowsAffected1 = preparedStatement1.executeUpdate();
                      int rowsAffected2 = preparedStatement2.executeUpdate();

                      if (rowsAffected1>0 && rowsAffected2 >0){
                          System.out.println("Transaction Successful");
                           connection.commit();
                           connection.setAutoCommit(true);
                      }
                      else {
                          System.out.println("Transaction failed");
                          connection.rollback();
                          connection.setAutoCommit(true);
                      }

                    }
                    else {
                        System.out.println("Not enough Balance in your Account");
                    }

                }
                else {
                    System.out.println("Invalid Security Pin");
                }
            }
            else {
                System.out.println("Invalid Account numbers");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getBalance(long account_no){
        sc.nextLine();
        System.out.println("Enter Security Pin");
        String security_pin = sc.nextLine();

      try {
          String getBalanceQuery = "select balance from accounts where account_no = ? and security_pin = ?";

          PreparedStatement preparedStatement = connection.prepareStatement(getBalanceQuery);
          preparedStatement.setLong(1, account_no);
          preparedStatement.setString(2, security_pin);

         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()){
             double balance = resultSet.getDouble("balance");
             System.out.println("Balance: " + balance);
         }
         else {
             System.out.println("Invalid Pin");
         }
      }
      catch (SQLException e){
          System.out.println(e.getMessage());
      }
    }


}
