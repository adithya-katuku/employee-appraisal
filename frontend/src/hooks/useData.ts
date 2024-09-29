import axios from "axios";
import { RootState, setAppraisalRequestDetails, setAppraisalRequests, setAppraisals, setTasks } from "../stores/store";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import RateTaskModel from "../models/admin/RateTaskModel";
import RateAttributeModel from "../models/admin/RateAttributesModel";

const useData = () => {
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

  const fetchAppraisalRequests = async () => {
    await axios
      .get("http://localhost:8080/" + loginState.role + "/appraisal-requests", {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(setAppraisalRequests(res.data));
      })
      .catch((err) => {
        console.log(err);
        navigate("/login")
      });
  };

  const fetchAppraisalRequestDetails = async (appraisalId:number) => {
    await axios
      .get("http://localhost:8080/" + loginState.role + "/appraisal-requests/"+appraisalId, {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        const employeeDetails = res.data.employeeResponseDTO;
        const attributes = res.data.attributes;
        const tasks = res.data.tasks;
        dispatch(setAppraisalRequestDetails({employeeDetails:employeeDetails, attributes:attributes, tasks:tasks}));
      })
      .catch((err) => {
        console.log(err);
        navigate("/login")
      });
  };

  const rateTask = async(rateTask:RateTaskModel)=>{
    await axios
      .put("http://localhost:8080/" + loginState.role + "/appraisal-requests/tasks", {...rateTask}, {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then(() => {
        // fetchAppraisalRequests()
      })
      .catch((err) => {
        console.log(err);
        navigate("/login")
      });
  }

  const rateAttributes = async({appraisalId, attributes}:RateAttributeModel)=>{
    await axios
      .put("http://localhost:8080/" + loginState.role + "/appraisal-requests/"+appraisalId+"/attributes", {attributes:[...attributes]}, {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then(() => {
        // fetchAppraisalRequests()
      })
      .catch((err) => {
        console.log(err);
      });
  }
  return { fetchTasks, fetchAppraisals, fetchAppraisalRequests, fetchAppraisalRequestDetails, rateTask, rateAttributes };
};

export default useData;
