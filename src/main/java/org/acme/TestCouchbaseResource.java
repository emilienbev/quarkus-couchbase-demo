package org.acme;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/couchbase")
public class TestCouchbaseResource {
    @Inject
    Cluster cluster;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/test")
    public String run() {
        // Get a reference to a particular Couchbase bucket and its default collection
        var bucket = cluster.bucket("default");
        var collection = bucket.defaultCollection() ;

        // Upsert a new document
        collection.upsert("test", JsonObject.create().put("foo", "bar"));

        // Fetch and print a document
        var doc = bucket.defaultCollection().get("test");
        System.out.println("Got doc " + doc.contentAsObject().toString());

        // Perform a N1QL query
        var queryResult = cluster.query("select * from `default`.`_default`.`_default` limit 10");

        queryResult.rowsAsObject().forEach(row -> {
            System.out.println(row.toString());
        });

        return "Success!";
    }
}