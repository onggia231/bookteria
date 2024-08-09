package com.devteria.identity.configuration;

import com.devteria.identity.service.AuthenticationService;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;

    private final AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    public CustomJwtDecoder(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // Phương thức decode được triển khai từ giao diện JwtDecoder, có trách nhiệm giải mã token JWT
    @Override
    public Jwt decode(String token) throws JwtException {

        // Doan check token nay da duoc check o api-gateway nen khong can nua
//        try {
//            var response = authenticationService.introspect(
//                    IntrospectRequest.builder().token(token).build());
//
//            if (!response.isValid()) throw new JwtException("Token invalid");
//        } catch (JOSEException | ParseException e) {
//            throw new JwtException(e.getMessage());
//        }
//
//        if (Objects.isNull(nimbusJwtDecoder)) {
//            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
//            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
//                    .macAlgorithm(MacAlgorithm.HS512)
//                    .build();
//        }
//
//        return nimbusJwtDecoder.decode(token);

        try {

            //Phương thức cố gắng parse token JWT từ chuỗi đầu vào. SignedJWT là một lớp từ thư viện Nimbus JOSE + JWT, được sử dụng để xử lý các token JWT đã ký.
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Nếu việc parse thành công, phương thức sẽ lấy các thông tin từ token đã parse (như thời gian phát hành, thời gian hết hạn, header, và các claims)
            // và sử dụng các thông tin này để tạo một đối tượng Jwt từ Spring Security
            return new Jwt(token, // token: Chuỗi token gốc.
                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(), // issueTime: Thời gian phát hành token (dạng Instant).
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(), // expirationTime: Thời gian hết hạn của token (dạng Instant).
                    signedJWT.getHeader().toJSONObject(), // header: Các header của token (dạng JSON).
                    signedJWT.getJWTClaimsSet().getClaims()); // claims: Các claims (payload) của token.
        } catch (ParseException e) {
            throw new JwtException("Invalid token");
        }

    }
}
