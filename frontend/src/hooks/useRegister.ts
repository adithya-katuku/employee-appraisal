import { useSelector, useDispatch } from "react-redux";
import { RootState, setAttributes, setDesignations } from "../stores/store";
import DesignationModel from "../models/admin/register-employee/DesignationModel";
import RegisterUserModel from "../models/admin/register-employee/RegisterUserModel";
import useAPI from "./useAPI";

const useRegister = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const dispatch = useDispatch();
  const {api} = useAPI();

  const fetchDesignations = async () => {
    await api
      .get(loginState.role + "/all-designations", {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(setDesignations(res.data));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const fetchAttributes = async () => {
    await api
      .get(loginState.role + "/all-attributes", {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(setAttributes(res.data));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const saveDesignation = async (designation:DesignationModel) => {
    await api
      .post(loginState.role + "/all-designations", {...designation}, {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then(() => {
        fetchDesignations();
      })
      .catch((err) => {
        console.log(err);
      });
  };
  const saveUser = async (user:RegisterUserModel) => {
    try{
      await api
      .post(loginState.role + "/register", {...user}, {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then(() => {
        fetchDesignations();
      })
    }
    catch (err){
      console.log(err);
      throw err;
    }
    
  };

  return {fetchDesignations, fetchAttributes, saveDesignation, saveUser};
};

export default useRegister;
