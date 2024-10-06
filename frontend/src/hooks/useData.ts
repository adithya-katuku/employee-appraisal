import axios from "axios";
import {
  clearSearchedEmployees,
  RootState,
  setAppraisals,
  setEmployeeDetails,
  setSearchedEmployees,
  setSearchedName,
  setTasks,
} from "../stores/store";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import EmployeeDetailsModel from "../models/EmployeeDetailsModel";

const useData = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const fetchInfo = async () => {
    await axios
      .get("http://localhost:8080/" + loginState.role + "/info", {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(setEmployeeDetails(res.data));
      })
      .catch((err) => {
        console.log(err);
        navigate("/login");
      });
  };

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
      .catch((err) => {
        console.log(err);
        navigate("/login");
      });
  };
  const submitAppraisal = async (appraisalId: number) => {
    await axios
      .put(
        "http://localhost:8080/" +
          loginState.role +
          "/appraisals/" +
          appraisalId,
        {},
        {
          headers: {
            Authorization: "Bearer " + loginState.token,
          },
        }
      )
      .then(() => {
        fetchTasks();
        fetchAppraisals();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const searchEmployees = async (name: string) => {
    await axios
      .get("http://localhost:8080/" + loginState.role + "/people/" + name, {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(setSearchedEmployees(res.data));
      })
      .catch((err) => {
        console.log(err);
      });
  };
  const setSearchedEmployeeDetails = (employee: EmployeeDetailsModel) => {
    dispatch(setEmployeeDetails(employee));
  };
  const clearEmployees = () => {
    dispatch(setSearchedName(undefined));
    dispatch(clearSearchedEmployees());
  };
  return {
    fetchInfo,
    fetchTasks,
    fetchAppraisals,
    searchEmployees,
    setSearchedEmployeeDetails,
    clearEmployees,
    submitAppraisal,
  };
};

export default useData;
