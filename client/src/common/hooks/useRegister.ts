"use client"

import { useMutation, useQueryClient } from "@tanstack/react-query";
import { addToast } from "@/common/redux-store/toast";
import { AppDispatch } from "@/common/redux-store";

import { httpClient } from "@/common/utils/http-client";
import { useDispatch } from "react-redux";
import { useRouter } from "next/navigation";

type RegisterResponse = {
    accessToken: string;
    refreshToken: string;
}

type RegisterRequest = {
    name: string;
    email: string;
    password: string;
}

async function register(request: RegisterRequest): Promise<RegisterResponse> {
    return httpClient.post("/auth/register", request);
}

export function useRegister() {
    const queryClient = useQueryClient();
    const dispatch = useDispatch<AppDispatch>();
    const router = useRouter();

    return useMutation({
        mutationFn: register,
        onSuccess: async () => {
            await queryClient.invalidateQueries({ queryKey: ["user-profile-account"] });
            router.push("/")
        },
        onError: (error) => {
            dispatch(addToast({ message: error.message, type: "error" }));
        }
    })

}