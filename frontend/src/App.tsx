import { useSelector } from "react-redux";
import Home from "./components/Home";
import LoginForm from "./components/LoginForm";
import {BrowserRouter, Routes, Route, Navigate} from "react-router-dom";
import { RootState } from "./stores/store";

function App() {
  const isLoggedIn = useSelector((state: RootState) => state.store.isLoggedIn);
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" Component={LoginForm}></Route>
        {isLoggedIn && <Route path="/home" Component={Home}></Route>}
        <Route path="/*" Component={()=><Navigate to="/login"/>}></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
