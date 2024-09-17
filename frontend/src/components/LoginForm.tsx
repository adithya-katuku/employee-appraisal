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
import axios from "axios";
import { useEffect, useState } from "react";

const LoginForm = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [url, setUrl] = useState("");
  const [captcha, setCaptcha] = useState("");
  const toast = useToast();

  useEffect(() => {
    setUrl("http://localhost:8080/captcha/");
  }, [url]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await axios
      .post("http://localhost:8080/captcha/verify-captcha/" + captcha)
      .then(async () => {
        await axios
          .post("http://localhost:8080/login", { email, password })
          .then(() => {
            toast({
              title: "Login Successful",
              description: "You are now logged in.",
              status: "success",
              duration: 3000,
              isClosable: true,
            });
            setCaptcha("");
            setUrl("");
          })
          .catch(() => {
            toast({
              title: "Login Failed",
              description: "Invalid email or password.",
              status: "error",
              duration: 3000,
              isClosable: true,
            });
            setCaptcha("");
            setUrl("");
          });
      })
      .catch((err) => {
        console.log(err);
        setCaptcha("");
        setUrl("");
        toast({
          title: "Captcha Verfication Failed",
          description: "Please enter the correct captcha",
          status: "error",
          duration: 3000,
          isClosable: true,
        });
      });
  };

  return (
    <Flex minH="100vh" align="center" justify="center" bg="gray.50">
      <Box
        maxW="md"
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

            <Image src={url} alt="Captcha" />
            <FormControl id="captcha" isRequired>
              <FormLabel>Captcha</FormLabel>
              <Input
                type="text"
                placeholder="Enter your captcha"
                value={captcha}
                onChange={(e) => setCaptcha(e.target.value)}
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
