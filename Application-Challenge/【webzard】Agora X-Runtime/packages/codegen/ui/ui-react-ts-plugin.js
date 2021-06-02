const path = require("path");
const ejs = require("ejs");
const prettier = require("prettier");
const _ = require("lodash");
const { capitalize, capitalizeFirst } = require("../common");

const viewImportMap = {
  table: ["Table", "Thead", "Tbody", "Tr", "Td", "Th"],
  list: ["List", "ListItem", "Flex", "Divider"],
  kanban: ["Flex", "VStack", "HStack", "Box", "Heading", "Text"],
  form: [
    "FormErrorMessage",
    "FormLabel",
    "FormControl",
    "Button",
    "ModalFooter",
    "useToast",
    "IconButton",
    "Flex",
  ],
  button: ["Button"],
  modal: [
    "Modal",
    "ModalOverlay",
    "ModalContent",
    "ModalHeader",
    "ModalBody",
    "ModalCloseButton",
    "useDisclosure",
  ],
  calendar: [],
};
const SCALARS = new Set(["Int", "Float", "String", "Boolean", "DateTime"]);

function formatMappings(mappings) {
  const mappingObject = {};
  const output = [];

  for (const mapping of mappings) {
    const [src, dest] = mapping.split("->").map((frag) => frag.trim());
    _.set(mappingObject, src, dest);
  }

  const walk = (obj) => {
    output.push("{");
    for (const key in obj) {
      output.push(`${key}:`);
      if (typeof obj[key] === "object") {
        output.push(walk(obj[key]));
      } else {
        output.push(obj[key].replace("$", "selected?"));
      }
      output.push(",");
    }
    output.push("}");
  };
  walk(mappingObject);

  return output.join(" ");
}

function getRenderType(item) {
  if (item.customType === "DateTime") {
    return item.customType;
  }
  return item.baseType;
}

module.exports = async function (dataMeta, documentMeta) {
  const views = {
    table: [],
    list: [],
    kanban: [],
    form: [],
    button: [],
    modal: [],
    calendar: [],
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
  const dataImportSet = new Set([
    "Scalars",
    ...dataMeta.types
      .filter((type) => type.is === "enum")
      .map((enumType) => enumType.name),
  ]);

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
    const isClientOnly = type.attributes.some(
      (attr) => attr.name === "clientOnly"
    );
    const selection = type.selections[0];
    const viewAttr = selection.attributes.find((attr) => attr.name === "view");
    let viewType;
    if (viewAttr) {
      viewType = viewAttr.arguments.find((arg) => arg.name === "type").value;
    }
    if (selection.name === "staticComponent") {
      viewType = selection.selections[0].name;
    }
    if (!viewType) {
      throw new Error(`Please set view.`);
    }
    if (!viewImportMap[viewType]) {
      throw new Error(`Unknown view type "${viewType}".`);
    }
    viewImportMap[viewType].forEach((component) =>
      componentImportSet.add(component)
    );

    const modalAttr = selection.attributes.find(
      (attr) => attr.name === "modal"
    );
    if (modalAttr) {
      viewImportMap.modal.forEach((component) =>
        componentImportSet.add(component)
      );
    }

    const componentName = capitalize(type.name);
    const dataHookType =
      type.is === "query"
        ? `${componentName}Query`
        : `${componentName}Mutation`;
    const dataHook = `use${dataHookType}`;
    if (!isClientOnly) {
      dataImportSet.add(dataHook);
      dataImportSet.add(dataHookType);
    }

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
          const nestPath = `${path}.${nestField.name}`;
          const safePath = nestPath.split(".").join("?.");
          if (nestField.fields && !nestField.list) {
            walk(nestField.fields, nestPath);
          } else {
            flattenedFields.push({
              ...nestField,
              path: nestPath,
              safePath,
              label: labelMap[nestPath] || nestPath,
              placeholder: placeholderMap[nestPath],
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

    const selectionWithIdAttr = selection.selections.find((sel) =>
      sel.attributes.some((attr) => attr.name === "id")
    );
    views[viewType].push({
      componentName,
      selectionName: selection.name,
      dataHook,
      dataHookType,
      selections: selection.selections.map((sel) => {
        const output = [];
        let renderType;
        let rootType = selection.customType || selection.baseType;

        const collect = (_sel) => {
          output.push(_sel.name);
          renderType = getRenderType(_sel);
          if (_sel.selections.length === 0) {
            // nothing to do
          } else if (_sel.selections.length === 1) {
            collect(_sel.selections[0]);
            rootType = _sel.customType || _sel.baseType;
          } else {
            const firstNonIdSel = _sel.selections.find((s) => s.name !== "id");
            collect(firstNonIdSel);
            rootType = _sel.customType || _sel.baseType;
          }
        };
        collect(sel);

        return {
          ...sel,
          path: output.join("."),
          renderType,
          rootType,
        };
      }),
      attributes: selection.attributes,
      idName: selectionWithIdAttr ? selectionWithIdAttr.name : undefined,
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
        enumOptionsRawMap: dataMeta.types
          .filter((type) => type.is === "enum")
          .reduce((prev, cur) => {
            prev[cur.name] = `[${cur.fields
              .find((f) => f.name === "members")
              .enum.map((item) => {
                const value = ` ${cur.name}.${capitalizeFirst(item)}`;
                return `{ text: ${value}, value: ${value} }`;
              })
              .join(", ")}]`;
            return prev;
          }, {}),
        common: {
          capitalizeFirst,
          capitalize,
          formatMappings,
          formatRawProps(variables) {
            const propsVariable = variables.find((v) => v.name === "props");
            if (!propsVariable) {
              return "{}";
            }
            const output = ["{"];
            for (const field of propsVariable.fields) {
              if (
                propsVariable.defaultValue &&
                propsVariable.defaultValue[field.name]
              ) {
                output.push(`${field.name}:`);
                const defaultValue = propsVariable.defaultValue[field.name];
                let formattedValue = defaultValue;
                if (field.baseType === "String") {
                  formattedValue = `"${defaultValue}"`;
                } else if (field.baseType === "Enum") {
                  formattedValue = `${field.customType}.${capitalizeFirst(
                    defaultValue
                  )}`;
                }
                output.push(`${formattedValue},`);
              }
            }
            output.push("}");
            return output.join(" ");
          },
          dotToDash(str) {
            return str.replace(/\./g, "_");
          },
          getKeyAndPath(selections, attributeName) {
            const find = (sel, path) => {
              const newPath = `${path}${path ? "?." : ""}${sel.name}`;
              if (sel.attributes.some((attr) => attr.name === attributeName)) {
                return {
                  name: sel.name,
                  path: newPath,
                };
              } else if (sel.selections.length) {
                for (const subSel of sel.selections) {
                  const result = find(subSel, newPath);
                  if (result) {
                    return result;
                  }
                }
              } else {
                return null;
              }
            };

            for (const sel of selections) {
              const result = find(sel, "");
              if (result) {
                return result;
              }
            }
          },
          getRenderType,
        },
      }
    ),
    {
      parser: "typescript",
    }
  );
};
