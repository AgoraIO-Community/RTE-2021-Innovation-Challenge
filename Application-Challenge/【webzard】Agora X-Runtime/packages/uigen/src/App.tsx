import React, { useState } from "react";
import { Container, Box, Grid, GridItem, Button } from "@chakra-ui/react";
import { useUsersQuery } from "./generated/graphql-components";

const Query = `
query {
  users {
      id
      name
      role
  }
}
`;

function App() {
  const [{ data, fetching, error }] = useUsersQuery();
  const [count, setCount] = useState(0);

  console.log({ data, fetching, error });

  return (
    <Container>
      <Button onClick={() => setCount(count + 1)}>{count}</Button>
      <Grid
        templateRows="repeat(2, 1fr)"
        templateColumns="repeat(5, 1fr)"
        gap={4}
      >
        {data?.users.map((_, idx) => {
          return <GridItem key={idx}>{_.name}</GridItem>;
        })}
      </Grid>
    </Container>
  );
}

export default App;
