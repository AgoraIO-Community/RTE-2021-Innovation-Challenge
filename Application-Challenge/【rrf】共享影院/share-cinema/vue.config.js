/*
 * vue config
 * */
const fs = require("fs-extra");
module.exports = {
  publicPath: "/",
  devServer: {
    port: 8020,
    overlay: {
      warnings: false,
      errors: true,
    },
    openPage: "#/?path=main",
    proxy: {
      "/socket.io": {
        target: "http://localhost:5000/",
        changeOrigin: true,
        ws: true,
      },
      "sockjs-node": {
        target: "http://localhost:5000",
        ws: false,
        changeOrigin: true,
      },
      "/server": {
        target: "http://localhost:5000/",
        changeOrigin: true,
        pathRewrite: {
          "^/server": "",
        },
      },
    },
  },
  configureWebpack: (config) => {
    if (process.env.NODE_ENV === "development") {
      config.devtool = "eval-source-map";
    }
  },
};
