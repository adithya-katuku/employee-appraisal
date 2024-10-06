import {
  Box,
  Button,
  FormControl,
  FormLabel,
  Input,
  Stack,
  Heading,
  Image,
  Flex,
} from "@chakra-ui/react";
import { useEffect } from "react";
import { useSelector } from "react-redux";
import {  RootState } from "../stores/store";
import { RepeatIcon } from "@chakra-ui/icons";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import useAuth from "../hooks/useAuth";


const schema = z.object({
  email: z.string(),
  password: z.string(),
  captchaAnswer: z.string(),
});

type validForm = z.infer<typeof schema>;

const LoginForm = () => {
  const { register, handleSubmit } = useForm<validForm>({
    resolver: zodResolver(schema),
  });
  const { refreshJwt, redirect, generateCaptcha, loginWithCredentials } =
    useAuth();
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const url = useSelector((state: RootState) => state.store.url);

  useEffect(() => {
    const authFun = async () => {
      try {
        await refreshJwt();
      } catch {
        generateCaptcha();
      }
    };

    if (loginState.isLoggedIn) {
      redirect();
    } else {
      authFun();
    }
  }, []);

  const onSubmit = async (data: validForm) => {
    loginWithCredentials(data);
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
