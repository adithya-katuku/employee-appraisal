import { Flex } from "@chakra-ui/react";
import TaskModel from "../../models/TaskModel";
import DeleteTask from "./buttons/DeleteTask";
import EditTask from "./buttons/EditTask";

const ModifyTaskOptions = (task: TaskModel) => {
  return (
    <Flex w="100%" p="1" gap="1" justifyContent="end">
      {task.taskId && (
        <>
          <EditTask {...task} />
          <DeleteTask taskId={task.taskId} disabled={!task.editable} />
        </>
      )}
    </Flex>
  );
};

export default ModifyTaskOptions;
