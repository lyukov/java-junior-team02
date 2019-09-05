package com.lapushki.chat.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {

    private final String DELIMITER = ";";
    private String url;
    private String user;
    private String password;
    private String database;
    private String table;
    private String fileName;

    public Parser(String fileName) {
        this.fileName = fileName;
        parseConfig();
    }

    public void parseConfig() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(DELIMITER);
                    String name = values[0].trim().toLowerCase();
                    String value = values[1].trim();
                    if ("url".equals(name))
                        url = value;
                    else if ("user".equals(name))
                        user = value;
                    else if ("password".equals(name))
                        password = value;
                    else if ("database".equals(name))
                        database = value;
                    else if ("table".equals(name))
                        table = value;
                    else
                        throw new IOException("Wrong parameter!");
                }
                if (url == null || url.isEmpty() ||
                        user == null || user.isEmpty() ||
                        password == null || password.isEmpty() ||
                        database == null || database.isEmpty() ||
                        table == null || table.isEmpty())
                    useDefaultConfig();
            } catch (IOException e) {
                e.printStackTrace();
                useDefaultConfig();
            }
        } catch (IOException e) {
            e.printStackTrace();
            useDefaultConfig();
        }
    }

    private void useDefaultConfig() {
        System.out.println("Use default config");
        url = "jdbc:mysql://localhost:3306/chat";
        user = "user";
        password = "password";
        database = "chat";
        table = "messages";
    }

    public String getUrl() {
        return url;
    }

    public String getPassword() {
        return password;
    }

    public String getUser() {
        return user;
    }

    public String getDatabase() {
        return database;
    }

    public String getTable() {
        return table;
    }
}

