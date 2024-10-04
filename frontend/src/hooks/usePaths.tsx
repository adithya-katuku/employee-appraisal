import { useSelector } from "react-redux";
import { RootState } from "../stores/store";
import { MdDashboard, MdRateReview } from "react-icons/md";
import { FaTasks } from "react-icons/fa";
import { FaUserGroup } from "react-icons/fa6";
import { IoMdPersonAdd } from "react-icons/io";

const usePaths = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const employeeRoutes = [
    {
      name: "Dashboard",
      path: `/employee/home`,
      icon: <MdDashboard color="rgb(0, 50, 200)" />,
    },
    {
      name: "Tasks",
      path: `/employee/tasks`,
      icon: <FaTasks color="rgb(0, 50, 200)" />,
    },
    {
      name: "People",
      path: `/employee/people`,
      icon: <FaUserGroup color="rgb(0, 50, 200)" />,
    },
    {
      name: "Appraisals",
      path: `/employee/appraisals`,
      icon: <MdRateReview color="rgb(0, 50, 200)" />,
    },
  ];

  const adminRoutes = [
    {
      name: "Dashboard",
      path: `/admin/home`,
      icon: <MdDashboard color="rgb(0, 50, 200)" />,
    },
    {
      name: "Tasks",
      path: `/admin/tasks`,
      icon: <FaTasks color="rgb(0, 50, 200)" />,
    },
    {
      name: "People",
      path: `/admin/people`,
      icon: <FaUserGroup color="rgb(0, 50, 200)" />,
    },
    {
      name: "Appraisal Requests",
      path: `/admin/appraisal-requests`,
      icon: <MdRateReview color="rgb(0, 50, 200)" />,
    },
    {
      name: "Create User",
      path: `/admin/create-user`,
      icon: <IoMdPersonAdd color="rgb(0, 50, 200)" />,
    }
  ];
  let options = [];
  if(loginState.role==="admin"){
    options = adminRoutes;
  }
  else{
    options = employeeRoutes;
  }
  return { options };
};

export default usePaths;
