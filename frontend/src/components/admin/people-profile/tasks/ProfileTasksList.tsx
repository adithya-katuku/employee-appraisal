import { Box, Text } from "@chakra-ui/react";
import ProfileTask from "./ProfileTask";
import TaskModel from "../../../../models/TaskModel";

interface Props{
  tasks:TaskModel[] |undefined
}
const ProfileTasksList = ({tasks}:Props) => {
  return tasks && tasks[0] ? (
    <Box>
      {tasks.map((task, index) => {
        return <ProfileTask task={task} key={index} />;
      })}
    </Box>
  ) : (
    <Text>No tasks yet :)</Text>
  );
};

export default ProfileTasksList;
