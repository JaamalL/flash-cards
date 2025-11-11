"use client";

import { useForm } from "@tanstack/react-form";

import { useLogin } from "@/common/hooks/useLogin";
import { AuthFormWrapper } from "@/common/components/form/AuthForm";
import { FormField } from "@/common/components/form/FormField";

export const LoginForm = () => {
    const { mutateAsync: login, isPending } = useLogin();

    const form = useForm({
        defaultValues: { email: "", password: "" },
        onSubmit: async ({ value }) => await login(value),
    });

    return (
        <AuthFormWrapper
            title="Login to Start Learning"
            isPending={isPending}
            submitText="Login"
            onSubmit={async (e) => {
                e.preventDefault();
                await form.handleSubmit();
            }}
        >
            <form.Field
                name="email"
                validators={{
                    onChange: ({ value }) => {
                        if (!value.trim()) return "Email is required";
                        if (!/\S+@\S+\.\S+/.test(value)) return "Invalid email";
                    },
                }}
            >
                {(field) => <FormField field={field} label="Email" type="email" />}
            </form.Field>

            <form.Field
                name="password"
                validators={{
                    onChange: ({ value }) =>
                        value.length < 6 ? "Password must be at least 6 characters" : undefined,
                }}
            >
                {(field) => <FormField field={field} label="Password" type="password" />}
            </form.Field>
        </AuthFormWrapper>
    );
};
