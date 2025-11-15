import styles from "@/common/components/button/button-styles.module.scss";

import { ReactNode, FC, ButtonHTMLAttributes } from "react";

type ButtonTypes = "filled" | "outlined" | "disabled";
type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
    children: ReactNode;
    variant?: ButtonTypes;
};
export const Button: FC<ButtonProps> = ({ children, variant = "filled", ...props }) => {
    return (
        <button {...props} className={`${styles.button} ${styles[variant]} ${props.className || ""}`}>
            {children}
        </button>
    );
}