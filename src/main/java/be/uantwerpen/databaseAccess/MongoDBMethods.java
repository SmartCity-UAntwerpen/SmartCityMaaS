package be.uantwerpen.databaseAccess;

import be.uantwerpen.model.Delivery;
import com.mongodb.*;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Frédéric Melaerts on 15/04/2017.
 */
public class MongoDBMethods {

    protected MongoClient mongo;

    protected MongoDatabase db;
    protected BasicDBObject searchQuery = null;
    protected Query query = null;
    protected String specificQueryInfo = null;

    public enum Query{
        SELECT_ALL, SELECT_OBJECT_ID, SELECT_PUT_DELIVERY, SELECT_DELIVERYTYPE, SELECT_USERNAME, SELECT_FIRSTNAME, SELECT_LASTNAME,SELECT_POINTA,SELECT_POINTB, SELECT_PASSENGERS, SELECT_TIME, SELECT_LAST
    }

    /**
     * Initialize the parameters fot the MongoDB client connection.
     */
    public MongoDBMethods()
    {
        // IP address public centos VM : 143.129.39.159
        // IP address for proxy server : 172.10.0.8
        // Windows VM on own PC : 192.168.10.2
        mongo =  new MongoClient( "192.168.10.2" , 27017 );
        /**** Get database ****/
        // if database doesn't exists, MongoDB will create it for you
        db = mongo.getDatabase("local");


        //db = mongo.get("local");

    }
    public String performStatement()
    {
		System.out.println("MongoDB");
		System.out.println("Show all databases");
        List<String> dbs = mongo.getDatabaseNames();
		for(String db : dbs){
            System.out.println(db);
        }
        System.out.println("----- Read data from MongoDB -----");
        FindIterable<Document> mydatabaserecords  = db.getCollection("deliveries").find();
        MongoCursor<Document> iterator = mydatabaserecords.iterator();
        while (iterator.hasNext()) {
            Document doc = iterator.next();

            ObjectId object_id = doc.getObjectId("_id");
            String type = doc.getString("delivery");
            String username = doc.getString("username");

            String firstname = doc.getString("firstname");

            String lastname = doc.getString("lastname");

            String pointA = doc.getString("pointA");

            String pointB = doc.getString("pointB");
            Date time = doc.getDate("timestamp");
            System.out.println("Object_ID: " + object_id);
            System.out.println("type: " + type);
            System.out.println("firstname: " + firstname);
            System.out.println("lastname: " + lastname);
            System.out.println("pointA: " + pointA);
            System.out.println("pointB: " + pointB);
            System.out.println("time: " + time);
        }
        return "h";
    }


    // public List<TrackSample> getObject(String databaseCollection)

