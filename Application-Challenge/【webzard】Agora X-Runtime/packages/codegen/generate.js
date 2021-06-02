const path = require("path");
const fs = require("fs");
const dataMeta = require("../meta/json/data.json");
const documentMeta = require("../meta/json/document.json");
const renderPrismaSchema = require("./prisma2/prisma2-schema-plugin");
const renderPrismaNexus = require("./prisma2/prisma2-nexus-plugin");
const renderReactTsUI = require("./ui/ui-react-ts-plugin");
const renderReactRendererTypes = require("./ui/react-renderer-types-plugin");
const renderUIGraphQL = require("./ui/ui-graphql-plugin");

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

  fs.writeFileSync(
    path.resolve(__dirname, "../uigen/src/generated/ui-components.tsx"),
    await renderReactTsUI(dataMeta, documentMeta)
  );

  fs.writeFileSync(
    path.resolve(__dirname, "../uigen/src/generated/renderer-types.ts"),
    await renderReactRendererTypes(dataMeta, documentMeta)
  );

  fs.writeFileSync(
    path.resolve(__dirname, "../uigen/src/generated/document.graphql"),
    await renderUIGraphQL(documentMeta)
  );
})();
