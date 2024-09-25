import { BellIcon } from "@chakra-ui/icons";
import { Box } from "@chakra-ui/react";

const NotificationsButton = () => {
  return (
    <Box as="button" rounded="50%" p="1">
      <BellIcon fontSize="1.5rem" />
    </Box>
  );
};

export default NotificationsButton;
