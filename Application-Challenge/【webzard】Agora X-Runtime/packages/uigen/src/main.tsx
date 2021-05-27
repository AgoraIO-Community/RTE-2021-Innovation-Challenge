import React from "react";
import ReactDOM from "react-dom";
import { ChakraProvider } from "@chakra-ui/react";
import { createClient, Provider as UrqlProvider } from "urql";
import App from "./App";

const client = createClient({
  url: "/graphql",
});

ReactDOM.render(
  <React.StrictMode>
    <UrqlProvider value={client}>
      <ChakraProvider>
        <App />
      </ChakraProvider>
    </UrqlProvider>
  </React.StrictMode>,
  document.getElementById("root")
);
