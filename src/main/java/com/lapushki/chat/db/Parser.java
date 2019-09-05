package com.lapushki.chat.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {

    private static final String DELIMITER = ";";
    private static String url;
    private static String user;
    private static String password;
    private static String database;
    private static String table;

    public static void parseConfig(String fileName) {
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
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Use default config");
        }
    }

    public static String getUrl() {
        return url;
    }

    public static String getPassword() {
        return password;
    }

    public static String getUser() {
        return user;
    }

    public static String getDatabase() {
        return database;
    }

    public static String getTable() {
        return table;
    }
}

