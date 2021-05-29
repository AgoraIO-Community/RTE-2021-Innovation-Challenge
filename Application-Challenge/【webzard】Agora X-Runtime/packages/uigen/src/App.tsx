import React from "react";
import { Box, Heading } from "@chakra-ui/react";
import { useForm } from "react-hook-form";
import {
  Role,
  useCreateOneUserFormMutation,
} from "./generated/data-components";
import {
  UserTable,
  UserList,
  UserKanban,
  CreateOneUserForm,
} from "./generated/ui-components";

import Calendar from "@toast-ui/react-calendar";
import "tui-calendar/dist/tui-calendar.css";

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

function App() {
  const [, trigger] = useCreateOneUserFormMutation();
  const {
    handleSubmit,
    register,
    formState: { errors, isSubmitting },
  } = useForm<{
    data: {
      name: string;
    };
    email: string;
    role: Role;
  }>();

  async function onSubmit(values: { name: string; email: string; role: Role }) {
    console.log(values);
  }

  return (
    <Box width="full">
      <Box>
        <Heading>table</Heading>
        <UserTable />
      </Box>
      <Box>
        <Heading>list</Heading>
        <UserList />
      </Box>
      <Box>
        <Heading>kanban</Heading>
        <UserKanban />
      </Box>
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
      <Box>
        <Heading>Form</Heading>
        <CreateOneUserForm />
      </Box>
    </Box>
  );
}

export default App;
