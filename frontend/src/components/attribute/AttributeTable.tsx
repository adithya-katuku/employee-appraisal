import {
  TableContainer,
  Table,
  Thead,
  Tr,
  Th,
  Tbody,
  Td,
} from "@chakra-ui/react";
import AttributeModel from "../../models/AttributeModel";

interface Props{
    attributes: AttributeModel[]
}

const AttributeTable = ({attributes}: Props) => {
  return (
    <TableContainer>
      <Table maxW="50rem">
        <Thead>
          <Tr>
            <Th>Attribute</Th>
            <Th>Rating</Th>
          </Tr>
        </Thead>
        <Tbody>
          {attributes &&
            attributes.map((attribute, index) => (
              <Tr key={index}>
                <Td>{attribute.name}</Td>
                <Td>{attribute.rating}</Td>
              </Tr>
            ))}
        </Tbody>
      </Table>
    </TableContainer>
  );
};

export default AttributeTable;
