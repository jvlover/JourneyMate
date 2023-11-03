package com.journeymate.apigatewayservice.filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class AuthenticationHeaderFilter extends AbstractGatewayFilterFactory<AuthenticationHeaderFilter.Config> {

    Environment env;

    public AuthenticationHeaderFilter(Environment env){
        this.env = env;
    }

    public static class Config{

    }

    @Override
    public GatewayFilter apply(AuthenticationHeaderFilter.Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, "헤더가 없어요", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(
                org.springframework.http.HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");

            if(!isJwtValid(jwt)){
                return onError(exchange, "토큰이 유효하지 않아요", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);

        });
    }
    private boolean isJwtValid(String jwt) {
        boolean res = true;

        String subject = null;

        try{
        subject = Jwts.parser().setSigningKey(env.getProperty("token.secret")).parseClaimsJws(jwt).getBody().getSubject();

        }catch (Exception ex){
            res = false;
        }
        if(subject == null || subject.isEmpty()){
            res = false;
        }

        //todo: 아이디랑 비교하는 로직

        return res;
    }

    // Mono, Flux -> Spring WebFlux 단일 값이면 Mono 다중 값이면 Flux (비동기 방식?)
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();
    }

}
