import { Box, Flex, Text } from "@chakra-ui/react";
import TaskModel from "../../../models/TaskModel";
import ViewTask from "../../../buttons/appraisalrequests/ViewTask";

interface Props{
  task:TaskModel;
  appraisalId:number;
}

const AppraisalRequestTask = ({task, appraisalId}:Props) => {
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
        <ViewTask task={task} appraisalId={appraisalId}/>
      </Flex>
      <Text border="1px" borderColor="gray.200" rounded="md" p="1" my="1">
        {task.description}
      </Text>
      <Text fontSize="sm">Self Rating: {task.selfRating} / 10</Text>
      {task.adminRating && task.adminRating >= 0 && (
        <Text fontSize="sm">Admin Rating: {task.adminRating} / 10</Text>
      )}
    </Box>
  );
};

export default AppraisalRequestTask;
