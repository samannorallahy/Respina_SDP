//package com.nor.sdpplugin.dataBase;
//
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoCursor;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.Projections;
//import com.nor.sdpplugin.model.RequestsModel;
//import org.bson.Document;
//import org.bson.conversions.Bson;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CrudMongoDB {
//    public static void insertRequest(String sourceID, String requestID) {
//        MongoDatabase database = new MongoDBConnection().getMongoDatabase("callSDP");
//        MongoCollection<Document> collection = database.getCollection("requests");
//        Document doc = new Document("sourceID", sourceID).append("requestID", requestID);
//        collection.insertOne(doc);
//    }
//
//    public static void getAllRequestList() {
//        MongoDatabase database = new MongoDBConnection().getMongoDatabase("callSDP");
//        MongoCollection<Document> collection = database.getCollection("requests");
//        Bson projection = Projections.fields(Projections.include("sourceID", "requestID"), Projections.excludeId());
//        MongoCursor<Document> cursor = collection.find().projection(projection).iterator();
//        List<RequestsModel> requestsModels = new ArrayList<>();
//        try {
//            while (cursor.hasNext()) {
//                Document next = cursor.next();
//                RequestsModel model = new RequestsModel();
//                model.setSourceID(next.get("sourceID").toString());
//                model.setRequestID(next.get("requestID").toString());
//                requestsModels.add(model);
//            }
//        } finally {
//            cursor.close();
//        }
//        System.out.println(requestsModels);
//
//    }
//
//}
