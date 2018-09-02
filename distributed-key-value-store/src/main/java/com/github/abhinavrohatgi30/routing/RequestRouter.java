package com.github.abhinavrohatgi30.routing;

import com.github.abhinavrohatgi30.model.RequestPayload;


public interface RequestRouter {

    String routeGetRequest(RequestPayload requestPayload);
    String routePutRequest(RequestPayload requestPayload);
}
