import { Box, Button, useDisclosure } from "@chakra-ui/react"
import StartAppraisalModal from "../modals/StartAppraisalModal";

const StartAppraisalButton = () => {
    const {isOpen, onOpen, onClose} = useDisclosure();
  return (
    <Box>
        <Button colorScheme="green" onClick={onOpen}>Start Appraisal</Button>

        <StartAppraisalModal isOpen={isOpen}  onClose={onClose} />
    </Box>
  )
}

export default StartAppraisalButton