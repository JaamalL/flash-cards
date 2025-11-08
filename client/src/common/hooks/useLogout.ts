"use client"

import { useMutation, useQueryClient } from "@tanstack/react-query";
import Cookies from "js-cookie";
import { addToast } from "@/common/redux-store/toast";
import { AppDispatch } from "@/common/redux-store";

import { httpClient } from "@/common/utils/http-client";
import { useDispatch } from "react-redux";
import { useRouter } from "next/navigation";

type LogoutResponse = {
    message: string;
}
async function logout(): Promise<LogoutResponse> {
    const result = httpClient.post<LogoutResponse>("/auth/logout", {})
    Cookies.remove("accessToken");
    console.log(result);
    return result;
}

export function useLogout() {
    const queryClient = useQueryClient();
    const dispatch = useDispatch<AppDispatch>();
    const router = useRouter();

    return useMutation({
        mutationFn: logout,
        onSuccess: async () => {
            await queryClient.invalidateQueries({ queryKey: ["user-profile-account"] });
            router.push("/")
        },
        onError: (error) => {
            dispatch(addToast({ message: error.message, type: "error" }));
        }
    })
}