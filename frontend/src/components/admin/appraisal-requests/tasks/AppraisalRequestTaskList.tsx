import { Box, Text } from "@chakra-ui/react";
import TaskModel from "../../../../models/TaskModel";
import AppraisalRequestTask from "./AppraisalRequestTask";

interface Props {
  tasks: TaskModel[];
  appraisalId: number;
}

const AppraisalRequestTaskList = ({ tasks, appraisalId }: Props) => {
  return (
    <Box>
      <Text m="1" fontWeight="bold">
        Tasks:
      </Text>
      {tasks &&
        tasks.map((task, index) => {
          return (
            <AppraisalRequestTask
              task={task}
              appraisalId={appraisalId}
              key={index}
            />
          );
        })}
    </Box>
  );
};

export default AppraisalRequestTaskList;
