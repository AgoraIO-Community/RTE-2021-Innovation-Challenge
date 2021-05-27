const path = require("path");
const fs = require("fs");
const dataMeta = require("../meta/json/data.json");
const renderPrismaSchema = require("./prisma2/prisma2-schema-plugin");
const renderPrismaNexus = require("./prisma2/prisma2-nexus-plugin");

(async function () {
  const schemaOutput = await renderPrismaSchema(dataMeta);
  fs.writeFileSync(
    path.resolve(__dirname, "../data/prisma/schema.prisma"),
    schemaOutput
  );

  const nexusOutput = await renderPrismaNexus(dataMeta);
  fs.writeFileSync(
    path.resolve(__dirname, "../data/src/generated/types.ts"),
    nexusOutput
  );
})();
