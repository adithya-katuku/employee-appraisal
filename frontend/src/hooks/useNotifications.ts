import axios from "axios";
import { RootState, setNotifications, setSearchedName, setSelectedPage } from "../stores/store";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import NotificationModel from "../models/NotificationModel";
// import useData from "./useData";

const useNotifications = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const navigate = useNavigate();
  const dispatch = useDispatch();

//   const {searchEmployees} = useData();

  const fetchNotifications = async () => {
    await axios
      .get("http://localhost:8080/" + loginState.role + "/notifications", {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(setNotifications(res.data));
      })
      .catch((err) => {
        console.log(err);
        navigate("/login");
      });
  };
  const deleteNotifications = async (notificationId: number) => {
    await axios
      .delete(
        "http://localhost:8080/" +
          loginState.role +
          "/notifications/" +
          notificationId,
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
        navigate("/login");
      });
  };
  const markNotificationsRead = async (notificationId: number) => {
    await axios
      .put(
        "http://localhost:8080/" +
          loginState.role +
          "/notifications/" +
          notificationId,
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
        navigate("/login");
      });
  };

  const notificationRedirect = async(notification:NotificationModel) => {
    if(notification.notificationTitle==="Appraisal Review"){
        dispatch(setSelectedPage(3));
        navigate(`/${loginState.role}/appraisal-requests`);
    }

    if(notification.notificationTitle==="Pending Appraisal"){
        dispatch(setSelectedPage(2));
        dispatch(setSearchedName(`${notification.fromId}`));
        // searchEmployees(`${notification.fromId}`);
        navigate(`/${loginState.role}/people`);
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
