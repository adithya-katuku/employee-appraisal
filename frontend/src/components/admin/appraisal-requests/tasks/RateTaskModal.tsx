import {
  Box,
  Button,
  Flex,
  FormControl,
  FormLabel,
  Input,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Text,
} from "@chakra-ui/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { FieldValues, useForm } from "react-hook-form";
import { z } from "zod";
import TaskModel from "../../../../models/TaskModel";
import useAdmin from "../../../../hooks/useAdmin";

interface Props {
  isOpen: boolean;
  onClose: () => void;
  task: TaskModel;
  appraisalId: number;
}

const schema = z.object({
  taskId: z.number(),
  rating: z
    .preprocess(
      (value) => (typeof value === "string" ? parseFloat(value) : value),
      z.number().max(10)
    )
    .optional(),
});

type validForm = z.infer<typeof schema>;

const RateTaskModal = ({ isOpen, onClose, task, appraisalId }: Props) => {
  const { rateTask, fetchAppraisalRequestDetails } = useAdmin();

  const { register, handleSubmit, setValue, reset } = useForm<validForm>({
    resolver: zodResolver(schema),
  });
  if (task.taskId) {
    setValue("taskId", task.taskId);
  }
  if (task.adminRating) {
    setValue("rating", task.adminRating >= 0 ? task.adminRating : 0);
  }

  const onSubmit = async (data: FieldValues) => {
    await rateTask({ taskId: data.taskId, rating: data.rating }).then(() => {
      fetchAppraisalRequestDetails(appraisalId);
    });
    handleClose();
  };

  const handleClose = () => {
    reset();
    onClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={handleClose} size="xl">
      <ModalOverlay />
      <ModalContent  w={{base:"85vw", md:"80rem"}} >
        <ModalHeader>Rate Task</ModalHeader>
        <ModalCloseButton />
        <form onSubmit={handleSubmit(onSubmit)}>
          <ModalBody>
            <Box
              m="1"
              p="2"
              border="1px"
              borderColor="gray.300"
              rounded="lg"
              maxW="50rem"
            >
              <Box borderBottom="1px" borderColor="gray.400" pb="2">
                <Flex justifyContent="space-between">
                  <Box>
                    <Text>{task.taskTitle}</Text>
                    <Text fontSize="sm">
                      {new Date(task.startDate).toISOString().split("T")[0] +
                        " - " +
                        new Date(task.endDate).toISOString().split("T")[0]}
                    </Text>
                  </Box>
                </Flex>
                <Text
                  border="1px"
                  borderColor="gray.200"
                  rounded="md"
                  p="1"
                  my="1"
                >
                  {task.description}
                </Text>
                <Text>Self Rating: {task.selfRating} / 10</Text>
              </Box>
              <FormControl my="2">
                <FormLabel>Admin Rating:</FormLabel>
                <Input type="number" {...register("rating")} />
              </FormControl>
            </Box>
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
            <Button type="submit" colorScheme="blue">
              Save
            </Button>
          </ModalFooter>
        </form>
      </ModalContent>
    </Modal>
  );
};

export default RateTaskModal;
