const sg = require("@sendgrid/mail");
const express = require("express");
const bodyParser = require("body-parser");

sg.setApiKey(process.env.SG_API_KEY);

const app = express();
app.use(bodyParser.json());

app.post("/send", async (req, res) => {
  try {
    await sg.send(req.body);
    res.send(req.body);
  } catch (error) {
    res.status(400).send(error);
  }
});

const port = process.env.PORT || 8080;
app.listen(port, () => {
  console.log("token server ready at", port);
});
