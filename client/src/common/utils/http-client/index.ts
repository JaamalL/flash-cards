import { HttpClient } from "@/common/utils/http-client/http-client";

export const httpClient = new HttpClient({
    baseUrl: process.env.NEXT_PUBLIC_SPRING_API_URL ?? "http://localhost:7777",
})