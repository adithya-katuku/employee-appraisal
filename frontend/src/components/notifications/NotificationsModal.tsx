import { Modal, ModalBody, ModalContent, Text } from "@chakra-ui/react";
import { useSelector } from "react-redux";
import { RootState } from "../../stores/store";
import NotificationComponent from "./NotificationComponent";

interface Props {
  isOpen: boolean;
  onClose: () => void;
}
const NotificationsModal = ({ isOpen, onClose }: Props) => {
  const notifications = useSelector(
    (state: RootState) => state.store.notifications
  );
  return (
    <Modal isOpen={isOpen} onClose={onClose} size="sm">
      <ModalContent
        shadow="xl"
        position="absolute"
        top="0rem"
        right="5%"
        maxW="17rem"
        maxH="20rem"
        overflow="auto"
        bg="gray.100"
      >
        <ModalBody p="3">
          {notifications && notifications.length > 0 ? (
            notifications.map((notification) => (
              <NotificationComponent
                notification={notification}
                onClose={onClose}
                key={notification.notificationId}
              />
            ))
          ) : (
            <Text>No notifications.</Text>
          )}
        </ModalBody>
      </ModalContent>
    </Modal>
  );
};

export default NotificationsModal;
