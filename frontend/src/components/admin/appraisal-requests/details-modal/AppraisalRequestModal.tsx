import {
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalCloseButton,
  ModalBody,
  ModalFooter,
  Button,
} from "@chakra-ui/react";
import { useSelector } from "react-redux";
import { RootState } from "../../../../stores/store";
import EmployeeDetails from "../../../employee/EmployeeDetails";
import AppraisalRequestTaskList from "../tasks/AppraisalRequestTaskList";
import AppraisalRequestAttributes from "../attributes/AppraisalRequestAttributes";
import SubmitRating from "../buttons/SubmitRating";

interface Props {
  isOpen: boolean;
  onClose: () => void;
  appraisalId: number;
}
const AppraisalRequestModal = ({ isOpen, onClose, appraisalId }: Props) => {
  const appraisalRequestDetails = useSelector(
    (state: RootState) => state.store.appraisalRequestDetails
  );

  return appraisalRequestDetails ? (
    <Modal isOpen={isOpen} onClose={onClose} size="2xl">
      <ModalOverlay />
      <ModalContent w={{base:"90vw", md:"100rem"}}>
        <ModalHeader>Appraisal Request</ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <EmployeeDetails
            employeeDetails={appraisalRequestDetails.employeeDetails}
          />
          <AppraisalRequestTaskList
            appraisalId={appraisalId}
            tasks={appraisalRequestDetails.tasks}
          />

          <AppraisalRequestAttributes
            appraisalId={appraisalId}
            attributes={appraisalRequestDetails.attributes}
          />
        </ModalBody>
        <ModalFooter>
          <Button variant="outline" colorScheme="red" mr={3} onClick={onClose}>
            Cancel
          </Button>
          <SubmitRating appraisalId={appraisalId} onClose={onClose} disabled={!appraisalRequestDetails.fullyRated} />
        </ModalFooter>
      </ModalContent>
    </Modal>
  ) : (
    <></>
  );
};

export default AppraisalRequestModal;
