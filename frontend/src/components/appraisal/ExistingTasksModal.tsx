import {
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalCloseButton,
  ModalBody,
  ModalFooter,
  Button,
} from "@chakra-ui/react";
import { useSelector } from "react-redux";
import { RootState } from "../../stores/store";
import TaskList from "../task/TaskList";

interface Props {
  isOpen: boolean;
  onClose: () => void;
}

const ExistingTasksModal = ({ isOpen, onClose}: Props) => {
  const tasks = useSelector((state: RootState) => state.store.tasks);
  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>Add Existing Tasks</ModalHeader>
        <ModalCloseButton />
        <ModalBody>
            {tasks && <TaskList tasks={tasks.filter(task=>!task.appraisable)}/>}
        </ModalBody>

        <ModalFooter>
          <Button variant="outline" colorScheme="red" mr={3} onClick={onClose}>
            Cancel
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
};

export default ExistingTasksModal;
