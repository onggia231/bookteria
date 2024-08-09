package com.devteria.identity.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

// Class duoc sinh ra de giai quyet van de: Khi gọi api create User thi trong ham co call tiep den api tao Profile
// Khi tao Profile no se bao loi khong co quyen
// De giai quyet van de nay tao AuthenticationRequestInterceptor đảm bảo rằng mọi yêu cầu HTTP gửi đi đều có header "Authorization"
// Muon dung thi class @FeignClien can khai configuration la AuthenticationRequestInterceptor de su dung
@Slf4j
public class AuthenticationRequestInterceptor implements RequestInterceptor { // RequestInterceptor để chặn và chỉnh sửa các yêu cầu HTTP trước khi chúng được gửi đi
    @Override
    public void apply(RequestTemplate template) {

        // Sử dụng RequestContextHolder để lấy các thuộc tính yêu cầu hiện tại từ ngữ cảnh của Spring.
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // Từ các thuộc tính yêu cầu, lấy giá trị của header "Authorization".
        var authHeader = servletRequestAttributes.getRequest().getHeader("Authorization");

        log.info("Header: {}", authHeader);

        // Nếu header "Authorization" có giá trị (không phải null hoặc rỗng), thì thêm header này vào RequestTemplate.
        if (StringUtils.hasText(authHeader))
            template.header("Authorization", authHeader);
    }
}
/*
Mục đích: Mục đích chính của AuthenticationRequestInterceptor là đảm bảo rằng mọi yêu cầu HTTP gửi đi đều có header "Authorization"
nếu header này tồn tại trong yêu cầu HTTP hiện tại.
Điều này thường được sử dụng trong các ứng dụng nơi mà các yêu cầu cần phải bao gồm token xác thực để truy cập các tài nguyên được bảo vệ.

Áp dụng trong Feign Client: Interceptor này thường được sử dụng trong các ứng dụng sử dụng Feign Client để thực hiện các yêu cầu RESTful.
Bằng cách thêm AuthenticationRequestInterceptor, mỗi yêu cầu Feign sẽ tự động bao gồm header "Authorization" nếu có,
giúp đơn giản hóa việc quản lý xác thực trong ứng dụng.
* */
