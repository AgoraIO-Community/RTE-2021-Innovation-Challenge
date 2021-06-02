const path = require("path");
const ejs = require("ejs");

function formatAttribute(attribute) {
  const output = ["@", attribute.name];
  if (!attribute.arguments || !attribute.arguments.length) {
    return output.join("");
  }
  output.push("(");
  output.push(attribute.arguments.map(formatArgument).join(", "));
  output.push(")");
  return output.join("");
}

function formatArgument(argument) {
  const output = [];
  if (argument.name && argument.name !== "_") {
    output.push(argument.name, ":");
  }
  switch (argument.type) {
    case "Function":
      output.push(argument.functionName, "()");
      break;
    case "String":
      output.push(`"${argument.value}"`);
      break;
    case "Enum":
    case "Int":
      output.push(argument.value);
      break;
    case "List":
      output.push("[", argument.values.map(formatArgument), "]");
      break;
    default:
      break;
  }

  return output.join("");
}

module.exports = async function (meta) {
  return await ejs.renderFile(
    path.resolve(__dirname, "./prisma2-schema-template.ejs"),
    {
      meta: {
        ...meta,
        types: meta.types.map((type) => ({
          ...type,
          is: type.is === "type" ? "model" : type.is,
          fields: type.fields.map((field) => {
            return {
              ...field,
              attributes: (field.attributes || [])
                .filter((attribute) => !["invisible"].includes(attribute.name))
                .map(formatAttribute),
            };
          }),
        })),
      },
    }
  );
};
