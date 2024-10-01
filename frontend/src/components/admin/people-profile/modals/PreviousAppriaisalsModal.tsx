import {
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalCloseButton,
  ModalBody,
  ModalFooter,
  Button,
  Box,
  Flex,
  Select,
  Text,
} from "@chakra-ui/react";
import { RootState } from "../../../../stores/store";
import { useSelector } from "react-redux";
import { useState } from "react";
import ProfileTasksList from "../tasks/ProfileTasksList";
import SubmitAppraisal from "../../../appraisal/buttons/SubmitAppraisal";
import AttributeTable from "../../../attribute/AttributeTable";

interface Props {
  isOpen: boolean;
  onClose: () => void;
}

const PreviousAppriaisalsModal = ({ isOpen, onClose }: Props) => {
  const appraisals = useSelector((state: RootState) => state.store.appraisals);
  const tasks = useSelector((state: RootState) => state.store.tasks);
  const [index, setIndex] = useState(0);
  return (
    <Modal isOpen={isOpen} onClose={onClose} size="2xl">
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>
          Employee Tasks:
          <ModalCloseButton />
        </ModalHeader>
        <ModalBody>
          {appraisals && appraisals.length>0 ? (
            <Box>
              <Box
                my="1"
                p="2"
                pb="4"
                borderBottom="1px"
                borderColor="gray.500"
              >
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
                    {appraisals && appraisals[index].appraisalStatus}
                  </Text>
                </Flex>
              </Box>
              {appraisals &&
                (appraisals[index].appraisalStatus === "APPROVED" ||
                  appraisals[index].appraisalStatus === "REJECTED") && (
                  <Box my="1" p="2" pb="4">
                    <Text m="1" fontWeight="bold">
                      Attribute rating:
                    </Text>
                    {appraisals[index].attributes && (
                      <AttributeTable
                        attributes={appraisals[index].attributes}
                      />
                    )}
                  </Box>
                )}
              <Box my="1" p="2" pb="4">
                <Text m="1" fontWeight="bold">
                  Tasks:
                </Text>

                {appraisals && tasks && (
                  <ProfileTasksList
                    tasks={tasks.filter(
                      (task) => task.appraisalId === appraisals[index].id
                    )}
                  />
                )}
              </Box>

              {appraisals &&
                appraisals[index].appraisalStatus === "INITIATED" && (
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
        </ModalBody>
        <ModalFooter>
          <Button variant="outline" colorScheme="red" mr={3} onClick={onClose}>
            Cancel
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
};

export default PreviousAppriaisalsModal;
