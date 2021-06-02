const express = require("express");
const graphql = require("graphql.js");
const axios = require("axios");

const ENDPOINT = process.env.ENDPOINT;
const app = express();

const events = [];

app.get("/events", (req, res) => {
  res.send(events);
});

const port = process.env.PORT || 8080;
app.listen(port, () => {
  console.log("token server ready at", port);
});

const graph = graphql(ENDPOINT, {
  headers: {
    Host: "x-runtime-data.x-runtime.example.com",
  },
  asJSON: true,
});

setInterval(async () => {
  const lessonsGraph = graph(`
  query {
    lessons(where: { startedAt: { gte: "${new Date().toISOString()}", lt: "${new Date(
    Date.now() + 10 * 60 * 1000
  ).toISOString()}" } }) {
      name
      class {
        name
        teacher {
          name
        }
        students {
          email
          name
        }
      }
      startedAt
    }
  }
  `);
  try {
    const { lessons } = await lessonsGraph();
    for (const lesson of lessons) {
      if (
        events.some(
          (e) => e.type === "SEND_LESSON_MAIL" && e.data.id === lesson.id
        )
      ) {
        continue;
      }
      const event = {
        type: "SEND_LESSON_MAIL",
        data: lesson,
      };
      events.push(event);
      for (const student of lesson.class.students) {
        try {
          await axios({
            headers: {
              Host: "send-mail.x-runtime.example.com",
              "Content-Type": "application/json",
            },
            method: "post",
            url: `${ENDPOINT}/send`,
            data: JSON.stringify({
              to: student.email,
              from: "no-reply@x-runtime.io",
              subject: "[x-runtime] 开课提醒",
              html: `Hi ${student.name}, 您参加的课程“${lesson.class.name}-${lesson.name}”即将开始，请准时参加。`,
            }),
          });
        } catch (error) {
          console.log(error.response);
        }
      }
    }
  } catch (error) {
    console.error(error);
  }
}, 30000);
