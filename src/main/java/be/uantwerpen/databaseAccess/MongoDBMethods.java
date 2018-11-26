package be.uantwerpen.databaseAccess;

import be.uantwerpen.model.Delivery;
import com.mongodb.*;
import com.mongodb.client.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Frédéric Melaerts on 15/04/2017.
 */
public class MongoDBMethods {

    private static final Logger logger = LogManager.getLogger(MongoDBMethods.class);

    protected MongoClient mongo;

    protected MongoDatabase db;

    /**
     * Initialize the parameters fot the MongoDB client connection.
     */
    public MongoDBMethods()
    {
        // IP address public centos VM : 143.129.39.159
        // IP address for proxy server : 172.10.0.8
        // Windows VM on own PC : 192.168.10.2
        mongo =  new MongoClient( "143.129.39.127" , 27017 );
        /**** Get database ****/
        // if database doesn't exists, MongoDB will create it for you
        db = mongo.getDatabase("local");
    }

    /**
     * Put a specific delivery on the database.
     * @param delivery
     */
    public void putStatement(Delivery delivery)
    {
        logger.info("Start writing data to MongoDB");
        MongoCollection<Document> mydatabaserecords = db.getCollection("deliveries");
        Document document = new Document();
        document.put("typeDelivery", delivery.getType());
        document.put("username", delivery.getUserName());
        document.put("firstname", delivery.getFirstName());
        document.put("lastname", delivery.getLastName());
        document.put("pointA", delivery.getPointA());
        document.put("pointB", delivery.getPointB());
        document.put("passengers", (""+delivery.getPassengers()));
        document.put( "timesample",new Date());
        mydatabaserecords.insertOne(document);
        logger.info("Finished writing data to MongoDB");
        //String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ").format(d);
    }

    /**
     * Return all the deliveries from the database.
     * @return
     */
    public List<Delivery> getAllDeliveries()
    {
        logger.info("Get all deliveries data of MongoDB");
        List<Delivery> deliveries = new ArrayList<Delivery>();
        MongoCollection<Document> mydatabaserecords = db.getCollection("deliveries");
        FindIterable<Document> cursor = mydatabaserecords.find();
        if(cursor != null) {
            for(Document it: cursor)
            {
                Document doc = it;
                Delivery delivery = new Delivery();
                ObjectId object_id = doc.getObjectId("_id");
                String typeDelivery = doc.getString("typeDelivery");
                String username = doc.getString("username");
                String firstname = doc.getString("firstname");
                String lastname = doc.getString("lastname");
                String pointA = doc.getString("pointA");
                String pointB = doc.getString("pointB");
                String passengers = doc.getString("passengers");
                Date d = new Date(doc.getDate("timesample").getTime());
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ").format(d);
                delivery.setIdDelivery(object_id.toString());
                delivery.setUserName(username);
                delivery.setFirstName(firstname);
                delivery.setLastName(lastname);
                delivery.setPointA(pointA);
                delivery.setPointB(pointB);
                delivery.setType(typeDelivery);
                delivery.setPassengers(Integer.parseInt(passengers));
                delivery.setDate(timestamp);
                deliveries.add(delivery);
            }
        }
        return deliveries;
    }

    /**
     * Return the last added delivery from the database.
     * @return
     */
    public Delivery getLastDelivery()
    {
        logger.info("Get last data of MongoDB");
        Delivery lastDelivery = new Delivery();
        MongoCollection<Document> mydatabaserecords = db.getCollection("deliveries");
        FindIterable<Document> cursor = mydatabaserecords.find().sort(new Document("_id", -1)).limit(1);
        if(cursor != null) {
            Document doc = cursor.first();
            ObjectId object_id = doc.getObjectId("_id");
            String typeDelivery = doc.getString("typeDelivery");
            String username = doc.getString("username");
            String firstname = doc.getString("firstname");
            String lastname = doc.getString("lastname");
            String pointA = doc.getString("pointA");
            String pointB = doc.getString("pointB");
            String passengers = doc.getString("passengers");
            Date d = new Date(doc.getDate("timesample").getTime());
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ").format(d);
            lastDelivery.setIdDelivery(object_id.toString());
            lastDelivery.setUserName(username);
            lastDelivery.setFirstName(firstname);
            lastDelivery.setLastName(lastname);
            lastDelivery.setPointA(pointA);
            lastDelivery.setPointB(pointB);
            lastDelivery.setType(typeDelivery);
            lastDelivery.setPassengers(Integer.parseInt(passengers));
            lastDelivery.setDate(timestamp);
        }
        return lastDelivery;
    }

    /**
     * Delete delivery from database.
     * @param deliveryID
     */
    public void deleteDelivery(String deliveryID)
    {
        logger.info("Remove delivery ["+deliveryID+"]'s data from MongoDB");
        MongoCollection<Document> collection = db.getCollection("deliveries");
        //collection.deleteOne(new Document("_id", new ObjectId("57a49c6c33b10927ff09623e")));
        BasicDBObject query = new BasicDBObject();
        query.append("_id", new ObjectId(deliveryID));
        collection.deleteOne(query);//.remove(query);
        return;
    }
}
