import { Button } from "@chakra-ui/react";
import { MdDelete } from "react-icons/md";
import useTasks from "../../../hooks/useTasks";

interface Props{
    taskId:number;
    disabled:boolean;
}
const DeleteTask = ({taskId, disabled}:Props) => {
  const {deleteTask} = useTasks();

  const handleClick = async () => {
    deleteTask(taskId)
  };
  return (
    <>
      <Button  colorScheme="red"  h="1.25rem" p="0" isDisabled={disabled} onClick={handleClick}>
        <MdDelete fontSize="1.15rem" />
      </Button>
    </>
  );
};

export default DeleteTask;
