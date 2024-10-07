import {
  appendTask,
  removeTask,
  RootState,
  setTasks,
  updateTask,
} from "../stores/store";
import { useDispatch, useSelector } from "react-redux";
import TaskModel from "../models/TaskModel";
import useAPI from "./useAPI";

const useTasks = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const dispatch = useDispatch();
  const { api } = useAPI();

  const addTask = async (task: TaskModel) => {
    await api
      .post(loginState.role + "/tasks", task, {
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

  const fetchTasks = async () => {
    await api
      .get(loginState.role + "/tasks", {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(setTasks(res.data));
      })
      .catch((err) => {
        console.log(err);
      });
  };


  const editTask = async (task: TaskModel) => {
    await api
      .put(loginState.role + "/tasks", task, {
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

  const deleteTask = async (taskId: number) => {
    await api
      .delete(loginState.role + "/tasks/" + taskId, {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then(() => {
        dispatch(removeTask(taskId));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return {
    addTask,
    fetchTasks,
    editTask,
    deleteTask,
  };
};

export default useTasks;
