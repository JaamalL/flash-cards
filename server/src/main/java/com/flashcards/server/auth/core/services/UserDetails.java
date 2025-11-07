package com.flashcards.server.auth.core.services;

import com.flashcards.server.auth.core.entities.Account;
import com.flashcards.server.auth.core.entities.User;
import com.flashcards.server.auth.core.ports.repository.IAccountRepository;
import com.flashcards.server.auth.core.ports.repository.IUserRepository;
import com.flashcards.server.auth.core.ports.services.IUserDetails;
import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ApiException(new ApiError(
                        HttpStatus.NOT_FOUND,
                        "ACCOUNT_NOT_FOUND",
                        "Account with this id was not found"
                )));

        return buildResult(user, account);

    }

    private Map<String, Object> buildResult(User user, Account account) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId().toString());
        userMap.put("email", user.getEmail());
        userMap.put("role", user.getRole().name());
        userMap.put("createdAt", user.getCreatedAt().toString());
        userMap.put("updatedAt", user.getUpdatedAt().toString());

        Map<String, Object> accountMap = new HashMap<>();
        accountMap.put("id", account.getId().toString());
        accountMap.put("userId", account.getUserId().toString());
        accountMap.put("provider", account.getProvider().name());
        accountMap.put("isVerified", account.isVerified());
        accountMap.put("createdAt", account.getCreatedAt().toString());
        accountMap.put("updatedAt", account.getUpdatedAt().toString());

        result.put("user", userMap);
        result.put("account", accountMap);

        return result;
    }
}