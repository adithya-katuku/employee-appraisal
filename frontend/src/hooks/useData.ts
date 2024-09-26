import axios from "axios";
import { setAppraisals, setTasks } from "../stores/store";
import { useDispatch } from "react-redux";

const useData = () => {
  const dispatch = useDispatch();
  const fetchTasks = async () => {
    await axios
      .get("http://localhost:8080/" + localStorage.role + "/tasks", {
        headers: {
          Authorization: "Bearer " + sessionStorage.jwt,
        },
      })
      .then((res) => {
        dispatch(setTasks(res.data));
      })
      .catch((err) => console.log(err));
  };

  const fetchAppraisals = async () => {
    await axios
      .get("http://localhost:8080/" + localStorage.role + "/appraisals", {
        headers: {
          Authorization: "Bearer " + sessionStorage.jwt,
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
