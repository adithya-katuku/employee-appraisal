import {
  Drawer,
  DrawerBody,
  DrawerContent,
  DrawerOverlay,
} from "@chakra-ui/react";
import SidebarOptions from "../SidebarOptions";
interface Props {
  isOpen: boolean;
  onClose: () => void;
}
const SidebarDrawer = ({ isOpen, onClose }: Props) => {
  return (
    <Drawer
      isOpen={isOpen}
      placement="left"
      size="fit-content"
      onClose={onClose}
    >
      <DrawerOverlay />
      <DrawerContent
        m="1"
        maxWidth="fit-content"
        mt="5rem"
        rounded="md"
        h="80vh"
      >
        <DrawerBody w="fit-content" p="2" onClick={onClose}>
          <SidebarOptions />
        </DrawerBody>
      </DrawerContent>
    </Drawer>
  );
};

export default SidebarDrawer;
