/*
 * Copyright 2016 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.acme.security.openid.connect;

import java.io.IOException;


import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.authorization.ResourceServerRepresentation;
import org.keycloak.util.JsonSerialization;

/**
 * Tests for the partial import endpoint in admin client.  Also tests the
 * server side functionality of each resource along with "fail, skip, overwrite"
 * functions.
 *
 * @author Stan Silvert ssilvert@redhat.com (C) 2016 Red Hat Inc.
 */
public class PartialImportTest {

    private static final int NUM_RESOURCE_TYPES = 6;
    private static final String CLIENT_ROLES_CLIENT = "clientRolesClient";
    private static final String CLIENT_SERVICE_ACCOUNT = "clientServiceAccount";
    private static final String USER_PREFIX = "user";
    private static final String GROUP_PREFIX = "group";
    private static final String CLIENT_PREFIX = "client";
    private static final String REALM_ROLE_PREFIX = "realmRole";
    private static final String CLIENT_ROLE_PREFIX = "clientRole";
    private static final String[] IDP_ALIASES = {"twitter", "github", "facebook", "google", "linkedin", "microsoft", "stackoverflow"};
    private static final int NUM_ENTITIES = IDP_ALIASES.length;
    private static final ResourceServerRepresentation resourceServerSampleSettings;

    private PartialImportRepresentation piRep;
    private String realmId;

    static {
        try {
            resourceServerSampleSettings = JsonSerialization.readValue(
                PartialImportTest.class.getResourceAsStream("/import/sample-authz-partial-import.json"),
                ResourceServerRepresentation.class);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load sample resource server configuration", e);
        }
    }

    public static PartialImportRepresentation loadFile() throws Exception {
        var realmRepresentation = JsonSerialization.readValue(
                PartialImportTest.class.getResourceAsStream("/import/realm-export.json"),
                RealmRepresentation.class);

        PartialImportRepresentation partialImport = new PartialImportRepresentation();
        partialImport.setIfResourceExists(PartialImportRepresentation.Policy.SKIP.toString());
        partialImport.setClients(realmRepresentation.getClients());
        partialImport.setGroups(realmRepresentation.getGroups());
        partialImport.setIdentityProviders(realmRepresentation.getIdentityProviders());
        partialImport.setRoles(realmRepresentation.getRoles());
        partialImport.setUsers(realmRepresentation.getUsers());


        return partialImport;
    }
}
