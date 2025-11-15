"use client"
import Cookies from "js-cookie";
import { useRouter } from "next/navigation";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import { httpClient } from "@/common/utils/http-client";

type LoginResponse = {
    accessToken: string;
}

type LoginRequest = {
    email: string;
    password: string;
}

async function login(request: LoginRequest): Promise<LoginResponse> {
    return httpClient.post("/auth/login", request);
}

export function useLogin() {
    const queryClient = useQueryClient();
    const router = useRouter();

    return useMutation({
        mutationFn: login,
        onSuccess: async (data) => {
            Cookies.set("accessToken", data.accessToken)
            await queryClient.invalidateQueries({ queryKey: ["user-data"] });
            router.push("/")
        }
    })
}