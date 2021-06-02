import React, { useState, forwardRef } from "react";
import { Box } from "@chakra-ui/react";
import RGL, { WidthProvider, Layout } from "react-grid-layout";
import {
  UserTable,
  CreateOneUserModal,
  CreateOneClassModal,
  DeleteOneUserModal,
  LessonTable,
  ClassTable,
  CreateOneLessonModal,
  LessonCalendar,
} from "./generated/ui-components";
import "./App.css";

const GridLayout = WidthProvider(RGL);

function getFromLS(key: string) {
  try {
    return (JSON.parse(localStorage.getItem("x-runtime")!) || {})[key];
  } catch (e) {
    /*Ignore*/
  }
}

function saveToLS(key: string, value: unknown) {
  localStorage.setItem(
    "x-runtime",
    JSON.stringify({
      [key]: value,
    })
  );
}

const originalLayout = getFromLS("layout") || [];

const LayoutItem = forwardRef<
  HTMLDivElement,
  { key: string; children?: React.ReactChild }
>(({ key, children, ...rest }, ref) => {
  return (
    <Box
      key={key}
      ref={ref}
      rounded="md"
      shadow="md"
      p="2"
      backgroundColor="white"
      overflow="auto"
      {...rest}
    >
      {children}
    </Box>
  );
});

function App() {
  const [layout, setLayout] = useState<Layout[]>(
    JSON.parse(JSON.stringify(originalLayout))
  );

  return (
    <Box width="full" p="12">
      <GridLayout
        className="layout"
        layout={layout}
        onLayoutChange={(value) => {
          saveToLS("layout", value);
          setLayout(value);
        }}
        cols={24}
        rowHeight={56}
        style={{ backgroundColor: "white" }}
      >
        <LayoutItem key="CreateOneUserModal">
          <CreateOneUserModal />
        </LayoutItem>
        <LayoutItem key="UserModal">
          <UserTable />
        </LayoutItem>
        <LayoutItem key="DeleteOneUserModal">
          <DeleteOneUserModal />
        </LayoutItem>
        <LayoutItem key="CreateOneClassModal">
          <CreateOneClassModal />
        </LayoutItem>
        <LayoutItem key="ClassTable">
          <ClassTable />
        </LayoutItem>
        <LayoutItem key="LessonTable">
          <LessonTable />
        </LayoutItem>
        <LayoutItem key="CreateOneLessonModal">
          <CreateOneLessonModal />
        </LayoutItem>
        <LayoutItem key="LessonCalendar">
          <LessonCalendar />
        </LayoutItem>
      </GridLayout>
    </Box>
  );
}

export default App;
