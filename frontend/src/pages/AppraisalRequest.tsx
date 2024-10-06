import { ChevronRightIcon } from "@chakra-ui/icons";
import { Box, Flex, HStack, Text } from "@chakra-ui/react";
import { useEffect } from "react";
import { IoHome } from "react-icons/io5";
import useAdmin from "../hooks/useAdmin";
import { useSelector } from "react-redux";
import { RootState } from "../stores/store";
import AppraisalTable from "../components/admin/appraisal-requests/requests-table/AppraisalRequestsTable";

const AppraisalRequest = () => {
  const apprasialRequests = useSelector(
    (state: RootState) => state.store.appraisalRequests
  );
  const { fetchAppraisalRequests } = useAdmin();

  useEffect(() => {
    localStorage.page = 3;
    fetchAppraisalRequests();
  }, []);
  return (
    <>
      <HStack mb="2">
        <IoHome />
        <ChevronRightIcon />
        <Text>Appraisal Requests</Text>
      </HStack>
      <Box m="auto" maxW="60rem" >
        {apprasialRequests && apprasialRequests.length > 0 ? (
          <AppraisalTable appraisalRequests={apprasialRequests} />
        ) : (
          <Flex justifyContent="center">
            <Text mx="4" p="2" fontSize="1.5rem">
              No pending appraisals
            </Text>
          </Flex>
        )}
      </Box>
    </>
  );
};

export default AppraisalRequest;
