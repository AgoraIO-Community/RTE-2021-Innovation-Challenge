import path from "path";
import { ApolloServer } from "apollo-server";
import { nexusPrisma } from "nexus-plugin-prisma";
import { makeSchema } from "nexus";
import { PrismaClient } from "@prisma/client";
import "./generated/nexus";
import * as genTypes from "./generated/types";

const prisma = new PrismaClient();

const schema = makeSchema({
  types: genTypes,
  plugins: [
    nexusPrisma({
      prismaClient: (ctx) => (ctx.prisma = prisma),
      experimentalCRUD: true,
      outputs: {
        typegen: path.resolve(
          __dirname,
          "./generated/nexus-plugin-prisma.d.ts"
        ),
      },
    }),
  ],
  outputs: {
    schema: path.resolve(__dirname, "../schema.graphql"),
    typegen: path.resolve(__dirname, "./generated/nexus.ts"),
  },
});

const server = new ApolloServer({
  schema,
  mocks: process.env.MOCKS
    ? {
        DateTime() {
          return new Date().toISOString();
        },
      }
    : undefined,
});

const port = process.env.PORT || 8080;
server.listen(port).then(({ url }) => {
  console.log(`🚀 Server ready at ${url}`);
});
