import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { configureStore } from "@reduxjs/toolkit";


interface Store{
  isLoggedIn: boolean;
  
}

const initialState: Store = {
    isLoggedIn : false
};

export const slice = createSlice({
  name: "cart",
  initialState,
  reducers: {
    login: (state, action: PayloadAction<boolean>) => {
        state.isLoggedIn = action.payload;
    }
  },
});

export const {
  login
} = slice.actions;

export const store = configureStore({
  reducer: {
    store: slice.reducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
