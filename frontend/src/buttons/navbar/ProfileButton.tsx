import { Box } from "@chakra-ui/react";
import { FaUserCircle } from "react-icons/fa";

const ProfileButton = () => {
  return (
    <Box as="button" p="1">
      <FaUserCircle fontSize="1.5rem" />
    </Box>
  );
};

export default ProfileButton;
