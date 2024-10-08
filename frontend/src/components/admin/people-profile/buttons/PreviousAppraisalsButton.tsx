import { Box, Button, useDisclosure } from "@chakra-ui/react";
import { useContext } from "react";
import { EmployeeDetailsContext } from "../AdminEmployeeProfileView";
import PreviousAppriaisalsModal from "../modals/PreviousAppriaisalsModal";
import useAdmin from "../../../../hooks/useAdmin";

const PreviousAppraisalsButton = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const { employeeId } = useContext(EmployeeDetailsContext);
  const { fetchEmployeeTasks, fetchEmployeeAppraisals } = useAdmin();
  const handleClick = () => {
    fetchEmployeeTasks(employeeId);
    fetchEmployeeAppraisals(employeeId);
    onOpen();
  };

  return (
    <Box >
      <Button colorScheme="teal" w={{base:"12rem", md:"fit-content"}}  onClick={handleClick}>
        Previous Appraisals
      </Button>
      <PreviousAppriaisalsModal isOpen={isOpen} onClose={onClose} />
    </Box>
  );
};

export default PreviousAppraisalsButton;
