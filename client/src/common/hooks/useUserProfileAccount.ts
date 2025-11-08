"use client"

import { useQuery } from "@tanstack/react-query";
import { httpClient } from "@/common/utils/http-client";

type User = {
    id: string;
    role: string;
    email: string;
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

type UserAccountResponse = {
    user: User;
    account: Account;
};

export type FullUserProfileResponse = UserAccountResponse & {
    profile: Profile;
};

async function fetchUserAndAccount(): Promise<UserAccountResponse> {
    return httpClient.get<UserAccountResponse>("/user/me");
}

async function fetchUserProfile(): Promise<Profile> {
    return httpClient.get<Profile>("/profile/me");
}

export function useUserProfileAccount() {
    return useQuery<FullUserProfileResponse>({
        queryKey: ["user-profile-account"],
        queryFn: async () => {
            const [userAccount, userProfile] = await Promise.all([
                fetchUserAndAccount(),
                fetchUserProfile()
            ]);
            return { ...userAccount, profile: userProfile };
        },
        refetchOnWindowFocus: false,
        retry: false,
    });
}
