import "@/app/globals.css";
import { ReactNode } from "react";

import ToastProvider from "@/common/providers/toast-provider";
import ReduxProvider from "@/common/providers/redux-provider";
import QueryProvider from "@/common/providers/query-provider";

import Header from "@/common/components/header";

type RootLayoutProps = {
    children: ReactNode;
};

export default function RootLayout({ children } : RootLayoutProps) {
  return (
    <html lang="en">
      <body>
        <ReduxProvider>
            <QueryProvider>
                <ToastProvider>
                    <Header />
                    {children}
                </ToastProvider>
            </QueryProvider>
        </ReduxProvider>
      </body>
    </html>
  );
}
