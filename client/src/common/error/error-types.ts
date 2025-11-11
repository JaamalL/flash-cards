export type ErrorResponse = {
    status: number;
    code: string;
    message: string;
};

export class ApiError extends Error {
    readonly status: number;
    readonly code: string;

    constructor({ status, code, message }: ErrorResponse) {
        super(message);
        this.status = status;
        this.code = code;
        this.name = "ApiError";

        Object.setPrototypeOf(this, ApiError.prototype);
    }
}