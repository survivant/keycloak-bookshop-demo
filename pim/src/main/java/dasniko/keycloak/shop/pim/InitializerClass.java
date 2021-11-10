package dasniko.keycloak.shop.pim;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


import io.quarkus.runtime.Startup;

@ApplicationScoped
@Startup(0)
public class InitializerClass {

    @Inject
    CatalogueService catalogueService;

    @PostConstruct
    public void init(){
        System.out.println("list items = " + catalogueService.listItemInCart());
    }
}
