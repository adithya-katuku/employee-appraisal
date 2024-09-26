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
import { useDispatch } from "react-redux";
import { z } from "zod";
import { addTask } from "../../stores/store";

interface Props {
  isOpen: boolean;
  onClose: () => void;
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
      z.number().positive().max(10)
    )
    .optional(),
});

type validForm = z.infer<typeof schema>;

const NewTaskModal = ({ isOpen, onClose }: Props) => {
  const [isAppraisable, setIsAppraisable] = useState(false);

  const dispatch = useDispatch();

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<validForm>({ resolver: zodResolver(schema) });

  const toggleAppraisal = () => {
    setIsAppraisable(!isAppraisable);
  };

  
  const onSubmit = async (data: FieldValues) => {
    await axios.post("http://localhost:8080/"+localStorage.role+"/tasks", data, {
      headers:{
        Authorization: "Bearer "+sessionStorage.jwt
      }
    })
    .then(res=>{
      console.log(res.data);
      dispatch(addTask(res.data));
    })
    .catch(err=>{
      console.log(err);
    })
    handleClose();
  };
  // console.log();
  const handleClose = () => {
    setIsAppraisable(false);
    reset();
    onClose();
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={handleClose}
    >
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>Add Task</ModalHeader>
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

export default NewTaskModal;
