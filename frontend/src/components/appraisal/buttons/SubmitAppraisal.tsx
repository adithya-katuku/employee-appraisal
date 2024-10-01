import { Box, Button } from "@chakra-ui/react";
import useData from "../../../hooks/useData";
import axios from "axios";

interface Props {
  appraisalId: number;
}
const SubmitAppraisal = ({ appraisalId }: Props) => {
  const { fetchTasks, fetchAppraisals } = useData();
  const handleClick = async () => {
    await axios
      .put(
        "http://localhost:8080/" +
          localStorage.role +
          "/appraisals/" +
          appraisalId,
        {},
        {
          headers: {
            Authorization: "Bearer " + sessionStorage.jwt,
          },
        }
      )
      .then(() => {
        fetchTasks();
        fetchAppraisals();
      })
      .catch((err) => {
        console.log(err);
      });
  };
  return (
    <Box>
      <Button
        m="1"
        p="2"
        border="1px"
        borderColor="gray.400"
        colorScheme="purple"
        onClick={handleClick}
      >
        Submit
      </Button>
    </Box>
  );
};

export default SubmitAppraisal;
