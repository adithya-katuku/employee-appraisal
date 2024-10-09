import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { useDispatch, useSelector } from "react-redux";
import { login, RootState, store } from "../stores/store";
import useBroadcast from "./useBroadcast";

interface JwtPayload {
  exp: number;
}

const useAPI = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const dispatch = useDispatch();
  const channel = useBroadcast();

  const api = axios.create({
    baseURL: "http://localhost:8080",
  });

  const refreshAccessToken = async (): Promise<{accessToken:string, role:string}> => {
    try {
      const res = await axios.post(
        "http://localhost:8080/refresh-token",
        {},
        { withCredentials: true }
      );
      const { accessToken, role } = res.data;
      await refreshLogin(accessToken, role);
      return { accessToken, role };
    } catch (err) {
      console.log("Failed to refresh token", err);
      channel.postMessage({ type: "LOGOUT" });
      throw err;
    }
  };

  const refreshLogin = async(accessToken:string, role:string)=>{
    const newLoginState = {
      isLoggedIn: loginState.isLoggedIn,
      role: role,
      token: accessToken,
    };
    dispatch(login(newLoginState));
    console.log(store.getState()); 
  }

  api.interceptors.request.use(
    async (config) => {
      const existingAccessToken = store.getState().store.loginState.token;
      if (existingAccessToken) {
        const decodedToken = jwtDecode<JwtPayload>(existingAccessToken);
        const isTokenExpired = decodedToken.exp * 1000 < Date.now();
        if (isTokenExpired) {
          console.log("here", decodedToken.exp, decodedToken);
          const { accessToken } = await refreshAccessToken();
          config.headers["Authorization"] = `Bearer ${accessToken}`;
        } else {
          config.headers["Authorization"] = `Bearer ${existingAccessToken}`;
        }
      }
      return config;
    },
    (error) => Promise.reject(error)
  );

  api.interceptors.response.use(
    (response) => response,
    async (error) => {
      const originalRequest = error.config;

      if (
        error.response &&
        error.response.status === 401 &&
        !originalRequest._retry
      ) {
        try {
          originalRequest._retry = true;
          const { accessToken, role } = await refreshAccessToken();
          const newLoginState = {
            isLoggedIn: loginState.isLoggedIn,
            role: role,
            token: accessToken,
          };
          dispatch(login(newLoginState));
          originalRequest.headers["Authorization"] = `Bearer ${accessToken}`;
          return api(originalRequest);
        } catch (err) {
          console.log(err);
        }
      }

      return Promise.reject(error);
    }
  );

  return { api };
};

export default useAPI;
