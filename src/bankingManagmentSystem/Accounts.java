package bankingManagmentSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Accounts {

    private Connection connection;
    private Scanner sc;

    public Accounts(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }

    public long openAccount(String email){

        if (!account_exist(email)){

            sc.nextLine();
            System.out.println("Enter Full Name");
            String name = sc.nextLine();

            System.out.println("Enter Initial Amount");
            double balance = sc.nextDouble();

            sc.nextLine();
            System.out.println("Enter Security Pin");
            String security_pin = sc.nextLine();

            String query = "insert into accounts(account_no, name, email, balance, security_pin) values (?,?,?,?,?)";
          try {
              long account_no = accountNo_generator();


              PreparedStatement preparedStatement = connection.prepareStatement(query);
              preparedStatement.setLong(1, account_no);
              preparedStatement.setString(2, name);
              preparedStatement.setString(3, email);
              preparedStatement.setDouble(4, balance);
              preparedStatement.setString(5, security_pin);
               int rowsAffected = preparedStatement.executeUpdate();

               if (rowsAffected>0){
                  return account_no;
               }
               else {
                   throw new RuntimeException("Account Creation Failed");
               }

          }
          catch (SQLException e){
              System.out.println(e.getMessage());
          }
        }
        throw new RuntimeException("Account Already Exist");
    }

    public long getAccountNo(String email){
        String query = "select * from accounts where email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet=  preparedStatement.executeQuery();

            if (resultSet.next()){
                return resultSet.getLong("account_no");

            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account does not exist");
    }

    public long accountNo_generator(){
        String query = "select account_no from accounts order by account_no desc limit 1";

      try {
          PreparedStatement preparedStatement = connection.prepareStatement(query);
          ResultSet resultSet = preparedStatement.executeQuery();

          if (resultSet.next()){
              long lastAccountNo = resultSet.getLong("account_no");
              return lastAccountNo+1;
          }
          else {
              return 10000100;
          }
      }
      catch (SQLException e){
          System.out.println(e.getMessage());
      }
      return 10000100;
    }

    public boolean account_exist(String email){
        String query = "select * from accounts where email = ?";

       try {
           PreparedStatement preparedStatement = connection.prepareStatement(query);
           preparedStatement.setString(1, email);
           ResultSet resultSet = preparedStatement.executeQuery();

           if (resultSet.next()){
               return true;
           }
           else {
               return false;
           }
       }
       catch (SQLException e){
           System.out.println(e.getMessage());
       }
        return false;
    }
}
