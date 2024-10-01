import { Box } from "@chakra-ui/react";
import { IoIosLogOut } from "react-icons/io";
import useBroadcast from "../../../hooks/useBroadcast";
import { useDispatch } from "react-redux";
import { setSelectedPage } from "../../../stores/store";

const LogoutButton = () => {
  const channel = useBroadcast();
  const dispath = useDispatch();
  const handleClick = () => {
    dispath(setSelectedPage(0));
    localStorage.clear();
    channel.postMessage({ type: "LOGOUT" });
  };
  return (
    <Box as="button" rounded="50%" p="1" onClick={handleClick}>
      <IoIosLogOut fontSize="1.5rem" />
    </Box>
  );
};

export default LogoutButton;
