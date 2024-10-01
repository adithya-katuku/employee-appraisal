import { Box } from "@chakra-ui/react";
import ProfileTask from "./ProfileTask";
import TaskModel from "../../../../models/TaskModel";

interface Props{
  tasks:TaskModel[] |undefined
}
const ProfileTasksList = ({tasks}:Props) => {
  return tasks ? (
    <Box>
      {tasks.map((task, index) => {
        return <ProfileTask task={task} key={index} />;
      })}
    </Box>
  ) : (
    <></>
  );
};

export default ProfileTasksList;
