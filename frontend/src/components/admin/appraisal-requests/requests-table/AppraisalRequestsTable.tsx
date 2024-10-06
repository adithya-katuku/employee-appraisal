import {
  TableContainer,
  Table,
  Thead,
  Tr,
  Th,
  Tbody,
  Td,
} from "@chakra-ui/react";
import AppraisalRequestsFormEntry from "../../../../models/admin/appraisal-requests/AppraisalRequestsFormEntry";
import ViewDetails from "../buttons/ViewDetails";

interface Props {
  appraisalRequests: AppraisalRequestsFormEntry[];
}

const AppraisalTable = ({ appraisalRequests }: Props) => {
  return (
    <TableContainer>
      <Table maxW="50rem">
        <Thead>
          <Tr>
            <Th>Employee ID</Th>
            <Th>Name</Th>
            <Th>Action</Th>
          </Tr>
        </Thead>
        <Tbody>
          {appraisalRequests &&
            appraisalRequests.map((appraisalRequest, index) => (
              <Tr key={index}>
                <Td>{appraisalRequest.employeeId}</Td>
                <Td>{appraisalRequest.employeeName}</Td>
                <Td>
                  <ViewDetails appriasalId={appraisalRequest.appraisalId} />
                </Td>
              </Tr>
            ))}
        </Tbody>
      </Table>
    </TableContainer>
  );
};

export default AppraisalTable;
