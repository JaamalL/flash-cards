export class ErrorTypes {
    status: number
    code: string

    constructor(status: number, code: string) {
        this.status = status
        this.code = code
    }

    static BAD_REQUEST = new ErrorTypes(400, 'BAD_REQUEST');
    static UNAUTHORIZED = new ErrorTypes(401, 'UNAUTHORIZED');
    static FORBIDDEN = new ErrorTypes(403, 'FORBIDDEN');
    static NOT_FOUND = new ErrorTypes(404, 'NOT_FOUND');
    static METHOD_NOT_ALLOWED = new ErrorTypes(405, 'METHOD_NOT_ALLOWED');
    static CONFLICT = new ErrorTypes(409, 'CONFLICT');
    static UNPROCESSABLE_ENTITY = new ErrorTypes(422, 'UNPROCESSABLE_ENTITY');

    static INTERNAL = new ErrorTypes(500, 'INTERNAL');
    static NOT_IMPLEMENTED = new ErrorTypes(501, 'NOT_IMPLEMENTED');
    static BAD_GATEWAY = new ErrorTypes(502, 'BAD_GATEWAY');
    static SERVICE_UNAVAILABLE = new ErrorTypes(503, 'SERVICE_UNAVAILABLE');
    static GATEWAY_TIMEOUT = new ErrorTypes(504, 'GATEWAY_TIMEOUT');
}