import { ChevronRightIcon } from "@chakra-ui/icons";
import { Box, HStack, Text } from "@chakra-ui/react";
import { IoHome } from "react-icons/io5";
import { useSelector } from "react-redux";
import { RootState } from "../stores/store";
import EmployeeDetails from "../components/employee/EmployeeDetails";
import { useEffect } from "react";

const Profile = () => {
  const info = useSelector(
    (state: RootState) => state.store.searchedEmployeeDetails
  );
  useEffect(() => {
    localStorage.page = -1;
  }, []);
  return (
    <Box>
      <HStack mb="2">
        <IoHome />
        <ChevronRightIcon />
        <Text>Profile</Text>
      </HStack>
      <Box m="auto" maxW="60rem">
        <EmployeeDetails employeeDetails={info} />
      </Box>
    </Box>
  );
};

export default Profile;
