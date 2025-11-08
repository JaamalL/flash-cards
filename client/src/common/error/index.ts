import { ErrorTypes } from "@/common/error/error-types"

export class HttpError extends Error {
    status: number
    code: string
    isOperational: boolean

    constructor(message: string, type = ErrorTypes.INTERNAL) {
        super(message)
        this.status = type.status
        this.code = type.code
        this.isOperational = true
        Error.captureStackTrace?.(this, this.constructor)
    }
}