import { Flex } from "@chakra-ui/react";
import StartAppraisalButton from "./buttons/StartAppraisalButton";
import ViewTasksButton from "./buttons/ViewTasksButton";
import PreviousAppraisalsButton from "./buttons/PreviousAppraisalsButton";

const AdminEmployeeControls = () => {
  return (
    <Flex
      flexDir={{ base: "column", md: "row" }}
      justifyContent={{ base: "flex-start", md: "flex-end" }}
      alignItems={{ base: "flex-end", md: "flex-start" }}
      gap="2"
      rowGap="2"
    >
      <PreviousAppraisalsButton />
      <ViewTasksButton />
      <StartAppraisalButton />
    </Flex>
  );
};

export default AdminEmployeeControls;
