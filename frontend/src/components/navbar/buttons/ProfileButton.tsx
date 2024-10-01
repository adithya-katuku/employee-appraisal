import { Box } from "@chakra-ui/react";
import { FaUserCircle } from "react-icons/fa";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { RootState, setSelectedPage } from "../../../stores/store";
import useData from "../../../hooks/useData";

const ProfileButton = () => {
  const role = useSelector((state: RootState) => state.store.loginState.role);
  const dispatch = useDispatch();
  const {fetchInfo} = useData();
  const handleClick = () => {
    dispatch(setSelectedPage(-1));
    fetchInfo();
  };

  return (
    <Box as="button" p="1">
      <Link to={`${role}/profile`} onClick={handleClick}>
        <FaUserCircle fontSize="1.5rem" />
      </Link>
    </Box>
  );
};

export default ProfileButton;
