const path = require("path");
const ejs = require("ejs");
const prettier = require("prettier");
const { capitalize, capitalizeFirst } = require("../common");

const viewImportMap = {
  table: ["Table", "Thead", "Tbody", "Tr", "Td", "Th"],
  list: ["List", "ListItem", "Flex"],
  kanban: ["Flex", "VStack", "HStack", "Box", "Heading", "Text"],
  form: ["FormErrorMessage", "FormLabel", "FormControl", "Input", "Button"],
};
const SCALARS = new Set(["Int", "Float", "String", "Boolean", "DateTime"]);

module.exports = async function (dataMeta, documentMeta) {
  const views = {
    table: [],
    list: [],
    kanban: [],
    form: [],
  };
  const componentImportSet = new Set([
    "Spinner",
    "Center",
    "Text",
    "Popover",
    "PopoverTrigger",
    "PopoverContent",
    "PopoverCloseButton",
    "PopoverHeader",
    "PopoverBody",
  ]);
  const dataImportSet = new Set(["Scalars"]);

  function formatVariableType(variable) {
    if (SCALARS.has(variable.type)) {
      return `Scalars["${variable.type}"]`;
    }
    const output = ["{"];
    for (const field of variable.fields) {
      output.push(
        `${field.name}${field.nullable ? "?" : ""}: ${formatFieldType(field)}${
          field.list ? "[]" : ""
        }`
      );
    }
    output.push("}");
    return output.join("\r\n");
  }

  function formatFieldType(field) {
    if (SCALARS.has(field.baseType)) {
      return `Scalars["${field.baseType}"]`;
    }
    if (SCALARS.has(field.customType)) {
      return `Scalars["${field.customType}"]`;
    }
    if (field.baseType === "Enum") {
      dataImportSet.add(field.customType);
      return field.customType;
    }
    const output = ["{"];
    for (const nestField of field.fields) {
      output.push(
        `${nestField.name}${nestField.nullable ? "?" : ""}: ${formatFieldType(
          nestField
        )}${nestField.list ? "[]" : ""}`
      );
    }
    output.push("}");
    return output.join("\r\n");
  }

  for (const type of documentMeta.types) {
    if (type.selections.length !== 1) {
      throw new Error(
        `Found ${type.selections.length} top-level selection in ${type.name}.`
      );
    }
    const selection = type.selections[0];
    const viewAttr = selection.attributes.find((attr) => attr.name === "view");
    if (!viewAttr) {
      throw new Error(`Please set view.`);
    }
    const viewType = viewAttr.arguments.find(
      (arg) => arg.name === "type"
    ).value;
    if (!viewImportMap[viewType]) {
      throw new Error(`Unknown view type "${viewType}".`);
    }
    viewImportMap[viewType].forEach((component) =>
      componentImportSet.add(component)
    );

    const componentName = capitalize(type.name);
    const dataHook =
      type.is === "query"
        ? `use${componentName}Query`
        : `use${componentName}Mutation`;
    dataImportSet.add(dataHook);

    const groupBySelection = selection.selections.find((sel) =>
      sel.attributes.some((attr) => attr.name === "groupBy")
    );
    let groupByEnum = [];
    if (groupBySelection) {
      if (groupBySelection.baseType !== "Enum") {
        throw new Error("Currently we only support group by Enum.");
      }
      dataImportSet.add(groupBySelection.customType);
      groupByEnum = dataMeta.types
        .find(
          (type) =>
            type.name === groupBySelection.customType && type.is === "enum"
        )
        .fields.find((field) => field.name === "members").enum;
    }

    const variables = type.variables.map((variable) => {
      const flattenedFields = [];
      const labelAttr = variable.attributes.find(
        (attr) => attr.name === "label"
      );
      const labelMap = labelAttr
        ? labelAttr.arguments.reduce((prev, cur) => {
            prev[cur.name] = cur.value;
            return prev;
          }, {})
        : {};
      const placeholderAttr = variable.attributes.find(
        (attr) => attr.name === "placeholder"
      );
      const placeholderMap = placeholderAttr
        ? placeholderAttr.arguments.reduce((prev, cur) => {
            prev[cur.name] = cur.value;
            return prev;
          }, {})
        : {};

      const walk = (fields, path) => {
        for (const nestField of fields) {
          const nestPath = `${path}.${nestField.name}${
            nestField.list ? "[0]" : ""
          }`;
          const safePath = nestPath
            .split(".")
            .join("?.")
            .replace(/\[0\]/g, "?.[0]");
          if (nestField.fields) {
            walk(nestField.fields, nestPath);
          } else {
            flattenedFields.push({
              ...nestField,
              path: nestPath,
              safePath,
              label: labelMap[nestField.name] || nestField.name,
              placeholder: placeholderMap[nestField.name],
            });
          }
        }
      };
      walk(variable.fields, variable.name);

      return {
        ...variable,
        rawType: formatVariableType(variable),
        flattenedFields,
      };
    });

    views[viewType].push({
      componentName,
      selectionName: selection.name,
      dataHook,
      selections: selection.selections,
      idName: selection.selections.find((sel) =>
        sel.attributes.some((attr) => attr.name === "id")
      ).name,
      groupByName: groupBySelection ? groupBySelection.name : undefined,
      groupByType: groupBySelection ? groupBySelection.customType : undefined,
      groupByEnum,
      variables,
    });
  }

  return prettier.format(
    await ejs.renderFile(
      path.resolve(__dirname, "./ui-react-ts-template.ejs"),
      {
        componentImports: Array.from(componentImportSet),
        dataImports: Array.from(dataImportSet),
        views,
        common: {
          capitalizeFirst,
        },
      }
    ),
    {
      parser: "typescript",
    }
  );
};
