import axios, { AxiosError } from "axios";
import { useDispatch, useSelector } from "react-redux";
import useBroadcast from "./useBroadcast";
import {
  login,
  RootState,
  setCaptchaId,
  setSelectedPage,
  setUrl,
} from "../stores/store";
import { useNavigate } from "react-router-dom";
import usePaths from "./usePaths";
import { AlertStatus, useToast } from "@chakra-ui/react";
import useAPI from "./useAPI";

interface loginForm {
  email: string;
  password: string;
  captchaAnswer: string;
}
interface errorResponse {
  detail: string;
}
const useAuth = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const captchaId = useSelector((state: RootState) => state.store.captchaId);
  const navigate = useNavigate();
  const channel = useBroadcast();
  const dispatch = useDispatch();
  const { options } = usePaths();
  const { api } = useAPI();
  const toast = useToast();
  const callToast = (
    title: string,
    description: string,
    status: AlertStatus
  ) => {
    toast({
      title: title,
      description: description,
      status: status,
      duration: 3000,
      isClosable: true,
    });
  };

  const refreshJwt = async () => {
    try {
      const res = await axios.post(
        "http://localhost:8080/refresh-token",
        {},
        { withCredentials: true }
      );
      const { accessToken, role } = res.data;
      const newLoginState = {
        isLoggedIn: loginState.isLoggedIn,
        role: role,
        token: accessToken,
      };
      dispatch(login(newLoginState));
      return accessToken;
    } catch (err){
      console.log("Failed to refresh access token.");
      channel.postMessage({ type: "LOGOUT" });
      throw err;
    }
  };

  const loginWIthJwt = async () => {
    try {
      const accessToken = await refreshJwt();
      const res = await axios.get("http://localhost:8080/jwt-login", {
        headers: {
          Authorization: "Bearer " + accessToken,
        },
      });
      const newLoginState = {
        isLoggedIn: true,
        role: res.data.role,
        token: accessToken,
      };
      dispatch(login(newLoginState));
      redirect();
    } catch (err) {
      console.log("Failed to login with jwt.");
      throw err;
    }
  };

  const generateCaptcha = async () => {
    await axios
      .get("http://localhost:8080/captcha/generate-captcha")
      .then(({ data }) => {
        dispatch(setUrl(`data:image/png;base64,${data.captcha}`));
        dispatch(setCaptchaId(data.captchaId));
      });
  };

  const loginWithCredentials = async ({
    email,
    password,
    captchaAnswer,
  }: loginForm) => {
    await axios
      .post(
        "http://localhost:8080/login",
        { email, password, captchaAnswer, captchaId },
        { withCredentials: true }
      )
      .then((res) => {
        const newLoginState = {
          isLoggedIn: true,
          role: res.data.role,
          token: res.data.accessToken,
        };
        dispatch(login(newLoginState));
        callToast("Login Successful", "You are now logged in.", "success");
        redirect();
      })
      .catch((err: AxiosError<errorResponse>) => {
        callToast(
          "Login Failed",
          err.response ? err.response.data.detail : "Login failed",
          "error"
        );
        generateCaptcha();
      });
  };

  const redirect = () => {
    const prevpage = localStorage.page ? parseInt(localStorage.page) : 0;
    dispatch(setSelectedPage(prevpage));
    const path = prevpage >= 0 ? options[prevpage].path : options[0].path;
    navigate(path);
  };

  const logout = async () => {
    await api.post(
      "/logout",
      {},
      {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
        withCredentials: true,
      }
    );
    channel.postMessage({ type: "LOGOUT" });
  };

  return {
    logout,
    refreshJwt,
    redirect,
    loginWithCredentials,
    generateCaptcha,
    loginWIthJwt,
  };
};

export default useAuth;
