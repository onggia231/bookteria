package com.devteria.gateway.configuration;

import com.devteria.gateway.dto.ApiResponse;
import com.devteria.gateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true) // cho truy cap trong cung goi
public class AuthenticationFilter implements GlobalFilter, Ordered { // để cấu hình và tùy chỉnh hành vi của các bộ lọc trong gateway

    IdentityService identityService; // check xem Introspect co dung token khong
    ObjectMapper objectMapper; // chuyển đổi giữa các đối tượng Java và các định dạng JSON

    // list endpoint khong can authentication
    @NonFinal
    private String[] publicEndpoints = {
            "/identity/auth/.*",
            "/identity/users/registration",
            "/notification/email/send"};

    @Value("${app.api-prefix}")
    @NonFinal
    private String apiPrefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // ServerWebExchange: Đại diện cho yêu cầu và phản hồi HTTP trong Spring Cloud Gateway.
        // GatewayFilterChain: Cho phép chuyển yêu cầu tiếp tục qua các bộ lọc và cuối cùng đến dịch vụ đích.

        log.info("Enter authentication filter...");

        // Kiểm tra xem yêu cầu có phải là đến một endpoint công khai không. Nếu đúng, yêu cầu sẽ được chuyển tiếp đến dịch vụ đích mà không cần xác thực.
        if (isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange); // Nếu endpoint là công khai, bỏ qua bộ lọc và tiếp tục xử lý yêu cầu.


        // Get token from authorization header
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION); // Lấy tiêu đề Authorization từ yêu cầu HTTP. Tiêu đề này thường chứa token JWT.
        if (CollectionUtils.isEmpty(authHeader)) // Nếu không có tiêu đề, yêu cầu sẽ bị coi là không được xác thực và trả về phản hồi không được xác thực.
            return unauthenticated(exchange.getResponse());

        // Lấy token từ tiêu đề Authorization (giả định là token có dạng Bearer token). Bearer sẽ được loại bỏ để lấy token thực tế.
        String token = authHeader.stream().findFirst().get().replace("Bearer ", "");
        log.info("Token: {}", token);

        // check xem Introspect co dung token khong
//        identityService.introspect(token).subscribe(introspectResponseApiResponse -> {
//            log.info("Introspect Response: {}", introspectResponseApiResponse.getResult().isValid());
//        });

        // Gọi dịch vụ xác thực (có thể là dịch vụ xác thực JWT) để kiểm tra tính hợp lệ của token
        return identityService.introspect(token).flatMap(introspectResponse -> {
            if (introspectResponse.getResult().isValid()) // Nếu token hợp lệ
                return chain.filter(exchange); // yêu cầu sẽ được chuyển tiếp đến dịch vụ đích
            else
                return unauthenticated(exchange.getResponse());
        }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) { // ServerHttpRequest đại diện cho yêu cầu HTTP hiện tại.

        // request.getURI().getPath(): Lấy đường dẫn (path) của URI từ yêu cầu HTTP. Ví dụ: /api/v1/users/123.
        // Đây là tiền tố (prefix) của API, có thể được cấu hình trong ứng dụng. Ví dụ: /api/v1
        // Chuyển đổi mảng publicEndpoints thành một stream để thực hiện các phép toán trên từng phần tử của mảng.
        // anyMatch: Kiểm tra xem có bất kỳ phần tử nào trong stream thỏa mãn điều kiện không.
        return Arrays.stream(publicEndpoints)
                .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
    }

    // để xử lý các yêu cầu không được xác thực (unauthenticated requests)
    Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1401)
                .message("Unauthenticated")
                .build();

        String body = null;
        try {
            // Sử dụng ObjectMapper để chuyển đối tượng ApiResponse thành chuỗi JSON
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
