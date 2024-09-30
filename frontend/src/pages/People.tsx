import { ChevronRightIcon } from "@chakra-ui/icons";
import {
  Box,
  HStack,
  Input,
  InputGroup,
  InputLeftElement,
  Text,
} from "@chakra-ui/react";
import { useEffect, useState } from "react";
import { FaSearch } from "react-icons/fa";
import { IoHome } from "react-icons/io5";
import { useDebounce } from "use-debounce";
import useData from "../hooks/useData";
import SelectEmployee from "../components/people/SelectEmployee";

const People = () => {
  const [name, setName] = useState<string>("");
  const [debouncedName] = useDebounce(name, 1000);
  const { searchEmployees,  clearEmployees } = useData();
  
  useEffect(() => {
    localStorage.page = 6;
    if (debouncedName) {
      searchEmployees(debouncedName);
    }
    return clearEmployees;
  }, [debouncedName]);

  return (
    <Box>
      <HStack mb="2">
        <IoHome />
        <ChevronRightIcon />
        <Text>People</Text>
      </HStack>
      <InputGroup>
        <InputLeftElement pointerEvents="none">
          <FaSearch />
        </InputLeftElement>
        <Input
          type="tel"
          placeholder="Please enter name to search"
          onChange={(e) => setName(e.target.value)}
        />
      </InputGroup>

      <SelectEmployee />
    </Box>
  );
};

export default People;
