package dasniko.keycloak.shop.pim;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.RolesRepresentation;

import io.quarkus.runtime.Startup;

@ApplicationScoped
@Startup(0)
public class InitializerClass {

    @Inject
    CatalogueService catalogueService;

    @PostConstruct
    public void init(){

        System.out.println("Import realms");
        String KEYCLOAK_REALM = "quarkus2";
        KeycloakRealmsHelper client = new KeycloakRealmsHelper();

        RealmRepresentation realm =null;
        try {
            realm = client.getRealm(KEYCLOAK_REALM);
        } catch(Exception e){

        }

        if(realm==null) {
            realm = createRealm(KEYCLOAK_REALM);
            realm.setRevokeRefreshToken(true);
            realm.setRefreshTokenMaxReuse(0);
            realm.setAccessTokenLifespan(3);

            client.createRealm(realm);
        }

        // import realms
        try {
            var partialImportRepresentation = KeycloakRealmsHelper.loadFile("/import/realm-export.json", PartialImportRepresentation.Policy.SKIP.toString());
            client.partialImport(KEYCLOAK_REALM, partialImportRepresentation);
        } catch (Exception e) {

        }


        System.out.println("list items = " + catalogueService.listItemInCart());

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
}
