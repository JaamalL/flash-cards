import Image from "next/image";
import logo from "#/logo.png";
import s from "@/common/components/header/styles/style.module.scss";

export const HeaderLogo = () => (
    <div className={s.header_wrapper_logo}>
        <Image
            className={s.header_wrapper_logo_icon}
            src={logo}
            alt="Logo"
            priority
        />
    </div>
);