import { Box, Checkbox, Flex, Text } from "@chakra-ui/react";
import TaskModel from "../models/TaskModel";
import EditTask from "../buttons/task/EditTask";

const Task = (task: TaskModel) => {
  return (
    <Box
      m="1"
      p="2"
      border="1px"
      borderColor="gray.300"
      rounded="lg"
      maxW="50rem"
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
          <EditTask />
          <Flex mx="2" alignItems="center" fontSize="sm" minW="fit-content">
            <Checkbox isDisabled isChecked={task.appraisable} mx="2" />
            <Text>Marked for appraisal</Text>
          </Flex>
        </Box>
      </Flex>
      <Text border="1px" borderColor="gray.200" rounded="md" p="1" my="1">
        {task.description}
      </Text>
      {task.appraisable && (
        <>
          <Text fontSize="sm">Self Rating: {task.selfRating} / 10</Text>
          <Text fontSize="sm">Admin Rating: {task.adminRating} / 10</Text>
        </>
      )}
    </Box>
  );
};

export default Task;
