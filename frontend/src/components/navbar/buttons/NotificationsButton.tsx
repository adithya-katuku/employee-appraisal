import { BellIcon } from "@chakra-ui/icons";
import { Box, Flex, Text, useDisclosure } from "@chakra-ui/react";
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
    <Box as="button" p="1" onClick={handleClick}>
      <Flex border="1px" borderColor="gray.200" rounded= "md" >
        <BellIcon fontSize="1.5rem" />
        <Text mx="1" color={count>0?"red":"black"} >{count}</Text>
      </Flex>
      <NotificationsModal isOpen={isOpen} onClose={onClose} />
    </Box>
  );
};

export default NotificationsButton;
