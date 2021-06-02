const path = require("path");
const ejs = require("ejs");

module.exports = async function (dataMeta, documentMeta) {
  const imports = [];
  const types = [];

  for (const type of dataMeta.types) {
    if (type.is === "type") {
      imports.push(type.name);
      types.push(type);
    }
  }

  return await ejs.renderFile(
    path.resolve(__dirname, "./react-renderer-types-template.ejs"),
    {
      imports,
      types,
    }
  );
};
