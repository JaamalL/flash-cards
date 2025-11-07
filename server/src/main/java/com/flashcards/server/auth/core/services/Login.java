package com.flashcards.server.auth.core.services;

import com.flashcards.server.auth.core.dtos.LoginDto;
import com.flashcards.server.auth.core.entities.CredentialsAccount;
import com.flashcards.server.auth.core.entities.User;
import com.flashcards.server.auth.core.enums.Provider;
import com.flashcards.server.auth.core.ports.repository.IAccountRepository;
import com.flashcards.server.auth.core.ports.repository.IUserRepository;
import com.flashcards.server.auth.core.ports.services.ILogin;
import com.flashcards.server.auth.core.values.AuthResult;
import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import com.flashcards.server.common.utils.hasher.IPasswordHasher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class Login implements ILogin {

    private final IUserRepository userRepository;
    private final IAccountRepository<CredentialsAccount> accountRepository;
    private final IPasswordHasher passwordHasher;

    public Login(
            IUserRepository userRepository,
            IAccountRepository<CredentialsAccount> accountRepository,
            IPasswordHasher passwordHasher
    ) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public AuthResult loginUserByCredentials(LoginDto dto) {
        var user = getUserByEmail(dto.email());
        var account = getCredentialsAccount(user.getId())
                .orElseThrow(() -> buildProviderException(user.getId()));

        verifyPassword(account, dto.password());
        return new AuthResult(user, account.getId());
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(new ApiError(
                        HttpStatus.NOT_FOUND,
                        "USER_NOT_FOUND",
                        "User with this email was not found"
                )));
    }

    private java.util.Optional<CredentialsAccount> getCredentialsAccount(UUID userId) {
        return accountRepository.findByUserId(userId, Provider.CREDENTIALS);
    }

    private ApiException buildProviderException(UUID userId) {
        var accounts = accountRepository.findAllAccountsByUserId(userId);
        if (accounts.length == 0) {
            throw new ApiException(new ApiError(
                    HttpStatus.NOT_FOUND,
                    "NO_ACCOUNTS",
                    "No accounts found for user, please register first"
            ));
        }

        var providers = Arrays.stream(accounts)
                .map(a -> a.getProvider().name())
                .distinct()
                .collect(Collectors.joining(", "));

        throw new ApiException(new ApiError(
                HttpStatus.BAD_REQUEST,
                "WRONG_PROVIDER",
                "Try login via provider(s): " + providers + " or connect credentials authentication method"
        ));
    }

    private void verifyPassword(CredentialsAccount account, String password) {
        if (!passwordHasher.verifyPassword(account.getHashedPassword(), password)) {
            throw new ApiException(new ApiError(
                    HttpStatus.UNAUTHORIZED,
                    "WRONG_PASSWORD",
                    "The provided password is incorrect"
            ));
        }
    }
}
