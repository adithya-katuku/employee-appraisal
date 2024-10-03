import { Box } from "@chakra-ui/react";
import { IoIosLogOut } from "react-icons/io";
import useBroadcast from "../../../hooks/useBroadcast";
import { useDispatch } from "react-redux";
import {  setSelectedPage } from "../../../stores/store";
import axios from "axios";

const LogoutButton = () => {
  const channel = useBroadcast();
  const dispath = useDispatch();
  const handleClick = async () => {
    dispath(setSelectedPage(0));
    await axios
      .post(
        "http://localhost:8080/log-out",
        {},
        {
          withCredentials: true,
        }
      )
      .then((res) => {
        // console.log(res);
      });
    channel.postMessage({ type: "LOGOUT" });
  };
  return (
    <Box as="button" rounded="50%" p="1" onClick={handleClick}>
      <IoIosLogOut fontSize="1.5rem" />
    </Box>
  );
};

export default LogoutButton;
