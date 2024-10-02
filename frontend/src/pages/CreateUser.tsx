import { ChevronRightIcon } from "@chakra-ui/icons";
import {
  Box,
  Button,
  Flex,
  FormControl,
  FormLabel,
  HStack,
  Input,
  Select,
  Text,
} from "@chakra-ui/react";
import { IoHome } from "react-icons/io5";
import NewDesignation from "../components/admin/create-user/buttons/NewDesignation";
// import { useEffect } from "react";

const CreateUser = () => {

  // useEffect(()=>{
  //   localStorage.page=4;
  // })

  return (
    <Box>
      <HStack mb="2">
        <IoHome />
        <ChevronRightIcon />
        <Text>Create User</Text>
      </HStack>
      <Box my="5">
        <form>
          <FormControl maxW="50rem" isRequired my="3">
            <FormLabel >Employee ID:</FormLabel>
            <Input type="number" />
          </FormControl>
          <FormControl maxW="50rem" isRequired my="3">
            <FormLabel >Full Name:</FormLabel>
            <Input type="text" />
          </FormControl>
          <FormControl maxW="50rem" isRequired my="3">
            <FormLabel >Email:</FormLabel>
            <Input type="email" />
          </FormControl>
          <FormControl maxW="50rem" isRequired my="2">
            <Flex justify="space-between"  my="1">
              <FormLabel bg="white" my="auto" >Designation:</FormLabel>
              <NewDesignation />
            </Flex>
            <Select placeholder="Select designation">
              <option value="option1">Option 1</option>
              <option value="option2">Option 3</option>
              <option value="option3">Option 3</option>
            </Select>
          </FormControl>
          <FormControl maxW="50rem" isRequired my="3">
            <FormLabel >Roles:</FormLabel>
            <Select placeholder="Select roles">
              <option value="option1">Option 1</option>
              <option value="option2">Option 3</option>
              <option value="option3">Option 3</option>
            </Select>
          </FormControl>
          <FormControl maxW="50rem" isRequired my="3">
            <FormLabel >Joining Date:</FormLabel>
            <Input type="date" />
          </FormControl>
          <FormControl maxW="50rem" isRequired my="3">
            <FormLabel >Password:</FormLabel>
            <Input type="password" />
          </FormControl>
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