import { ChevronRightIcon } from "@chakra-ui/icons";
import { Box, HStack, Text } from "@chakra-ui/react";
import { IoHome } from "react-icons/io5";

const CreateUser = () => {
  return (
    <Box>
      <HStack mb="2">
        <IoHome />
        <ChevronRightIcon />
        <Text>Create User</Text>
      </HStack>
      <Box>
        Create user
      </Box>
    </Box>
  );
};

export default CreateUser;
