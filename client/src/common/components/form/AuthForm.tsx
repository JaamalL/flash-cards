"use client";

import { FormEvent, ReactNode } from "react";
import Image from "next/image";

import Button from "@/common/components/button";
import Typography from "@/common/components/typography";

import formImage from "#/2-auth-form-image.png";
import googleImage from "#/google.png";

import style from "@/common/components/form/styles/auth-form.module.scss";

interface AuthFormWrapperProps {
    title: string;
    isPending: boolean;
    onSubmit: (e: FormEvent<HTMLFormElement>) => Promise<void>;
    children: ReactNode;
    submitText: string;
}

export const AuthFormWrapper = ({
    title,
    isPending,
    onSubmit,
    children,
    submitText,
}: AuthFormWrapperProps) => {
    function handleGoogle() {
        window.location.href =
            "https://accounts.google.com/o/oauth2/v2/auth?client_id=283957544051-ru0lts3mke8gr5p3qkd0apgo0oo5c1pk.apps.googleusercontent.com&redirect_uri=http://localhost:7777/auth/google/callback&response_type=code&scope=openid%20profile%20email&access_type=offline";
    }

    return (
        <div className={style.form_container}>
            <form onSubmit={onSubmit} className={style.form}>
                <Typography tag="h1" className={style.form_title}>
                    {title}
                </Typography>

                {children}

                <Button className={style.form_button} type="submit" disabled={isPending}>
                    <Typography className={style.form_button_title}>
                        {isPending ? "Loading..." : submitText}
                    </Typography>
                </Button>

                <Button
                    onClick={handleGoogle}
                    type="button"
                    variant="secondary"
                    className={`${style.form_button} ${style.form_button_google}`}
                >
                    <Image
                        src={googleImage}
                        alt="google icon"
                        className={style.form_button_google_image}
                    />
                    <Typography tag="span" className={style.form_button_title}>
                        Continue with Google
                    </Typography>
                </Button>
            </form>

            <div className={style.form_image_wrapper}>
                <Image className={style.form_image} src={formImage} alt="form image" />
            </div>
        </div>
    );
};
