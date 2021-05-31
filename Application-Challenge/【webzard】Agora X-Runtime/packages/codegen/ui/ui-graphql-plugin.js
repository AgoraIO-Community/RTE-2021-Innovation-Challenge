const path = require("path");
const ejs = require("ejs");
const prettier = require("prettier");

function formatSelections(selections) {
  const output = ["{"];

  for (const selection of selections) {
    output.push(selection.name);

    if (selection.arguments.length) {
      output.push("(");
      for (const argument of selection.arguments) {
        output.push(
          `${argument.name}: ${argument.type === "Variable" ? "$" : ""}${
            argument.value
          }`
        );
      }
      output.push(")");
    }

    if (selection.selections.length) {
      output.push(formatSelections(selection.selections));
    }
  }

  output.push("}");
  return output.join("\r\n");
}

function formatVariables(variables) {
  if (!variables.length) {
    return "";
  }
  const output = ["("];

  for (const variable of variables) {
    output.push(
      `\$${variable.name}:`,
      `${variable.type}${variable.nullable ? "" : "!"}`
    );
  }

  output.push(")");
  return output.join(" ");
}

module.exports = async function (documentMeta) {
  const types = [];

  for (const type of documentMeta.types) {
    if (type.attributes.some((attr) => attr.name === "clientOnly")) {
      continue;
    }
    types.push({
      name: type.name,
      is: type.is,
      rawSelections: formatSelections(type.selections),
      rawVariables: formatVariables(type.variables),
    });
  }

  return prettier.format(
    await ejs.renderFile(path.resolve(__dirname, "./ui-graphql-template.ejs"), {
      types,
    }),
    {
      parser: "graphql",
    }
  );
};
