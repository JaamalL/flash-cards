import Cookies from "js-cookie";
import { ApiError, ErrorResponse } from "@/common/error/error-types";
import type { SearchParams, RequestOptions } from "@/common/utils/http-client/types";

export class HttpClient {
    private readonly baseUrl: string;
    private readonly defaultHeaders?: Record<string, string>;
    private readonly defaultParams?: SearchParams;
    private readonly defaultOptions?: RequestOptions;
    private refreshPromise?: Promise<void>;

    constructor(init: {
        baseUrl: string;
        headers?: Record<string, string>;
        params?: SearchParams;
        options?: RequestOptions;
    }) {
        this.baseUrl = init.baseUrl;
        this.defaultHeaders = init.headers;
        this.defaultParams = init.params;
        this.defaultOptions = init.options;
    }

    private createSearchParams(params?: SearchParams): string {
        const combined = { ...this.defaultParams, ...(params || {}) };
        const searchParams = new URLSearchParams();

        Object.entries(combined).forEach(([key, value]) => {
            if (value == null) return;
            if (Array.isArray(value)) {
                value.forEach(v => searchParams.append(key, String(v)));
            } else {
                searchParams.set(key, String(value));
            }
        });

        return searchParams.toString() ? `?${searchParams.toString()}` : "";
    }

    private getHeaders(optionsHeaders?: Record<string, string>, body?: object | FormData) {
        const headers = { ...this.defaultHeaders, ...optionsHeaders };
        const token = Cookies.get("accessToken")?.trim();
        if (token) headers["Authorization"] = `Bearer ${token}`;
        if (body && !(body instanceof FormData)) headers["Content-Type"] = "application/json";
        return headers;
    }

    private async refreshToken(): Promise<void> {
        if (this.refreshPromise) return this.refreshPromise;

        this.refreshPromise = (async () => {
            const response = await fetch(`${this.baseUrl}/auth/refresh`, {
                method: "POST",
                credentials: "include",
            });

            const contentType = response.headers.get("Content-Type") ?? "";
            const isJson = contentType.includes("application/json");
            const data = isJson ? await response.json() : {};

            if (!response.ok) {
                const errorData = data as ErrorResponse;
                Cookies.remove("accessToken", { path: "/" });
                throw new ApiError(errorData);
            }

            const tokenData = data as { accessToken?: string };
            if (!tokenData.accessToken) {
                throw new ApiError({
                    status: 500,
                    code: "INTERNAL",
                    message: "No access token returned",
                });
            }

            Cookies.set("accessToken", tokenData.accessToken.trim());
        })();

        try {
            await this.refreshPromise;
        } finally {
            this.refreshPromise = undefined;
        }
    }

    private async fetchWithRefresh(url: string, config: RequestInit): Promise<Response> {
        let response = await fetch(url, config);

        let responseBody: ErrorResponse | null;
        try {
            responseBody = await response.clone().json() as ErrorResponse;
        } catch {
            responseBody = null;
        }

        if (response.status === 401 && responseBody?.code === "UNAUTHORIZED") {
            Cookies.remove("accessToken", { path: "/" });
            try {
                await this.refreshToken();
                config.headers = this.getHeaders(
                    config.headers as Record<string, string>,
                    config.body instanceof FormData ? config.body : undefined
                );
                response = await fetch(url, config);
            } catch (err) {
                throw err instanceof ApiError
                    ? err
                    : new ApiError({ status: 500, code: "INTERNAL", message: String(err) });
            }
        }

        return response;
    }

    private async handleResponse<ResponseType>(response: Response): Promise<ResponseType> {
        if (!response.ok) {
            const data = await response.json() as ErrorResponse;
            throw new ApiError(data);
        }

        const contentType = response.headers.get("Content-Type") ?? "";
        return contentType.startsWith("application/json")
            ? (await response.json()) as ResponseType
            : (await response.text()) as unknown as ResponseType;
    }

    private async request<ResponseType, BodyType extends object | FormData | undefined = undefined>(
        endpoint: string,
        method: "GET" | "POST" | "PUT" | "PATCH" | "DELETE",
        options: RequestOptions & { body?: BodyType } = {}
    ): Promise<ResponseType> {
        const url = `${this.baseUrl}${endpoint}${options.params ? this.createSearchParams(options.params) : ""}`;
        const controller = new AbortController();
        const timeout = setTimeout(() => controller.abort(), 5000);

        const config: RequestInit = {
            ...this.defaultOptions,
            ...options,
            method,
            headers: this.getHeaders(options.headers, options.body),
            body: options.body instanceof FormData ? options.body : options.body !== undefined ? JSON.stringify(options.body) : undefined,
            credentials: "include",
            signal: controller.signal,
        };

        try {
            const response = await this.fetchWithRefresh(url, config);
            return await this.handleResponse<ResponseType>(response);
        } finally {
            clearTimeout(timeout);
        }
    }

    public get<ResponseType>(endpoint: string, options?: Omit<RequestOptions, "body">) {
        return this.request<ResponseType>(endpoint, "GET", options);
    }

    public post<ResponseType, BodyType extends object | FormData = object>(
        endpoint: string,
        body: BodyType,
        options?: RequestOptions
    ) {
        return this.request<ResponseType, BodyType>(endpoint, "POST", { ...options, body });
    }

    public delete<ResponseType, BodyType extends object | FormData | undefined = undefined>(
        endpoint: string,
        body?: BodyType,
        options?: RequestOptions
    ) {
        return this.request<ResponseType, BodyType>(endpoint, "DELETE", { ...options, body });
    }
}
