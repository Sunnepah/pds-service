package com.sunnepah.savewithme.db;

import com.mongodb.DB;
import com.mongodb.MongoURI;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Set;

/**
 * Created by sunnepah on 03/11/2016.
 * Sunday Ayandokun @sundayayandokun
 */
public class MongoDAO {

    public void sampleMongo() throws URISyntaxException {

        MongoURI mongoURI = new MongoURI(System.getenv("MONGOHQ_URL"));

        DB db = null;

        try {

            db = mongoURI.connectDB();
            db.authenticate(mongoURI.getUsername(), mongoURI.getPassword());

        } catch (UnknownHostException e) {
            throw new RuntimeException("Unknown host");
        }

        //Use the db object to talk to MongoDB
        Set<String> colls = db.getCollectionNames();
    }
}
