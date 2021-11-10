package dasniko.keycloak.shop.pim;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;


import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.quarkus.oidc.client.filter.OidcClientFilter;

/**
 * @author Niko Köbler, https://www.n-k.de, @dasniko
 */
@ApplicationScoped
@RegisterRestClient
@OidcClientFilter
@Consumes(MediaType.APPLICATION_JSON)
public interface CheckoutClient {

    @GET
    @Path("/checkout")
    String listItemInCart();
}
