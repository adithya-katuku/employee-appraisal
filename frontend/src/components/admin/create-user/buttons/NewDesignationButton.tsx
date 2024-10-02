import { Box, Button, useDisclosure } from "@chakra-ui/react";
import NewDesignationModal from "../modals/NewDesignationModal";
import useRegister from "../../../../hooks/useRegister";

const NewDesignationButton = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const {fetchAttributes}= useRegister();
  const handleClick = () => {
    fetchAttributes();
    onOpen();
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

export default NewDesignationButton;
