import { ChevronRightIcon } from "@chakra-ui/icons";
import { Box, HStack, Text } from "@chakra-ui/react";
import { useEffect } from "react";
import { IoHome } from "react-icons/io5";
import useData from "../hooks/useData";
import { useSelector } from "react-redux";
import { RootState } from "../stores/store";
import AppraisalTable from "../components/admin/appraisal-requests/requests-table/AppraisalRequestsTable";

const AppraisalRequest = () => {
  const apprasialRequests = useSelector(
    (state: RootState) => state.store.appraisalRequests
  );
  const { fetchAppraisalRequests } = useData();

  useEffect(() => {
    localStorage.page = 7;
    fetchAppraisalRequests();
  }, []);
  return (
    <>
      <HStack mb="2">
        <IoHome />
        <ChevronRightIcon />
        <Text>Appraisal Requests</Text>
      </HStack>
      <Box>
        {apprasialRequests && (
          <AppraisalTable appraisalRequests={apprasialRequests} />
        )}
      </Box>
    </>
  );
};

export default AppraisalRequest;
