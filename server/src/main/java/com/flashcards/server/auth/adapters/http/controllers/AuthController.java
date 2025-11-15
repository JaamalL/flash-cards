package com.flashcards.server.auth.adapters.http.controllers;

import com.flashcards.server.auth.adapters.http.handlers.*;
import com.flashcards.server.auth.core.dtos.LoginDto;
import com.flashcards.server.auth.core.dtos.VerificationCodeDto;
import com.flashcards.server.common.annotation.Authorize;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.flashcards.server.auth.core.dtos.RegisterDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    private final RegisterHandler registerHandler;
    private final LoginHandler loginHandler;
    private final VerifyTokenHandler verifyTokenHandler;
    private final VerifyCodeHandler verifyCodeHandler;
    private final GoogleAuthHandler googleAuthHandler;
    private final LogoutHandler logoutHandler;
    private final RefreshHandler refreshHandler;

    public AuthController(
            RegisterHandler registerHandler,
            LoginHandler loginHandler,
            VerifyTokenHandler verifyTokenHandler,
            VerifyCodeHandler verifyCodeHandler,
            GoogleAuthHandler googleAuthHandler,
            LogoutHandler logoutHandler,
            RefreshHandler refreshHandler
    ) {
        this.registerHandler = registerHandler;
        this.loginHandler = loginHandler;
        this.verifyTokenHandler = verifyTokenHandler;
        this.verifyCodeHandler = verifyCodeHandler;
        this.googleAuthHandler = googleAuthHandler;
        this.logoutHandler = logoutHandler;
        this.refreshHandler = refreshHandler;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto dto, HttpServletRequest req, HttpServletResponse res) {
        return registerHandler.handle(dto, req, res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto dto, HttpServletRequest req, HttpServletResponse res) {
        return loginHandler.handle(dto, req, res);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        return refreshHandler.handle(refreshToken);
    }

    @GetMapping("/verify/token")
    public RedirectView verifyToken(@RequestParam("verifyToken") String token, HttpServletRequest req, HttpServletResponse res) {
        return verifyTokenHandler.handle(token, req, res);
    }

    @Authorize(allowUnverified = true)
    @PostMapping("/verify/code")
    public ResponseEntity<?> verifyCode(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid VerificationCodeDto dto, HttpServletResponse res) {
        return verifyCodeHandler.handle(jwt, dto, res);
    }

    @GetMapping("/google/callback")
    public RedirectView google(@RequestParam("code") String code, HttpServletRequest req, HttpServletResponse res) {
        return googleAuthHandler.handle(code, req, res);
    }

    @Authorize(allowUnverified = true)
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refreshToken", required = false) String refreshToken, HttpServletResponse res) {
        return logoutHandler.handle(refreshToken, res);
    }
}