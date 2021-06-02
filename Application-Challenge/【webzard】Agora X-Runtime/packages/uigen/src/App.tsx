import React, { useState, forwardRef } from "react";
import { Box } from "@chakra-ui/react";
import RGL, { WidthProvider, Layout } from "react-grid-layout";
import i18n from "i18next";
import { useTranslation, initReactI18next } from "react-i18next";
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

i18n.use(initReactI18next).init({
  resources: {
    en: {
      translation: {
        "data.email": "邮箱",
        "data.name": "姓名",
        "data.role": "角色",
        "where.id": "ID",
        "data.students.connect": "学生",
        "data.teacher.connect.id": "老师",
        "data.class.connect.id": "所属课程",
        "data.duration": "时长（秒）",
        "data.startedAt": "开课时间",
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
