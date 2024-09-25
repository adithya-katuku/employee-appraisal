import { Flex, useDisclosure } from "@chakra-ui/react";
import { FaPlus } from "react-icons/fa6";
import NewTaskForm from "../../components/NewTaskForm";

const AddTask = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();

  return (
    <>
      <Flex
        as="button"
        p="1"
        justifyContent="center"
        align="center"
        onClick={onOpen}
        border="1px"
        borderColor="gray.400"
        rounded="50%"
        bg="blue.500"
        w="3.5rem"
        h="3.5rem"
      >
        <FaPlus color="white" fontSize="1.75rem" />
      </Flex>

      <NewTaskForm isOpen={isOpen} onClose={onClose} />
    </>
  );
};

export default AddTask;
