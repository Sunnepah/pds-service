package com.sunnepah.pdsservice.db;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.sunnepah.pdsservice.core.FacebookUser;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.mongojack.JacksonDBCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by sunnepah on 22/11/2016.
 * Sunday Ayandokun @sundayayandokun
 */
public class UserRepository implements Repository {

    private final static Logger LOG = LoggerFactory.getLogger(UserRepository.class);
    private static final String COLLECTION_NAME = "users";

    private DB database;
    private Jongo jongo;
    private DBCollection collection;

    @Inject
    public UserRepository(DB database, Jongo jongo) {
        this.database = database;
        this.jongo = jongo;
        this.collection = database.getCollection(COLLECTION_NAME);
    }

    @Override
    public void saveUser(FacebookUser facebookUser) {
        ObjectId id = new ObjectId();
        LOG.info("New User document id generated: {}", id.toString());

        JacksonDBCollection<FacebookUser, String> coll = JacksonDBCollection.wrap(collection, FacebookUser.class, String.class);

        coll.insert(facebookUser);
    }
}
