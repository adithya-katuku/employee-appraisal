import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { useDispatch, useSelector } from "react-redux";
import { login, RootState } from "../stores/store";
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

  const refreshAccessToken = async (): Promise<string> => {
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
    } catch (err) {
      console.log("Failed to refresh token", err);
      channel.postMessage({ type: "LOGOUT" });
      throw err;
    }
  };

  api.interceptors.request.use(
    async (config) => {
      const accessToken = loginState.token;
      if (accessToken) {
        const decodedToken = jwtDecode<JwtPayload>(accessToken);
        const isTokenExpired = decodedToken.exp * 1000 < Date.now();
        if (isTokenExpired) {
          const newAccessToken = await refreshAccessToken();
          config.headers["Authorization"] = `Bearer ${newAccessToken}`;
        } else {
          config.headers["Authorization"] = `Bearer ${accessToken}`;
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
          const newAccessToken = await refreshAccessToken();

          originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;
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
