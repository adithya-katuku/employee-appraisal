import { Box, Flex, HStack, Text } from "@chakra-ui/react";
import Task from "../components/Task";
import { ChevronRightIcon } from "@chakra-ui/icons";
import { IoHome } from "react-icons/io5";
import { useEffect, useState } from "react";
import TaskModel from "../models/TaskModel";
import axios from "axios";
import AddTask from "../buttons/task/AddTask";

const Tasks = () => {
  const [tasks, setTasks] = useState<TaskModel[]>();

  const fetchTasks = async () => {
    await axios
      .get("http://localhost:8080/" + localStorage.role + "/tasks", {
        headers: {
          Authorization: "Bearer " + sessionStorage.jwt,
        },
      })
      .then((res) => setTasks(res.data))
      .catch((err) => console.log(err));
  };

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
      <Box>
        {tasks &&
          tasks.map((task, index) => {
            return <Task {...task} key={index} />;
          })}
      </Box>
      <Flex justifyContent="end" position="sticky" bottom="3">
        <AddTask />
      </Flex>
    </Box>
  );
};

export default Tasks;
