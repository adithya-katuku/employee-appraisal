import { Box, Button, useDisclosure } from "@chakra-ui/react";
import NewDesignationModal from "../modals/NewDesignationModal";

const NewDesignation = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const handleClick = () => {
    onOpen();
    console.log("clicked");
  };
  return (
    <Box>
      <Button
        onClick={handleClick}
        colorScheme="blue"
        variant="outline"
        h="2rem"
        px="1"
      >
        New Designation
      </Button>

      <NewDesignationModal isOpen={isOpen} onClose={onClose} />
    </Box>
  );
};

export default NewDesignation;
