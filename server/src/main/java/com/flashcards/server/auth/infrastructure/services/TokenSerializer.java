package com.flashcards.server.auth.infrastructure.services;

import com.flashcards.server.auth.core.enums.Role;
import com.flashcards.server.auth.core.values.AccessToken;
import com.flashcards.server.auth.core.values.RefreshToken;
import com.flashcards.server.auth.core.values.VerifyToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.*;

import com.flashcards.server.auth.core.ports.services.ITokenSerializer;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TokenSerializer implements ITokenSerializer
{
    private static final Logger logger = LoggerFactory.getLogger(TokenSerializer.class);

    private final String issuer;
    private final String audience;

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public TokenSerializer(
            @Value("${auth.tokens.issuer}") String issuer,
            @Value("${auth.tokens.audience}") String audience,

            JwtEncoder jwtEncoder,
            JwtDecoder jwtDecoder
    ) {
        this.issuer = issuer;
        this.audience = audience;

        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public String SerializeAccessToken(AccessToken token) {
        var issuedAt = Instant.ofEpochSecond(token.getIat());
        var expiresAt = Instant.ofEpochSecond(token.getExp());

        var claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .audience(List.of(audience))
                .subject(token.getSub().toString())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .claim("accountId", token.getAccountId().toString())
                .claim("role", token.getRole().name())
                .claim("isVerified", token.isVerified())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public AccessToken DeserializeAccessToken(String token) {
        var jwt = jwtDecoder.decode(token);

        var sub = UUID.fromString(jwt.getSubject());
        var accountId = UUID.fromString(jwt.getClaim("accountId").toString());
        var role = jwt.getClaim("role").toString();
        var isVerified = Boolean.parseBoolean(jwt.getClaim("isVerified").toString());
        long iat = jwt.getIssuedAt().getEpochSecond();
        long exp = jwt.getExpiresAt().getEpochSecond();

        return new AccessToken(sub, accountId, Role.valueOf(role), isVerified, iat, exp);
    }

    @Override
    public String SerializeRefreshToken(RefreshToken token) {
        var issuedAt = Instant.ofEpochSecond(token.getIat());
        var expiresAt = Instant.ofEpochSecond(token.getExp());

        var claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .audience(List.of(audience))
                .subject(token.getSub().toString())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .claim("id", token.getId().toString())
                .claim("accountId", token.getAccountId().toString())
                .claim("os", token.getOs())
                .claim("device", token.getDevice())
                .claim("ip", token.getIp())
                .claim("browser", token.getBrowser())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public RefreshToken DeserializeRefreshToken(String token) {
        var jwt = jwtDecoder.decode(token);

        var id = UUID.fromString(jwt.getClaim("id").toString());
        var sub = UUID.fromString(jwt.getSubject());
        var accountId = UUID.fromString(jwt.getClaim("accountId").toString());
        var os = jwt.getClaim("os").toString();
        var device = jwt.getClaim("device").toString();
        var ip = jwt.getClaim("ip").toString();
        var browser = jwt.getClaim("browser").toString();
        long iat = jwt.getIssuedAt().getEpochSecond();
        long exp = jwt.getExpiresAt().getEpochSecond();

        return new RefreshToken(id, sub, accountId, os, device, ip, browser, iat, exp);
    }

    @Override
    public String SerializeVerifyToken(VerifyToken token) {
        var issuedAt = Instant.ofEpochSecond(token.getIat());
        var expiresAt = Instant.ofEpochSecond(token.getExp());

        var claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .audience(List.of(audience))
                .subject(token.getSub().toString())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .claim("accountId", token.getAccountId().toString())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public VerifyToken DeserializeVerifyToken(String token) {
        var jwt = jwtDecoder.decode(token);

        var sub = UUID.fromString(jwt.getSubject());
        var accountId = UUID.fromString(jwt.getClaim("accountId").toString());
        long iat = jwt.getIssuedAt().getEpochSecond();
        long exp = jwt.getExpiresAt().getEpochSecond();

        return new VerifyToken(sub, accountId, iat, exp);
    }
}
