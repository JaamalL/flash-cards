import React from "react";

import { roboto } from "@/common/fonts";

import s from "@/common/components/typography/styles/style.module.scss"

type TypographyTag = "p" | "span" | "h1" | "h2" | "h3" | "h4" | "h5" | "h6";

type TypographyProps<T extends TypographyTag = "p"> = {
    tag?: T;
    children: React.ReactNode;
} & React.ComponentPropsWithoutRef<T>;

const Typography = <T extends TypographyTag = "p">({
    tag,
    children,
    ...props
}: TypographyProps<T>) => {
    const Tag = tag || "p";
    return (
        <Tag {...props} className={`${roboto.className} ${s.typography} ${props.className || ""}`}>
            { children }
        </Tag>
    );
};

export default Typography;