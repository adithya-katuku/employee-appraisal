import { Box, Flex, Text } from "@chakra-ui/react";
import { useSelector } from "react-redux";
import { RootState } from "../stores/store";

const VerticalNavbar = () => {
  const isLoggedIn = useSelector((state: RootState) => state.store.isLoggedIn);

  const options = [
    "Dashboard",
    "Timings",
    "Leaves",
    "WFH",
    "Tasks",
    "Teams",
    "People",
    "Appraisal",
  ];

  return isLoggedIn ? (
    <Box
      as="nav"
      w="15rem"
      p="2"
      shadow="2xl"
      rounded="xl"
      mx="3"
      bg="white"
      h="80vh"
      position="fixed"
      top="5rem"
      left="0"
      overflow="auto"
      onScroll={(e:React.UIEvent)=>{e.preventDefault()}}
    >
      <Box bg="white" >
        {options.map((option) => {
          return (
            <Flex
              as="button"
              w="100%"
              h="3rem"
              border="1px"
              borderColor="gray.200"
              align="center"
              p="3"
              gap="2"
              my="0.5"
              rounded="md"
            >
              <Box w="2rem" bg="gray">
                I
              </Box>
              <Text>{option}</Text>
            </Flex>
          );
        })}
      </Box>
    </Box>
  ) : null;
};

export default VerticalNavbar;
