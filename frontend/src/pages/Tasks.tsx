import { Box, Flex, HStack, Text } from "@chakra-ui/react";
import { ChevronRightIcon } from "@chakra-ui/icons";
import { IoHome } from "react-icons/io5";
import { useEffect } from "react";
import { useSelector } from "react-redux";
import { RootState } from "../stores/store";
import TaskList from "../components/task/TaskList";
import AddTask from "../components/task/buttons/CreateTask";
import useTasks from "../hooks/useTasks";

const Tasks = () => {
  const tasks = useSelector((state: RootState) => state.store.tasks);
  const { fetchTasks } = useTasks();

  useEffect(() => {
    localStorage.page = 1;
    fetchTasks();
  }, []);

  return (
    <Box>
      <HStack mb="2">
        <IoHome />
        <ChevronRightIcon />
        <Text>Tasks</Text>
      </HStack>
      <Flex justifyContent="center">{tasks && <TaskList tasks={tasks} />}</Flex>

      <Flex justifyContent="end" position="sticky" bottom="3">
        <AddTask />
      </Flex>
    </Box>
  );
};

export default Tasks;
