package bankingManagmentSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner sc;

    public User(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }

    public void register(){
        sc.nextLine();
        System.out.println("Enter Name");
        String name = sc.nextLine();

        System.out.println("Enter E-mail");
        String email =  sc.nextLine();

        System.out.println("Enter New Password");
        String password = sc.nextLine();

        if(user_exist(email)){
            System.out.println("Account already exist with this email ID");
            return;
        }

        String register_query = "insert into user(name, email, password) values (?,?,?)";

      try {
          PreparedStatement preparedStatement = connection.prepareStatement(register_query);
          preparedStatement.setString(1, name);
          preparedStatement.setString(2, email);
          preparedStatement.setString(3, password);

         int rowAffected =  preparedStatement.executeUpdate();

         if (rowAffected>0){
             System.out.println("Account Registered");
         }
         else {
             System.out.println("Account registration failed");
         }
      }
      catch (SQLException e ){
          System.out.println(e.getMessage());
      }
    }


    public String login(){
        sc.nextLine();

        System.out.println("Enter Registered E-mail");
        String email = sc.nextLine();

        System.out.println("Enter your password");
        String password = sc.nextLine();

        String login_query = "select email from user where email = ? and password = ?";
      try {
          PreparedStatement preparedStatement = connection.prepareStatement(login_query);
          preparedStatement.setString(1, email);
          preparedStatement.setString(2,password);

          ResultSet resultSet = preparedStatement.executeQuery();

          if(resultSet.next()){

              return resultSet.getString("email");
          }

      }
      catch (SQLException e){
          System.out.println(e.getMessage()); ;
      }
      return null;
    }

    public boolean user_exist(String email){
        String userExist_query = "select email from user where email = ?";
       try {
           PreparedStatement preparedStatement = connection.prepareStatement(userExist_query);
           preparedStatement.setString(1, email);
          ResultSet resultSet = preparedStatement.executeQuery();

           if (resultSet.next()){
               return true;
           }

       }
       catch (SQLException e){
           System.out.println(e.getMessage());
       }
       return false;
    }
}
