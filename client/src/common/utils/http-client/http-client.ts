import Cookies from "js-cookie";
import { HttpError } from "@/common/error";
import { ErrorTypes } from "@/common/error/error-types";
import { ErrorStatusMapper } from "@/common/error/map-error";
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
            if (Array.isArray(value)) value.forEach(v => searchParams.append(key, String(v)));
            else searchParams.set(key, String(value));
        });

        const queryString = searchParams.toString();
        return queryString ? `?${queryString}` : "";
    }

    private getHeaders(optionsHeaders?: Record<string, string>, body?: object | FormData): Record<string, string> {
        const headers: Record<string, string> = { ...this.defaultHeaders, ...optionsHeaders };
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

            if (!response.ok) throw new HttpError("Refresh token failed", ErrorTypes.UNAUTHORIZED);

            const data: { accessToken: string } = await response.json();
            if (!data.accessToken) throw new HttpError("No access token returned on refresh", ErrorTypes.INTERNAL);

            Cookies.set("accessToken", data.accessToken.trim());
        })();

        try {
            await this.refreshPromise;
        } finally {
            this.refreshPromise = undefined;
        }
    }

    private async fetchWithRefresh(url: string, config: RequestInit): Promise<Response> {
        let response = await fetch(url, config);

        if (response.status === 401) {
            Cookies.remove("accessToken");
            try {
                await this.refreshToken();
                config.headers = this.getHeaders(
                    config.headers as Record<string, string>,
                    config.body instanceof FormData ? config.body : undefined
                );
                response = await fetch(url, config);
            } catch {
                Cookies.remove("accessToken");
                throw new HttpError("Unauthorized and refresh failed", ErrorTypes.UNAUTHORIZED);
            }
        }

        return response;
    }

    private async handleResponse<ResponseType>(response: Response): Promise<ResponseType> {
        if (!response.ok) {
            let errorMessage = response.statusText;
            try {
                const data = await response.json();
                if (data?.message) errorMessage = data.message;
            } catch {}

            const errorType = ErrorStatusMapper.mapStatusToErrorType(response.status);
            throw new HttpError(errorMessage, errorType);
        }

        const contentType = response.headers.get("Content-Type") ?? "";
        if (contentType.startsWith("application/json"))
            return (await response.json()) as ResponseType;
        return (await response.text()) as unknown as ResponseType;
    }

    private handleError(err: unknown): HttpError {
        if (err instanceof HttpError) return err;
        if (err instanceof DOMException && err.name === "AbortError")
            return new HttpError("Request timed out", ErrorTypes.INTERNAL);

        const message = err instanceof Error ? err.message : String(err);
        return new HttpError(message || "Network or unknown error", ErrorTypes.INTERNAL);
    }

    private async request<ResponseType, BodyType extends object | FormData | undefined = undefined>(
        endpoint: string,
        method: "GET" | "POST" | "PUT" | "PATCH" | "DELETE",
        options: RequestOptions & { body?: BodyType } = {}
    ): Promise<ResponseType> {
        let url = `${this.baseUrl}${endpoint}`;
        if (options.params) url += this.createSearchParams(options.params);

        const controller = new AbortController();
        const timeout = setTimeout(() => controller.abort(), 100000);

        const config: RequestInit = {
            ...this.defaultOptions,
            ...options,
            method,
            headers: this.getHeaders(options.headers, options.body),
            body: options.body instanceof FormData
                ? options.body
                : options.body !== undefined
                    ? JSON.stringify(options.body)
                    : undefined,
            credentials: "include",
            signal: controller.signal,
        };

        try {
            const response = await this.fetchWithRefresh(url, config);
            return await this.handleResponse<ResponseType>(response);
        } catch (err: unknown) {
            return Promise.reject(this.handleError(err));
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

    public put<ResponseType, BodyType extends object | FormData = object>(
        endpoint: string,
        body: BodyType,
        options?: RequestOptions
    ) {
        return this.request<ResponseType, BodyType>(endpoint, "PUT", { ...options, body });
    }

    public patch<ResponseType, BodyType extends object | FormData = object>(
        endpoint: string,
        body: BodyType,
        options?: RequestOptions
    ) {
        return this.request<ResponseType, BodyType>(endpoint, "PATCH", { ...options, body });
    }

    public delete<ResponseType, BodyType extends object | FormData | undefined = undefined>(
        endpoint: string,
        body?: BodyType,
        options?: RequestOptions
    ) {
        return this.request<ResponseType, BodyType>(endpoint, "DELETE", { ...options, body });
    }
}
