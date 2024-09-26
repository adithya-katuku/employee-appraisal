import { Flex } from "@chakra-ui/react"
import EditTask from "../../buttons/task/EditTask"
import TaskModel from "../../models/TaskModel"
import DeleteTask from "../../buttons/task/DeleteTask"

const ModifyTaskOptions = (task:TaskModel) => {
  return (
    <Flex  w="100%" p="1" gap="1" justifyContent="end">
      <EditTask {...task} />

      <DeleteTask taskId={task.taskId} disabled={!task.editable}/>
      
    </Flex>
  )
}

export default ModifyTaskOptions