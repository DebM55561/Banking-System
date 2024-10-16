import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class bankingapp {


    private static final String url = "jdbc:mysql://localhost:3306/BankingSys";

    public String getUrl() {
        return url;
    }

    private static final String username = "root";

    public String getUsername() {
        return username;
    }

    private static final String password = "dan55561";

    public String getPassword() {
        return password;
    }

    private int cho1;

    public static void main(String[] args) throws SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);
            Accounts accounts = new Accounts(connection, scanner);
            User user = new User(connection, scanner);
            AccManager acm = new AccManager(connection, scanner);

            String email;
            long AccNo;

            while(true)
            {
                System.out.println("BANKING SYSTEM\n");
                System.out.println("1.Register\n");
                System.out.println("2.Login\n");
                System.out.println("3.Exit\n");
                System.out.print("Enter Your choice: ");
                int choice = scanner.nextInt();
                switch (choice)
                {
                    case 1:
                        user.Reg();
                        break;
                    case 2:
                        email=user.login();
                        if(email!=null)
                        {
                            System.out.println("User Logged In");
                            if(!accounts.CheckAcc(email))
                            {
                                System.out.println("1.Open A New Bank Account\n");
                                System.out.println("2.Exit\n");
                                int cho = scanner.nextInt();
                                if(cho==1)
                                {
                                    AccNo = accounts.OpenAcc(email);
                                    System.out.println("Account Created Successfully\n");
                                    System.out.println("Your Account Number is: "+ AccNo+"\n");
                                }
                                else
                                {
                                    break;
                                }
                            }
                            else {
                                AccNo = accounts.GetAccNo(email);
                                int choice2 =0;
                                while(choice2!=5)
                                {
                                    System.out.println("1. Debit Money");
                                    System.out.println("2. Credit Money");
                                    System.out.println("3. Check Balance");
                                    System.out.println("4. Transfer Money");
                                    System.out.println("5. Exit");
                                    choice2 = scanner.nextInt();
                                    switch (choice2)
                                    {
                                        case 1:
                                            acm.DebitMoney(AccNo);
                                            System.out.println("Exit?\n 1.Yes\n2.No");
//                                            cho1=scanner.nextInt();

                                            break;
                                        case 2:
                                            acm.creditMoney(AccNo);
                                            break;
                                        case 3:
                                            acm.GetBalance(AccNo);
                                            break;
                                        case 4:
                                            acm.TransferMoney(AccNo);
                                            break;
                                        case 5:
                                            break;
                                    }
                                }
                            }
                        }else {
                            System.out.println("Wrong Email Address.");
                        }
                    case 3:
                        System.out.println("Thank you for using Dan's Bank\n\n");
                        break;

                    default:
                        System.out.println("Error");

                }
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
}

