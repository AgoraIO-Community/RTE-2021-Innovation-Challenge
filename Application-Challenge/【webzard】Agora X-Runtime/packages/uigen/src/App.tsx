import React, { useState } from "react";
import { Container, Box, Grid, GridItem, Button } from "@chakra-ui/react";

function App() {
  const [count, setCount] = useState(0);

  return (
    <Container>
      <Button onClick={() => setCount(count + 1)}>{count}</Button>
      <Grid
        templateRows="repeat(2, 1fr)"
        templateColumns="repeat(5, 1fr)"
        gap={4}
      >
        {Array.from({ length: count })
          .fill(null)
          .map((_, idx) => {
            return <GridItem key={idx}>{idx}</GridItem>;
          })}
      </Grid>
    </Container>
  );
}

export default App;
