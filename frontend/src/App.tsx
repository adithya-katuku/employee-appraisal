import { Box, Flex } from '@chakra-ui/react';
import LoginForm from './components/LoginForm';

function App() {

  return (
    <Flex
      minH="100vh"
      align="center"
      justify="center"
      bg="gray.50"
    >
      <Box>
        <LoginForm />
      </Box>
    </Flex>
  )
}

export default App
