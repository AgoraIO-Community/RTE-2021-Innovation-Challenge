import React, { useState, forwardRef } from "react";
import { Box } from "@chakra-ui/react";
import RGL, { WidthProvider, Layout } from "react-grid-layout";
import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import {
  UserTable,
  UserKanban,
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

const originalLayout = getFromLS("layout") || [
  {
    w: 3,
    h: 1,
    x: 0,
    y: 10,
    i: "CreateOneUserModal",
    moved: false,
    static: false,
  },
  { w: 12, h: 5, x: 0, y: 11, i: "UserTable", moved: false, static: false },
  { w: 12, h: 8, x: 12, y: 14, i: "UserKanban", moved: false, static: false },
  {
    w: 3,
    h: 1,
    x: 3,
    y: 10,
    i: "DeleteOneUserModal",
    moved: false,
    static: false,
  },
  {
    w: 3,
    h: 1,
    x: 0,
    y: 5,
    i: "CreateOneClassModal",
    moved: false,
    static: false,
  },
  { w: 12, h: 4, x: 0, y: 6, i: "ClassTable", moved: false, static: false },
  { w: 24, h: 4, x: 0, y: 1, i: "LessonTable", moved: false, static: false },
  {
    w: 4,
    h: 1,
    x: 0,
    y: 0,
    i: "CreateOneLessonModal",
    moved: false,
    static: false,
  },
  {
    w: 12,
    h: 9,
    x: 12,
    y: 5,
    i: "LessonCalendar",
    moved: false,
    static: false,
  },
];

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
      p="3"
      backgroundColor="white"
      overflow="auto"
      {...rest}
    >
      {children}
    </Box>
  );
});

i18n.use(initReactI18next).init({
  resources: {
    en: {
      translation: {
        "data.email": "??????",
        "data.name": "??????",
        "data.role": "??????",
        "where.id": "ID",
        "data.students.connect": "??????",
        "data.teacher.connect.id": "??????",
        "data.class.connect.id": "????????????",
        "data.duration": "???????????????",
        "data.startedAt": "????????????",
        LessonTable: {
          id: "ID",
          name: "????????????",
          "class.name": "????????????",
          "class.teacher.name": "??????",
          "class.students.name": "??????",
          startedAt: "????????????",
          duration: "??????",
          thumbnails: "??????",
        },
        ClassTable: {
          id: "ID",
          name: "????????????",
          "teacher.name": "??????",
          "students.name": "??????",
        },
        UserTable: {
          id: "ID",
          name: "??????",
          role: "??????",
          email: "??????",
          createdAt: "??????????????????",
        },
      },
    },
  },
  lng: "en",
  fallbackLng: "en",
  debug: true,
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
        <LayoutItem key="UserTable">
          <UserTable />
        </LayoutItem>
        <LayoutItem key="UserKanban">
          <UserKanban />
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
