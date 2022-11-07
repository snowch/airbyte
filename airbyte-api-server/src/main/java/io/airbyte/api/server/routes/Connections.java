/*
 * Copyright (c) 2022 Airbyte, Inc., all rights reserved.
 */

package io.airbyte.api.server.routes;

import dev.speakeasyapi.micronaut.EnableSpeakeasy;
import dev.speakeasyapi.sdk.SpeakeasyMiddlewareController;

import io.airbyte.api.server.generated.ConnectionApi;
import io.airbyte.api.server.model.generated.Connection;
import io.airbyte.api.server.model.generated.ConnectionCreate;
import io.airbyte.api.server.model.generated.ConnectionList;
import io.airbyte.api.server.model.generated.Identifier;
import io.airbyte.api.server.repositories.ConnectionsRepository;
import java.util.UUID;
import javax.ws.rs.Path;

import io.micronaut.http.annotation.RequestAttribute;
import lombok.extern.slf4j.Slf4j;

@Path("/connections")
@Slf4j
@EnableSpeakeasy
public class Connections implements ConnectionApi {

  final ConnectionsRepository connectionsRepository;

  Connections(final ConnectionsRepository connectionsRepository) {
    this.connectionsRepository = connectionsRepository;
  }

  @Override
  public Connection createConnection(final ConnectionCreate connectionCreate) {
    return null;
  }

  @Override
  public ConnectionList listConnectionsForWorkspace(final Identifier identifiers, final UUID workspaceId, final String slug, final String cursor) {
    return null;
  }

  @Override
  public void resetConnection(final UUID connectionId, @RequestAttribute(SpeakeasyMiddlewareController.Key) SpeakeasyMiddlewareController ctrl) {
    connectionsRepository.reset(connectionId);
  }

  @Override
  public void syncConnection(final UUID connectionId, final String xEndpointAPIUserInfo, @RequestAttribute(SpeakeasyMiddlewareController.Key) SpeakeasyMiddlewareController ctrl) {
    connectionsRepository.sync(connectionId, xEndpointAPIUserInfo);
  }

}
