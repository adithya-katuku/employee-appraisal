import {
  clearSearchedEmployees,
  RootState,
  setAppraisals,
  setEmployeeDetails,
  setSearchedEmployees,
  setSearchedName,
} from "../stores/store";
import { useDispatch, useSelector } from "react-redux";
import EmployeeDetailsModel from "../models/EmployeeDetailsModel";
import useTasks from "./useTasks";
import useAPI from "./useAPI";

const useData = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const dispatch = useDispatch();
  const {fetchTasks} = useTasks();
  const { api } = useAPI();

  const fetchInfo = async () => {
    await api
      .get(loginState.role + "/info", {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(setEmployeeDetails(res.data));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const fetchAppraisals = async () => {
    await api
      .get(loginState.role + "/appraisals", {
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

  const submitAppraisal = async (appraisalId: number) => {
    await api
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
    await api
      .get(loginState.role + "/people/" + name, {
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
    fetchAppraisals,
    searchEmployees,
    setSearchedEmployeeDetails,
    clearEmployees,
    submitAppraisal,
  };
};

export default useData;
