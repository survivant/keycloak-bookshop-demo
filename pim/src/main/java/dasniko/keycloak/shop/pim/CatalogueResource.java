package dasniko.keycloak.shop.pim;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Niko Köbler, https://www.n-k.de, @dasniko
 */
@Path("catalogue")
@Produces(MediaType.APPLICATION_JSON)
public class CatalogueResource {

    @Inject
    CatalogueService catalogueService;

    @GET
    @RolesAllowed("serviceAccount") // only a user with this role can have access
    public List<Book> getBooks() {
        return catalogueService.getBooks();
    }

    @GET
    @Path("listItemInCart")
    @RolesAllowed({"serviceAccount","user"}) // only a user with this role can have access
    public String listItemInCart() {
        return catalogueService.listItemInCart();
    }
}
