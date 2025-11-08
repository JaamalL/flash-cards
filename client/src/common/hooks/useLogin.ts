"use client"

import { useMutation, useQueryClient } from "@tanstack/react-query";
import { addToast } from "@/common/redux-store/toast";
import { AppDispatch } from "@/common/redux-store";

import { httpClient } from "@/common/utils/http-client";
import { useDispatch } from "react-redux";
import { useRouter } from "next/navigation";

type LoginResponse = {
    accessToken: string;
    refreshToken: string;
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
    const dispatch = useDispatch<AppDispatch>();
    const router = useRouter();

    return useMutation({
        mutationFn: login,
        onSuccess: async () => {
            await queryClient.invalidateQueries({ queryKey: ["user-profile-account"] });
            router.push("/")
        },
        onError: (error) => {
            dispatch(addToast({ message: error.message, type: "error" }));
        }
    })
}