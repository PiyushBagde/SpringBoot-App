package com.supermarket.gateway.config;

import com.supermarket.gateway.config.util.JWTUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (request.getURI().getPath().startsWith("/user/login") ||
                request.getURI().getPath().startsWith("/user/register")) {
            return chain.filter(exchange); // Don't block login/register
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("invalid auth header");
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isTokenValid(token)) {
            return unauthorized(exchange);
        }

        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);
        String fullRole = "ROLE_" + role;
        ServerHttpRequest modifiedRequest = exchange.getRequest()
                .mutate()
                .header("X-Authenticated_User", username)
                .header("X-Role", role)
                .build();
        System.out.println("** Token validated. User: " + username + ", Role: " + fullRole);
        return chain
                .filter(exchange
                        .mutate()
                        .request(modifiedRequest)
                        .build());
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

