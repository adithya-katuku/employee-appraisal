import { useSelector } from "react-redux";
import { RootState } from "../../stores/store";
import { Navigate, Outlet } from "react-router-dom";

const ProtectedRoutes = () => {
  const isLoggedIn = useSelector((state: RootState) => state.store.loginState.isLoggedIn);
    
  return isLoggedIn ? <Outlet /> : <Navigate to="/login" />;
};

export default ProtectedRoutes;
