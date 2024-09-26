import { Box } from "@chakra-ui/react";
import TaskModel from "../../models/TaskModel";
import Task from "./Task";

interface Props{
    tasks: TaskModel[]
}

const TaskList = ({tasks}: Props) => {
  return (
    <Box>
      {tasks &&
        tasks.map((task, index) => {
          return <Task {...task} key={index} />;
        })}
    </Box>
  );
};

export default TaskList;
