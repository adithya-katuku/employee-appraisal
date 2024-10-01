import { useSelector } from "react-redux";
import EmployeeDetailsModel from "../../models/EmployeeDetailsModel";
import { RootState } from "../../stores/store";
import AdminEmployeeProfileView from "../admin/people-profile/AdminEmployeeProfileView";
import EmployeeDetails from "../employee/EmployeeDetails";

interface Props {
  employeeDetails: EmployeeDetailsModel;
}

const ProfileView = ({ employeeDetails }: Props) => {
  const role = useSelector((state: RootState) => state.store.loginState.role);
  return role === "admin" ? (
    <AdminEmployeeProfileView employeeDetails={employeeDetails} />
  ) : (
    <EmployeeDetails employeeDetails={employeeDetails} />
  );
};

export default ProfileView;
