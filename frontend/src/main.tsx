import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./App.tsx";
import { ChakraProvider, extendTheme } from "@chakra-ui/react";
import { Provider } from "react-redux";
import { store } from "./stores/store.ts";
import { MultiSelectTheme } from "chakra-multiselect";
const theme = extendTheme({
  components: {
    MultiSelect: MultiSelectTheme
  }
})
createRoot(document.getElementById("root")!).render(
  
  <StrictMode>
    <Provider store={store}>
      <ChakraProvider theme={theme}>
        <App />
      </ChakraProvider>
    </Provider>
  </StrictMode>
);
