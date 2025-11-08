'use client'

import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "@/common/redux-store";
import {ReactNode, useEffect} from "react";
import {removeToast} from "@/common/redux-store/toast";

const ToastProvider = ({ children }: { children: ReactNode }) => {
    const toasts = useSelector((state: RootState) => state.toast.toasts);
    const dispatch = useDispatch<AppDispatch>();
    useEffect(() => {
        toasts.forEach((toast) => {
            const timer = setTimeout(() => {
                dispatch(removeToast(toast.id));
            }, 3000);
            return () => clearTimeout(timer);
        });
    }, [toasts, dispatch]);
    return (
        <>
            {children}
            <div style={{ position: 'fixed', bottom: 20, right: 20, zIndex: 9999, width: '300px' }}>
                {toasts.map((toast) => (
                    <div
                        key={toast.id}
                        style={{
                            marginBottom: 8,
                            padding: '10px 20px',
                            borderRadius: 5,
                            color: '#fff',
                            backgroundColor:
                                toast.type === 'success'
                                    ? 'green'
                                    : toast.type === 'error'
                                        ? 'red'
                                        : 'gray',
                            boxShadow: '0 2px 6px rgba(0,0,0,0.2)',
                        }}
                    >
                        {toast.message}
                    </div>
                ))}
            </div>
        </>
    );
};

export default ToastProvider;
