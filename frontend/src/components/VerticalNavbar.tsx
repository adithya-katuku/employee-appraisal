import { Box, Flex, Text } from "@chakra-ui/react";
import { useSelector } from "react-redux";
import { RootState } from "../stores/store";
import { useState } from "react";
import { Link } from "react-router-dom";

const VerticalNavbar = () => {
  const isLoggedIn = useSelector((state: RootState) => state.store.isLoggedIn);

  const options = [
    { name: "Home", path: "/home" },
    { name: "Timings", path: "/home" },
    { name: "Leaves", path: "/home" },
    { name: "WFH", path: "/home" },
    { name: "Tasks", path: "/tasks" },
    { name: "Teams", path: "/home" },
    { name: "People", path: "/home" },
    { name: "Appraisal", path: "/appraisal" },
  ];

  const [selectedWindow, setSelectedWindow] = useState(localStorage.page?.substring(1) || "/home");

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
      onScroll={(e: React.UIEvent) => {
        e.preventDefault();
      }}
    >
      <Box bg="white">
        {options.map((option, index) => {
          return (
            <Link to={option.path} key={index}>
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
                bg={option.name.toLocaleLowerCase() == selectedWindow ? "gray.300" : "white"}
                onClick={() => {
                  setSelectedWindow(option.name.toLocaleLowerCase());
                }}
              >
                <Box w="2rem" bg="gray">
                  I
                </Box>
                <Text>{option.name}</Text>
              </Flex>
            </Link>
          );
        })}
      </Box>
    </Box>
  ) : null;
};

export default VerticalNavbar;
