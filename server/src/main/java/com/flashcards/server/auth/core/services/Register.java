package com.flashcards.server.auth.core.services;

import com.flashcards.server.auth.core.dtos.CreateProfileDto;
import com.flashcards.server.auth.core.ports.repository.IRoleRepository;
import com.flashcards.server.auth.core.ports.services.IProfileCreateClient;
import com.flashcards.server.auth.core.ports.services.IRegister;
import com.flashcards.server.auth.core.ports.services.IVerificationSender;
import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.flashcards.server.auth.core.dtos.RegisterDto;
import com.flashcards.server.auth.core.entities.User;
import com.flashcards.server.auth.core.entities.CredentialsAccount;
import com.flashcards.server.auth.core.enums.Provider;
import com.flashcards.server.auth.core.ports.repository.IAccountRepository;
import com.flashcards.server.auth.core.ports.repository.IUserRepository;
import com.flashcards.server.common.utils.hasher.IPasswordHasher;
import com.flashcards.server.auth.core.values.AuthResult;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Register implements IRegister
{
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IAccountRepository<CredentialsAccount> accountRepository;
    private final IPasswordHasher passwordHasher;
    private final IVerificationSender verificationSender;
    private final IProfileCreateClient profileCreateClient;

    public Register(
            IUserRepository userRepository,
            IRoleRepository roleRepository,
            IAccountRepository<CredentialsAccount> accountRepository,
            IPasswordHasher passwordHasher,
            IVerificationSender verificationSender,
            IProfileCreateClient profileCreateClient
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.passwordHasher = passwordHasher;
        this.verificationSender = verificationSender;
        this.profileCreateClient = profileCreateClient;
    }

    @Override
    public AuthResult registerUserByCredentials(RegisterDto dto) {
        return userRepository.findByEmail(dto.email())
                .map(existingUser -> handleExistingUser(existingUser, dto))
                .orElseGet(() -> handleNewUser(dto));
    }

    private AuthResult handleExistingUser(User user, RegisterDto dto) {
        var accountOpt = accountRepository.findByUserId(user.getId(), Provider.CREDENTIALS);

        if (accountOpt.isPresent()) {
            throw new ApiException(new ApiError(
                    HttpStatus.CONFLICT,
                    "USER_ALREADY_EXISTS",
                    "User with these credentials already exists: " + dto.email()
            ));
        }

        var hashedPassword = passwordHasher.hashPassword(dto.password());
        var newAccount = new CredentialsAccount(user.getId(), hashedPassword);
        accountRepository.create(newAccount);

        verificationSender.send(user, newAccount);

        return new AuthResult(user, newAccount.getId());
    }

    private AuthResult handleNewUser(RegisterDto dto) {
        var role = roleRepository.findByName("USER").orElseThrow(() -> new ApiException(
                new ApiError(HttpStatus.NOT_FOUND, "ROLE_NOT_FOUND", "role USER not found")));
        
        var user = new User(dto.email());
        user.addRole(role);

        var createdUser = userRepository.create(user);

        var profileDto = new CreateProfileDto(createdUser.getId(), dto.name(), null, null, null, null);
        profileCreateClient.createProfile(profileDto);

        var hashedPassword = passwordHasher.hashPassword(dto.password());

        var newAccount = new CredentialsAccount(createdUser.getId(), hashedPassword);
        accountRepository.create(newAccount);

        verificationSender.send(createdUser, newAccount);

        return new AuthResult(createdUser, newAccount.getId());
    }
}