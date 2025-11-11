"use client";

import { useForm } from "@tanstack/react-form";

import { AuthFormWrapper } from "@/common/components/form/AuthForm";
import { FormField } from "@/common/components/form/FormField";

import { useRegister } from "@/common/hooks/useRegister";

export const RegisterForm = () => {
    const { mutateAsync: register, isPending } = useRegister();

    const form = useForm({
        defaultValues: { name: "", email: "", password: "" },
        onSubmit: async ({ value }) => await register(value),
    });

    return (
        <AuthFormWrapper
            title="Register free to Start Learning"
            isPending={isPending}
            submitText="Register"
            onSubmit={async (e) => {
                e.preventDefault();
                await form.handleSubmit();
            }}
        >
            <form.Field
                name="name"
                validators={{
                    onChange: ({ value }) =>
                        !value.trim() ? "Name is required" : undefined,
                }}
            >
                {(field) => <FormField field={field} label="Name" />}
            </form.Field>

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
