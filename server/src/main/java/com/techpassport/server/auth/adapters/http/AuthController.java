package com.techpassport.server.auth.adapters.http;

import com.techpassport.server.auth.core.dtos.LoginDto;
import com.techpassport.server.auth.core.dtos.VerificationCodeDto;
import com.techpassport.server.auth.core.ports.services.*;
import com.techpassport.server.common.utils.http.IClientInfo;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techpassport.server.auth.core.dtos.RegisterDto;

import java.time.Duration;
import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${auth.tokens.access-token.lifetime}")
    private Duration accessTokenLifetime;

    @Value("${auth.tokens.refresh-token.lifetime}")
    private Duration refreshTokenLifetime;

    private final IClientInfo clientInfo;
    private final ISession session;
    private final IRegister register;
    private final ILogin login;
    private final ILogout logout;
    private final IVerification verification;
    private final IGoogle google;

    public AuthController(
            IClientInfo clientInfo,
            ISession session,
            IRegister register,
            ILogin login,
            ILogout logout,
            IVerification verification,
            IGoogle google
    ) {
        this.clientInfo = clientInfo;
        this.session = session;
        this.register = register;
        this.login = login;
        this.logout = logout;
        this.verification = verification;
        this.google = google;
    }

    private void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
        var accessCookie = new jakarta.servlet.http.Cookie("accessToken", accessToken);
        accessCookie.setPath("/");
        accessCookie.setHttpOnly(false);
        accessCookie.setSecure(true);
        accessCookie.setMaxAge((int) accessTokenLifetime.getSeconds());
        response.addCookie(accessCookie);
    }
    private void deleteAccessTokenCookie(HttpServletResponse response) {
        var accessCookie = new jakarta.servlet.http.Cookie("accessToken", "");
        accessCookie.setPath("/");
        accessCookie.setHttpOnly(false);
        accessCookie.setSecure(true);
        accessCookie.setMaxAge(0);
        response.addCookie(accessCookie);
    }
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        var refreshCookie = new jakarta.servlet.http.Cookie("refreshToken", refreshToken);
        refreshCookie.setPath("/auth");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setMaxAge((int) refreshTokenLifetime.getSeconds());
        response.addCookie(refreshCookie);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(
            @RequestBody RegisterDto dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {

            var user = register.registerUserByCredentials(dto);

            var clientInfoDto = clientInfo.getClientInfo(request);
            var tokens = session.createSession(user, clientInfoDto);

            setAccessTokenCookie(response, tokens.accessToken());
            setRefreshTokenCookie(response, tokens.refreshToken());

            return ResponseEntity.ok("success");
        } catch (Exception ex) {
            logger.error("Failed to register user: {}", dto.email(), ex);
            return ResponseEntity.badRequest().body("failure");
        }
    }

    @PostMapping(value = "login")
    public ResponseEntity<String> loginUser(
            @RequestBody LoginDto dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            var result = login.loginUserByCredentials(dto);

            var clientInfoDto = clientInfo.getClientInfo(request);
            var tokens = session.createSession(result, clientInfoDto);

            setAccessTokenCookie(response, tokens.accessToken());
            setRefreshTokenCookie(response, tokens.refreshToken());

            return ResponseEntity.ok("success");
        }
        catch (Exception ex) {
            logger.error("Failed to login user: {}", dto.email(), ex);
            return ResponseEntity.badRequest().body("failure");
        }
    }

    @GetMapping(value = "verify/token")
    public ResponseEntity<String> verifyUser(
            @RequestParam("verifyToken") String verifyToken,
            HttpServletResponse response
    ) {
        try {
            var result = verification.verifyUserByToken(verifyToken);
            var accessToken = session.refreshSession(result.user().getId(), result.accountId());

            setAccessTokenCookie(response, accessToken);

            return ResponseEntity.ok("success");
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body("failure");
        }
    }

    @PostMapping(value = "verify/code")
    public ResponseEntity<String> verifyUser(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody VerificationCodeDto dto,
            HttpServletResponse response
    ) {
        try {
            var userId = UUID.fromString(jwt.getSubject());
            var accountId = UUID.fromString(jwt.getClaimAsString("accountId"));

            var result = verification.verifyUserByCode(userId, accountId, dto.verificationCode());
            var accessToken = session.refreshSession(result.user().getId(), result.accountId());

            setAccessTokenCookie(response, accessToken);

            return ResponseEntity.ok("success");
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body("failure");
        }
    }

    @GetMapping(value = "google/callback")
    public ResponseEntity<String> googleAuth(
            @RequestParam("code") String code,
            HttpServletRequest request,
            HttpServletResponse response
    )
    {
        try {
            var result = google.googleAuth(code);
            var clientInfoDto = clientInfo.getClientInfo(request);
            var tokens = session.createSession(result, clientInfoDto);

            setAccessTokenCookie(response, tokens.accessToken());
            setRefreshTokenCookie(response, tokens.refreshToken());

            return ResponseEntity.ok("success");
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body("failure");
        }
    }

    @PostMapping(value = "refresh")
    public ResponseEntity<String> refreshSession (
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    )
    {
        try {
            var accessToken = session.refreshSession(refreshToken);
            setAccessTokenCookie(response, accessToken);

            return ResponseEntity.ok("success");
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body("failure");
        }
    }

    @PostMapping(value = "logout")
    public ResponseEntity<String> logout (
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    )
    {
        logout.deleteSession(refreshToken);

        try {
            deleteAccessTokenCookie(response);
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body("failure");
        }

        return ResponseEntity.ok("success");
    }
}
