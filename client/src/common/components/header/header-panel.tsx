"use client"

import { HeaderAuthButtons } from "@/common/components/header/header-auth-buttons";
import { HeaderUserInfo } from "@/common/components/header/header-user-info";
import s from "@/common/components/header/styles/style.module.scss";
import { useUserProfileAccount } from "@/common/hooks/useUserProfileAccount";

export const HeaderPanel = () => {
    const { data, isLoading, error } = useUserProfileAccount();

    return (
        <nav className={s.header_wrapper_panel}>
            <ul className={s.header_wrapper_panel_list}>
                {isLoading ? (
                    <></> //todo skeleton
                ) : error || !data ? (
                    <HeaderAuthButtons />
                ) : (
                    <HeaderUserInfo data={data} />
                )}
            </ul>
        </nav>
    );
};