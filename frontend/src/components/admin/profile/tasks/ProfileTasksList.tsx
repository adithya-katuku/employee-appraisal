import { useSelector } from "react-redux";
import { RootState } from "../../../../stores/store";
import { Box } from "@chakra-ui/react";
import ProfileTask from "./ProfileTask";

const ProfileTasksList = () => {
  const tasks = useSelector((state: RootState) => state.store.tasks);
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
