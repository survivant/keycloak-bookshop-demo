package org.acme.security.openid.connect;

import java.util.List;


import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.util.JsonSerialization;

import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.RestAssured;

public class KeycloakTestClient2 extends KeycloakTestClient {


    public static PartialImportRepresentation loadFile(String filename, String policyIfResourceExists) throws Exception {
        var realmRepresentation = JsonSerialization.readValue(KeycloakTestClient2.class.getResourceAsStream(filename), RealmRepresentation.class);

        var partialImport = new PartialImportRepresentation();
        partialImport.setIfResourceExists(policyIfResourceExists);
        partialImport.setClients(realmRepresentation.getClients());
        partialImport.setGroups(realmRepresentation.getGroups());
        partialImport.setIdentityProviders(realmRepresentation.getIdentityProviders());
        partialImport.setRoles(realmRepresentation.getRoles());
        partialImport.setUsers(realmRepresentation.getUsers());

        return partialImport;
    }

    /**
     * Get a realm
     */
    public RealmRepresentation getRealm(String realm) {
        return RestAssured
                .given()
                .auth().oauth2(getAdminAccessToken())
                .when()
                .get(getAuthServerBaseUrl() + "/admin/realms/" + realm)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(RealmRepresentation.class);
    }

    public RealmRepresentation loadClients(RealmRepresentation realm) {
        List<ClientRepresentation> clients = RestAssured
                .given()
                .auth().oauth2(getAdminAccessToken())
                .when()
                .get(getAuthServerBaseUrl() + "/admin/realms/" + realm.getRealm() + "/clients")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ClientRepresentation.class);

        realm.setClients(clients);
        return realm;
    }

    public RealmRepresentation loadUsers(RealmRepresentation realm) {
        List<UserRepresentation> users = RestAssured
                .given()
                .auth().oauth2(getAdminAccessToken())
                .when()
                .get(getAuthServerBaseUrl() + "/admin/realms/" + realm.getRealm() + "/users")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", UserRepresentation.class);

        realm.setUsers(users);
        return realm;
    }


    public void partialImport(String realm, PartialImportRepresentation partialImportRepresentation) {

        RestAssured
                .given()
                .auth().oauth2(getAdminAccessToken())
                .when()
                .accept("application/json, text/plain, */*")
                .contentType("application/json; charset=UTF-8")
                .body(partialImportRepresentation)
                .post(getAuthServerBaseUrl() + "/admin/realms/" + realm + "/partialImport")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
    }
}
