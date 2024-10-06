import { Box, Button } from "@chakra-ui/react";
import useData from "../../../hooks/useData";

interface Props {
  appraisalId: number;
}
const SubmitAppraisal = ({ appraisalId }: Props) => {
  const { submitAppraisal } = useData();
  const handleClick = async () => {
    submitAppraisal(appraisalId);
  };
  return (
    <Box>
      <Button
        m="1"
        p="2"
        border="1px"
        borderColor="gray.400"
        colorScheme="purple"
        onClick={handleClick}
      >
        Submit
      </Button>
    </Box>
  );
};

export default SubmitAppraisal;
