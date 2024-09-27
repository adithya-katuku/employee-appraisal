import { Box } from "@chakra-ui/react";
import { IoIosLogOut } from "react-icons/io";
import usePaths from "../../hooks/usePaths";
import useBroadcast from "../../hooks/useBroadcast";

const LogoutButton = () => {
  const {setSelected} = usePaths();
  const channel = useBroadcast();
  const handleClick = () => {
    setSelected(0);
    channel.postMessage({type:'LOGOUT'})
    localStorage.clear();
  };
  return (
    <Box as="button" rounded="50%" p="1" onClick={handleClick}>
      <IoIosLogOut fontSize="1.5rem" />
    </Box>
  );
};

export default LogoutButton;
