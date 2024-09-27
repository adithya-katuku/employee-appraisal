import { Box, Flex, Text } from "@chakra-ui/react";
import { useSelector } from "react-redux";
import { RootState } from "../../stores/store";
import { Link } from "react-router-dom";
import usePaths from "../../hooks/usePaths";

const VerticalNavbar = () => {
  const isLoggedIn = useSelector(
    (state: RootState) => state.store.loginState.isLoggedIn
  );
  const {options, setSelected} = usePaths();
  

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
                bg={option.selected ? "gray.300" : "white"}
                onClick={() => {
                  setSelected(index);
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
