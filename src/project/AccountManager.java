package project;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Scanner;

public class AccountManager {
    private MongoDatabase database;
    private Scanner scanner;

    AccountManager(MongoDatabase database, Scanner scanner) {
        this.database = database;
        this.scanner = scanner;
    }

    public void credit_money(long account_number) {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        MongoCollection<Document> accountsCollection = database.getCollection("Accounts");
        Document account = accountsCollection.find(Filters.and(
                Filters.eq("account_number", account_number),
                Filters.eq("security_pin", security_pin)
        )).first();

        if (account != null) {
            double new_balance = account.getDouble("balance") + amount;
            accountsCollection.updateOne(Filters.eq("account_number", account_number),
                    new Document("$set", new Document("balance", new_balance)));
            System.out.println("Rs." + amount + " credited Successfully");
        } else {
            System.out.println("Invalid Security Pin or Account Number!");
        }
    }

    public void debit_money(long account_number) {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        MongoCollection<Document> accountsCollection = database.getCollection("Accounts");
        Document account = accountsCollection.find(Filters.and(
                Filters.eq("account_number", account_number),
                Filters.eq("security_pin", security_pin)
        )).first();

        if (account != null) {
            double current_balance = account.getDouble("balance");
            if (amount <= current_balance) {
                double new_balance = current_balance - amount;
                accountsCollection.updateOne(Filters.eq("account_number", account_number),
                        new Document("$set", new Document("balance", new_balance)));
                System.out.println("Rs." + amount + " debited Successfully");
            } else {
                System.out.println("Insufficient Balance!");
            }
        } else {
            System.out.println("Invalid Security Pin or Account Number!");
        }
    }

    public void transfer_money(long sender_account_number) {
        scanner.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number = scanner.nextLong();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        MongoCollection<Document> accountsCollection = database.getCollection("Accounts");
        Document senderAccount = accountsCollection.find(Filters.and(
                Filters.eq("account_number", sender_account_number),
                Filters.eq("security_pin", security_pin)
        )).first();

        Document receiverAccount = accountsCollection.find(Filters.eq("account_number", receiver_account_number)).first();

        if (senderAccount != null && receiverAccount != null) {
            double current_balance = senderAccount.getDouble("balance");
            if (amount <= current_balance) {
                double sender_new_balance = current_balance - amount;
                double receiver_new_balance = receiverAccount.getDouble("balance") + amount;

                accountsCollection.updateOne(Filters.eq("account_number", sender_account_number),
                        new Document("$set", new Document("balance", sender_new_balance)));
                accountsCollection.updateOne(Filters.eq("account_number", receiver_account_number),
                        new Document("$set", new Document("balance", receiver_new_balance)));

                System.out.println("Transaction Successful!");
                System.out.println("Rs." + amount + " Transferred Successfully");
            } else {
                System.out.println("Insufficient Balance!");
            }
        } else {
            System.out.println("Invalid Security Pin or Account Number!");
        }
    }

    public void getBalance(long account_number) {
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        MongoCollection<Document> accountsCollection = database.getCollection("Accounts");
        Document account = accountsCollection.find(Filters.and(
                Filters.eq("account_number", account_number),
                Filters.eq("security_pin", security_pin)
        )).first();

        if (account != null) {
            double balance = account.getDouble("balance");
            System.out.println("Balance: " + balance);
        } else {
            System.out.println("Invalid Security Pin or Account Number!");
        }
    }
}

