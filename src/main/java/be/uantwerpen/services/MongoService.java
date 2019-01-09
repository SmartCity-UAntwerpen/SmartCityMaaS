package be.uantwerpen.services;

import be.uantwerpen.model.Delivery;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 15/04/2017.
 */
@Service
public class MongoService {

    private static final Logger logger = LogManager.getLogger(MongoService.class);

    protected MongoClient mongo;

    protected MongoDatabase db;

    /**
     * Initialize the parameters fot the MongoDB client connection.
     */
    public MongoService() {
        // IP address public centos VM : 143.129.39.159
        // IP address for proxy server : 172.10.0.8
        // Windows VM on own PC : 192.168.10.2
        mongo = new MongoClient("143.129.39.127", 27017);
        /* Get database */
        // if database doesn't exists, MongoDB will create it for you
        db = mongo.getDatabase("local");
    }

    /**
     * Put a specific delivery on the database.
     *
     * @param delivery The delivery to add
     */
    public void putStatement(Delivery delivery) {
        logger.info("Start writing data to MongoDB");
        MongoCollection<Document> mydatabaserecords = db.getCollection("deliveries");
        Document document = new Document();
        document.put("typeDelivery", delivery.getType());
        document.put("username", delivery.getUserName());
        document.put("firstname", delivery.getFirstName());
        document.put("lastname", delivery.getLastName());
        document.put("pointA", delivery.getPointA());
        document.put("pointB", delivery.getPointB());
        document.put("mapA", delivery.getMapA());
        document.put("mapB", delivery.getMapB());
        document.put("passengers", ("" + delivery.getPassengers()));
        document.put("timesample", new Date());
        mydatabaserecords.insertOne(document);
        logger.info("Finished writing data to MongoDB");
        //String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ").format(d);
    }

    /**
     * Return all the deliveries from the database.
     *
     * @return A list of deliveries
     */
    public List<Delivery> getAllDeliveries() {
        logger.info("Get all deliveries data of MongoDB");
        MongoCollection<Document> mydatabaserecords = db.getCollection("deliveries");
        FindIterable<Document> cursor = mydatabaserecords.find();
        return GetListData(cursor);
    }

    /**
     * Return the last added delivery from the database.
     *
     * @return the last added delivery
     */
    public Delivery getLastDelivery() {
        logger.info("Get last data of MongoDB");
        Delivery lastDelivery = new Delivery();
        MongoCollection<Document> mydatabaserecords = db.getCollection("deliveries");
        FindIterable<Document> cursor = mydatabaserecords.find().sort(new Document("_id", -1)).limit(1);
        if (cursor != null) {
            Document doc = cursor.first();
            ObjectId object_id = doc.getObjectId("_id");
            String typeDelivery = doc.getString("typeDelivery");
            String username = doc.getString("username");
            String firstname = doc.getString("firstname");
            String lastname = doc.getString("lastname");
            String pointA = doc.getString("pointA");
            String pointB = doc.getString("pointB");
            int mapA = doc.getInteger("mapA");
            int mapB = doc.getInteger("mapB");
            String passengers = doc.getString("passengers");
            Date d = new Date(doc.getDate("timesample").getTime());
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ").format(d);
            lastDelivery.setIdDelivery(object_id.toString());
            lastDelivery.setUserName(username);
            lastDelivery.setFirstName(firstname);
            lastDelivery.setLastName(lastname);
            lastDelivery.setPointA(pointA);
            lastDelivery.setPointB(pointB);
            lastDelivery.setMapA(mapA);
            lastDelivery.setMapB(mapB);
            lastDelivery.setType(typeDelivery);
            lastDelivery.setPassengers(Integer.parseInt(passengers));
            lastDelivery.setDate(timestamp);
        }
        return lastDelivery;
    }

    /**
     * Delete delivery from database.
     *
     * @param deliveryID The ID of the delivery to be deleted
     */
    public void deleteDelivery(String deliveryID) {
        logger.info("Remove delivery [" + deliveryID + "]'s data from MongoDB");
        MongoCollection<Document> collection = db.getCollection("deliveries");
        //collection.deleteOne(new Document("_id", new ObjectId("57a49c6c33b10927ff09623e")));
        BasicDBObject query = new BasicDBObject();
        query.append("_id", new ObjectId(deliveryID));
        collection.deleteOne(query);//.remove(query);
    }


    /**
     * Return all the deliveries from the database which type is not set as 'done'.
     *
     * @return A list of deliveries
     */
    public List<Delivery> getAllBusyDeliveries() {
        logger.info("Get all busy deliveries data of MongoDB");

        MongoCollection<Document> mydatabaserecords = db.getCollection("deliveries");
        BasicDBObject searchObject = new BasicDBObject();
        searchObject.put("typeDelivery", "HumanTransport");
        FindIterable<Document> cursor = mydatabaserecords.find(searchObject);
        return GetListData(cursor);

    }

    /**
     * Set a delivery as done.
     *
     * @param deliveryID The ID of the delivery to be set
     */
    public void setDeliveryDone(String deliveryID) {
        logger.info("Setting delivery [" + deliveryID + "] as done.");
        MongoCollection<Document> collection = db.getCollection("deliveries");

        BasicDBObject query = new BasicDBObject();
        query.put("_id", deliveryID);
        BasicDBObject carrier = new BasicDBObject();
        carrier.put("typeDelivery", "done");
        BasicDBObject set = new BasicDBObject("$set", carrier);
        collection.updateOne(query, set);
    }


    public List<Delivery> GetListData(FindIterable<Document> cursor) {
        List<Delivery> deliveries = new ArrayList<Delivery>();
        if (cursor != null) {
            for (Document it : cursor) {
                Delivery delivery = new Delivery();
                ObjectId object_id = it.getObjectId("_id");
                String typeDelivery = it.getString("typeDelivery");
                String username = it.getString("username");
                String firstname = it.getString("firstname");
                String lastname = it.getString("lastname");
                String pointA = it.getString("pointA");
                String pointB = it.getString("pointB");
                int mapA = it.getInteger("mapA");
                int mapB = it.getInteger("mapB");
                String passengers = it.getString("passengers");
                Date d = new Date(it.getDate("timesample").getTime());
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ").format(d);
                delivery.setIdDelivery(object_id.toString());
                delivery.setUserName(username);
                delivery.setFirstName(firstname);
                delivery.setLastName(lastname);
                delivery.setPointA(pointA);
                delivery.setPointB(pointB);
                delivery.setMapA(mapA);
                delivery.setMapB(mapB);
                delivery.setType(typeDelivery);
                delivery.setPassengers(Integer.parseInt(passengers));
                delivery.setDate(timestamp);
                deliveries.add(delivery);
            }
        }
        return deliveries;
    }

}