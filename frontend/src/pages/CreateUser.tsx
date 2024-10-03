import { ChevronRightIcon } from "@chakra-ui/icons";
import {
  Box,
  Button,
  Flex,
  FormControl,
  FormLabel,
  HStack,
  Input,
  Text,
} from "@chakra-ui/react";
import { IoHome } from "react-icons/io5";
import NewDesignationButton from "../components/admin/create-user/buttons/NewDesignationButton";
import { useEffect } from "react";
import useRegister from "../hooks/useRegister";
import { useSelector } from "react-redux";
import { RootState } from "../stores/store";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { MultiValue, SingleValue } from "react-select";
import Select from "react-select";
const schema = z.object({
  employeeId: z.number(),
  name: z.string(),
  designation: z.string(),
  email: z.string(),
  joiningDate: z.string(),
  roles: z.string().array().nonempty(),
  password: z.string(),
});
type validForm = z.infer<typeof schema>;

interface Option {
  label: string;
  value: string;
}

const CreateUser = () => {
  const existingDesignations = useSelector(
    (state: RootState) => state.store.designations
  );
  const {
    register,
    setValue,
    handleSubmit,
    setError,
    reset,
    formState: { errors },
  } = useForm<validForm>({ resolver: zodResolver(schema) });

  const { fetchDesignations, saveUser } = useRegister();
  const existingRoles = ["ADMIN", "EMPLOYEE"];
  const roleOptions = existingRoles.map((role) => {
    return {
      label: role,
      value: role,
    };
  });
  const designationOptions = existingDesignations.map((designation) => {
    return {
      label: designation,
      value: designation,
    };
  });

  const handleRoleChange = (options: MultiValue<Option>) => {
    const selectedRoles = options.map((option) => option.value);
    setError("roles", {});
    if (selectedRoles.length > 0) {
      setValue("roles", [selectedRoles[0], ...selectedRoles.slice(1)]);
    } else {
      reset({
        roles: [],
      });
    }
  };

  const handleDesignationChange = (option: SingleValue<Option>) => {
    if (option) {
      setValue("designation", option.value);
    } else {
      reset({
        designation: undefined,
      });
    }
  };
  setValue("joiningDate", new Date().toISOString().split("T")[0]);
  useEffect(() => {
    // localStorage.page=4;
    fetchDesignations();
  }, []);
  const onSubmit = async (data: validForm) => {
    console.log(data);
    await saveUser(data).then(()=>{
      reset();
    })
    .catch(err=>{
      console.log(err);
    });
  };
  return (
    <Box>
      <HStack mb="2">
        <IoHome />
        <ChevronRightIcon />
        <Text>Create User</Text>
      </HStack>
      <Box my="5">
        <form onSubmit={handleSubmit(onSubmit)}>
          <FormControl maxW="50rem" isRequired my="3">
            <FormLabel>Employee ID:</FormLabel>
            <Input
              type="number"
              {...register("employeeId", { valueAsNumber: true })}
            />
          </FormControl>
          <FormControl maxW="50rem" isRequired my="3">
            <FormLabel>Full Name:</FormLabel>
            <Input type="text" {...register("name")} />
          </FormControl>
          <FormControl maxW="50rem" isRequired my="3">
            <FormLabel>Email:</FormLabel>
            <Input type="email" {...register("email")} />
          </FormControl>
          <FormControl maxW="50rem" isRequired my="2">
            <Flex justify="space-between" my="1">
              <FormLabel bg="white" my="auto">
                Designation:
              </FormLabel>
              <NewDesignationButton />
            </Flex>
            <Select
              options={designationOptions}
              onChange={handleDesignationChange}
              placeholder="Select designation"
            />
          </FormControl>
          <FormControl maxW="50rem" isRequired my="3">
            <FormLabel>Roles:</FormLabel>
            <Select
              options={roleOptions}
              isMulti
              onChange={handleRoleChange}
              placeholder="Select roles"
            />
          </FormControl>
          <FormControl maxW="50rem" isRequired my="3">
            <FormLabel>Joining Date:</FormLabel>
            <Input type="date" {...register("joiningDate")} />
          </FormControl>
          <FormControl maxW="50rem" isRequired my="3">
            <FormLabel>Password:</FormLabel>
            <Input type="password" {...register("password")} />
          </FormControl>
          {errors.designation && (
            <Text color="red">{errors.designation.message}</Text>
          )}
          <Flex maxW="50rem" justify="center">
            <Button type="submit" mt="2">
              Save
            </Button>
          </Flex>
        </form>
      </Box>
    </Box>
  );
};

export default CreateUser;
