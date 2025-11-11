"use client"

import Cookies from "js-cookie";

import { useRouter } from "next/navigation";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import { httpClient } from "@/common/utils/http-client";

type LogoutResponse = {
    message: string;
}
async function logout(): Promise<LogoutResponse> {
    return httpClient.post<LogoutResponse>("/auth/logout", {})
}

export function useLogout() {
    const queryClient = useQueryClient();
    const router = useRouter();

    return useMutation({
        mutationFn: logout,
        onSuccess: async () => {
            Cookies.remove("accessToken");
            await queryClient.invalidateQueries({ queryKey: ["user-data"] });
            router.push("/auth/login");
        }

    })
}