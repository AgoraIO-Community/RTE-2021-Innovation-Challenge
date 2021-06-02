import React, { useState } from "react";
import { Tabs, TabList, TabPanels, Tab, TabPanel } from "@chakra-ui/react";
import _Calendar from "@toast-ui/react-calendar";
import { ISchedule } from "tui-calendar";
import "tui-calendar/dist/tui-calendar.css";

const COLORS = ["#FED7D7", "#C6F6D5", "#FEFCBF", "#BEE3F8"];

const views = ["day", "week", "month"];

const Calendar: React.FC<{
  schedules: Array<Omit<ISchedule, "id"> & { id: number }>;
}> = ({ schedules }) => {
  const [view, setView] = useState("week");

  return (
    <Tabs onChange={(index) => setView(views[index])}>
      <TabList>
        {views.map((view) => (
          <Tab key={view}>{view}</Tab>
        ))}
      </TabList>

      <TabPanels>
        <_Calendar
          height="100%"
          disableDblClick={true}
          disableClick={false}
          isReadOnly
          month={{
            startDayOfWeek: 0,
          }}
          template={{
            time: function (schedule) {
              return schedule.title + "\r\n" + `<div>${schedule.body}</div>`;
            },
          }}
          schedules={schedules.map((s) => {
            const { id, ...rest } = s;
            return {
              category: "time",
              bgColor: COLORS[id % COLORS.length],
              id: id.toString(),
              ...rest,
            };
          })}
          scheduleView
          taskView
          view={view}
        />
      </TabPanels>
    </Tabs>
  );
};

export default Calendar;
