"use client"

import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useRouter } from "next/navigation";

import { httpClient } from "@/common/utils/http-client";

type RegisterResponse = {
    accessToken: string;
    refreshToken: string;
}

export type RegisterRequest = {
    name: string;
    email: string;
    password: string;
}

async function register(request: RegisterRequest): Promise<RegisterResponse> {
    return httpClient.post("/auth/register", request);
}

export function useRegister() {
    const queryClient = useQueryClient();
    const router = useRouter();

    return useMutation({
        mutationFn: register,
        onSuccess: async () => {
            await queryClient.invalidateQueries({ queryKey: ["user-data"] });
            router.push("/")
        }
    })

}