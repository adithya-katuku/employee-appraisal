import { Box } from "@chakra-ui/react";
import {  useSelector } from "react-redux";
import { RootState } from "../../stores/store";
import SidebarOptions from "./SidebarOptions";

const Sidebar = () => {
  const isLoggedIn = useSelector(
    (state: RootState) => state.store.loginState.isLoggedIn
  );

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
      zIndex="1"
      display={{base:"none", md:"block"}}
      onScroll={(e: React.UIEvent) => {
        e.stopPropagation();
      }}
    >
      <SidebarOptions />
    </Box>
  ) : null;
};

export default Sidebar;
