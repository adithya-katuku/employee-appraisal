import LoginForm from "./components/LoginForm";
import {BrowserRouter, Routes, Route, Navigate} from "react-router-dom";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" Component={LoginForm}></Route>
        <Route path="/*" Component={()=><Navigate to="/login"/>}></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
