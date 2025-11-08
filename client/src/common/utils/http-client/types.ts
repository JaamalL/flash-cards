export type SearchParams = Record<string, string | number | boolean | Array<string | number | boolean>>
export type RequestOptions = Omit<RequestInit, 'method' | 'body' | 'headers'> & {
    headers?: Record<string, string>
    params?: SearchParams
}