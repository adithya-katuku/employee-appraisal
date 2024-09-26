import { Box } from "@chakra-ui/react";
import { IoIosLogOut } from "react-icons/io";
import { useDispatch } from "react-redux";
import { login } from "../../stores/store";

const LogoutButton = () => {
  const dispatch = useDispatch();
  const handleClick = () => {
    sessionStorage.clear();
    localStorage.clear();
    dispatch(login(false));
  };
  return (
    <Box as="button" rounded="50%" p="1" onClick={handleClick}>
      <IoIosLogOut fontSize="1.5rem" />
    </Box>
  );
};

export default LogoutButton;
