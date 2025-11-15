"use server"

import "@/app/globals-styles/globals.css";

import { ReactNode } from "react";

import ReduxProvider from "@/common/providers/redux-provider";
import QueryProvider from "@/common/providers/query-provider";
import ToastProvider from "@/common/providers/toast-provider";
import ErrorProvider from "@/common/providers/error-provider";

import Header from "@/common/components/header";

type RootLayoutProps = {
    children: ReactNode;
};

export default async function RootLayout({ children } : RootLayoutProps) {
  return (
    <html lang="en">
      <body>
        <ReduxProvider>
            <QueryProvider>
                <ToastProvider>
                    <ErrorProvider>
                        <main className="main">
                            <Header />
                            {children}
                        </main>
                    </ErrorProvider>
                </ToastProvider>
            </QueryProvider>
        </ReduxProvider>
      </body>
    </html>
  );
}
