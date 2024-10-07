import { useDispatch, useSelector } from "react-redux";
import {
  RootState,
  setAppraisalRequestDetails,
  setAppraisalRequests,
  setAppraisals,
  setTasks,
} from "../stores/store";
import StartAppraisalModel from "../models/admin/appraisal-requests/StartAppraisalModel";
import RateAttributeModel from "../models/admin/appraisal-requests/RateAttributesModel";
import RateTaskModel from "../models/admin/appraisal-requests/RateTaskModel";
import useAPI from "./useAPI";

const useAdmin = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const dispatch = useDispatch();
  const { api } = useAPI();

  const startAppraisal = async (
    employeeId: number,
    startAppraisal: StartAppraisalModel
  ) => {
    await api
      .put(
        loginState.role + "/appraisal/" + employeeId,
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
      });
  };

  const fetchEmployeeTasks = async (employeeId: number) => {
    await api
      .get(loginState.role + "/employee/" + employeeId + "/tasks", {
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

  const fetchEmployeeAppraisals = async (employeeId: number) => {
    await api
      .get(loginState.role + "/employee/" + employeeId + "/appraisals", {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(setAppraisals(res.data));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const fetchAppraisalRequests = async () => {
    await api
      .get(loginState.role + "/appraisal-requests", {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(setAppraisalRequests(res.data));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const fetchAppraisalRequestDetails = async (appraisalId: number) => {
    await api
      .get(loginState.role + "/appraisal-requests/" + appraisalId, {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
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
            fullyRated: fullyRated,
          })
        );
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const rateTask = async (rateTask: RateTaskModel) => {
    await api
      .put(
        loginState.role + "/appraisal-requests/tasks",
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
      });
  };

  const rateAttributes = async ({
    appraisalId,
    attributes,
  }: RateAttributeModel) => {
    await api
      .put(
        loginState.role + "/appraisal-requests/" + appraisalId + "/attributes",
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
    await api
      .put(
        loginState.role + "/appraisal-requests/" + appraisalId,
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
