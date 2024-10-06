import { Box, Button, useDisclosure } from "@chakra-ui/react";
import AppraisalRequestModal from "../details-modal/AppraisalRequestModal";
import useAdmin from "../../../../hooks/useAdmin";

interface Props {
  appriasalId: number;
}
const ViewDetails = ({ appriasalId }: Props) => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const { fetchAppraisalRequestDetails } = useAdmin();

  const handleClick = async () => {
    await fetchAppraisalRequestDetails(appriasalId);
    onOpen();
  };
  return (
    <Box>
      <Button
        m="1"
        p="2"
        onClick={handleClick}
        border="1px"
        borderColor="gray.400"
        colorScheme="blue"
      >
        View Details
      </Button>

      <AppraisalRequestModal
        isOpen={isOpen}
        onClose={onClose}
        appraisalId={appriasalId}
      />
    </Box>
  );
};

export default ViewDetails;
