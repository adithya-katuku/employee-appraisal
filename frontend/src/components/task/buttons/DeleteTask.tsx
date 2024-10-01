import { Button } from "@chakra-ui/react";
import axios from "axios";
import { MdDelete } from "react-icons/md";
import { useDispatch } from "react-redux";
import { deleteTask } from "../../../stores/store";

interface Props{
    taskId:number;
    disabled:boolean;
}
const DeleteTask = ({taskId, disabled}:Props) => {
  const dispatch = useDispatch();

  const handleClick = async () => {
    await axios
      .delete(
        "http://localhost:8080/" + localStorage.role + "/tasks/" + taskId,
        {
          headers: {
            Authorization: "Bearer " + sessionStorage.jwt,
          },
        }
      )
      .then(() => {
        console.log("deleted");
        dispatch(deleteTask(taskId));
      })
      .catch((err) => {
        console.log(err);
      });
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
