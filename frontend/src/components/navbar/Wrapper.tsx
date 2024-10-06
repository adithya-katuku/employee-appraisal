import { Box } from "@chakra-ui/react";
import { ReactNode } from "react";
import { useSelector } from "react-redux";
import { RootState } from "../../stores/store";

interface Props {
  children: ReactNode;
}

const Wrapper = ({ children }: Props) => {
  const isLoggedIn = useSelector((state: RootState) => state.store.loginState.isLoggedIn);
  return isLoggedIn ? (
    <Box
      p="3"
      border="1px"
      borderColor="gray"
      m="1"
      rounded="md"
      flexGrow="1"
      ml="17rem"
      minH="80vh"
    >
      {children}
    </Box>
  ) : (
    <>{children}</>
  );
};

export default Wrapper;
