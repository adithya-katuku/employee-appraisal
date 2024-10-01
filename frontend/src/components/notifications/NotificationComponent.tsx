import { Box, Button, Flex, Text } from "@chakra-ui/react";
import NotificationModel from "../../models/NotificationModel";
import { IoMdClose } from "react-icons/io";
import useNotifications from "../../hooks/useNotifications";

interface Props {
  notification: NotificationModel;
  onClose: () => void;
}

const NotificationComponent = ({ notification, onClose }: Props) => {
  const { deleteNotifications, markNotificationsRead, notificationRedirect } =
    useNotifications();

  const handleClick = () => {
    if (!notification.read) {
      markNotificationsRead(notification.notificationId);
    }
    onClose();
    notificationRedirect(notification);
  };

  const handleDelete = () => {
    deleteNotifications(notification.notificationId);
  };
  return (
    <Box
      my="1"
      p="2"
      rounded="md"
      bg="white"
      onClick={handleClick}
      cursor="pointer"
      opacity={notification.read?"75%":"100%"}
    >
      <Flex justifyContent="space-between" p="1">
        <Text fontWeight="bold">{notification.notificationTitle}</Text>
        <Button
          h="1.5rem"
          variant="outline"
          colorScheme="red"
          onClick={(e) => {
            e.stopPropagation();
            handleDelete();
          }}
        >
          <IoMdClose />
        </Button>
      </Flex>
      <Box border="1px" borderColor="gray.200" rounded="md" p="1">
        <Text>{notification.description}</Text>
      </Box>
    </Box>
  );
};

export default NotificationComponent;
