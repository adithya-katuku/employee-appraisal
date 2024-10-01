import { useSelector } from "react-redux";
import { RootState } from "../stores/store";
import { MdDashboard, MdRateReview } from "react-icons/md";
import { FaTasks } from "react-icons/fa";
import { FaUserGroup } from "react-icons/fa6";
import { IoMdPersonAdd } from "react-icons/io";

const usePaths = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const options = [
    {
      name: "Dashboard",
      path: `/${loginState.role}/home`,
      icon: <MdDashboard color="rgb(0, 50, 200)" />,
    },
    {
      name: "Tasks",
      path: `/${loginState.role}/tasks`,
      icon: <FaTasks color="rgb(0, 50, 200)" />,
    },
    {
      name: "People",
      path: `/${loginState.role}/people`,
      icon: <FaUserGroup color="rgb(0, 50, 200)" />,
    },
    loginState.role === "admin"
      ? {
          name: "Appraisal Requests",
          path: `/${loginState.role}/appraisal-requests`,
          icon: <MdRateReview color="rgb(0, 50, 200)" />,
        }
      : {
          name: "Appraisal",
          path: `/${loginState.role}/appraisal`,
          icon: <MdRateReview color="rgb(0, 50, 200)" />,
        },
  ];

  if(loginState.role === "admin"){
    options.push(
      {
        name: "Create User",
        path: `/${loginState.role}/create-user`,
        icon: <IoMdPersonAdd color="rgb(0, 50, 200)" />,
      }
    )
  }

  return { options };
};

export default usePaths;
