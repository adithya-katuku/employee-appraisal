import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { configureStore } from "@reduxjs/toolkit";
import TaskModel from "../models/TaskModel";
import AppraisalModel from "../models/AppraisalModel";

interface LoginState {
  isLoggedIn: boolean;
  role: string;
  token: string;
}

interface Store {
  employeeId:number|undefined;
  loginState: LoginState;
  tasks: TaskModel[] | undefined;
  appraisals: AppraisalModel[] | undefined;
}

const initialState: Store = {
  employeeId:undefined,
  loginState: {
    isLoggedIn: false,
    role: "",
    token: "",
  },
  tasks: undefined,
  appraisals: undefined,
};

export const slice = createSlice({
  name: "cart",
  initialState,
  reducers: {
    login: (state, action: PayloadAction<LoginState>) => {
      state.loginState.isLoggedIn = action.payload.isLoggedIn;
      state.loginState.role = action.payload.role;
      state.loginState.token = action.payload.token;
    },
    setTasks: (state, action: PayloadAction<TaskModel[]>) => {
      state.tasks = action.payload;
    },
    addTask: (state, action: PayloadAction<TaskModel>) => {
      state.tasks = state.tasks
        ? [action.payload, ...state.tasks]
        : [action.payload];
    },
    updateTask: (state, action: PayloadAction<TaskModel>) => {
      state.tasks = state.tasks?.map((task) =>
        task.taskId === action.payload.taskId ? action.payload : task
      );
    },
    deleteTask: (state, action: PayloadAction<number>) => {
      state.tasks = state.tasks?.filter(
        (task) => task.taskId !== action.payload
      );
    },
    setAppraisals: (state, action: PayloadAction<AppraisalModel[]>) => {
      state.appraisals = action.payload;
    },
  },
});

export const {
  login,
  setTasks,
  addTask,
  updateTask,
  deleteTask,
  setAppraisals,
} = slice.actions;

export const store = configureStore({
  reducer: {
    store: slice.reducer,
  },
});
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
