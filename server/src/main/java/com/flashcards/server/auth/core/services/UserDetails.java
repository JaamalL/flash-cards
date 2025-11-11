package com.flashcards.server.auth.core.services;

import com.flashcards.server.auth.core.entities.Account;
import com.flashcards.server.auth.core.entities.Role;
import com.flashcards.server.auth.core.entities.User;
import com.flashcards.server.auth.core.ports.repository.IAccountRepository;
import com.flashcards.server.auth.core.ports.repository.IUserRepository;
import com.flashcards.server.auth.core.ports.services.IUserDetails;
import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserDetails implements IUserDetails
{
    private final IUserRepository userRepository;
    private final IAccountRepository<Account> accountRepository;

    public UserDetails(IUserRepository userRepository, IAccountRepository<Account> accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Map<String, Object> getUserDetails(UUID userId, UUID accountId)
    {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(new ApiError(
                        HttpStatus.NOT_FOUND,
                        "USER_NOT_FOUND",
                        "User with this id was not found"
                )));

        var currentAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ApiException(new ApiError(
                        HttpStatus.NOT_FOUND,
                        "ACCOUNT_NOT_FOUND",
                        "Account with this id was not found"
                )));

        return buildResult(user, currentAccount);
    }

    private Map<String, Object> buildResult(User user, Account currentAccount) {
        Map<String, Object> result = new HashMap<>();

        var userMap = buildUserResult(user);
        var otherAccountsMap = user.getAccounts().stream()
                .filter(account -> !account.getId().equals(currentAccount.getId()))
                .map(this::buildAccountResult)
                .collect(Collectors.toSet());

        var currentAccountMap = buildAccountResult(currentAccount);

        result.put("user", userMap);
        result.put("currentAccount", currentAccountMap);
        result.put("otherAccounts", otherAccountsMap);

        return result;
    }

    private Map<String, Object> buildUserResult(User user) {
        var roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId().toString());
        userMap.put("email", user.getEmail());
        userMap.put("roles", roles);
        userMap.put("createdAt", user.getCreatedAt().toString());
        userMap.put("updatedAt", user.getUpdatedAt().toString());

        return userMap;
    }

    private Map<String, Object> buildAccountResult(Account account)
    {
        Map<String, Object> accountMap = new HashMap<>();
        accountMap.put("id", account.getId().toString());
        accountMap.put("userId", account.getUserId().toString());
        accountMap.put("provider", account.getProvider().name());
        accountMap.put("isVerified", account.isVerified());
        accountMap.put("createdAt", account.getCreatedAt().toString());
        accountMap.put("updatedAt", account.getUpdatedAt().toString());

        return accountMap;
    }
}
