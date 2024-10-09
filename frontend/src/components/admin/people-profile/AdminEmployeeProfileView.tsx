import { Box } from "@chakra-ui/react";
import EmployeeDetailsModel from "../../../models/EmployeeDetailsModel";
import EmployeeDetails from "../../employee/EmployeeDetails";
import AdminEmployeeControls from "./AdminEmployeeControls";
import { createContext } from "react";

interface Props {
  employeeDetails: EmployeeDetailsModel;
}
export const EmployeeDetailsContext = createContext<{
  employeeId: number;
  previousAppraisalDate?: string;
}>({
  employeeId: -1,
  previousAppraisalDate: undefined,
});
const AdminEmployeeProfileView = ({ employeeDetails }: Props) => {
  return (
    <Box>
      <EmployeeDetails employeeDetails={employeeDetails} />
      {employeeDetails.appraisalEligibility && (
        <EmployeeDetailsContext.Provider value={employeeDetails}>
          <AdminEmployeeControls />
        </EmployeeDetailsContext.Provider>
      )}
    </Box>
  );
};

export default AdminEmployeeProfileView;
