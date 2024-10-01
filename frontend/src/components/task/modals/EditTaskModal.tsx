import {
  Button,
  Checkbox,
  FormControl,
  FormLabel,
  Input,
  InputGroup,
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
import axios from "axios";
import { useState } from "react";
import { FieldValues, useForm } from "react-hook-form";
import { z } from "zod";
import TaskModel from "../../../models/TaskModel";
import { useDispatch } from "react-redux";
import { updateTask } from "../../../stores/store";

interface Props {
  isOpen: boolean;
  onClose: () => void;
  task: TaskModel;
}

const schema = z.object({
  taskId: z.number().optional(),
  taskTitle: z.string(),
  description: z.string(),
  startDate: z.string(),
  endDate: z.string(),
  appraisable: z.boolean().optional(),
  selfRating: z
    .preprocess(
      (value) => (typeof value === "string" ? parseFloat(value) : value),
      z.number().positive().max(10).nullable()
    )
    .optional(),
});

type validForm = z.infer<typeof schema>;

const EditTaskModal = ({ isOpen, onClose, task }: Props) => {
  const [isAppraisable, setIsAppraisable] = useState(task.appraisable);
  const dispatch = useDispatch();

  const {
    register,
    handleSubmit,
    setValue,
    reset,
    formState: { errors },
  } = useForm<validForm>({ resolver: zodResolver(schema) });

  const toggleAppraisal = () => {
    setIsAppraisable(!isAppraisable);
  };

  setValue("taskId", task.taskId);
  setValue("taskTitle", task.taskTitle);
  setValue("description", task.description);
  setValue("startDate", new Date(task.startDate).toISOString().split("T")[0]);
  setValue("endDate", new Date(task.endDate).toISOString().split("T")[0]);
  setValue("appraisable", task.appraisable);
  setValue("selfRating", task.selfRating);

  const onSubmit = async (data: FieldValues) => {
    await axios
      .put("http://localhost:8080/" + localStorage.role + "/tasks", data, {
        headers: {
          Authorization: "Bearer " + sessionStorage.jwt,
        },
      })
      .then((res) => {
        console.log(res.data);
        dispatch(updateTask(res.data));
        setIsAppraisable(res.data.appraisable);
      })
      .catch((err) => {
        console.log(err);
      });
    handleClose();
  };

  const handleClose = () => {
    setIsAppraisable(task.appraisable);
    reset();
    onClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={handleClose}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>Edit Task</ModalHeader>
        <ModalCloseButton />
        <form onSubmit={handleSubmit(onSubmit)}>
          <ModalBody>
            <FormControl isRequired my="1">
              <FormLabel>Task title</FormLabel>
              <Input placeholder="Title" {...register("taskTitle")} />
            </FormControl>
            <FormControl isRequired my="1">
              <FormLabel>Description</FormLabel>
              <Textarea
                placeholder="Description"
                {...register("description")}
              />
            </FormControl>
            <FormControl isRequired my="1">
              <FormLabel>Duration</FormLabel>
              <InputGroup>
                <Input type="date" mx="1" {...register("startDate")} />
                <Input type="date" mx="1" {...register("endDate")} />
              </InputGroup>
              {errors.startDate && <Text>{errors.startDate.message}</Text>}
            </FormControl>
            <FormControl my="1" p="1">
              <Checkbox
                size="lg"
                {...register("appraisable")}
                onChange={toggleAppraisal}
              >
                Mark for appraisal
              </Checkbox>
            </FormControl>
            {isAppraisable && (
              <FormControl isRequired my="1">
                <FormLabel>Self Rating</FormLabel>
                <Input
                  type="number"
                  placeholder="Rate your task out of 10"
                  {...register("selfRating")}
                />
              </FormControl>
            )}
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

export default EditTaskModal;
