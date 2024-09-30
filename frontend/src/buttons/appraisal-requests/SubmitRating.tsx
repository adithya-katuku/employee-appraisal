import { Button } from "@chakra-ui/react";
import useData from "../../../../hooks/useData";

interface Props {
  appraisalId: number;
  onClose: () => void;
}
const SubmitRating = ({ appraisalId, onClose }: Props) => {
  const { submitRating } = useData();
  const handleClick = () => {
    submitRating(appraisalId);
    onClose();
  };
  return (
    <Button colorScheme="green" onClick={handleClick}>
      Submit
    </Button>
  );
};

export default SubmitRating;
