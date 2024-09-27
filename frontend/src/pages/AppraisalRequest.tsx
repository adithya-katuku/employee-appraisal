import { ChevronRightIcon } from "@chakra-ui/icons";
import { Box, HStack, Text } from "@chakra-ui/react";
import { useEffect } from "react";
import { IoHome } from "react-icons/io5";

const AppraisalRequest = () => {


  useEffect(() => {
    localStorage.page = 7;
  });
  return (
    <>
      <HStack mb="2">
        <IoHome />
        <ChevronRightIcon />
        <Text>Appraisal Requests</Text>
      </HStack>
      <Box>
        
      </Box>
    </>
  );
};

export default AppraisalRequest;