    public List<Delivery> getObject(String databaseCollection)
    {
        System.out.println("MongoDB");

        MongoIterable<String> dbs = mongo.listDatabaseNames();
        System.out.println("Show all databases");
        for(String db : dbs){
            System.out.println(db);
        }
       // BasicDBObject searchQuery = new BasicDBObject();
        //searchQuery.put("RFID_tag", "tag1");
    // } catch(SQLException se){
        System.out.println("----- Get object from MongoDB -----");

        List<Delivery> deliveries = null;
        try {

            MongoCollection<Document> mydatabaserecords  = db.getCollection(databaseCollection);

            if(query == Query.SELECT_LAST)
            {
                Delivery delivery = null;
                FindIterable<Document> cursor = mydatabaserecords.find().sort(new Document("_id", -1)).limit(1);
                if(cursor != null) {
                    deliveries = new LinkedList<>();
                    Document doc = cursor.first();
                    ObjectId object_id = doc.getObjectId("_id");
                    String type = doc.getString("typeDelivery");
                    String username = doc.getString("username");
                    String firstname = doc.getString("firstname");
                    String lastname = doc.getString("lastname");
                    String pointA = doc.getString("pointA");
                    String pointB = doc.getString("pointB");
                    String pas = doc.getString("passengers");
                    int passengers = Integer.parseInt(pas);
                    Date time = doc.getDate("timestamp");
                    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ").format(time);
                    delivery = new Delivery(object_id.toString(), type, firstname,lastname,pointA,pointB,passengers, timestamp);
                    deliveries.add(delivery);
                }

            }else {

                MongoCursor<Document> cursor = mydatabaserecords.find(searchQuery).iterator();
                if (cursor != null) {
                    deliveries = new LinkedList<>();
                    Delivery delivery = null;
                    int i = 0;
                    try {
                        while (cursor.hasNext()) {
                            Document doc = cursor.next();
                            ObjectId object_id = doc.getObjectId("_id");
                            String typeDelivery = doc.getString("typeDelivery");
                            String username = doc.getString("username");
                            String firstname = doc.getString("firstname");
                            String lastname = doc.getString("lastname");
                            String pointA = doc.getString("pointA");
                            String pointB = doc.getString("pointB");
                            String pas = doc.getString("passengers");
                            String passengers = doc.getString(pas);
                            Date time = doc.getDate("timestamp");
                            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ").format(time);
                            delivery = new Delivery();
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
                            System.out.println("Object_ID: " + object_id);
                            System.out.println("type: " + typeDelivery);
                            System.out.println("firstname: " + firstname);
                            System.out.println("lastname: " + lastname);
                            System.out.println("pointA: " + pointA);
                            System.out.println("pointB: " + pointB);
                            System.out.println("passengers: " + passengers);
                            System.out.println("time: " + timestamp);
                        }
                    } finally {
                        cursor.close();
                    }
                } else {
                    switch (query) {
                        case SELECT_ALL:
                            System.out.println("No objects in the database " + databaseCollection + "!");
                            break;
                        case SELECT_OBJECT_ID:
                            System.out.println("No objects in the database " + databaseCollection + " with OBJECT_ID " + specificQueryInfo + "!");
                            break;
                        case SELECT_DELIVERYTYPE:
                            System.out.println("No objects in the database " + databaseCollection + " with DELIVERYTYPE " + specificQueryInfo + "!");
                            break;
                        case SELECT_FIRSTNAME:
                            System.out.println("No objects in the database " + databaseCollection + " with FIRSTNAME " + specificQueryInfo + "!");
                            break;
                        case SELECT_LASTNAME:
                            System.out.println("No objects in the database " + databaseCollection + " with LASTNAME " + specificQueryInfo + "!");
                            break;
                        case SELECT_POINTA:
                            System.out.println("No objects in the database " + databaseCollection + " with POINTA " + specificQueryInfo + "!");
                            break;
                        case SELECT_POINTB:
                            System.out.println("No objects in the database " + databaseCollection + " with POINTB " + specificQueryInfo + "!");
                            break;
                        case SELECT_PASSENGERS:
                            System.out.println("No objects in the database " + databaseCollection + " with PASSENGERS " + specificQueryInfo + "!");
                            break;
                        case SELECT_TIME:
                            System.out.println("No objects in the database " + databaseCollection + " with TIME " + specificQueryInfo + "!");
                            break;
                        default:
                            System.out.println("No objects in the database " + databaseCollection + "!");
                            break;
                    }
                }
            }
        } catch(MongoException e){
            e.printStackTrace();
        }
        return deliveries;
    }

    public String putStatement()
    {
        System.out.println("----- Write data to MongoDB -----");
        MongoCollection<Document> mydatabaserecords = db.getCollection("deliveries");
        Document document = new Document();
        document.put("RFID_tag", "tag1");
        document.put("Device_ID", "aef");
        document.put( "Timesample",new Date());
        mydatabaserecords.insertOne(document);
        System.out.println("----- Finish writing to MongoDB -----");
        return "h";
    }

    public void putStatement(Delivery delivery)
    {
        System.out.println("----- Write data to MongoDB -----");
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
        System.out.println("----- Finish writing to MongoDB -----");
        //String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ").format(d);
    }

    public Delivery getLastDelivery()
    {
        System.out.println("----- Get last data of MongoDB -----");

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

    public void defineStatement(Query Q, String specific)
    {
        searchQuery = new BasicDBObject();
        query = Q;
        specificQueryInfo = specific;
        switch(query) {
            case SELECT_ALL:
                searchQuery.put("",null);
                break;
            case SELECT_OBJECT_ID:
                if (ObjectId.isValid(specific)) {
                    searchQuery.put("_id",new ObjectId(specific));
                } else {
                    searchQuery.put("_id",null);
                    System.out.println("Invalid id");
                }
                break;
            case SELECT_DELIVERYTYPE:
                searchQuery.put("typeDelivery",specific);
                break;
            case SELECT_USERNAME:
                searchQuery.put("username",specific);
                break;
            case SELECT_FIRSTNAME:
                searchQuery.put("firstname",specific);
                break;
            case SELECT_LASTNAME:
                searchQuery.put("lastname",specific);
                break;
            case SELECT_POINTA:
                searchQuery.put("pointA",specific);
                break;
            case SELECT_POINTB:
                searchQuery.put("pointB",specific);
                break;
            case SELECT_TIME:
                /*
                 * The allowed date format in the URL is yyyy-MM-dd HH:mm:ss:SSS ZZZ
                 * For example: 2017-04-16%2021:41:19:314Z
                 */
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS ZZZ").parse(specific);
                    searchQuery = new BasicDBObject("timestamp", date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
               //new Timestamp(Long.parseLong(specific)));
                break;
            case SELECT_PASSENGERS:
                searchQuery.put("passengers",specific);
                break;
            case SELECT_PUT_DELIVERY:
                searchQuery = null;
                break;
            case SELECT_LAST:
                searchQuery = null;
                break;
            default:
                searchQuery.put("typeDelivery",specific);
                break;
        }
    }
}
