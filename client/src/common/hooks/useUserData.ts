"use client";

import { useQuery } from "@tanstack/react-query";
import { httpClient } from "@/common/utils/http-client";

type User = {
    id: string;
    email: string;
    roles: string[];
    createdAt: string;
    updatedAt: string;
};

type Account = {
    id: string;
    provider: string;
    isVerified: boolean;
    userId: string;
    createdAt: string;
    updatedAt: string;
};

type UserAccounts = {
    user: User;
    currentAccount: Account;
    otherAccounts: Account[];
};

type Profile = {
    id: string;
    userId: string;
    firstName: string;
    lastName?: string;
    phone?: string;
    avatar?: string;
    bio?: string;
    createdAt: string;
    updatedAt: string;
};

export type UserDataResponse = UserAccounts & {
    profile: Profile;
};

async function fetchUserAccounts(): Promise<UserAccounts> {
    return httpClient.get<UserAccounts>("/user/me");
}

async function fetchUserProfile(): Promise<Profile> {
    return httpClient.get<Profile>("/profile/me");
}

export function useUserData() {
    return useQuery<UserDataResponse>({
        queryKey: ["user-data"],
        queryFn: async () => {
            const [accounts, profile] = await Promise.all([
                fetchUserAccounts(),
                fetchUserProfile(),
            ]);
            return { ...accounts, profile };
        },
        refetchOnMount: true,
        refetchOnWindowFocus: false,
        retry: false,
    });
}
