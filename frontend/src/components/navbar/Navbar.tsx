import { Flex, Heading, HStack } from "@chakra-ui/react";
import { useSelector } from "react-redux";
import { RootState } from "../../stores/store";
import LogoutButton from "./buttons/LogoutButton";
import NotificationsButton from "./buttons/NotificationsButton";
import ProfileButton from "./buttons/ProfileButton";
import SidebarButton from "./buttons/SidebarButton";

const Navbar = () => {
  const isLoggedIn = useSelector(
    (state: RootState) => state.store.loginState.isLoggedIn
  );

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
      zIndex="2"
    >
      <SidebarButton />

      <Flex>
        <Heading color="blue.600">Bee</Heading>
        <Heading color="green.600">Hyv</Heading>
      </Flex>

      <HStack gap="3" mx="2">
        <NotificationsButton />

        <ProfileButton />

        <LogoutButton />
      </HStack>
    </Flex>
  ) : null;
};

export default Navbar;
