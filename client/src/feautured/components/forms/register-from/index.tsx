"use client";

import React, {FC, FormEvent, useState} from "react";
import s from "@/feautured/components/forms/register-from/styles/style.module.scss";

import { useRegister } from "@/common/hooks/useRegister";
import Button from "@/common/components/button";

const RegisterForm: FC = () => {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const { mutateAsync: register, isPending } = useRegister();

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        try {
            await register({ name, email, password });
            // eslint-disable-next-line @typescript-eslint/no-unused-vars
        } catch (error) {}
    }

    return (
        <div className={s.form_wrapper}>
            <div className={s.register_form}>
                <form onSubmit={handleSubmit}>
                    <label>
                        Name:
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                    </label>

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
                    <Button type="submit" disabled={isPending}>{isPending ? "Loading..." : "Submit"}</Button>
                </form>
            </div>
        </div>
    );
};

export default RegisterForm;
