import { BellIcon } from "@chakra-ui/icons";
import { Flex, Text, useDisclosure, VStack } from "@chakra-ui/react";
import NotificationsModal from "../../notifications/NotificationsModal";
import useNotifications from "../../../hooks/useNotifications";
import { useEffect } from "react";
import { RootState } from "../../../stores/store";
import { useSelector } from "react-redux";

const NotificationsButton = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const notifications = useSelector(
    (state: RootState) => state.store.notifications
  );
  let count = 0;
  if (notifications) {
    count = notifications.filter((notification) => !notification.read).length;
  }
  const { fetchNotifications } = useNotifications();
  const handleClick = () => {
    fetchNotifications();
    onOpen();
  };
  useEffect(() => {
    const intervalId = setInterval(() => {
      // fetchNotifications();
    }, 5000);

    return () => clearInterval(intervalId);
  }, []);
  return (
    <VStack 
      as="button"
      onClick={handleClick}
      rounded="50%"
      h="3rem"
      gap="0" 
    >
      <Flex h="0.8rem" justify="end" w="2rem" position="relative" overflow="visible">
        {count>0 &&
          <Text
            fontSize="1rem"
            h="1rem"
            rounded="50%"
            w="1.7rem"
            color="red"
            fontWeight="bold"
            bg="white"
            position="absolute"
            top="0.3rem"
          >
            {count}
          </Text> }
      </Flex>
      <BellIcon fontSize="1.5rem" />
      <NotificationsModal isOpen={isOpen} onClose={onClose} />
    </VStack>
  );
};

export default NotificationsButton;
