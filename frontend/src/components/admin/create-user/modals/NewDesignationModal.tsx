import {
  Button,
  FormControl,
  FormLabel,
  Input,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Text,
} from "@chakra-ui/react";
import { zodResolver } from "@hookform/resolvers/zod";
import {  useForm } from "react-hook-form";
import { useSelector } from "react-redux";
import { z } from "zod";
import { RootState } from "../../../../stores/store";
import useRegister from "../../../../hooks/useRegister";
import { MultiValue } from "react-select"
import CreatableSelect from "react-select/creatable";
interface Props {
  isOpen: boolean;
  onClose: () => void;
}

const schema = z.object({
  name: z.string(),
  attributes: z.string().array().nonempty({ message: "Attributes can't be empty!" }),
});

interface Option{
  label:string;
  value:string;
}
type validForm = z.infer<typeof schema>;

const NewDesignationModal = ({ isOpen, onClose }: Props) => {
  const existingAttributes = useSelector((state:RootState)=>state.store.attributes);
  const attributeOptions = existingAttributes.map((attribute)=>{
    return {
      label:attribute,
      value:attribute
    }
  });
  const { handleSubmit, setValue, reset, setError, formState:{errors}, register } = useForm<validForm>({
    resolver: zodResolver(schema),
  });

  const handleAttributeChange= (selectedOptions:MultiValue<Option>)=>{
    const selectedAttributes = selectedOptions.map(option=>option.value.toString());
    setError("attributes", {});
    
    if(selectedAttributes.length>0){
        setValue("attributes",[selectedAttributes[0], ...selectedAttributes.slice(1)]) ;
    }
    else{
        reset({
            attributes:[]
        })
    }
  }
  const {saveDesignation} = useRegister();

  const onSubmit = (data:validForm) => {
      saveDesignation(data);
    handleClose();
  };
  const handleClose = () => {
    reset();
    onClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={handleClose}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>
          New Designation
          <ModalCloseButton />
        </ModalHeader>
        <form onSubmit={handleSubmit(onSubmit)}>
          <ModalBody>
            <FormControl maxW="50rem" isRequired my="3">
              <FormLabel>Designation name:</FormLabel>
              <Input type="text" {...register("name")} />
            </FormControl>
            <FormControl maxW="50rem" isRequired my="3">
              <FormLabel>Attributes:</FormLabel>

              <CreatableSelect options={attributeOptions} isMulti onChange={handleAttributeChange} />
            {errors.attributes && <Text color="red.400" >{errors.attributes.message}</Text>}
            </FormControl>
          </ModalBody>
          <ModalFooter gap="2">
            <Button type="submit" colorScheme="green">
              Save
            </Button>
            <Button colorScheme="red" variant="outline" onClick={handleClose}>
              Cancel
            </Button>
          </ModalFooter>
        </form>
      </ModalContent>
    </Modal>
  );
};

export default NewDesignationModal;
