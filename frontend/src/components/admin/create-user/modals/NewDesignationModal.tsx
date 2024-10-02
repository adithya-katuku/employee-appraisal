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
import { MultiSelect,  SelectOnChange, useMultiSelect } from "chakra-multiselect";
import {  useForm } from "react-hook-form";
import { useSelector } from "react-redux";
import { z } from "zod";
import { RootState } from "../../../../stores/store";
import useRegister from "../../../../hooks/useRegister";

interface Props {
  isOpen: boolean;
  onClose: () => void;
}

const schema = z.object({
  name: z.string(),
  attributes: z.string().array().nonempty({ message: "Attributes can't be empty!" }),
});

type validForm = z.infer<typeof schema>;

const NewDesignationModal = ({ isOpen, onClose }: Props) => {
  const existingAttributes = useSelector((state:RootState)=>state.store.attributes);
  const defaultAttributeOptions = existingAttributes.map((attribute)=>{
    return {
      label:attribute,
      value:attribute
    }
  });
  const { handleSubmit, setValue, reset, setError, formState:{errors}, register } = useForm<validForm>({
    resolver: zodResolver(schema),
  });
  const { value, options, onChange } = useMultiSelect({
    value: [],
    options: defaultAttributeOptions,
  });
  const handleAttributeChange:SelectOnChange = (options)=>{
    onChange(options);
    const selectedAttributes = Array.isArray(options)?options.map(option=>option.value.toString()):[options.value.toString()];
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
    console.log(data);
    saveDesignation(data);
    handleClose();
  };
  const handleClose = () => {
    onChange([]);
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
              <MultiSelect
              placeholder="Select attributes"
                options={options}
                value={value}
                onChange={handleAttributeChange} 
                create
              />
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
