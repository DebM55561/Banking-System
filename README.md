# Banking-System
A banking system program in Java with all basic functionalities.
The program contains 4 java classes- BankingApp, User, Accounts, AccManager.
BankingApp- implements all the defined functions from the other classes
User- User class creates a User account and has functions such as Registration, login and a function to check if User Already Exists
Accounts- This class has 4 functions. checkAccount- to check if a user has a bank account. 
                                      OpenAccounts- to create an account for an already existing user
                                      GetAccNo- to fetch a user's account number
                                      GenAccNo- to Generate an account number based on the last account number enterd in the database
AccManager- all the main functionalities are written here. 
            DebitMoney- takes Account Number as argument and takes input the amount and security pin.
            CreditMoney- takes Account Number as argument and takes input the amount and security pin.
            CheckBalance- takes Account Number as argument and security pin as input to fetch User's balance from database.
            TransferMoney- takes Account Number as argument and takes input the Reciever's account number, amount and security pin.
