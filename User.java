import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner scanner;
    AccManager acm = new AccManager(connection,scanner);

    public User(Connection connection, Scanner scanner)
    {
        this.connection=connection;
        this.scanner= scanner;
    }
    

    public boolean userExist(String email)
    {
        String query = "select * from user where email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;

    }

    public void Reg(){

        scanner.nextLine();
        System.out.print("Enter Full Name: ");
        String FullName = scanner.nextLine();


        System.out.print("Enter Email");
        String Email = scanner.nextLine();

        System.out.print("Enter Password: ");
        String Password=scanner.nextLine();

        if(userExist(Email))
        {
            System.out.println("User Already Exists.");
        }

        String RegQuery = "insert into user(FullName, email, password) values(?,?,?)";
        try{
        PreparedStatement preparedStatement= connection.prepareStatement(RegQuery);
        preparedStatement.setString(1,FullName);
        preparedStatement.setString(2,Email);
        preparedStatement.setString(3,acm.dohashing(Password));
//        ResultSet resultSet = preparedStatement.executeQuery(RegQuery);
        int affectedrows = preparedStatement.executeUpdate();
        if(affectedrows>0){
            System.out.println("registration successful!");
        }
        else {
            System.out.println("Registration Unsuccessful");
        }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public String login() throws SQLException{
        scanner.nextLine();
        System.out.println("enter Email: ");
        String Email= scanner.nextLine();
        System.out.println("Enter Password: ");
        String Password = scanner.nextLine();
        String loginQuery = "select * from user where email = ? and password = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(loginQuery);
            preparedStatement.setString(1,Email);
            preparedStatement.setString(2,acm.dohashing(Password));
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return Email;
            }
            else return null;
        }
        catch (SQLException  e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
