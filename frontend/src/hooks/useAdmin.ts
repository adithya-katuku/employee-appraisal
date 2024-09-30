import {  useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { RootState, setAppraisals, setTasks } from "../stores/store";
import axios from "axios";
import StartAppraisalModal from "../models/admin/StartAppraisalModal";

const useAdmin = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const navigate = useNavigate();
    const dispatch = useDispatch();
  const startAppraisal = async (employeeId:number, startAppraisal:StartAppraisalModal) => {
    await axios
      .put("http://localhost:8080/" + loginState.role + "/appraisal/"+employeeId, {...startAppraisal}, {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then(() => {

      })
      .catch((err) => {
        console.log(err);
        navigate("/login");
      });
  };

  const fetchEmployeeTasks = async (employeeId:number) => {
    await axios
      .get("http://localhost:8080/" + loginState.role + "/employee/"+employeeId +"/tasks", {
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

  const fetchEmployeeAppraisals = async (employeeId:number) => {
    await axios
      .get("http://localhost:8080/" + loginState.role + "/employee/"+employeeId + "/appraisals", {
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

  return {startAppraisal, fetchEmployeeTasks, fetchEmployeeAppraisals};
};

export default useAdmin;
