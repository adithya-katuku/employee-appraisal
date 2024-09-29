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
import usePaths from "../hooks/usePaths";

interface response {
  detail: string;
}

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
  const [url, setUrl] = useState("");
  const [captchaId, setCaptchaId] = useState(0);
  const toast = useToast();
  const dispatch = useDispatch();
  const { options, setSelected } = usePaths();
  const loginState = useSelector((state: RootState) => state.store.loginState);

  const generateCaptcha = async () => {
    await axios
      .get("http://localhost:8080/captcha/generate-captcha")
      .then(({ data }) => {
        setUrl(`data:image/png;base64,${data.captcha}`);
        setCaptchaId(data.captchaId);
      });
  };
  const prevpage = localStorage.page ? parseInt(localStorage.page) : 0;
  const path = options[prevpage].path;
  useEffect(() => {
    setSelected(prevpage);
    generateCaptcha();
  }, []);
  if (loginState.isLoggedIn) {
    return <Navigate to={path} />;
  }

  const refreshJwt = (refreshTokenId: number, refreshToken: string) => {
    axios
      .post("http://localhost:8080/refresh-token", {
        refreshTokenId,
        refreshToken,
      })
      .then((res) => {
        sessionStorage.setItem("jwt", res.data.jwtToken);
        loginWIthJwt(res.data.jwtToken);
      })
      .catch((err: AxiosError) => console.log(err));
  };
  const loginWIthJwt = (jwt: string) => {
    axios
      .get("http://localhost:8080/jwt-login", {
        headers: {
          Authorization: "Bearer " + jwt,
        },
      })
      .then((res) => {
        localStorage.role = res.data.role;
        const newLoginState = {
          isLoggedIn: true,
          role: res.data.role,
          token: jwt,
        };
        dispatch(login(newLoginState));
      })
      .catch((err: AxiosError) => {
        console.log(err);
        refreshJwt(sessionStorage.refreshTokenId, sessionStorage.refreshToken);
      });
  };
  if (loginState.token) {
    loginWIthJwt(loginState.token);
  }
  if (sessionStorage.refreshToken && sessionStorage.refreshTokenId) {
    refreshJwt(sessionStorage.refreshTokenId, sessionStorage.refreshToken);
  }

  const callToast = (
    title: string,
    description: string,
    status: AlertStatus
  ) => {
    toast({
      title: title,
      description: description,
      status: status,
      duration: 3000,
      isClosable: true,
    });
  };

  const onSubmit = async (data: FieldValues) => {
    await axios
      .post("http://localhost:8080/login", { ...data, captchaId })
      .then((res) => {
        console.log(res.data);
        sessionStorage.setItem("refreshToken", res.data.refreshToken);
        sessionStorage.setItem("refreshTokenId", res.data.refreshTokenId);
        const newLoginState = {
          isLoggedIn: true,
          role: res.data.role,
          token: res.data.jwtToken,
        };
        dispatch(login(newLoginState));
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
