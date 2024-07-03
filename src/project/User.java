package project;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Scanner;

public class User {
    private MongoDatabase database;
    private Scanner scanner;

    public User(MongoDatabase database, Scanner scanner) {
        this.database = database;
        this.scanner = scanner;
    }

    public void register() {
        String email, password;

        scanner.nextLine();
        System.out.print("Enter Email: ");
        email = scanner.nextLine();
        System.out.print("Enter Password: ");
        password = scanner.nextLine();

        if (account_exist(email)) {
            System.out.println("Email already registered");
            return;
        }

        Document newUser = new Document("email", email)
                .append("password", password);

        MongoCollection<Document> usersCollection = database.getCollection("Users");
        usersCollection.insertOne(newUser);
        System.out.println("User registered successfully");
    }

    public String login() {
        String email, password;

        scanner.nextLine();
        System.out.print("Enter Email: ");
        email = scanner.nextLine();
        System.out.print("Enter Password: ");
        password = scanner.nextLine();

        MongoCollection<Document> usersCollection = database.getCollection("Users");
        Document user = usersCollection.find(Filters.and(
                Filters.eq("email", email),
                Filters.eq("password", password)
        )).first();

        if (user != null) {
            return email;
        } else {
            return null;
        }
    }

    private boolean account_exist(String email) {
        MongoCollection<Document> usersCollection = database.getCollection("Users");
        Document user = usersCollection.find(Filters.eq("email", email)).first();

        return user != null;
    }
}

