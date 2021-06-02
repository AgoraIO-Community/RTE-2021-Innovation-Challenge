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
    lessons(where: { startedAt: { gte: "${new Date().toISOString()}" } }) {
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

setInterval(async () => {
  const lessonsGraph = graph(`
  query {
    lessons {
      id
      duration
      thumbnails
      startedAt
    }
  }
  `);
  try {
    const { lessons } = await lessonsGraph();
    for (const lesson of lessons) {
      if (lesson.thumbnails.length) {
        continue;
      }
      if (new Date(lesson.startedAt).getTime() > Date.now()) {
        continue;
      }
      if (
        events.some(
          (e) => e.type === "ADD_THUMBNAIL" && e.data.id === lesson.id
        )
      ) {
        continue;
      }
      const event = {
        type: "ADD_THUMBNAIL",
        data: lesson,
      };
      events.push(event);
      try {
        const { data } = await axios({
          headers: {
            Host: "lesson-thumbnail.x-runtime.example.com",
            "Content-Type": "application/json",
          },
          method: "post",
          url: `${ENDPOINT}/thumbnail/${lesson.id}`,
        });
        const updateLessonGraph = graph(`
        mutation {
          updateOneLesson(where: { id: ${
            lesson.id
          } }, data: { thumbnails: { set: ${JSON.stringify(data.imgs)} } }) {
            id
          }
        }
        `);
        const result = await updateLessonGraph().catch((error) => {
          console.log("?", error);
        });
        console.log(result);
      } catch (error) {
        console.log(error.response);
      }
    }
  } catch (error) {
    console.error(error);
  }
}, 5000);
