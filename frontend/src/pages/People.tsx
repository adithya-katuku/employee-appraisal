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
import { RootState } from "../stores/store";
import { useSelector } from "react-redux";

const People = () => {
  const searchedName = useSelector(
    (state: RootState) => state.store.searchedName
  );
  const role = useSelector((state: RootState) => state.store.loginState.role);
  const [name, setName] = useState<string>(searchedName || "");
  const [debouncedName] = useDebounce(name, 1000);
  const { searchEmployees, clearEmployees } = useData();

  useEffect(() => {
    localStorage.page = 2;
    if (debouncedName) {
      searchEmployees(debouncedName);
    }
    return clearEmployees;
  }, [debouncedName]);

  useEffect(() => {
    if (searchedName) {
      setName(searchedName);
    }
  }, [searchedName]);

  const placeholder = `Please enter name ${
    role === "admin" ? "or id " : ""
  }to search`;

  return (
    <Box>
      <HStack mb="2">
        <IoHome />
        <ChevronRightIcon />
        <Text>People</Text>
      </HStack>
      <Box m="auto" maxW="60rem">
        <InputGroup>
          <InputLeftElement pointerEvents="none">
            <FaSearch />
          </InputLeftElement>
          <Input
            type="text"
            value={name}
            placeholder={placeholder}
            onChange={(e) => setName(e.target.value)}
          />
        </InputGroup>
        <SelectEmployee />
      </Box>
    </Box>
  );
};

export default People;
