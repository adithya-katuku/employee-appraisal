import { Box, Button, useDisclosure } from "@chakra-ui/react";
import { VscThreeBars } from "react-icons/vsc";
import SidebarDrawer from "../drawers/SidebarDrawer";

const SidebarButton = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  return (
    <Box display={{ md: "none" }}>
      <Button onClick={onOpen} variant="ghost" p="1" >
        <VscThreeBars fontSize="1.5rem"/>
      </Button>

      <SidebarDrawer isOpen={isOpen} onClose={onClose}/>
    </Box>
  );
};

export default SidebarButton;
