import { useSelector } from "react-redux";
import { RootState } from "../../stores/store";
import { Box, Select } from "@chakra-ui/react";
import useData from "../../hooks/useData";
import { useEffect } from "react";
import ProfileView from "./ProfileView";

const SelectEmployee = () => {
  const searchedEmployees = useSelector(
    (state: RootState) => state.store.searchedEmployees
  );
  const employeeDetails = useSelector(
    (state: RootState) => state.store.searchedEmployeeDetails
  );
  const { setSearchedEmployeeDetails } = useData();

  const handleChange = (index: number) => {
    if (searchedEmployees) {
      setSearchedEmployeeDetails(searchedEmployees[index]);
    }
  };
  useEffect(() => {
    if (searchedEmployees) {
      setSearchedEmployeeDetails(searchedEmployees[0]);
    }
  }, [searchedEmployees]);

  return searchedEmployees ? (
    <Box my="2"  >
      <Select
        onChange={(e) => {
          handleChange(parseInt(e.target.value));
        }}
      >
        {searchedEmployees.map((employee, index) => {
          return (
            <option value={index} key={index}>
              {employee.name} [{employee.employeeId}]
            </option>
          );
        })}
      </Select>
      <Box my="2" >
        {employeeDetails && (
          <ProfileView employeeDetails={employeeDetails} />
        )}
      </Box>
    </Box>
  ) : (
    <></>
  );
};

export default SelectEmployee;
