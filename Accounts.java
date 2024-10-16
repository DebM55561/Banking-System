import java.sql.*;
import java.util.Scanner;

class Accounts {


    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    public boolean CheckAcc(String email) throws SQLException {
        String existQuery = "select * from accounts where email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(existQuery);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return  true;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;

    }

    public long OpenAcc(String email) throws SQLException {
        if(!CheckAcc(email))
        {
            String openaccQuery = "insert into accounts (FullName, email, balance, securitypin, AccNo) values(?,?,?,?,?)";
            System.out.print("Enter FullName: ");
            String fullName = scanner.nextLine();
            scanner.nextLine();
            System.out.print("Enter initial amounts: ");
            int balance = scanner.nextInt();
//            scanner.nextLine();
//            System.out.println("Enter AccNo: ");
//            int AccNo = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Security Pin: ");
            String secpin = scanner.nextLine();
            try{
                PreparedStatement preparedStatement = connection.prepareStatement(openaccQuery);
                preparedStatement.setString(1,fullName);
                preparedStatement.setString(2,email);
                preparedStatement.setInt(3,balance);
                preparedStatement.setString(4,secpin);
                preparedStatement.setLong(5,GenAccNO());
                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected>0)
                {
                    System.out.println("Account Created.");
                    return GetAccNo(email);
                }
                else {
                    System.out.println("Unable to create account.");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Account Already Exists.");


    }

    public long GetAccNo(String email) {
        String Query= "select AccNo from accounts where email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(Query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return resultSet.getLong("AccNo");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw  new RuntimeException("Account Number Doesn't Exist");
    }

    public long GenAccNO() throws SQLException {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select AccNo from accounts order by AccNo desc limit 1");
            if (resultSet.next()) {
                long LastAccNo = resultSet.getLong("AccNo");
                return LastAccNo + 1;
            }
            else {
                return 10000100;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 10000100;

    }

}
