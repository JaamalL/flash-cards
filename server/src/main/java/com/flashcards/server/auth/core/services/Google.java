package com.flashcards.server.auth.core.services;

import com.flashcards.server.auth.core.dtos.CreateProfileDto;
import com.flashcards.server.auth.core.entities.GoogleAccount;
import com.flashcards.server.auth.core.entities.User;
import com.flashcards.server.auth.core.enums.Provider;
import com.flashcards.server.auth.core.ports.repository.IAccountRepository;
import com.flashcards.server.auth.core.ports.repository.IRoleRepository;
import com.flashcards.server.auth.core.ports.repository.IUserRepository;
import com.flashcards.server.auth.core.ports.services.IGoogle;
import com.flashcards.server.auth.core.ports.services.IGoogleTokenExchanger;
import com.flashcards.server.auth.core.ports.services.IProfileCreateClient;
import com.flashcards.server.auth.core.ports.services.IVerificationSender;
import com.flashcards.server.auth.core.values.AuthResult;
import com.flashcards.server.auth.core.values.GooglePayload;

import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class Google implements IGoogle {

    private static final Logger logger = LoggerFactory.getLogger(Google.class);

    private final IGoogleTokenExchanger tokenExchanger;
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IAccountRepository<GoogleAccount> accountRepository;
    private final IVerificationSender verificationSender;
    private final IProfileCreateClient profileCreateClient;

    public Google(
            IGoogleTokenExchanger tokenExchanger,
            IUserRepository userRepository,
            IRoleRepository roleRepository,
            IAccountRepository<GoogleAccount> accountRepository,
            IVerificationSender verificationSender,
            IProfileCreateClient profileCreateClient
    ) {
        this.tokenExchanger = tokenExchanger;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.verificationSender = verificationSender;
        this.profileCreateClient = profileCreateClient;
    }

    @Override
    public AuthResult googleAuth(String code) {
        var googlePayload = tokenExchanger.exchange(code);

        return userRepository.findByEmail(googlePayload.email())
                .map(existingUser -> handleExistingUser(existingUser, googlePayload))
                .orElseGet(() -> handleNewUser(googlePayload));
    }

    private AuthResult handleExistingUser(User user, GooglePayload googlePayload) {
        var accountOpt = accountRepository.findByUserId(user.getId(), Provider.GOOGLE);

        var accountId = accountOpt.map(account -> {
            updateOrVerifyAccount(account, user, googlePayload);
            return account.getId();
        }).orElseGet(() -> createAndVerifyAccount(user, googlePayload));

        return new AuthResult(user, accountId);
    }

    private AuthResult handleNewUser(GooglePayload googlePayload) {
        var role = roleRepository.findByName("USER").orElseThrow(() -> new ApiException(
                new ApiError(HttpStatus.NOT_FOUND, "ROLE_NOT_FOUND", "role USER not found")));
        
        var user = new User(googlePayload.email());
        user.addRole(role);

        var createdUser = userRepository.create(user);

        var newAccount = new GoogleAccount(createdUser.getId(), googlePayload.sub());
        accountRepository.create(newAccount);

        var profileDto = new CreateProfileDto(
                createdUser.getId(),
                googlePayload.givenName(),
                googlePayload.familyName(),
                googlePayload.picture(),
                null,
                null);

        profileCreateClient.createProfile(profileDto);

        sendVerificationIfNeeded(createdUser, newAccount, googlePayload.emailVerified());

        return new AuthResult(createdUser, newAccount.getId());
    }

    private void updateOrVerifyAccount(GoogleAccount account, User user, GooglePayload googlePayload) {
        if (account.isVerified()) {
            return;
        }

        if (googlePayload.emailVerified()) {
            account.MarkVerified();
            accountRepository.update(account.getId(), account);
        } else {
            verificationSender.send(user, account);
        }
    }

    private UUID createAndVerifyAccount(User user, GooglePayload googlePayload) {
        var account = new GoogleAccount(user.getId(), googlePayload.sub());
        accountRepository.create(account);

        sendVerificationIfNeeded(user, account, googlePayload.emailVerified());
        return account.getId();
    }

    private void sendVerificationIfNeeded(User user, GoogleAccount account, boolean emailVerified) {
        if (emailVerified) {
            account.MarkVerified();
            accountRepository.update(account.getId(), account);
        } else {
            verificationSender.send(user, account);
        }
    }
}
