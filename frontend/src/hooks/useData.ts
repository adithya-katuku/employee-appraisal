import axios from "axios";
import { RootState, setAppraisals, setTasks } from "../stores/store";
import { useDispatch, useSelector } from "react-redux";

const useData = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const dispatch = useDispatch();
  const fetchTasks = async () => {
    await axios
      .get("http://localhost:8080/" + loginState.role + "/tasks", {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(setTasks(res.data));
      })
      .catch((err) => console.log(err));
  };

  const fetchAppraisals = async () => {
    await axios
      .get("http://localhost:8080/" + loginState.role + "/appraisals", {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(setAppraisals(res.data));
      })
      .catch((err) => console.log(err));
  };

  return { fetchTasks, fetchAppraisals };
};

export default useData;
