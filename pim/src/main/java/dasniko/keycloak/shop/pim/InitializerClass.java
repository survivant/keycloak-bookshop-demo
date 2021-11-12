package dasniko.keycloak.shop.pim;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


import org.eclipse.microprofile.config.inject.ConfigProperty;
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

    @ConfigProperty(name="keycloak.realm")
    String realm;

    @PostConstruct
    public void init(){

        System.out.println("Import realms");
        KeycloakRealmsHelper client = new KeycloakRealmsHelper();

        RealmRepresentation realmRepresentation = null;
        try {
            realmRepresentation = client.getRealm(realm);
        } catch(Exception e){

        }

        if(realmRepresentation==null) {
            realmRepresentation = createRealm(realm);
            realmRepresentation.setRevokeRefreshToken(true);
            realmRepresentation.setRefreshTokenMaxReuse(0);
            realmRepresentation.setAccessTokenLifespan(3);

            client.createRealm(realmRepresentation);
        }

        // import realms
        try {
            var partialImportRepresentation = KeycloakRealmsHelper.loadFile("/import/realm-export.json", PartialImportRepresentation.Policy.SKIP.toString());
            client.partialImport(realm, partialImportRepresentation);
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
