package org.acme.security.openid.connect;

import javax.inject.Inject;


import org.junit.jupiter.api.Test;

import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.Tokens;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.RestAssured;

@QuarkusTest
public class BearerTokenAuthenticationTest {

    @Inject
    OidcClient client;

    KeycloakTestClient keycloakClient = new KeycloakTestClient();

    @Test
    public void testGetCatalogue() {

        Tokens tokens = client.getTokens().await().indefinitely();

        RestAssured.given().auth().oauth2(getAccessToken("alice"))
                .when().get("/catalogue")
                .then()
                .statusCode(200);

        RestAssured.given().auth().oauth2(tokens.getAccessToken())
                .when().get("/catalogue")
                .then()
                .statusCode(200);
    }

    @Test
    public void testAllPermit() {

        RestAssured.given()
                .when().get("/catalogue2")
                .then()
                .statusCode(200);

    }

    @Test
    public void testGetItemsInCart() {

        Tokens tokens = client.getTokens().await().indefinitely();

        RestAssured.given().auth().oauth2(getAccessToken("alice"))
                .when().get("/catalogue/listItemInCart")
                .then()
                .statusCode(200);

        RestAssured.given().auth().oauth2(tokens.getAccessToken())
                .when().get("/catalogue/listItemInCart")
                .then()
                .statusCode(200);
    }

    protected String getAccessToken(String userName) {
        return keycloakClient.getAccessToken(userName, userName, "shop", "08d171bf-ba03-44db-8279-f678d68021d7");
    }
}
