"use client";

import { useEffect, ReactNode } from "react";
import { addToast } from "@/common/redux-store/toast";
import { store } from "@/common/redux-store";

export default function ErrorProvider({ children }: { children: ReactNode }) {
    useEffect(() => {
        const handleGlobalError = (event: ErrorEvent) => {
            store.dispatch(addToast({ message: event.error?.message || "Unexpected error", type: "error" }));
        };

        const handleUnhandledRejection = (event: PromiseRejectionEvent) => {
            const message =
                event.reason instanceof Error
                    ? event.reason.message
                    : typeof event.reason === "string"
                        ? event.reason
                        : "Unexpected promise rejection";
            store.dispatch(addToast({ message, type: "error" }));
        };

        window.addEventListener("error", handleGlobalError);
        window.addEventListener("unhandledrejection", handleUnhandledRejection);

        return () => {
            window.removeEventListener("error", handleGlobalError);
            window.removeEventListener("unhandledrejection", handleUnhandledRejection);
        };
    }, []);

    return <>{children}</>;
}
