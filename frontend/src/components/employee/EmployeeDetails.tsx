import { Box, Text, VStack } from "@chakra-ui/react";
import EmployeeDetailsModel from "../../models/EmployeeDetailsModel";

interface Props {
  employeeDetails: EmployeeDetailsModel | undefined;
}
const EmployeeDetails = ({ employeeDetails }: Props) => {
  return employeeDetails ? (
    <Box mb="3" >
      <Text m="1" fontWeight="bold">
        Employee:
      </Text>
      <VStack
        align="start"
        spacing={3}
        m="1"
        p="2"
        borderWidth="1px"
        borderRadius="md"
      >
        <Text>Employee Name: {employeeDetails.name}</Text>
        <Text>Employee ID: {employeeDetails.employeeId}</Text>
        <Text>Designation: {employeeDetails.designation}</Text>
        <Text>Email: {employeeDetails.email}</Text>
        <Text>
          Joining Date:{" "}
          {new Date(employeeDetails.joiningDate).toISOString().split("T")[0]}
        </Text>
        {employeeDetails.appraisalEligibility && (
          <Text>
            Appraisal Eligibility: {employeeDetails.appraisalEligibility}
          </Text>
        )}
        {employeeDetails.previousAppraisalDate && (
          <Text>
            Previous Appraisal Date: {new Date(employeeDetails.previousAppraisalDate).toISOString().split("T")[0]}
          </Text>
        )}
      </VStack>
    </Box>
  ) : (
    <></>
  );
};

export default EmployeeDetails;
