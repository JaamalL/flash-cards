"use client"

import Cookies from "js-cookie";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import { httpClient } from "@/common/utils/http-client";

export type RefreshResponse = {
    accessToken: string;
}

async function refresh(): Promise<RefreshResponse> {
    return httpClient.post("/auth/refresh", {});
}

export function useRefresh() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: refresh,
        onSuccess: async (data) => {
            Cookies.set("accessToken", data.accessToken)
            await queryClient.invalidateQueries({ queryKey: ["user-data"] });
        }
    })
}