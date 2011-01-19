package raft.util;

import java.net.UnknownHostException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/* sample usage
 * MongoDBReader conn = new MongoDBReader();
 * ...
 * long starttime = new Date().getTime();
 * driver.get("http://www.google.com/");
 * conn.InstertPermLog(starttime, "Landing_Page");
 */
public class MongoDBReader {
	
	private DB db;
	private DBCollection coll;
	
	public MongoDBReader(String hostname) throws UnknownHostException, MongoException{
		
		// Connect to Mongo DB server
		Mongo m = new Mongo( hostname );
		// Get Mongo DB Connection
		db = m.getDB("perfdb");
		// Prepare Collection
		coll = db.getCollection("perfTestColl");
		
	}

	public MongoDBReader() throws UnknownHostException, MongoException {
		
		//Connect to the localhost MongoDB by Default
		String hostname = "localhost";
		
		// Connect to Mongo DB server
		Mongo m = new Mongo( hostname );
		// Get Mongo DB Connection
		db = m.getDB("perfdb");
		// Prepare Collection
		coll = db.getCollection("perfTestColl");
	}

	public DB getDB(){
		return db;
	}
	
	public DBCollection getCollection(){
		return coll;
	}
	
	public void InstertPermLog(long startTime, String transactionName){
		
		long endtime = new java.util.Date().getTime();
		long responsetime = endtime - startTime;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S");
	    Date date = new Date();
	     
		BasicDBObject doc = new BasicDBObject();
		doc.put("TimeStamp", dateFormat.format(date));
		doc.put("Rtime", responsetime);
		doc.put("transaction", transactionName);
		
		coll.insert(doc);
	}
	
	public void Query(){
		//To-Do some code here later
	}
}
