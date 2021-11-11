package dasniko.keycloak.shop.pim;

import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.util.JsonSerialization;

import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.RestAssured;

public class KeycloakRealmsHelper extends KeycloakTestClient {


    public static PartialImportRepresentation loadFile(String filename, String policyIfResourceExists) throws Exception {
        var realmRepresentation = JsonSerialization.readValue(KeycloakRealmsHelper.class.getResourceAsStream(filename), RealmRepresentation.class);

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
