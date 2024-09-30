import { useSelector } from "react-redux";
import { RootState } from "../stores/store";
import EmployeeDetails from "../components/employee/EmployeeDetails";
import { Box } from "@chakra-ui/react";

const EmployeeDetailsPage = () => {
    const searchedEmployee = useSelector((state: RootState) => state.store.searchedEmployeeDetails);
  return searchedEmployee?(
    <Box>
      <EmployeeDetails employeeDetails={searchedEmployee}/>
    </Box>
  ):<></>
}

export default EmployeeDetailsPage