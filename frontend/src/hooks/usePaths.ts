import { useSelector } from "react-redux";
import { RootState } from "../stores/store";
import { useState } from "react";

const usePaths = () => {
  const loginState = useSelector((state: RootState) => state.store.loginState);
  const [selectedOption, setSelected] = useState(localStorage.page?JSON.parse(localStorage.page):0);
  const options = [
    { name: "Home", path: `/${loginState.role}/home`, selected: selectedOption===0 },
    { name: "Timings", path: `/${loginState.role}/home`, selected: selectedOption===1 },
    { name: "Leaves", path: `/${loginState.role}/home`, selected: selectedOption===2 },
    { name: "WFH", path: `/${loginState.role}/home`, selected: selectedOption===3 },
    { name: "Tasks", path: `/${loginState.role}/tasks`, selected: selectedOption===4 },
    { name: "Teams", path: `/${loginState.role}/home`, selected: selectedOption===5 },
    { name: "People", path: `/${loginState.role}/home`, selected: selectedOption===6 },
    loginState.role === "admin"
      ? {
          name: "Appraisal Requests",
          path: `/${loginState.role}/appraisal-requests`,
          selected: selectedOption===7,
        }
      : {
          name: "Appraisal",
          path: `/${loginState.role}/appraisal`,
          selected: selectedOption===7,
        },
  ];

  return {options, setSelected};
};

export default usePaths;
