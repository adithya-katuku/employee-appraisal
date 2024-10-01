import { Flex, Heading, HStack } from "@chakra-ui/react";
import { useSelector } from "react-redux";
import { RootState } from "../../stores/store";
import LogoutButton from "./buttons/LogoutButton";
import NotificationsButton from "./buttons/NotificationsButton";
import ProfileButton from "./buttons/ProfileButton";

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
      <Heading display={{ sm: "none" }}>O</Heading>
      <Heading>BeeSheets</Heading>

      <HStack gap="3" mx="2">
        <NotificationsButton />

        <ProfileButton />

        <LogoutButton />
      </HStack>
    </Flex>
  ) : null;
};

export default Navbar;
