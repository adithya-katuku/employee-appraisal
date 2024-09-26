import { ChevronRightIcon } from "@chakra-ui/icons";
import { Box, Button, Flex, HStack, Select, Text } from "@chakra-ui/react";
import { useEffect, useState } from "react";
import { IoHome } from "react-icons/io5";
import AddTask from "../buttons/task/AddTask";
import AttributeTable from "../components/attribute/AttributeTable";
import { useSelector } from "react-redux";
import { RootState } from "../stores/store";
import TaskList from "../components/task/TaskList";
import useData from "../hooks/useData";

const Appraisal = () => {
  const appraisals = useSelector((state: RootState) => state.store.appraisals);
  const tasks = useSelector((state: RootState) => state.store.tasks);
  const {fetchAppraisals, fetchTasks} = useData();
  const [index, setIndex] = useState(0);

  useEffect(() => {
    localStorage.page = "/appraisal";
    fetchTasks()
    fetchAppraisals();
  }, []);

  return (
    <>
      <HStack mb="2">
        <IoHome />
        <ChevronRightIcon />
        <Text>Appraisal</Text>
      </HStack>
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
            {appraisals &&
              appraisals.map((appraisal, index) => {
                return (
                    <option value={index} key={index}>
                      {new Date(appraisal.startDate)
                        .toISOString()
                        .split("T")[0] +
                        "  to " +
                        new Date(appraisal.endDate).toISOString().split("T")[0]}
                    </option>
                );
              })}
          </Select>
          <Flex my="1" p="2" gap="2" alignItems="center">
            <Text fontWeight="bold">Status: </Text>
            <Text color="white" bg="green.500" rounded="md" p="1">
              {appraisals && appraisals[index].appraisalStatus}
            </Text>
          </Flex>
        </Box>
        <Box my="1" p="2" pb="4">
          <Text m="1" fontWeight="bold">
            Attribute rating:
          </Text>
          {appraisals && appraisals[index].attributes && (
            <AttributeTable attributes={appraisals[index].attributes} />
          )}
        </Box>
        <Box my="1" p="2" pb="4">

          <Text m="1" fontWeight="bold">
            Tasks:
          </Text>

          <AddTask />
          {appraisals && tasks && (
            <TaskList tasks={tasks.filter(task=>task.appraisalId===appraisals[index].id)} />
          )}
        </Box>
        
        {appraisals && appraisals[index].appraisalStatus === "INITIATED" && (
          <Flex my="1" p="2" justifyContent="end">
            <Box>
              <Button colorScheme="teal">Submit</Button>
            </Box>
          </Flex>
        )}
      </Box>
    </>
  );
};

export default Appraisal;
