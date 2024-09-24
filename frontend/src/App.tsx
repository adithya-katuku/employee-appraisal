import { useSelector } from "react-redux";
import Home from "./pages/Home";
import Login from "./pages/Login";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { RootState } from "./stores/store";
import { Box } from "@chakra-ui/react";
import Navbar from "./components/Navbar";
import VerticalNavbar from "./components/VerticalNavbar";
import Wrapper from "./components/Wrapper";

function App() {
  const isLoggedIn = useSelector((state: RootState) => state.store.isLoggedIn);
  return (
    <BrowserRouter>
      <Navbar />
      <Box p="3" display={isLoggedIn ? "flex" : "contents"}>
        <VerticalNavbar />
        <Wrapper>
          <Routes>
            <Route path="/login" Component={Login}></Route>
            {isLoggedIn && <Route path="/home" Component={Home}></Route>}
            <Route path="/*" Component={() => <Navigate to="/login" />}></Route>
          </Routes>
        </Wrapper>
      </Box>
    </BrowserRouter>
  );
}

export default App;
