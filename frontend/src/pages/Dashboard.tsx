import { ChevronRightIcon } from "@chakra-ui/icons";
import { Box, Flex, HStack, Text } from "@chakra-ui/react";
import { useEffect } from "react";
import { IoHome } from "react-icons/io5";

const Dashboard = () => {
  useEffect(() => {
    localStorage.page = 0;
  }, []);
  return (
    <Box>
      <HStack mb="2">
        <IoHome />
        <ChevronRightIcon />
        <Text>Dashboard</Text>
      </HStack>
      <Flex
        h="5rem"
        justify="center"
        align="center"
        bgGradient="linear(to-r, green, blue)"
        w="90%"
        mx="auto"
        my="2rem"
        rounded="lg"
      >
        <Text fontSize="2rem" color="white">
          Welcome back!
        </Text>
      </Flex>
    </Box>
  );
};

export default Dashboard;
