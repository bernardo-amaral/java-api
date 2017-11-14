package com.bernardo.japi;

import com.bernardo.japi.services.v1.Token;

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

public class TokensDataManager {

    private static final Logger log = Logger.getLogger(TokensDataManager.class.getName());

    private static DB japiDB;

    private static DBCollection tokenCollection;

    private static TokensDataManager INSTANCE;

    public static TokensDataManager getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new TokensDataManager();
        }

        return INSTANCE;
    }

    /**
     *
     */
    private TokensDataManager() {
        try {
            MongoCredential credential = MongoCredential.createCredential("admin", "heroku_6q2kwfv2", "admin".toCharArray());
            MongoClient mongoClient = new MongoClient(new ServerAddress("ds241875.mlab.com", 41875), Arrays.asList(credential));
            japiDB = mongoClient.getDB("heroku_6q2kwfv2");

            tokenCollection = japiDB.getCollection("tokens");
        } catch (Exception e) {
            log.error("db connection error e=", e);
        }
    }

    /**
     *
     * @param token
     * @return
     */
    public Token insertToken(Token token) {

        BasicDBObject doc = new BasicDBObject();

        doc.put("name", token.getName());
        doc.put("active", token.isActive());

        tokenCollection.insert(doc);

        token.setId(doc.getString("_id").toString());

        return token;
    }

    /**
     *
     * @param dbObject
     * @return
     */
    public Token mapTokenFromDBObject(DBObject dbObject) {

        Token token = new Token();

        token.setId((String) dbObject.get("_id").toString());

        token.setName((String) dbObject.get("name"));

        return token;
    }

    /**
     *
     * @param tokenIdString
     * @return
     */
    public Token findTokenById(String tokenIdString) {

        if (tokenIdString == null) {
            return null;
        }

        try {
            DBObject searchById = new BasicDBObject("_id", new ObjectId(tokenIdString));

            DBObject tokenObject = tokenCollection.findOne(searchById);

            if (tokenObject != null) {
                return mapTokenFromDBObject(tokenObject);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("DBManager::findTokenById Exception e=", e);
        }

        return null;
    }

    /**
     *
     * @return
     */
    public List<Token> findAllTokens() {

        List<Token> tokens = new ArrayList<Token>();

        try {

            DBCursor cursor = tokenCollection.find();

            if (cursor != null) {

                while (cursor.hasNext()) {

                    BasicDBObject doc = (BasicDBObject) cursor.next();

                    Token item = mapTokenFromDBObject(doc);

                    tokens.add(item);

                }
                return tokens;
            }
            return null;
        } catch (Exception e) {

        }

        return null;
    }

    /**
     *
     * @param tokenId
     * @param attribute
     * @param value
     * @return
     */
    public Token updateTokenAttribute(String tokenId, String attribute, String value) {

        String updateValue = value;

        BasicDBObject doc = new BasicDBObject();

        doc.append("$set", new BasicDBObject().append(attribute, value));

        DBObject searchById = new BasicDBObject("_id", new ObjectId(tokenId));

        tokenCollection.update(searchById, doc);

        return findTokenById(tokenId);
    }

    /**
     *
     * @param tokenId
     * @return
     */
    public boolean delete(String tokenId) {
        try {
            DBObject searchById = new BasicDBObject("_id", new ObjectId(tokenId));
            tokenCollection.remove(searchById);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
