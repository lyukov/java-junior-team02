package com.lapushki.chat.db;

/*import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

public class DB {
    public static void main(String[] args) {

        System.out.println("Connecting to DB...");
//        List<MongoCredential> credentialsList = new ArrayList<>();
//        MongoCredential creds = MongoCredential.createCredential("quest_local_mgo01", "admin", "qweRTY1!".toCharArray());
//        credentialsList.add(creds);
//        ServerAddress serverAddress = new ServerAddress("localhost", 27017);
//        MongoClient mongoClient = new MongoClient(serverAddress, credentialsList);
        MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);
        System.out.println("Connected to MongoDB...");

        // ****** print existing databases
        //mongoClient.getDatabaseNames().forEach(System.out::println);
        mongoClient.listDatabaseNames().forEach((Block<? super String>) System.out::println);

        // get database
        MongoDatabase jcgDatabase = mongoClient.getDatabase("JavaCodeGeeks");

        // ****** create collection
        jcgDatabase.createCollection("authors");
        jcgDatabase.createCollection("posts");

        MongoCollection<Document> authorCollection = jcgDatabase.getCollection("authors");
        // ****** Inserting document in collection
        Document document = new Document();
        document.put("name", "Shubham");
        document.put("company", "JCG");
        authorCollection.insertOne(document);
        System.out.println("Inserted document = " + document);

        // ****** Updating Document
        Document updateQuery = new Document();
        updateQuery.put("name", "Shubham");

        Document newNameDocument = new Document();
        newNameDocument.put("name", "Shubham Aggarwal");

        Document updateObject = new Document();
        updateObject.put("$set", newNameDocument);

        UpdateResult updateResult = authorCollection.updateOne(updateQuery, updateObject);
        System.out.println("Documents updated: " + updateResult.getModifiedCount());

        // ****** Search Document
        Document searchQuery = new Document();
        searchQuery.put("company", "JCG");

        FindIterable<Document> documents = authorCollection.find(searchQuery);

        for (Document doc: documents) {
            System.out.println(doc);
        }

        // ****** Delete Document
        /*Document deleteSearchQuery = new Document();
        deleteSearchQuery.put("_id", new ObjectId("5b77c15cf0406c64b6c9dae4"));
        DeleteResult deleteResult = authorCollection.deleteOne(deleteSearchQuery);
        System.out.println("Documents updated: " + deleteResult.getDeletedCount());*/
//    }
//}
