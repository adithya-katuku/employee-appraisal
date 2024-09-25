import { Flex } from "@chakra-ui/react";
import { FaEdit } from "react-icons/fa";

const EditTask = () => {
  const handleClick = () => {
    console.log("editing");
  };
  return (
    <Flex as="button" w="100%" p="1" justifyContent="end" onClick={handleClick}>
      <FaEdit />
    </Flex>
  );
};

export default EditTask;
