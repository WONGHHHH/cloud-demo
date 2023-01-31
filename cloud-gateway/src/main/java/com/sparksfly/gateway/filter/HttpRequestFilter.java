package com.sparksfly.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * @author wangh
 * @desc: 自定义拦截器
 */
@Configuration
public class HttpRequestFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String path = serverHttpRequest.getURI().getPath();
        String url = null;
        try {
            url = path.split("/")[3];
        } catch (Exception e) {
            // nothing
        }
        /* 参数校验 */
        this.checkParams(serverHttpRequest.getQueryParams());
        /* 校验请求token是否过期 */
        if (this.isCheckToken(url)) {
            this.checkToken(serverHttpRequest);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 参数校验
     *
     * @param params 参数Map
     */
    private void checkParams(MultiValueMap<String, String> params) {
        /* 非法参数校验  */
    }

    private boolean isCheckToken(String path) {
        /* 判断当前请求路径是否需要校验token */
        return false;
    }

    private void checkToken(ServerHttpRequest serverHttpRequest) {
        /* 校验token是否正确 (token存放在请求头中,取出对应请求头即可) */
    }
}
