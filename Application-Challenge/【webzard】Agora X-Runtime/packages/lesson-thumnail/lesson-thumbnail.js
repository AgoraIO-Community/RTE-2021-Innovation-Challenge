const express = require("express");
const axios = require("axios");

const app = express();

app.post("/thumbnail/:id", async (req, res) => {
  const uid = "lesson-thumbnail";
  const {
    data: { token },
  } = await axios({
    method: "get",
    url: `${process.env.ENDPOINT}/token/${uid}`,
    headers: {
      Host: "agora-rtm-token.x-runtime.example.com",
      "Content-Type": "application/json",
    },
  });

  const { data } = await axios({
    method: "get",
    url: `https://api.agora.io/edu/apps/${process.env.AGORA_APP_ID}/v2/rooms/${req.params.id}/records`,
    headers: {
      "x-agora-token": token,
      "x-agora-uid": uid,
    },
  });

  const imgs = [];
  for (const item of data.data.list) {
    let step = 0;
    while (item.startTime + step < item.endTime) {
      imgs.push(
        `${item.recordDetails[0].url}?x-oss-process=video/snapshot,t_${step}`
      );
      step += 5000;
    }
  }

  res.send({ imgs });
});

const port = process.env.PORT || 8080;
app.listen(port, () => {
  console.log("token server ready at", port);
});
