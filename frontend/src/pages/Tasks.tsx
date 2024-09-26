import { Box, Flex, HStack, Text } from "@chakra-ui/react";
import { ChevronRightIcon } from "@chakra-ui/icons";
import { IoHome } from "react-icons/io5";
import { useEffect } from "react";
import AddTask from "../buttons/task/AddTask";
import { useSelector } from "react-redux";
import { RootState } from "../stores/store";
import TaskList from "../components/task/TaskList";
import useData from "../hooks/useData";

const Tasks = () => {
  const tasks = useSelector((state: RootState) => state.store.tasks);
  const {fetchTasks} = useData();
  
  useEffect(() => {
    localStorage.page = "/tasks";
    fetchTasks();
  }, []);

  return (
    <Box>
      <HStack mb="2">
        <IoHome />
        <ChevronRightIcon />
        <Text>Tasks</Text>
      </HStack>
      <Box>{tasks && <TaskList tasks={tasks} />}</Box>

      <Flex justifyContent="end" position="sticky" bottom="3">
        <AddTask />
      </Flex>
    </Box>
  );
};

export default Tasks;
