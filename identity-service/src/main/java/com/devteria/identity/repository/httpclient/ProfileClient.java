package com.devteria.identity.repository.httpclient;

import com.devteria.identity.configuration.AuthenticationRequestInterceptor;
import com.devteria.identity.dto.request.ApiResponse;
import com.devteria.identity.dto.request.ProfileCreationRequest;
import com.devteria.identity.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Feign client để gọi tới một service khác
@FeignClient(name = "profile-service", url = "${app.services.profile}",
        configuration = {AuthenticationRequestInterceptor.class})
// khi nao can request giua cac service voi nhau thi khai bao configuration
// AuthenticationRequestInterceptor được sử dụng để thêm header xác thực vào các request
public interface ProfileClient {

    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE) // produces Định nghĩa rằng request này sẽ sản xuất dữ liệu dưới dạng JSON
    ApiResponse<UserProfileResponse> createProfile(
            @RequestBody ProfileCreationRequest request);

}
