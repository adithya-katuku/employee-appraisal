import {
  Button,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
} from "@chakra-ui/react";
import ProfileTasksList from "../tasks/ProfileTasksList";
import { RootState } from "../../../../stores/store";
import { useSelector } from "react-redux";

interface Props {
  isOpen: boolean;
  onClose: () => void;
}

const ViewTasksModal = ({ isOpen, onClose }: Props) => {
  const tasks = useSelector((state: RootState) => state.store.tasks);
  return (
    <Modal isOpen={isOpen} onClose={onClose} size="2xl">
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>
          Employee Tasks:
          <ModalCloseButton />
        </ModalHeader>
        <ModalBody>
          <ProfileTasksList tasks={tasks} />
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

export default ViewTasksModal;
