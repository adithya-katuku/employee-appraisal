import { useSelector } from "react-redux";
import Home from "./pages/Home";
import Login from "./pages/Login";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { RootState } from "./stores/store";
import { Box } from "@chakra-ui/react";
import Navbar from "./components/navbar/Navbar";
import VerticalNavbar from "./components/navbar/VerticalNavbar";
import Wrapper from "./components/navbar/Wrapper";
import Appraisal from "./pages/Appraisal";
import Tasks from "./pages/Tasks";
import ProtectedRoutes from "./components/auth/ProtectedRoutes";
import EmployeeRoutes from "./components/auth/EmployeeRoutes";
import AppraisalRequest from "./pages/AppraisalRequest";
import AdminRoutes from "./components/auth/AdminRoutes";
import People from "./pages/People";
import EmployeeDetailsPage from "./pages/EmployeeDetailsPage";

function App() {
  const isLoggedIn = useSelector((state: RootState) => state.store.loginState.isLoggedIn);
  return (
    <BrowserRouter>
      <Navbar />
      <Box p="3" display={isLoggedIn ? "flex" : "contents"}>
        <VerticalNavbar />
        <Wrapper>
          <Routes>
            <Route path="/login" Component={Login}></Route>
            <Route Component={ProtectedRoutes}>
              <Route path="/employee" Component={EmployeeRoutes}>
                <Route path="home" Component={Home}></Route>
                <Route path="appraisal" Component={Appraisal}></Route>
                <Route path="tasks" Component={Tasks}></Route>
                <Route path="people" Component={People}></Route>
              </Route>
              <Route path="/admin" Component={AdminRoutes}>
                <Route path="home" Component={Home}></Route>
                <Route path="tasks" Component={Tasks}></Route>
                <Route path="people" Component={People}>
                  <Route path=":name" Component={EmployeeDetailsPage} ></Route>
                </Route>
                <Route path="appraisal-requests" Component={AppraisalRequest} />
              </Route>
            </Route>
            <Route path="/*" Component={() => <Navigate to="/login" />}></Route>
          </Routes>
        </Wrapper>
      </Box>
    </BrowserRouter>
  );
}

export default App;
