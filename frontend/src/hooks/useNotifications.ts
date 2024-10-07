import {
  RootState,
  setNotifications,
  setSearchedName,
  setSelectedPage,
} from "../stores/store";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import NotificationModel from "../models/NotificationModel";
import useAPI from "./useAPI";

const useNotifications = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { api } = useAPI();

  const fetchNotifications = async () => {
    await api
      .get(loginState.role + "/notifications", {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(setNotifications(res.data));
      })
      .catch((err) => {
        console.log(err);
      });
  };
  const deleteNotifications = async (notificationId: number) => {
    await api
      .delete(loginState.role + "/notifications/" + notificationId, {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then(() => {
        fetchNotifications();
      })
      .catch((err) => {
        console.log(err);
      });
  };
  const markNotificationsRead = async (notificationId: number) => {
    await api
      .put(
        loginState.role + "/notifications/" + notificationId,
        {},
        {
          headers: {
            Authorization: "Bearer " + loginState.token,
          },
        }
      )
      .then(() => {
        fetchNotifications();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const notificationRedirect = async (notification: NotificationModel) => {
    if (notification.notificationTitle === "Appraisal Review") {
      dispatch(setSelectedPage(3));
      navigate(`/${loginState.role}/appraisal-requests`);
    }

    if (notification.notificationTitle === "Pending Appraisal") {
      dispatch(setSelectedPage(2));
      dispatch(setSearchedName(`${notification.fromId}`));
      navigate(`/${loginState.role}/people`);
    }

    if (notification.notificationTitle === "Appraisal Inititated!" || notification.notificationTitle === "Appraisal Rated!") {
      dispatch(setSelectedPage(3));
      navigate(`/${loginState.role}/appraisals`);
    }
  };

  return {
    fetchNotifications,
    deleteNotifications,
    markNotificationsRead,
    notificationRedirect,
  };
};

export default useNotifications;
