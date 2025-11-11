import Image from "next/image";
import defaultProfileAvatar from "#/default-profile-avatar.svg";

import Typography from "@/common/components/typography";
import s from "@/common/components/header/styles/style.module.scss";
import type { UserDataResponse } from "@/common/hooks/useUserData";
import Button from "@/common/components/button";
import { useLogout } from "@/common/hooks/useLogout";
import { FormEvent } from "react";

type Props = {
    data: UserDataResponse;
};

export const HeaderUserInfo = (props: Props) => {
    const { mutateAsync: logout, isPending } = useLogout();
    async function handleLogout(e: FormEvent) {
        e.preventDefault();
        await logout();
    }

    return (
        <div className={s.header_wrapper_panel_list_user_info}>
            <Image className={s.header_wrapper_panel_list_user_info_image} width={50} height={50} src={props.data.profile.avatar || defaultProfileAvatar} alt={"avatar"}></Image>
            <Typography className={s.header_wrapper_panel_list_user_info_name} tag="span">
                {props.data.profile.firstName}
            </Typography>

            <div className={s.header_wrapper_panel_list_user_info_dropdown}>
                <Button onClick={handleLogout}>
                    {isPending ? "Loading" : "Logout"}
                </Button>
            </div>
        </div>
    )
};