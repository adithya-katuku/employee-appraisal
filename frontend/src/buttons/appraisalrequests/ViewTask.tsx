import { Button, useDisclosure } from "@chakra-ui/react";
import { FaEye } from "react-icons/fa";
import TaskModel from "../../models/TaskModel";
import RateTaskModal from "../../components/appraisalrequests/tasks/RateTaskModal";

interface Props{
    task:TaskModel;
    appraisalId:number;
}
const ViewTask = ({task, appraisalId}:Props) => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  return (
    <>
      <Button colorScheme="cyan" h="1.25rem" p="0" onClick={onOpen}>
        <FaEye fontSize="1.15rem" />
      </Button>

      <RateTaskModal isOpen={isOpen} onClose={onClose} task={task} appraisalId={appraisalId} />
    </>
  );
};

export default ViewTask;
