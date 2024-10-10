import { Box, Text } from "@chakra-ui/react";
import TaskModel from "../../models/TaskModel";
import Task from "./Task";

interface Props {
  tasks: TaskModel[];
}

const TaskList = ({ tasks }: Props) => {
  return tasks[0] ? (
    <Box flexDir="column" >
      {tasks &&
        tasks.map((task, index) => {
          return <Task {...task} key={index} />;
        })}
    </Box>
  ) : (
    <Text px="2" >No tasks yet :)</Text>
  );
};

export default TaskList;
