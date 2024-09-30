import { Flex } from "@chakra-ui/react";
import StartAppraisalButton from "./buttons/StartAppraisalButton";
import ViewTasksButton from "./buttons/ViewTasksButton";
import PreviousAppraisalsButton from "./buttons/PreviousAppraisalsButton";

const AdminEmployeeControls = () => {
  return (
    <Flex justifyContent="space-around">
      <PreviousAppraisalsButton />
      <ViewTasksButton />

      <StartAppraisalButton />
    </Flex>
  );
};

export default AdminEmployeeControls;
