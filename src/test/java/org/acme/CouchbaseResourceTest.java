package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class CouchbaseResourceTest {
    @Test
    void testTestEndpoint() {
        given()
                .when().get("/couchbase/test")
                .then()
                .statusCode(200)
                .body(is("Success!"));
    }
    @Test
    void testKvEndpoint() {
        given()
                .when().get("/couchbase/kv")
                .then()
                .statusCode(200)
                .body(is("Got doc {\"foo\":\"bar\"}"));
    }
}