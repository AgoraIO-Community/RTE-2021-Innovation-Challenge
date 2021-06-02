import { defineConfig } from "vite";
import reactRefresh from "@vitejs/plugin-react-refresh";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [reactRefresh()],
  server: {
    proxy: {
      "/graphql": {
        target: "http://192.168.31.227:31874",
        headers: {
          Host: "x-runtime-data.x-runtime.example.com",
        },
      },
      "/token": {
        target: "http://192.168.31.227:31874",
        headers: {
          Host: "agora-rtm-token.x-runtime.example.com",
        },
      },
    },
  },
});
