import { Box, Flex, Text } from "@chakra-ui/react";
import { useDispatch, useSelector } from "react-redux";
import { RootState, setSelectedPage } from "../../stores/store";
import { Link } from "react-router-dom";
import usePaths from "../../hooks/usePaths";

const SidebarOptions = () => {
  const isLoggedIn = useSelector(
    (state: RootState) => state.store.loginState.isLoggedIn
  );
  const selectedPage = useSelector(
    (state: RootState) => state.store.selectedPage
  );
  const dispatch = useDispatch();
  const { options } = usePaths();

  return isLoggedIn ? (
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
              bg={index === selectedPage ? "gray.300" : "white"}
              onClick={() => {
                dispatch(setSelectedPage(index));
              }}
            >
              <Flex w="2rem" justifyContent="center">
                {option.icon}
              </Flex>
              <Text>{option.name}</Text>
            </Flex>
          </Link>
        );
      })}
    </Box>
  ) : null;
};

export default SidebarOptions;
