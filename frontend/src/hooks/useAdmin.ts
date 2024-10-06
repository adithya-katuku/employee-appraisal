import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import {
  RootState,
  setAppraisalRequestDetails,
  setAppraisalRequests,
  setAppraisals,
  setTasks,
} from "../stores/store";
import axios from "axios";
import StartAppraisalModel from "../models/admin/appraisal-requests/StartAppraisalModel";
import RateAttributeModel from "../models/admin/appraisal-requests/RateAttributesModel";
import RateTaskModel from "../models/admin/appraisal-requests/RateTaskModel";

const useAdmin = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const startAppraisal = async (
    employeeId: number,
    startAppraisal: StartAppraisalModel
  ) => {
    await axios
      .put(
        "http://localhost:8080/" + loginState.role + "/appraisal/" + employeeId,
        { ...startAppraisal },
        {
          headers: {
            Authorization: "Bearer " + loginState.token,
          },
        }
      )
      .then(() => {})
      .catch((err) => {
        console.log(err);
        navigate("/login");
      });
  };

  const fetchEmployeeTasks = async (employeeId: number) => {
    await axios
      .get(
        "http://localhost:8080/" +
          loginState.role +
          "/employee/" +
          employeeId +
          "/tasks",
        {
          headers: {
            Authorization: "Bearer " + loginState.token,
          },
        }
      )
      .then((res) => {
        dispatch(setTasks(res.data));
      })
      .catch((err) => {
        console.log(err);
        navigate("/login");
      });
  };

  const fetchEmployeeAppraisals = async (employeeId: number) => {
    await axios
      .get(
        "http://localhost:8080/" +
          loginState.role +
          "/employee/" +
          employeeId +
          "/appraisals",
        {
          headers: {
            Authorization: "Bearer " + loginState.token,
          },
        }
      )
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
        navigate("/login");
      });
  };

  const fetchAppraisalRequestDetails = async (appraisalId: number) => {
    await axios
      .get(
        "http://localhost:8080/" +
          loginState.role +
          "/appraisal-requests/" +
          appraisalId,
        {
          headers: {
            Authorization: "Bearer " + loginState.token,
          },
        }
      )
      .then((res) => {
        const employeeDetails = res.data.employeeResponseDTO;
        const attributes = res.data.attributes;
        const tasks = res.data.tasks;
        const fullyRated = res.data.fullyRated;
        dispatch(
          setAppraisalRequestDetails({
            employeeDetails: employeeDetails,
            attributes: attributes,
            tasks: tasks,
            fullyRated:fullyRated,
          })
        );
      })
      .catch((err) => {
        console.log(err);
        navigate("/login");
      });
  };

  const rateTask = async (rateTask: RateTaskModel) => {
    await axios
      .put(
        "http://localhost:8080/" +
          loginState.role +
          "/appraisal-requests/tasks",
        { ...rateTask },
        {
          headers: {
            Authorization: "Bearer " + loginState.token,
          },
        }
      )
      .then(() => {
        // fetchAppraisalRequests()
      })
      .catch((err) => {
        console.log(err);
        navigate("/login");
      });
  };

  const rateAttributes = async ({
    appraisalId,
    attributes,
  }: RateAttributeModel) => {
    await axios
      .put(
        "http://localhost:8080/" +
          loginState.role +
          "/appraisal-requests/" +
          appraisalId +
          "/attributes",
        { attributes: [...attributes] },
        {
          headers: {
            Authorization: "Bearer " + loginState.token,
          },
        }
      )
      .then(() => {
        // fetchAppraisalRequests()
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const submitRating = async (appraisalId: number) => {
    await axios
      .put(
        "http://localhost:8080/" +
          loginState.role +
          "/appraisal-requests/" +
          appraisalId,
        {},
        {
          headers: {
            Authorization: "Bearer " + loginState.token,
          },
        }
      )
      .then(() => {
        fetchAppraisalRequests();
      })
      .catch((err) => {
        console.log(err);
      });
  };
  return {
    startAppraisal,
    fetchEmployeeTasks,
    fetchEmployeeAppraisals,
    fetchAppraisalRequestDetails,
    fetchAppraisalRequests,
    rateTask,
    rateAttributes,
    submitRating,
  };
};

export default useAdmin;
