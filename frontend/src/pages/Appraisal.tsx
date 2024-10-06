import { ChevronRightIcon } from "@chakra-ui/icons";
import { Box, Flex, HStack, Select, Text } from "@chakra-ui/react";
import { useEffect, useState } from "react";
import { IoHome } from "react-icons/io5";
import AttributeTable from "../components/attribute/AttributeTable";
import { useSelector } from "react-redux";
import { RootState } from "../stores/store";
import TaskList from "../components/task/TaskList";
import useData from "../hooks/useData";
import SubmitAppraisal from "../components/appraisal/buttons/SubmitAppraisal";
import AddExistingTasks from "../components/appraisal/buttons/AddExistingTasks";
import CreateTask from "../components/task/buttons/CreateTask";

const Appraisal = () => {
  const appraisals = useSelector((state: RootState) => state.store.appraisals);
  const tasks = useSelector((state: RootState) => state.store.tasks);
  const { fetchAppraisals, fetchTasks } = useData();
  const [index, setIndex] = useState(0);

  useEffect(() => {
    localStorage.page = 3;
    fetchTasks();
    fetchAppraisals();
  }, []);

  return (
    <Box>
      <HStack mb="2">
        <IoHome />
        <ChevronRightIcon />
        <Text>Appraisal</Text>
      </HStack>
      <Box m="auto" maxW="60rem">
        {appraisals && appraisals[index] ? (
          <Box>
            <Box my="1" p="2" pb="4" borderBottom="1px" borderColor="gray.500">
              <Text m="1" fontWeight="bold">
                Appraisal period:
              </Text>
              <Select
                maxW="50%"
                minW="fit-content"
                onChange={(e) => {
                  const ind = parseInt(e.target.value);
                  setIndex(ind);
                }}
              >
                {appraisals.map((appraisal, index) => {
                    return (
                      <option value={index} key={index}>
                        {new Date(appraisal.startDate)
                          .toISOString()
                          .split("T")[0] +
                          "  to " +
                          new Date(appraisal.endDate)
                            .toISOString()
                            .split("T")[0]}
                      </option>
                    );
                  })}
              </Select>
              <Flex my="1" p="2" gap="2" alignItems="center">
                <Text fontWeight="bold">Status: </Text>
                <Text color="white" bg="green.500" rounded="md" p="1">
                  {appraisals[index].appraisalStatus}
                </Text>
              </Flex>
            </Box>
            {(appraisals[index].appraisalStatus === "APPROVED" ||
                appraisals[index].appraisalStatus === "REJECTED") && (
                <Box my="1" p="2" pb="4">
                  <Text m="1" fontWeight="bold">
                    Attribute rating:
                  </Text>
                  {appraisals[index].attributes && (
                    <AttributeTable attributes={appraisals[index].attributes} />
                  )}
                </Box>
              )}
            <Box my="1" p="2" pb="4">
              <Text m="1" fontWeight="bold">
                Tasks:
              </Text>
              {appraisals[index].appraisalStatus === "INITIATED" && (
                  <Flex>
                    <CreateTask />
                    <AddExistingTasks />
                  </Flex>
                )}
              {tasks && (
                <TaskList
                  tasks={tasks.filter(
                    (task) => task.appraisalId === appraisals[index].id
                  )}
                />
              )}
            </Box>

            {appraisals[index].appraisalStatus === "INITIATED" && (
                <Flex
                  my="1"
                  p="2"
                  justifyContent="end"
                  position="sticky"
                  bottom="3"
                >
                  <SubmitAppraisal appraisalId={appraisals[index].id} />
                </Flex>
              )}
          </Box>
        ) : (
          <Text>No appraisals yet :)</Text>
        )}
      </Box>
    </Box>
  );
};

export default Appraisal;
