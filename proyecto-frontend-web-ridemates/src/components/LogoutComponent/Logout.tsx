import { useEffect } from "react";
import { Navigate } from "react-router-dom";
import { useAuthContext } from "../../contexts/AuthContext";


export default function Logout() {
    const context = useAuthContext();

    useEffect(() => {
        context.logout();
        console.log("logout");
        window.location.href = "/"; // With reload
    }, []);
    return (<></>);
}