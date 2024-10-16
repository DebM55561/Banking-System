import java.sql.*;
import java.util.Scanner;

public class AccManager {
    private final Connection connection;
    private final Scanner scanner;

    public AccManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

//    public boolean AccExist(String email) {
//        String query = "select * from accounts where email = ?";
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1, email);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                return true;
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return false;
//    }

    public void DebitMoney(long AccNo) throws SQLException
    {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        int Amt = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String SecurityPin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where AccNo = ? and securitypin = ?");
            preparedStatement.setLong(1,AccNo);
            preparedStatement.setString(2,SecurityPin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                double CurrentBalance = resultSet.getDouble("balance");
                if (Amt<=CurrentBalance)
                {
                    String debit_Queue = "update accounts set balance = balance + ? where AccNo = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(debit_Queue);
                    preparedStatement1.setInt(1,Amt);
                    preparedStatement1.setLong(2,AccNo);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if(rowsAffected>0)
                    {
                        System.out.println("Rs."+Amt+" Has Been Debited.");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }
                    else{
                        System.out.println("Transaction Failed, Please Try Again.");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                else {
                    System.out.println("Insufficient Balance.");
                }
            }
            else
            {
                System.out.println("\nWrong Credentials. Please try Again.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void creditMoney(Long AccNo) throws SQLException
    {
        scanner.nextLine();
        System.out.println("Enter Amount: ");
        int Amt = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter Security Pin: ");
        String sp = scanner.nextLine();
        String creditQuery = "select * from accounts where AccNo = ? and securitypin = ?";
        try{
            connection.setAutoCommit(false);
            if(AccNo!=0)
            {
                PreparedStatement preparedStatement = connection.prepareStatement(creditQuery);
                preparedStatement.setLong(1,AccNo);
                preparedStatement.setString(2,sp);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next())
                {
                    double currentBalance = resultSet.getDouble("balance");
                    if(Amt<=currentBalance)
                    {
                        String Debit_Query =  "update accounts set balance = balance - ? where AccNo = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(Debit_Query);
                        preparedStatement1.setInt(1,Amt);
                        preparedStatement1.setLong(2,AccNo);
//                        ResultSet resultSet1 = preparedStatement1.executeQuery();
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if(rowsAffected>0)
                        {
                            System.out.println("Rs."+ Amt+ " Has Been Credited.");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else{
                            System.out.println("Transaction Failed, Please Try Again.");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }


                    }
                    else {
                        System.out.println("Insufficient Balance.");
                    }
                }
                else
                {
                    System.out.println("Incorrect Credentials. please Try Again.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void GetBalance(Long AccNo)
    {
        scanner.nextLine();
        System.out.println("Enter Security Pin: ");
        String pw=scanner.nextLine();
        String Balance_Query = "select balance from accounts where AccNo = ? and securitypin = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(Balance_Query);
            preparedStatement.setLong(1,AccNo);
            preparedStatement.setString(2,pw);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                double balance = resultSet.getDouble("balance");
                System.out.println("Current Balancw: Rs."+balance );
            }
            else
            {
                System.out.println("Invalid Credentials");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void TransferMoney(Long AccNo)
    {
        System.out.println("Enter Receiver's Account Number: ");
        Long RecAccNo = scanner.nextLong();
        scanner.nextLine();
        System.out.println("Enter Amount: ");
        int Amt = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter Security Pin: ");
        String pin = scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(AccNo!=0 && RecAccNo!=0)
            {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where AccNo = ? and securitypin = ?");
                preparedStatement.setLong(1,AccNo);
                preparedStatement.setString(2,pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next())
                {
                    double currentBalance = resultSet.getDouble("balance");
                    if(Amt<=currentBalance)
                    {
                        String DebitQuery = "update accounts set balance = balance + ? where AccNo = ?";
                        String CreditQuery = "update accounts set balance = balance - ? where AccNo = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(DebitQuery);
                        PreparedStatement preparedStatement2 = connection.prepareStatement(CreditQuery);
                        preparedStatement1.setInt(1,Amt);
                        preparedStatement1.setLong(2, RecAccNo);
                        preparedStatement2.setInt(1, Amt);
                        preparedStatement2.setLong(2, AccNo);
                        int rowsAffected1 = preparedStatement1.executeUpdate();
                        int rowsAffected2 = preparedStatement2.executeUpdate();
                        if(rowsAffected1>0 && rowsAffected2>0)
                        {
                            System.out.println("Transaction Successful");
                            System.out.println("Rs."+ Amt+ " Has Been Transferred Successfully.");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else
                        {
                            System.out.println("Transaction Unsuccessful.");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else
                    {
                        System.out.println("Insufficient Balance.");
                    }
                }
                else
                {
                    System.out.println("Incorrect Credentials.");
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
