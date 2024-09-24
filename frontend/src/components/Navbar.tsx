import { Box, Flex, Heading, HStack } from "@chakra-ui/react";
import { useSelector } from "react-redux";
import { RootState } from "../stores/store";

const Navbar = () => {
  const isLoggedIn = useSelector((state: RootState) => state.store.isLoggedIn);


  return isLoggedIn ? (
    <Flex
      as="nav"
      alignItems="center"
      justifyContent="space-between"
      p="2"
      shadow="lg"
      h="4rem"
      position="sticky"
      top="0"
      bg="white"
    >
      <Heading display={{ sm: "none" }}>O</Heading>
      <Heading>Beesheets</Heading>

      <HStack>
        <Box bg="gray.400" p="1">
          N
        </Box>
        <Box bg="gray.400" p="1">
          P
        </Box>
      </HStack>
    </Flex>
  ) : null;
};

export default Navbar;
