"use client";

import React, {FC, FormEvent, useState} from "react";
import s from "@/feautured/components/forms/login-from/styles/style.module.scss";

import { useLogin } from "@/common/hooks/useLogin";

const LoginForm: FC = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const { mutateAsync: login, isPending } = useLogin();

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        try {
            await login({ email, password });
            // eslint-disable-next-line @typescript-eslint/no-unused-vars
        } catch (error) {}
    }

    return (
        <div className={s.form_wrapper}>
            <div className={s.register_form}>
                <form onSubmit={handleSubmit}>
                    <label>
                        Email:
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </label>

                    <label>
                        Password:
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </label>

                    <button type="submit" disabled={isPending}>
                        {isPending ? "Loading..." : "Submit"}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default LoginForm;
