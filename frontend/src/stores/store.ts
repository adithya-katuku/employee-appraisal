import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { configureStore } from "@reduxjs/toolkit";
import TaskModel from "../models/TaskModel";
import AppraisalModel from "../models/AppraisalModel";
import AppraisalRequestsListEntryModel from "../models/admin/appraisal-requests/AppraisalRequestsListEntryModel";
import AppraisalRequestDetailsModel from "../models/admin/appraisal-requests/AppraisalRequestDetailsModel";
import EmployeeDetailsModel from "../models/EmployeeDetailsModel";
import NotificationModel from "../models/NotificationModel";

interface LoginState {
  isLoggedIn: boolean;
  role: string;
  token: string;
}

interface Store {
  employeeId: number | undefined;
  url: string;
  captchaId: number;
  loginState: LoginState;
  tasks: TaskModel[] | undefined;
  appraisals: AppraisalModel[] | undefined;
  appraisalRequests: AppraisalRequestsListEntryModel[] | undefined;
  appraisalRequestDetails: AppraisalRequestDetailsModel | undefined;
  searchedEmployees: EmployeeDetailsModel[] | undefined;
  searchedEmployeeDetails: EmployeeDetailsModel | undefined;
  notifications: NotificationModel[] | undefined;
  selectedPage: number;
  searchedName: string | undefined;
  attributes: string[];
  designations: string[];
}

const initialState: Store = {
  employeeId: undefined,
  url: "",
  captchaId: 0,
  loginState: {
    isLoggedIn: false,
    role: "",
    token: "",
  },
  tasks: undefined,
  appraisals: undefined,
  appraisalRequests: undefined,
  appraisalRequestDetails: undefined,
  searchedEmployees: undefined,
  searchedEmployeeDetails: undefined,
  notifications: undefined,
  selectedPage: localStorage.page ? JSON.parse(localStorage.page) : 0,
  searchedName: undefined,
  attributes: [],
  designations: [],
};

export const slice = createSlice({
  name: "cart",
  initialState,
  reducers: {
    setCaptchaId: (state, action: PayloadAction<number>) => {
      state.captchaId = action.payload;
    },
    setUrl: (state, action: PayloadAction<string>) => {
      state.url = action.payload;
    },
    login: (state, action: PayloadAction<LoginState>) => {
      state.loginState.isLoggedIn = action.payload.isLoggedIn;
      state.loginState.role = action.payload.role;
      state.loginState.token = action.payload.token;
    },
    setTasks: (state, action: PayloadAction<TaskModel[]>) => {
      state.tasks = action.payload;
    },
    setEmployeeId: (state, action: PayloadAction<number>) => {
      state.employeeId = action.payload;
    },
    appendTask: (state, action: PayloadAction<TaskModel>) => {
      state.tasks = state.tasks
        ? [action.payload, ...state.tasks]
        : [action.payload];
    },
    updateTask: (state, action: PayloadAction<TaskModel>) => {
      state.tasks = state.tasks?.map((task) =>
        task.taskId === action.payload.taskId ? action.payload : task
      );
    },
    removeTask: (state, action: PayloadAction<number>) => {
      state.tasks = state.tasks?.filter(
        (task) => task.taskId !== action.payload
      );
    },
    setAppraisals: (state, action: PayloadAction<AppraisalModel[]>) => {
      state.appraisals = action.payload;
    },
    setAppraisalRequests: (
      state,
      action: PayloadAction<AppraisalRequestsListEntryModel[]>
    ) => {
      state.appraisalRequests = action.payload;
    },
    setAppraisalRequestDetails: (
      state,
      action: PayloadAction<AppraisalRequestDetailsModel>
    ) => {
      state.appraisalRequestDetails = action.payload;
    },
    setSearchedEmployees: (
      state,
      action: PayloadAction<EmployeeDetailsModel[]>
    ) => {
      state.searchedEmployees = action.payload;
    },
    clearSearchedEmployees: (state) => {
      state.searchedEmployees = undefined;
    },
    setEmployeeDetails: (
      state,
      action: PayloadAction<EmployeeDetailsModel>
    ) => {
      state.searchedEmployeeDetails = action.payload;
    },
    setNotifications: (state, action: PayloadAction<NotificationModel[]>) => {
      state.notifications = action.payload;
    },
    setSelectedPage: (state, action: PayloadAction<number>) => {
      state.selectedPage = action.payload;
    },
    setSearchedName: (state, action: PayloadAction<string | undefined>) => {
      state.searchedName = action.payload;
    },
    setAttributes: (state, action: PayloadAction<string[]>) => {
      state.attributes = action.payload;
    },
    setDesignations: (state, action: PayloadAction<string[]>) => {
      state.designations = action.payload;
    },
  },
});

export const {
  setCaptchaId,
  setUrl,
  login,
  setEmployeeId,
  setTasks,
  appendTask,
  updateTask,
  removeTask,
  setAppraisals,
  setAppraisalRequests,
  setAppraisalRequestDetails,
  setSearchedEmployees,
  setEmployeeDetails,
  clearSearchedEmployees,
  setNotifications,
  setSelectedPage,
  setSearchedName,
  setAttributes,
  setDesignations,
} = slice.actions;

export const store = configureStore({
  reducer: {
    store: slice.reducer,
  },
});
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
