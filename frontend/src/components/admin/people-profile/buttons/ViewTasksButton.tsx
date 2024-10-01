import { Box, Button, useDisclosure } from "@chakra-ui/react";
import useAdmin from "../../../../hooks/useAdmin";
import { useContext } from "react";
import { EmployeeDetailsContext } from "../AdminEmployeeProfileView";
import ViewTasksModal from "../modals/ViewTasksModal";

const ViewTasksButton = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const { employeeId } = useContext(EmployeeDetailsContext);
  const { fetchEmployeeTasks } = useAdmin();
  const handleClick = () => {
    fetchEmployeeTasks(employeeId);
    onOpen();
  };
  return (
    <Box>
      <Button colorScheme="blue" onClick={handleClick}>
        View Tasks
      </Button>

      <ViewTasksModal isOpen={isOpen} onClose={onClose} />
    </Box>
  );
};

export default ViewTasksButton;
