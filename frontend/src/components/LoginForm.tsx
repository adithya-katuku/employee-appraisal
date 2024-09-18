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
} from "@chakra-ui/react";
import axios, { AxiosError } from "axios";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { login, RootState } from "../stores/store";
import { Navigate } from "react-router-dom";

const LoginForm = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [url, setUrl] = useState("");
  const [captchaAnswer, setCaptchaAnswer] = useState("");
  const [captchaId, setCaptchaId] = useState(0);
  const toast = useToast();
  const dispatch = useDispatch();
  const isLoggedIn = useSelector((state: RootState) => state.store.isLoggedIn);

  const generateCaptcha = async ()=>{
    await axios
      .get("http://localhost:8080/captcha/generate-captcha")
      .then(({ data }) => {
        setUrl(`data:image/png;base64,${data.image}`);
        setCaptchaId(data.captchaId);
      });
  }
  useEffect(() => {
    generateCaptcha();
  }, []);

  if(isLoggedIn){
    return <Navigate to="/home"/>
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await axios
      .post("http://localhost:8080/login", {
        email,
        password,
        captchaId,
        captchaAnswer,
      })
      .then(() => {
        dispatch(login(true));
        generateCaptcha();
        toast({
          title: "Login Successful",
          description: "You are now logged in.",
          status: "success",
          duration: 3000,
          isClosable: true,
        });
        setCaptchaAnswer("");
        setUrl("");
      })
      .catch((err:AxiosError) => {
        toast({
          title: "Login Failed",
          description: err.message,
          status: "error",
          duration: 3000,
          isClosable: true,
        });
        setCaptchaAnswer("");
        generateCaptcha();
      });
  };

  return (
    <Flex minH="100vh" align="center" justify="center" bg="gray.50">
      <Box
        w="md"
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
        <form onSubmit={handleSubmit}>
          <Stack spacing={4}>
            <FormControl id="email" isRequired>
              <FormLabel>Email</FormLabel>
              <Input
                type="email"
                placeholder="Enter your email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </FormControl>

            <FormControl id="password" isRequired>
              <FormLabel>Password</FormLabel>
              <Input
                type="password"
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </FormControl>

            <Image minH="6.3rem" src={url} alt="Captcha" />

            <FormControl id="captcha" isRequired>
              <FormLabel>Captcha</FormLabel>
              <Input
                type="text"
                placeholder="Enter your captcha"
                value={captchaAnswer}
                onChange={(e) => setCaptchaAnswer(e.target.value)}
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
