import axios from "axios";
import {
    appendTask,
  RootState,
  setTasks,
  updateTask,
} from "../stores/store";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import TaskModel from "../models/TaskModel";

const useTasks = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const dispatch = useDispatch();
  const navigate = useNavigate();

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
      .catch((err) => {
        console.log(err);
        navigate("/login");
      });
  };

  const addTask = async (task:TaskModel) => {
    await axios
      .post("http://localhost:8080/" + localStorage.role + "/tasks", task, {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(appendTask(res.data));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const editTask = async (task:TaskModel) => {
    await axios
      .put("http://localhost:8080/" + localStorage.role + "/tasks", task, {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(updateTask(res.data));
      })
      .catch((err) => {
        console.log(err);
      });
  };


  return {
    fetchTasks,
    editTask,
    addTask,
  };
};

export default useTasks;
