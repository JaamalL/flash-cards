import { ErrorTypes } from "@/common/error/error-types";

export class ErrorStatusMapper {
    public static mapStatusToErrorType(status: number): ErrorTypes {
        switch (status) {
            case 400: return ErrorTypes.BAD_REQUEST;
            case 401: return ErrorTypes.UNAUTHORIZED;
            case 403: return ErrorTypes.FORBIDDEN;
            case 404: return ErrorTypes.NOT_FOUND;
            case 405: return ErrorTypes.METHOD_NOT_ALLOWED;
            case 409: return ErrorTypes.CONFLICT;
            case 422: return ErrorTypes.UNPROCESSABLE_ENTITY;

            case 500: return ErrorTypes.INTERNAL;
            case 501: return ErrorTypes.NOT_IMPLEMENTED;
            case 502: return ErrorTypes.BAD_GATEWAY;
            case 503: return ErrorTypes.SERVICE_UNAVAILABLE;
            case 504: return ErrorTypes.GATEWAY_TIMEOUT;

            default: return new ErrorTypes(status, `HTTP_${status}`);
        }
    }
}