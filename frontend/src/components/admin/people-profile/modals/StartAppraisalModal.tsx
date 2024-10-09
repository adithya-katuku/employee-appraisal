import {
  Button,
  FormControl,
  FormLabel,
  Input,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Text,
} from "@chakra-ui/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { useContext } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { EmployeeDetailsContext } from "../AdminEmployeeProfileView";
import useAdmin from "../../../../hooks/useAdmin";

const schema = z.object({
  startDate: z.string().refine((date) => !isNaN(Date.parse(date)), {
    message: "Invalid date format",
  }),
  endDate: z.string().refine((date) => !isNaN(Date.parse(date)), {
    message: "Invalid date format",
  }),
});

type validForm = z.infer<typeof schema>;

interface Props {
  isOpen: boolean;
  onClose: () => void;
}

const StartAppraisalModal = ({ isOpen, onClose }: Props) => {
  const { register, handleSubmit, setValue, reset, formState:{errors} } = useForm<validForm>({
    resolver: zodResolver(schema),
  });

  const employeeDetails = useContext(EmployeeDetailsContext);
  const { startAppraisal } = useAdmin();

  const handleClose = () => {
    onClose();
    reset();
  };
  const onSubmit = (data: validForm) => {
    console.log(data);
    startAppraisal(employeeDetails.employeeId, data);
    handleClose();
  };
  if(employeeDetails.previousAppraisalDate){
    setValue("startDate", new Date(employeeDetails.previousAppraisalDate).toISOString().split("T")[0])
  }
  setValue("endDate", new Date().toISOString().split("T")[0]);
  return (
    <Modal isOpen={isOpen} onClose={handleClose}>
      <ModalOverlay />
      <ModalContent  w={{base:"90vw", md:"100rem"}}>
        <ModalHeader>Start Appraisal</ModalHeader>
        <form onSubmit={handleSubmit(onSubmit)}>
          <ModalBody>
            <FormControl isRequired my="2">
              <FormLabel mx="1">Start Date: </FormLabel>
              <Input type="date" mx="1" {...register("startDate")} />
              {errors.startDate && <Text color="red">Start date: {errors.startDate.message}</Text>}
            </FormControl>
            <FormControl isRequired my="2">
              <FormLabel mx="1">End Date: </FormLabel>
              <Input type="date" mx="1" {...register("endDate")} />
              {errors.endDate && <Text color="red">Start date: {errors.endDate.message}</Text>}
            </FormControl>
          </ModalBody>
          <ModalFooter>
            <Button
              variant="outline"
              colorScheme="red"
              mr={3}
              onClick={handleClose}
            >
              Cancel
            </Button>
            <Button type="submit" colorScheme="green">
              Start
            </Button>
          </ModalFooter>
        </form>
      </ModalContent>
    </Modal>
  );
};

export default StartAppraisalModal;
