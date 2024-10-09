import {
  Box,
  Button,
  Flex,
  FormControl,
  Input,
  InputGroup,
  InputLeftAddon,
  Text,
} from "@chakra-ui/react";
import AttributeModel from "../../../../models/AttributeModel";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect } from "react";
import useAdmin from "../../../../hooks/useAdmin";

interface Props {
  appraisalId: number;
  attributes: AttributeModel[];
}

const attribute = z.object({
  name: z.string(),
  rating: z.number().min(1).max(10),
});

const attributesSchema = z.object({
  attributes: z.array(attribute),
});
type validForm = z.infer<typeof attributesSchema>;

const AppraisalRequestAttributes = ({ appraisalId, attributes }: Props) => {
  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<validForm>({
    resolver: zodResolver(attributesSchema),
  });

  const { rateAttributes, fetchAppraisalRequestDetails } = useAdmin();

  const onSubmit = async (data: validForm) => {
    await rateAttributes({
      appraisalId: appraisalId,
      attributes: data.attributes,
    });
    fetchAppraisalRequestDetails(appraisalId);
  };

  useEffect(() => {
    if (attributes) {
      attributes.forEach((attribute, index) => {
        setValue(`attributes.${index}.name`, attribute.name);
        setValue(
          `attributes.${index}.rating`,
          attribute.rating ? attribute.rating : 0
        );
      });
    }
  }, [attributes, setValue]);

  return attributes ? (
    <Box mt="3">
      <Text m="1" fontWeight="bold">
        Attributes:
      </Text>
      <Box border="1px" borderColor="gray.200" p="2" rounded="md">
        <form onSubmit={handleSubmit(onSubmit)}>
          {attributes.map((attribute, index) => (
            <FormControl key={attribute.name} m="1">
              <InputGroup>
                <InputLeftAddon w={{ base: "75%", md: "50%" }}>
                  {attribute.name}
                </InputLeftAddon>
                <Input
                  type="number"
                  isRequired
                  {...register(`attributes.${index}.rating`, {
                    valueAsNumber: true,
                    min: 0,
                    max: 10,
                  })}
                />
              </InputGroup>
              {errors.attributes && errors.attributes[index] && (
                <Text color="red">{errors.attributes[index].message}</Text>
              )}
            </FormControl>
          ))}
          <Flex justifyContent="end" m="1" mt="2">
            <Button type="submit">Save</Button>
          </Flex>
        </form>
      </Box>
    </Box>
  ) : (
    <></>
  );
};

export default AppraisalRequestAttributes;
