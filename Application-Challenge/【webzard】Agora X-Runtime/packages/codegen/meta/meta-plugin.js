const { parse, visit } = require("graphql");

function getTypename(node) {
  let nameNode = node;
  while ("type" in nameNode) {
    nameNode = nameNode.type;
  }
  const typename = nameNode.name.value;

  if (!typename) {
    console.log(node);
    throw new Error("failed to get typename");
  }

  return typename;
}

function isNullableField(node) {
  return node.type.kind !== "NonNullType";
}

function isListField(node) {
  if (node.type.kind === "ListType") {
    return true;
  }
  if (node.type.kind === "NonNullType") {
    return node.type.type.kind === "ListType";
  }
  return false;
}

function getTypeMap(node) {
  const typeMap = {
    inputs: new Set(),
    types: new Set(),
    enums: new Set(),
  };

  visit(node, {
    EnumTypeDefinition: (node) => {
      typeMap.enums.add(node.name.value);
    },
    ObjectTypeDefinition: (node) => {
      typeMap.types.add(node.name.value);
    },
    InputObjectTypeDefinition: (node) => {
      typeMap.inputs.add(node.name.value);
    },
  });

  return typeMap;
}

const SCALAR_TYPES = new Set([
  "Int",
  "Float",
  "String",
  "Boolean",
  "Object",
  "Enum",
  "InputObject",
]);
const CUSTOM_TYPE_MAP = {
  DateTime: String,
};

function isBaseType(type) {
  return SCALAR_TYPES.has(type);
}
function mapBaseType(type, typeMap) {
  if (isBaseType(type)) {
    return type;
  }
  if (type in CUSTOM_TYPE_MAP) {
    return CUSTOM_TYPE_MAP[type];
  }
  if (typeMap.types.has(type)) {
    return "Object";
  }
  if (typeMap.enums.has(type)) {
    return "Enum";
  }
  if (typeMap.inputs.has(type)) {
    return "InputObject";
  }
  throw new Error(`Unknown type "${type}"`);
}

const FUNCTION_REG = /(\w+|_)\(\)/;
function parseArgumentValue(argumentValue) {
  switch (argumentValue.kind) {
    case "StringValue": {
      const functionMatchArr = argumentValue.value.match(FUNCTION_REG);
      if (functionMatchArr) {
        return {
          type: "Function",
          functionName: functionMatchArr[1],
          functionArguments: [],
        };
      }
      return {
        type: "String",
        value: argumentValue.value,
      };
    }
    case "ListValue": {
      return {
        type: "List",
        values: argumentValue.values.map((v) => parseArgumentValue(v)),
      };
    }
    case "IntValue": {
      return {
        type: "Int",
        value: argumentValue.value,
      };
    }
    case "EnumValue": {
      return {
        type: "Enum",
        value: argumentValue.value,
      };
    }
    default:
      return {};
  }
}

module.exports = {
  plugin: (schema, documents, config, info) => {
    const rawSchema = schema.extensions.sources.reduce(
      (prev, cur) => prev + cur.body + "\r\n",
      ""
    );
    const astNode = parse(rawSchema);
    const typeMap = getTypeMap(astNode);

    const types = [];

    visit(astNode, {
      EnumTypeDefinition: (node) => {
        types.push({
          name: node.name.value,
          is: "enum",
          fields: [
            {
              name: "members",
              nullable: false,
              baseType: "String",
              enum: node.values.map((v) => v.name.value),
            },
          ],
        });
      },
      ObjectTypeDefinition: (node) => {
        const type = {
          name: node.name.value,
          is: "type",
          fields: [],
        };
        for (const fieldNode of node.fields) {
          const typename = getTypename(fieldNode);
          const field = {
            name: fieldNode.name.value,
            baseType: mapBaseType(typename, typeMap),
            customType: isBaseType(typename) ? undefined : typename,
            nullable: isNullableField(fieldNode),
            list: isListField(fieldNode),
            attributes: [],
          };

          for (const directiveNode of fieldNode.directives) {
            const attribute = {
              name: directiveNode.name.value,
              arguments: [],
            };

            for (const argumentNode of directiveNode.arguments) {
              const argument = {
                name: argumentNode.name.value,
                ...parseArgumentValue(argumentNode.value),
              };
              attribute.arguments.push(argument);
            }

            field.attributes.push(attribute);
          }

          type.fields.push(field);
        }
        types.push(type);
      },
    });

    return JSON.stringify({ types }, null, 2);
  },
};
