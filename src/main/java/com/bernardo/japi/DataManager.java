package com.bernardo.japi;

import com.bernardo.japi.services.v1.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class DataManager {

	private static final Logger log = Logger.getLogger(DataManager.class.getName());

	private static DB japiDB;

	private static DBCollection userCollection;

	private static DataManager INSTANCE;

	public static DataManager getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new DataManager();
		}

		return INSTANCE;
	}

	/**
	 *
	 */
	private DataManager() {
		try {
			MongoCredential credential = MongoCredential.createCredential("admin", "heroku_6q2kwfv2", "admin".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress("ds241875.mlab.com", 41875), Arrays.asList(credential));
			japiDB = mongoClient.getDB("heroku_6q2kwfv2");
			
			userCollection = japiDB.getCollection("users");
		} catch (Exception e) {
			log.error("db connection error e=", e);
		}
	}

	/**
	 *
	 * @param user
	 * @return
	 */
	public User insertUser(User user) {
		
		BasicDBObject doc = new BasicDBObject();
		
		doc.put("name", user.getName());
		
		userCollection.insert(doc);
		
		user.setId(doc.getString("_id").toString());
		
		return user;
	}

	/**
	 *
	 * @param dbObject
	 * @return
	 */
	public User mapUserFromDBObject(DBObject dbObject) {
		
		User user = new User();
		
		user.setId((String) dbObject.get("_id").toString());
		
		user.setName((String) dbObject.get("name"));
		
		return user;
	}

	/**
	 *
	 * @param userIdString
	 * @return
	 */
	public User findUserById(String userIdString) {
		
		if (userIdString == null) {
			return null;
		}
		
		try {
			DBObject searchById = new BasicDBObject("_id", new ObjectId(userIdString));
			
			DBObject userObject = userCollection.findOne(searchById);
			
			if (userObject != null) {
				return mapUserFromDBObject(userObject);
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error("DBManager::findUserById Exception e=", e);
		}
		
		return null;
	}

	/**
	 *
	 * @return
	 */
	public List<User> findAllUsers() {
		
		List<User> users = new ArrayList<User>();
		
		try {
			
			DBCursor cursor = userCollection.find();
			
			if (cursor != null) {
				
				while (cursor.hasNext()) {
					
					BasicDBObject doc = (BasicDBObject) cursor.next();
					
					User item = mapUserFromDBObject(doc);
					
					users.add(item);
					
				}
				return users;
			}
			return null;
		} catch (Exception e) {
			
		}
		
		return null;
	}

	/**
	 *
	 * @param userId
	 * @param attribute
	 * @param value
	 * @return
	 */
	public User updateUserAttribute(String userId, String attribute, String value) {
		
		String updateValue = value;
		
		BasicDBObject doc = new BasicDBObject();
		
		doc.append("$set", new BasicDBObject().append(attribute, value));
		
		DBObject searchById = new BasicDBObject("_id", new ObjectId(userId));
		
		userCollection.update(searchById, doc);
		
		return findUserById(userId);
	}

	/**
	 *
	 * @param userId
	 * @return
	 */
    public boolean delete(String userId) {
		try {
			DBObject searchById = new BasicDBObject("_id", new ObjectId(userId));
			userCollection.remove(searchById);
			return true;
		} catch (Exception e) {
			return false;
		}
    }
}
