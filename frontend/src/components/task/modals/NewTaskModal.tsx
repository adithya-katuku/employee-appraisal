import {
  Box,
  Button,
  Checkbox,
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
  Textarea,
} from "@chakra-ui/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import useTasks from "../../../hooks/useTasks";
import { RootState } from "../../../stores/store";
import { useSelector } from "react-redux";

interface Props {
  isOpen: boolean;
  onClose: () => void;
}

const schema = z.object({
  taskTitle: z.string({message:"Task title cannot be empty."}),
  description: z.string({message:"Task description cannot be empty."}),
  startDate: z.string({message:"Task must have a start date."}),
  endDate: z.string({message:"Task must have an end date."}),
  appraisable: z.boolean().optional(),
  selfRating: z
    .preprocess(
      (value) => (value ? typeof value === "string" ? parseFloat(value) : value:undefined),
      z.number().positive().max(10).optional()
    ),
});

type validForm = z.infer<typeof schema>;

const NewTaskModal = ({ isOpen, onClose }: Props) => {
  const [isAppraisable, setIsAppraisable] = useState(false);
  const { addTask } = useTasks();
  const role = useSelector((state: RootState) => state.store.loginState.role);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<validForm>({ resolver: zodResolver(schema) });

  const toggleAppraisal = () => {
    setIsAppraisable(!isAppraisable);
  };

  const onSubmit = async (data: validForm) => {
    addTask(data);
    handleClose();
  };

  const handleClose = () => {
    setIsAppraisable(false);
    reset();
    onClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={handleClose} >
      <ModalOverlay />
      <ModalContent maxW={{base:"90vw", md:"fit-content"}} >
        <ModalHeader>Add Task</ModalHeader>
        <ModalCloseButton />
        <form onSubmit={handleSubmit(onSubmit)}>
          <ModalBody >
            <FormControl isRequired my="1">
              <FormLabel>Task title</FormLabel>
              <Input placeholder="Title" {...register("taskTitle")} />
              {errors.taskTitle && <Text color="red">{errors.taskTitle.message}</Text>}
            </FormControl>
            <FormControl isRequired my="1">
              <FormLabel>Description</FormLabel>
              <Textarea
                placeholder="Description"
                {...register("description")}
              />
              {errors.description && <Text color="red">{errors.description.message}</Text>}
            </FormControl>
            <FormControl isRequired my="1">
              <FormLabel>Duration</FormLabel>
              <Box display={{md:"flex"}} >
                <Input type="date" mx="1" my={{base:"0.5", md:"0"}} {...register("startDate")} />
                <Input type="date" mx="1" my={{base:"0.5", md:"0"}} {...register("endDate")} />
              </Box>
              {errors.startDate && <Text color="red">Start date: {errors.startDate.message}</Text>}
              {errors.endDate && <Text color="red">End date: {errors.endDate.message}</Text>}
            </FormControl>
            {role === "employee" && (
              <>
                <FormControl my="1" p="1">
                  <Checkbox
                    size="lg"
                    {...register("appraisable")}
                    onChange={toggleAppraisal}
                  >
                    Mark for appraisal
                  </Checkbox>
                  {errors.appraisable && <Text color="red">{errors.appraisable.message}</Text>}
                </FormControl>
                {isAppraisable && (
                  <FormControl isRequired my="1">
                    <FormLabel>Self Rating</FormLabel>
                    <Input
                      type="number"
                      placeholder="Rate your task out of 10"
                      {...register("selfRating")}
                    />
                     {errors.selfRating && <Text color="red">Start date: {errors.selfRating.message}</Text>}
                  </FormControl>
                )}
              </>
            )}
            <Box>
              <Text color="red">{errors.root?.message}</Text>
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

export default NewTaskModal;
