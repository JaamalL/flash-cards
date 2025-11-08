"use client";

import { FC, useEffect, useState } from "react";
import { HeaderLogo } from "@/common/components/header/header-logo";
import { HeaderNav } from "@/common/components/header/header-nav";
import { HeaderPanel } from "@/common/components/header/header-panel";

import s from "@/common/components/header/styles/style.module.scss";

const Header: FC = () => {
    const [hidden, setHidden] = useState(false);

    useEffect(() => {
        let lastScrollY = window.scrollY;
        const handleScroll = () => {
            const currentY = window.scrollY;
            setHidden(currentY > lastScrollY);
            lastScrollY = currentY;
        };
        window.addEventListener("scroll", handleScroll);
        return () => window.removeEventListener("scroll", handleScroll);
    }, []);

    return (
        <header className={`${s.header} ${hidden ? s.header_hidden : ""}`}>
            <div className={s.header_wrapper}>
                <HeaderLogo />
                <HeaderNav />
                <HeaderPanel />
            </div>
        </header>
    );
};

export default Header;
