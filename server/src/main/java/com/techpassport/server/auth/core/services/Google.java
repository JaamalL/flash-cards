package com.techpassport.server.auth.core.services;

import com.techpassport.server.auth.core.entities.GoogleAccount;
import com.techpassport.server.auth.core.entities.User;
import com.techpassport.server.auth.core.enums.Provider;
import com.techpassport.server.auth.core.ports.repository.IAccountRepository;
import com.techpassport.server.auth.core.ports.repository.IUserRepository;
import com.techpassport.server.auth.core.ports.services.*;
import com.techpassport.server.auth.core.values.*;
import com.techpassport.server.auth.core.dtos.CreateProfileDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class Google implements IGoogle {

    private static final Logger logger = LoggerFactory.getLogger(Google.class);

    private final IGoogleTokenExchanger tokenExchanger;
    private final IUserRepository userRepository;
    private final IAccountRepository<GoogleAccount> accountRepository;
    private final ICreateProfileClient createProfileClient;
    private final IVerificationSender verificationSender;

    public Google(
            IGoogleTokenExchanger tokenExchanger,
            IUserRepository userRepository,
            IAccountRepository<GoogleAccount> accountRepository,
            ICreateProfileClient createProfileClient,
            IVerificationSender verificationSender
    ) {
        this.tokenExchanger = tokenExchanger;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.createProfileClient = createProfileClient;
        this.verificationSender = verificationSender;
    }

    @Override
    public AuthResult googleAuth(String code) {
        var googlePayload = tokenExchanger.exchange(code);

        try {
            return userRepository.findByEmail(googlePayload.email())
                    .map(existingUser -> handleExistingUser(existingUser, googlePayload))
                    .orElseGet(() -> handleNewUser(googlePayload));
        } catch (Exception ex) {
            logger.error("Error creating user by Google: {}", googlePayload.email(), ex);
            throw ex;
        }
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
        var createdUser = userRepository.create(new User(googlePayload.email()));

        var newAccount = new GoogleAccount(createdUser.getId(), googlePayload.sub());
        accountRepository.create(newAccount);

        createProfileClient.createProfile(new CreateProfileDto(
                createdUser.getId(),
                googlePayload.givenName(),
                googlePayload.familyName(),
                googlePayload.picture(),
                null,
                null
        ));

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
