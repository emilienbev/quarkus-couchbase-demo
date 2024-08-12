package org.acme;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.Duration;
import java.util.UUID;

@Path("/couchbase")
public class CouchbaseResource {
    @Inject
    Cluster cluster;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/test")
    public String test(){
        var bucket = cluster.bucket("default");
        var collection = bucket.defaultCollection();

        var docId = "quarkusKv_" + UUID.randomUUID();

        // Upsert a new document
        collection.upsert(docId, JsonObject.create().put("foo", "bar"));

        // Fetch and print a document
        var doc = collection.get(docId);

        return "Success!";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/kv")
    public String kvDemo() {
        // Assumes you have a Couchbase server running, with a bucket named "default"
        // Gets a reference to a particular Couchbase bucket and its default collection
        var bucket = cluster.bucket("default");
        var collection = bucket.defaultCollection();

        var docId = "quarkusKv_" + UUID.randomUUID();

        // Upsert a new document
        collection.upsert(docId, JsonObject.create().put("foo", "bar"));

        // Fetch and print a document
        var doc = collection.get(docId);

        return "Got doc " + doc.contentAsObject().toString();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/query")
    public String queryDemo() {

        var bucket = cluster.bucket("default");
        var collection = bucket.defaultCollection() ;

        // Upsert a new document
        for (int i = 0; i <= 10; i++){
            var docId = "quarkusQuery_" + UUID.randomUUID();
            collection.upsert(docId + i, JsonObject.create().put("hello", "world"));
        }


        var queryResult = cluster.query("select META().id, hello from `default`.`_default`.`_default` where META().id like 'quarkusQuery_%' limit 10");

        StringBuilder concatenatedString = new StringBuilder();
        for (var obj : queryResult.rowsAsObject()) {
            concatenatedString.append(obj.toString());
            concatenatedString.append("\n");
        }

        return concatenatedString.toString();
    }
}