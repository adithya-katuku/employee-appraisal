import { Box, Checkbox, Flex, Text } from "@chakra-ui/react";
import TaskModel from "../../models/TaskModel";
import ModifyTaskOptions from "./ModifyTaskOptions";
import { useSelector } from "react-redux";
import { RootState } from "../../stores/store";

const Task = (task: TaskModel) => {
  const role = useSelector((state: RootState) => state.store.loginState.role);
  return (
    <Box
      m="1"
      p="2"
      border="1px"
      borderColor="gray.300"
      rounded="lg"
      maxW="60rem"
    >
      <Flex justifyContent="space-between">
        <Box>
          <Text>{task.taskTitle}</Text>
          <Text fontSize="sm">
            {new Date(task.startDate).toISOString().split("T")[0] +
              " - " +
              new Date(task.endDate).toISOString().split("T")[0]}
          </Text>
        </Box>
        <Box>
          <ModifyTaskOptions {...task} />
          {role === "employee" && (
            <Flex mx="2" alignItems="center" fontSize="sm" minW="fit-content">
              <Checkbox isDisabled isChecked={task.appraisable} mx="2" />
              <Text>Marked for appraisal</Text>
            </Flex>
          )}
        </Box>
      </Flex>
      <Text border="1px" borderColor="gray.200" rounded="md" p="1" my="1">
        {task.description}
      </Text>
      {role === "employee" && task.appraisable && (
        <>
          <Text fontSize="sm">Self Rating: {task.selfRating} / 10</Text>
          {task.adminRating && (
            <Text fontSize="sm">Admin Rating: {task.adminRating} / 10</Text>
          )}
        </>
      )}
    </Box>
  );
};

export default Task;
