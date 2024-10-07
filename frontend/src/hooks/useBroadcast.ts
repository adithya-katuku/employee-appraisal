import { useDispatch } from "react-redux";
import { login } from "../stores/store";
import { useNavigate } from "react-router-dom";

const useBroadcast = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const channel = new BroadcastChannel("auth-channel");

  channel.onmessage = (e) => {
    if (e.data.type === "LOGOUT") {
      const newLoginState = { isLoggedIn: false, role: "", token: "" };
      dispatch(login(newLoginState));
      localStorage.clear();
      navigate("/login")
    }
  };

  return channel;
};

export default useBroadcast;
