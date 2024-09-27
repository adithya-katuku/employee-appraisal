import { useSelector } from "react-redux";
import { Navigate, Outlet } from "react-router-dom";
import { RootState } from "../../stores/store";

const AdminRoutes = () => {
    const role = useSelector((state: RootState) => state.store.loginState.role);

  return role === "admin" ? <Outlet /> : <Navigate to="/login"/>;
};

export default AdminRoutes;
