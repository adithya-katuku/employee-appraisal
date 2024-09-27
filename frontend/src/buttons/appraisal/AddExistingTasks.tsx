import { Box, Button, useDisclosure } from "@chakra-ui/react";
import ExistingTasksModal from "../../components/appraisal/ExistingTasksModal";

const AddExistingTasks = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();

  return (
    <Box>
      <Button
        m="1"
        p="2"
        border="1px"
        borderColor="gray.400"
        colorScheme="green"
        onClick={onOpen}
      >
        Add Existing
      </Button>

      <ExistingTasksModal isOpen={isOpen} onClose={onClose}  />
    </Box>
  );
};

export default AddExistingTasks;
