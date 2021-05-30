import { defineConfig } from "vite";
import reactRefresh from "@vitejs/plugin-react-refresh";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [reactRefresh()],
  server: {
    proxy: {
      "/graphql": {
        target: "http://localhost:8080",
        headers: {
          Host: "x-runtime-data.x-runtime.example.com",
        },
      },
    },
  },
});
