import React, { useState, forwardRef } from "react";
import { Box, Heading } from "@chakra-ui/react";
import RGL, { WidthProvider, Layout } from "react-grid-layout";
import {
  UserTable,
  CreateOneUserModal,
  CreateOneClassModal,
  DeleteOneUserModal,
  LessonTable,
  ClassTable,
  CreateOneLessonModal,
} from "./generated/ui-components";

import Calendar from "@toast-ui/react-calendar";
import "tui-calendar/dist/tui-calendar.css";

import "./App.css";

const today = new Date();
const getDate = (type: string, start: any, value: number, operator: string) => {
  start = new Date(start);
  type = type.charAt(0).toUpperCase() + type.slice(1);

  if (operator === "+") {
    start[`set${type}`](start[`get${type}`]() + value);
  } else {
    start[`set${type}`](start[`get${type}`]() - value);
  }

  return start;
};

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
      </GridLayout>
      <Box>
        <Heading>Calendar</Heading>
        <Calendar
          height="900px"
          calendars={[
            {
              id: "0",
              name: "Private",
              bgColor: "#9e5fff",
              borderColor: "#9e5fff",
            },
            {
              id: "1",
              name: "Company",
              bgColor: "#00a9ff",
              borderColor: "#00a9ff",
            },
          ]}
          disableDblClick={true}
          disableClick={false}
          isReadOnly={false}
          month={{
            startDayOfWeek: 0,
          }}
          schedules={[
            {
              id: "1",
              calendarId: "0",
              title: "TOAST UI Calendar Study",
              category: "time",
              dueDateClass: "",
              start: today.toISOString(),
              end: getDate("hours", today, 3, "+").toISOString(),
            },
            {
              id: "2",
              calendarId: "0",
              title: "Practice",
              category: "milestone",
              dueDateClass: "",
              start: getDate("date", today, 1, "+").toISOString(),
              end: getDate("date", today, 1, "+").toISOString(),
              isReadOnly: true,
            },
            {
              id: "3",
              calendarId: "0",
              title: "FE Workshop",
              category: "allday",
              dueDateClass: "",
              start: getDate("date", today, 2, "-").toISOString(),
              end: getDate("date", today, 1, "-").toISOString(),
              isReadOnly: true,
            },
            {
              id: "4",
              calendarId: "0",
              title: "Report",
              category: "time",
              dueDateClass: "",
              start: today.toISOString(),
              end: getDate("hours", today, 1, "+").toISOString(),
            },
          ]}
          scheduleView
          taskView
          template={{
            milestone(schedule) {
              return `<span style="color:#fff;background-color: ${schedule.bgColor};">${schedule.title}</span>`;
            },
            milestoneTitle() {
              return "Milestone";
            },
            allday(schedule) {
              return `${schedule.title}<i class="fa fa-refresh"></i>`;
            },
            alldayTitle() {
              return "All Day";
            },
          }}
          useDetailPopup
          useCreationPopup
          view="month"
        />
      </Box>
    </Box>
  );
}

export default App;
