import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { RootState, setAttributes, setDesignations } from "../stores/store";
import axios from "axios";
import DesignationModel from "../models/admin/register-employee/DesignationModel";
import RegisterUserModel from "../models/admin/register-employee/RegisterUserModel";

const useRegister = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const fetchDesignations = async () => {
    await axios
      .get("http://localhost:8080/" + loginState.role + "/all-designations", {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(setDesignations(res.data));
      })
      .catch((err) => {
        console.log(err);
        navigate("/login");
      });
  };

  const fetchAttributes = async () => {
    await axios
      .get("http://localhost:8080/" + loginState.role + "/all-attributes", {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then((res) => {
        dispatch(setAttributes(res.data));
      })
      .catch((err) => {
        console.log(err);
        navigate("/login");
      });
  };

  const saveDesignation = async (designation:DesignationModel) => {
    await axios
      .post("http://localhost:8080/" + loginState.role + "/all-designations", {...designation}, {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then(() => {
        fetchDesignations();
      })
      .catch((err) => {
        console.log(err);
        navigate("/login");
      });
  };
  const saveUser = async (user:RegisterUserModel) => {
    await axios
      .post("http://localhost:8080/" + loginState.role + "/register", {...user}, {
        headers: {
          Authorization: "Bearer " + loginState.token,
        },
      })
      .then(() => {
        fetchDesignations();
      })
      .catch((err) => {
        console.log(err);
        navigate("/login");
      });
  };

  return {fetchDesignations, fetchAttributes, saveDesignation, saveUser};
};

export default useRegister;
