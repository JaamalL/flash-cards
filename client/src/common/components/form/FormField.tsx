"use client";

import { type AnyFieldApi } from "@tanstack/react-form";
import Typography from "@/common/components/typography";

import style from "@/common/components/form/styles/form-field.module.scss";

interface FormFieldProps {
    field: AnyFieldApi;
    label: string;
    type?: string;
}

export const FormField = ({ field, label, type = "text" }: FormFieldProps) => {
    const showError =
        !field.state.meta.isValid && field.state.meta.isTouched;

    return (
        <div className={style.filed}>
            <label className={style.label} htmlFor={field.name}>
                {label}:
            </label>
            <input
                id={field.name}
                name={field.name}
                type={type}
                value={field.state.value}
                onBlur={field.handleBlur}
                onChange={(e) => field.handleChange(e.target.value)}
                className={`${style.input} ${showError ? style.input_error : ""}`}
            />
            {showError && (
                <Typography className={style.field_error}>
                    {field.state.meta.errors.join(", ")}
                </Typography>
            )}
        </div>
    );
};