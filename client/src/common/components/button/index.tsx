import React from "react";

import s from "@/common/components/button/styles/style.module.scss";

type ButtonTypes = "primary" | "secondary";

type ButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> & {
    children?: React.ReactNode;
    variant?: ButtonTypes;
};
const Button: React.FC<ButtonProps> = ({ children, variant = "primary", ...props }) => {
    return (
        <button {...props} className={`${s.button} ${s[variant]} ${props.className || ""}`}>
            { children }
        </button>
    );
}

export default Button