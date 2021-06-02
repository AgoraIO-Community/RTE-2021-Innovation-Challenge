const path = require("path");
const ejs = require("ejs");
const { pluralize, lowerCase } = require("../common");

module.exports = async function (meta) {
  return await ejs.renderFile(
    path.resolve(__dirname, "./prisma2-nexus-template.ejs"),
    {
      models: meta.types
        .filter((type) => type.is === "type")
        .map((type) => {
          return {
            ...type,
            fields: type.fields.map((field) => {
              return {
                ...field,
                invisible: field.attributes.some(
                  (attr) => attr.name === "invisible"
                ),
              };
            }),
            lowerCaseName: lowerCase(type.name),
            lowerCasePlural: pluralize(lowerCase(type.name)),
          };
        }),
    }
  );
};
