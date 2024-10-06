import { Button } from "@chakra-ui/react";
import useAdmin from "../../../../hooks/useAdmin";

interface Props {
  appraisalId: number;
  onClose: () => void;
  disabled:boolean;
}
const SubmitRating = ({ appraisalId, onClose, disabled }: Props) => {
  const { submitRating } = useAdmin();
  const handleClick = () => {
    submitRating(appraisalId);
    onClose();
  };
  return (
    <Button colorScheme="green" isDisabled={disabled} onClick={handleClick}>
      Submit
    </Button>
  );
};

export default SubmitRating;
