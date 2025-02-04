import { Box, Flex, HStack, Spacer, Text, VStack } from "@chakra-ui/react";
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
    <Flex minH="100%" flexDir="column">
      <HStack mb="2">
        <IoHome />
        <ChevronRightIcon />
        <Text>Tasks</Text>
      </HStack>
        <VStack maxW="60rem" ><Box minW="100%" >{tasks && <TaskList tasks={tasks} />}</Box> </VStack>
      <Spacer />
      <Flex justifyContent="end" position="sticky" bottom="3">
        <AddTask />
      </Flex>
    </Flex>
  );
};

export default Tasks;
