package com.github.abhinavrohatgi30.routing;

import com.github.abhinavrohatgi30.model.RequestPayload;


public interface RequestRouter {

    String routeGetRequest(RequestPayload requestPayload);
    int routePutRequest(RequestPayload requestPayload);
}
