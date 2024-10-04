import { Box } from "@chakra-ui/react";
import { IoIosLogOut } from "react-icons/io";
import { useDispatch } from "react-redux";
import {  setSelectedPage } from "../../../stores/store";
import useAuth from "../../../hooks/useAuth";

const LogoutButton = () => {
  const dispath = useDispatch();
  const {logout} = useAuth();
  const handleClick = async () => {
    dispath(setSelectedPage(0));
    logout();
  };
  return (
    <Box as="button" rounded="50%" p="1" onClick={handleClick}>
      <IoIosLogOut fontSize="1.5rem" />
    </Box>
  );
};

export default LogoutButton;
