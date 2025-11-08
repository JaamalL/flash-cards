import Button from "@/common/components/button";
import Typography from "@/common/components/typography";
import s from "@/common/components/header/styles/style.module.scss";
import Link from "next/link";

export const HeaderAuthButtons = () => (
    <>
        <li className={s.header_wrapper_panel_list_item}>
            <Link href={"/auth/register"}>
                <Button variant="secondary">
                    <Typography tag="span" className={s.header_wrapper_panel_list_item_button_title}>
                        Register
                    </Typography>
                </Button>
            </Link>
        </li>
        <li className={s.header_wrapper_panel_list_item}>
            <Link href={"/auth/login"}>
                <Button variant="primary">
                    <Typography tag="span" className={s.header_wrapper_panel_list_item_button_title}>
                        Log in
                    </Typography>
                </Button>
            </Link>
        </li>
    </>
);