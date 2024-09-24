import {
  Box,
  Button,
  FormControl,
  FormLabel,
  Input,
  Stack,
  Heading,
  useToast,
  Image,
  Flex,
  AlertStatus,
} from "@chakra-ui/react";
import axios, { AxiosError } from "axios";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { login, RootState } from "../stores/store";
import { Navigate } from "react-router-dom";
import { RepeatIcon } from "@chakra-ui/icons";
import { z } from "zod";
import { FieldValues, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

interface response {
  detail: string;
}

const schema = z.object({
  email:z.string(),
  password:z.string(),
  captchaAnswer:z.string()
})

type validForm = z.infer<typeof schema>;

const LoginForm = () => {
  const {register, handleSubmit} = useForm<validForm>({resolver:zodResolver(schema)});
  const [url, setUrl] = useState("");
  const [captchaId, setCaptchaId] = useState(0);
  const toast = useToast();
  const dispatch = useDispatch();
  const isLoggedIn = useSelector((state: RootState) => state.store.isLoggedIn);

  const generateCaptcha = async () => {
    await axios
      .get("http://localhost:8080/captcha/generate-captcha")
      .then(({ data }) => {
        setUrl(`data:image/png;base64,${data.captcha}`);
        setCaptchaId(data.captchaId);
      });
  };
  useEffect(() => {
    generateCaptcha();
  }, []);

  if (sessionStorage.jwt) {
    axios
      .get("http://localhost:8080/admin/info", {
        headers: {
          Authorization: "Bearer " + localStorage.jwt,
        },
      })
      .then(() => {
        dispatch(login(true));
      })
      .catch((err: AxiosError) => console.log(err));
    if (isLoggedIn) {
      return <Navigate to="/home" />;
    }
  }

  const callToast = (title: string, description: string, status: AlertStatus) => {
    toast({
      title: title,
      description: description,
      status: status,
      duration: 3000,
      isClosable: true,
    });
  };

  const onSubmit = async (data:FieldValues) => {
    await axios
      .post("http://localhost:8080/login", {...data,
        captchaId,
      })
      .then((res) => {
        console.log(res.data);
        sessionStorage.setItem("jwt", res.data.jwtToken);
        localStorage.setItem("jwt", res.data.jwtToken);
        localStorage.setItem("role", res.data.role);
        dispatch(login(true));
        generateCaptcha();
        callToast("Login Successful", "You are now logged in.", "success");
      })
      .catch((err: AxiosError<response>) => {
        console.log(err);
        callToast(
          "Login Failed",
          err.response ? err.response.data.detail : "Login failed",
          "error"
        );
        generateCaptcha();
      });
  };

  return (
    <Flex minH="100vh" align="center" justify="center" bg="gray.50">
      <Box
        minW="md"
        mx="auto"
        mt={10}
        p={6}
        boxShadow="lg"
        borderRadius="md"
        borderWidth={1}
        borderColor="gray.200"
      >
        <Heading mb={6} textAlign="center">
          Login
        </Heading>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Stack spacing={4}>
            <FormControl id="email" isRequired>
              <FormLabel>Email</FormLabel>
              <Input
                type="email"
                placeholder="Enter your email"
                {...register("email")}
              />
            </FormControl>

            <FormControl id="password" isRequired>
              <FormLabel>Password</FormLabel>
              <Input
                type="password"
                placeholder="Enter your password"
                {...register("password")}
              />
            </FormControl>
            <Flex alignItems="center">
              <Image minH="6.3rem" src={url} alt="Captcha" />
              <Button
                w="1rem"
                margin="1"
                colorScheme="teal"
                onClick={() => generateCaptcha()}
              >
                <RepeatIcon />
              </Button>
            </Flex>
            <FormControl id="captcha" isRequired>
              <FormLabel>Captcha</FormLabel>
              <Input
                type="text"
                placeholder="Enter your captcha"
                {...register("captchaAnswer")}
              />
            </FormControl>

            <Button type="submit" colorScheme="teal" width="full" mt={4}>
              Login
            </Button>
          </Stack>
        </form>
      </Box>
    </Flex>
  );
};

export default LoginForm;
