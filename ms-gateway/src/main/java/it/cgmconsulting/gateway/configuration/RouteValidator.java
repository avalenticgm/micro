package it.cgmconsulting.gateway.configuration;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {

    // v0 -> no token
    // v1 -> ADMIN
    // v2 -> WRITER
    // v3 -> MEMBER
    // v4 -> MODERATOR
    // ETC...

    public boolean isOpenEndpoint(ServerHttpRequest req){
        return req.getURI().getPath().contains("v0");
    }
}
