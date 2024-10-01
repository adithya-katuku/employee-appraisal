import { Button, useDisclosure } from "@chakra-ui/react";
import { FaEdit } from "react-icons/fa";
import TaskModel from "../../../models/TaskModel";
import EditTaskModal from "../modals/EditTaskModal";

const EditTask = (task: TaskModel) => {
  const { isOpen, onOpen, onClose } = useDisclosure();

  return (
    <>
      <Button
        colorScheme="cyan"
        h="1.25rem"
        p="0"
        isDisabled={!task.editable}
        onClick={onOpen}
      >
        <FaEdit fontSize="1.15rem" />
      </Button>

      <EditTaskModal isOpen={isOpen} onClose={onClose} task={task} />
    </>
  );
};

export default EditTask;
