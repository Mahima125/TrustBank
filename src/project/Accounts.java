
package project;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Scanner;

public class Accounts {
    private MongoDatabase database;
    private Scanner scanner;

    public Accounts(MongoDatabase database, Scanner scanner) {
        this.database = database;
        this.scanner = scanner;
    }

    public long open_account(String email) {
        if (!account_exist(email)) {
            String full_name;
            double balance;
            String security_pin;

            scanner.nextLine();
            System.out.print("Enter Full Name: ");
            full_name = scanner.nextLine();
            System.out.print("Enter Initial Amount: ");
            balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter Security Pin: ");
            security_pin = scanner.nextLine();

            long account_number = generateAccountNumber();
            Document newAccount = new Document("account_number", account_number)
                    .append("full_name", full_name)
                    .append("email", email)
                    .append("balance", balance)
                    .append("security_pin", security_pin);

            MongoCollection<Document> accountsCollection = database.getCollection("Accounts");
            accountsCollection.insertOne(newAccount);
            return account_number;
        } else {
            throw new RuntimeException("Account Already Exists");
        }
    }

    public long getAccount_number(String email) {
        MongoCollection<Document> accountsCollection = database.getCollection("Accounts");
        Document account = accountsCollection.find(Filters.eq("email", email)).first();

        if (account != null) {
            return account.getLong("account_number");
        } else {
            throw new RuntimeException("Account Number Doesn't Exist!");
        }
    }

    private long generateAccountNumber() {
        MongoCollection<Document> accountsCollection = database.getCollection("Accounts");
        Document lastAccount = accountsCollection.find().sort(new Document("account_number", -1)).first();

        if (lastAccount != null) {
            return lastAccount.getLong("account_number") + 1;
        } else {
            return 10000100;
        }
    }

    public boolean account_exist(String email) {
        MongoCollection<Document> accountsCollection = database.getCollection("Accounts");
        Document account = accountsCollection.find(Filters.eq("email", email)).first();

        return account != null;
    }
}

