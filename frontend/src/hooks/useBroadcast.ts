import { useDispatch } from "react-redux";
import { login } from "../stores/store";

const useBroadcast = () => {
  const dispatch = useDispatch();
  const channel = new BroadcastChannel("auth-channel");

  channel.onmessage = (e) => {
    if (e.data.type === "LOGOUT") {
      const newLoginState = { isLoggedIn: false, role: "", token: "" };
      dispatch(login(newLoginState));
      sessionStorage.clear();
    }
  };

  return channel;
};

export default useBroadcast;
