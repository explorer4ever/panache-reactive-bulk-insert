package org.practice.api;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.practice.services.TenantSetupService;

import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/v1/")
public class TenantSetupApi {
  @Inject
  TenantSetupService tenantSetupService;

  @POST
  @Path("/tenant-setup")
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<Response> initializeAccountMetadata() {
    try {
      return tenantSetupService
          .initializeTenantData()
          .onItem()
          .transform(response -> Response.ok().build());
    } catch (Exception e) {
      e.printStackTrace();
      return Uni.createFrom().item(Response.status(500).build());
    }
  }
}
