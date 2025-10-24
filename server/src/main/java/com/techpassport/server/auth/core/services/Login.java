package com.techpassport.server.auth.core.services;

import com.techpassport.server.auth.core.dtos.LoginDto;
import com.techpassport.server.auth.core.entities.CredentialsAccount;
import com.techpassport.server.auth.core.entities.User;
import com.techpassport.server.auth.core.enums.Provider;
import com.techpassport.server.auth.core.ports.repository.IAccountRepository;
import com.techpassport.server.auth.core.ports.repository.IUserRepository;
import com.techpassport.server.auth.core.ports.services.ILogin;
import com.techpassport.server.auth.core.values.AuthResult;
import com.techpassport.server.common.exceptions.NotFoundException;
import com.techpassport.server.common.utils.hasher.IPasswordHasher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class Login implements ILogin {

    private static final Logger log = LoggerFactory.getLogger(Login.class);

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
        try {
            var user = getUserByEmail(dto.email());
            var account = getCredentialsAccount(user.getId())
                    .orElseThrow(() -> buildProviderException(user.getId()));

            verifyPassword(account, dto.password());
            return new AuthResult(user, account.getId());

        } catch (Exception ex) {
            log.error("Error login by credentials: {}", dto.email(), ex);
            throw ex;
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private java.util.Optional<CredentialsAccount> getCredentialsAccount(UUID userId) {
        return accountRepository.findByUserId(userId, Provider.CREDENTIALS);
    }

    private NotFoundException buildProviderException(UUID userId) {
        var accounts = accountRepository.findAllAccountsByUserId(userId);
        if (accounts.length == 0) {
            return new NotFoundException("No accounts found for user, please register first");
        }

        var providers = Arrays.stream(accounts)
                .map(a -> a.getProvider().name())
                .distinct()
                .collect(Collectors.joining(", "));

        return new NotFoundException(
                "Try login via provider(s): " + providers + " or connect credentials authentication method"
        );
    }

    private void verifyPassword(CredentialsAccount account, String password) {
        if (!passwordHasher.verifyPassword(account.getHashedPassword(), password)) {
            throw new IllegalArgumentException("Wrong password");
        }
    }
}

