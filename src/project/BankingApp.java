package project;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.util.Scanner;

public class BankingApp {
    private static final String url = "localhost";
    private static final int port = 27017;

    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient(url, port);
        MongoDatabase database = mongoClient.getDatabase("banking_system");
        Scanner scanner = new Scanner(System.in);

        User user = new User(database, scanner);
        Accounts accounts = new Accounts(database, scanner);
        AccountManager accountManager = new AccountManager(database, scanner);

        String email;
        long account_number;

        while (true) {
            System.out.println("*** WELCOME TO TRUST BANK ***");
            System.out.println();
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("Enter your choice: ");
            int choice1 = scanner.nextInt();
            switch (choice1) {
                case 1:
                    user.register();
                    break;
                case 2:
                    email = user.login();
                    if (email != null) {
                        System.out.println();
                        System.out.println("User Logged In!");
                        if (!accounts.account_exist(email)) {
                            System.out.println();
                            System.out.println("1. Open a new Bank Account");
                            System.out.println("2. Exit");
                            if (scanner.nextInt() == 1) {
                                account_number = accounts.open_account(email);
                                System.out.println("Account Created Successfully");
                                System.out.println("Your Account Number is: " + account_number);
                            } else {
                                break;
                            }
                        }
                        account_number = accounts.getAccount_number(email);
                        int choice2 = 0;
                        while (choice2 != 5) {
                            System.out.println();
                            System.out.println("1. Credit");
                            System.out.println("2. Debit");
                            System.out.println("3. Transfer Money");
                            System.out.println("4. Check Balance");
                            System.out.println("5. Logout");
                            System.out.println("Enter your choice: ");
                            choice2 = scanner.nextInt();
                            switch (choice2) {
                                case 1:
                                    accountManager.credit_money(account_number);
                                    break;
                                case 2:
                                    accountManager.debit_money(account_number);
                                    break;
                                case 3:
                                    accountManager.transfer_money(account_number);
                                    break;
                                case 4:
                                    accountManager.getBalance(account_number);
                                    break;
                                case 5:
                                    System.out.println("User Logged Out!");
                                    break;
                                default:
                                    System.out.println("Invalid Choice!");
                            }
                        }
                    } else {
                        System.out.println("Invalid Credentials");
                    }
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}
