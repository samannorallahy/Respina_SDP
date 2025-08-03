//package com.nor.sdpplugin.dataBase;
//
//import com.mongodb.client.*;
//
//public class MongoDBConnection {
//    private MongoClient mongoClient;
//    private MongoDatabase database;
//
//    MongoDBConnection() {
//        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
//    }
//
//    public MongoDatabase getMongoDatabase(String dataBaseName) {
//        this.database = this.mongoClient.getDatabase(dataBaseName);
//        return this.database;
//    }
//
//
//    public static void main(String[] args) {
////        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
////        MongoDatabase database = mongoClient.getDatabase("saman");
////        MongoDatabase database = new MongoDBConnection().getMongoDatabase("callSDP");
////        MongoCollection<Document> collection = database.getCollection("requests");
//
////        crudMongoDB.insertRequest("1122","556688");
//        CrudMongoDB.getAllRequestList();
//
////Document document =
////        MongoCursor<Document> cursor = collection.find().iterator();
////        try {
////            while (cursor.hasNext()) {
////                System.out.println(cursor.next().toJson());
////            }
////        } finally {
////            cursor.close();
////        }
////        Scanner scanner = new Scanner(System.in);
////        System.out.println("Name:");
////        String name = scanner.next();
////        System.out.println("family:");
////        String family = scanner.next();
////        System.out.println("age:");
////        int age = scanner.nextInt();
//
////        Document doc = new Document("name", name).append("age", age).append("family", family);
////        collection.insertOne(doc);
//
////        Gson gson = new Gson();
////        MongoCollection<Document> collection3 = database.getCollection("my_collection_name");
////        Document document = Document.parse(gson.toJson(new AddRequest_InputModel()));
//
//
////        MongoCollection<AddRequest_InputModel> collection2 = database.getCollection("addRequest", AddRequest_InputModel.class);
////        collection3.insertOne(document);
//
//
////        Bson projection = Projections.fields(Projections.include("name"), Projections.excludeId());
////        cursor = collection.find().projection(projection).iterator();
////        try {
////            while (cursor.hasNext()) {
////                System.out.println(cursor.next().toJson());
////            }
////        } finally {
////            cursor.close();
////        }
//    }
//}
//
//
//
//
