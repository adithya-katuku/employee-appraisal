import { Box, Button, useDisclosure } from "@chakra-ui/react";
import NewTaskModal from "../modals/NewTaskModal";

const AddTask = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();

  return (
    <Box>
      <Button
        m="1"
        p="2"
        onClick={onOpen}
        border="1px"
        borderColor="gray.400"
        colorScheme="blue"
      >
        Create Task
      </Button>

      <NewTaskModal isOpen={isOpen} onClose={onClose} />
    </Box>
  );
};

export default AddTask;
