package com.techpassport.server.auth.core.services;

import com.techpassport.server.auth.core.ports.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techpassport.server.auth.core.dtos.RegisterDto;
import com.techpassport.server.auth.core.entities.User;
import com.techpassport.server.auth.core.entities.CredentialsAccount;
import com.techpassport.server.auth.core.enums.Provider;
import com.techpassport.server.auth.core.ports.repository.IAccountRepository;
import com.techpassport.server.auth.core.ports.repository.IUserRepository;
import com.techpassport.server.common.utils.hasher.IPasswordHasher;
import com.techpassport.server.auth.core.dtos.CreateProfileDto;
import com.techpassport.server.auth.core.values.AuthResult;

@Service
public class Register implements IRegister {

    private static final Logger logger = LoggerFactory.getLogger(Register.class);

    private final IUserRepository userRepository;
    private final IAccountRepository<CredentialsAccount> accountRepository;
    private final IPasswordHasher passwordHasher;
    private final ICreateProfileClient createProfileClient;
    private final IVerificationSender verificationSender;

    public Register(
            IUserRepository userRepository,
            IAccountRepository<CredentialsAccount> accountRepository,
            IPasswordHasher passwordHasher,
            ICreateProfileClient createProfileClient,
            IVerificationSender verificationSender
    ) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordHasher = passwordHasher;
        this.createProfileClient = createProfileClient;
        this.verificationSender = verificationSender;
    }

    @Override
    @Transactional
    public AuthResult registerUserByCredentials(RegisterDto dto) {
        try {
            return userRepository.findByEmail(dto.email())
                    .map(existingUser -> handleExistingUser(existingUser, dto))
                    .orElseGet(() -> handleNewUser(dto));
        } catch (Exception ex) {
            logger.error("Error creating user by credentials: {}", dto.email(), ex);
            throw ex;
        }
    }

    private AuthResult handleExistingUser(User user, RegisterDto dto) {
        var accountOpt = accountRepository.findByUserId(user.getId(), Provider.CREDENTIALS);

        if (accountOpt.isPresent()) {
            throw new IllegalStateException("User with these credentials already exists: " + dto.email());
        }

        var hashedPassword = passwordHasher.hashPassword(dto.password());
        var newAccount = new CredentialsAccount(user.getId(), hashedPassword);
        accountRepository.create(newAccount);

        verificationSender.send(user, newAccount);
        return new AuthResult(user, newAccount.getId());
    }

    private AuthResult handleNewUser(RegisterDto dto) {
        var createdUser = userRepository.create(new User(dto.email()));

        var hashedPassword = passwordHasher.hashPassword(dto.password());
        var newAccount = new CredentialsAccount(createdUser.getId(), hashedPassword);
        accountRepository.create(newAccount);

        createProfileClient.createProfile(new CreateProfileDto(
                createdUser.getId(), null, null, null, null, null
        ));

        verificationSender.send(createdUser, newAccount);
        return new AuthResult(createdUser, newAccount.getId());
    }
}