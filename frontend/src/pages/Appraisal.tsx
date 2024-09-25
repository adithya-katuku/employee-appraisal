import { ChevronRightIcon } from "@chakra-ui/icons";
import {
  Box,
  HStack,
  Select,
  Table,
  TableContainer,
  Tbody,
  Td,
  Text,
  Th,
  Thead,
  Tr,
} from "@chakra-ui/react";
import { useEffect, useState } from "react";
import { IoHome } from "react-icons/io5";
import Task from "../components/Task";
import axios from "axios";
import AppraisalModel from "../models/AppraisalModel";

const Appraisal = () => {
  const [appraisals, setAppraisals] = useState<AppraisalModel[]>();
  const [index, setIndex] = useState(0);

  const fetchTasks = async () => {
    await axios
      .get("http://localhost:8080/" + localStorage.role + "/appraisals", {
        headers: {
          Authorization: "Bearer " + sessionStorage.jwt,
        },
      })
      .then((res) => {
        setAppraisals(res.data);
      })
      .catch((err) => console.log(err));
  };

  useEffect(() => {
    localStorage.page = "/appraisal";
    fetchTasks();
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
                    {new Date(appraisal.startDate).toISOString().split("T")[0] +
                      "  to " +
                      new Date(appraisal.endDate).toISOString().split("T")[0]}
                  </option>
                );
              })}
          </Select>
        </Box>
        <Box my="1" p="2" pb="4">
          <Text m="1" fontWeight="bold">
            Attribute rating:
          </Text>
          <TableContainer>
            <Table maxW="50rem">
              <Thead>
                <Tr>
                  <Th>Attribute</Th>
                  <Th>Rating</Th>
                </Tr>
              </Thead>
              <Tbody>
                {appraisals && appraisals[index].attributes &&
                  appraisals[index].attributes.map((attribute, index) => (
                    <Tr key={index}>
                      <Td>{attribute.name}</Td>
                      <Td>{attribute.rating}</Td>
                    </Tr>
                  ))}
              </Tbody>
            </Table>
          </TableContainer>
        </Box>
        <Box my="1" p="2" pb="4">
          <Text m="1" fontWeight="bold">
            Tasks:
          </Text>
          {appraisals && appraisals[index].tasks &&
            appraisals[index].tasks.map((task) => {
              return <Task {...task} key={task.taskId} />;
            })}
        </Box>
      </Box>
    </>
  );
};

export default Appraisal;
