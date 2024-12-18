import { Outlet } from "react-router-dom";
import Navbar from "../../components/NavBar";

export default function DeliveryHome() {
    return (
        <>
            <div>
                <Navbar />
            </div>
            <Outlet />
        </>

    );
}