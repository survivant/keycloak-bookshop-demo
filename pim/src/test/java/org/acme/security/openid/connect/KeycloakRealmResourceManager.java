package org.acme.security.openid.connect;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;


import org.keycloak.representations.idm.*;

import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

@ApplicationScoped
public class KeycloakRealmResourceManager implements QuarkusTestResourceLifecycleManager, DevServicesContext.ContextAware {

    private static final String KEYCLOAK_REALM = "quarkus2";
    final KeycloakTestClient2 client = new KeycloakTestClient2();

    @Override
    public Map<String, String> start() {
        System.out.println("START");

        var realm = client.getRealm(KEYCLOAK_REALM);

        if(realm==null) {
            realm = createRealm(KEYCLOAK_REALM);
            realm.setRevokeRefreshToken(true);
            realm.setRefreshTokenMaxReuse(0);
            realm.setAccessTokenLifespan(3);

            client.createRealm(realm);
        }

        // import realms
        try {
            var partialImportRepresentation = KeycloakTestClient2.loadFile("/import/realm-export.json", PartialImportRepresentation.Policy.SKIP.toString());
            client.partialImport(KEYCLOAK_REALM, partialImportRepresentation);
        } catch (Exception e) {

        }

        return Collections.emptyMap();
    }



    private RealmRepresentation createRealm(String name) {
        RealmRepresentation realm = new RealmRepresentation();

        realm.setRealm(name);
        realm.setEnabled(true);
        realm.setUsers(new ArrayList<>());
        realm.setClients(new ArrayList<>());
        realm.setAccessTokenLifespan(3);

        RolesRepresentation roles = new RolesRepresentation();
        List<RoleRepresentation> realmRoles = new ArrayList<>();

        roles.setRealm(realmRoles);
        realm.setRoles(roles);

        realm.getRoles().getRealm().add(new RoleRepresentation("user", null, false));
        realm.getRoles().getRealm().add(new RoleRepresentation("admin", null, false));

        return realm;
    }

    private ClientRepresentation createClient(String clientId) {
        ClientRepresentation client = new ClientRepresentation();

        client.setClientId(clientId);
        client.setPublicClient(false);
        client.setSecret("secret");
        client.setDirectAccessGrantsEnabled(true);
        client.setServiceAccountsEnabled(true);
        client.setEnabled(true);

        return client;
    }

    private UserRepresentation createUser(String username, String... realmRoles) {
        UserRepresentation user = new UserRepresentation();

        user.setUsername(username);
        user.setEnabled(true);
        user.setCredentials(new ArrayList<>());
        user.setRealmRoles(Arrays.asList(realmRoles));
        user.setEmail(username + "@gmail.com");

        CredentialRepresentation credential = new CredentialRepresentation();

        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(username);
        credential.setTemporary(false);

        user.getCredentials().add(credential);

        return user;
    }

    @Override
    public void stop() {
        client.deleteRealm(KEYCLOAK_REALM);
    }

    @Override
    public void setIntegrationTestContext(DevServicesContext context) {
        client.setIntegrationTestContext(context);
    }
}
