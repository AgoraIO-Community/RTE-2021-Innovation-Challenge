const { visit, visitWithTypeInfo } = require("graphql");

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

function directiveNodeToAttribute(directiveNode) {
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

  return attribute;
}

function fieldNodeToSelection(fieldNode, typeInfo, typeMap) {
  const astNode = typeInfo.getFieldDef().astNode;
  const typename = getTypename(astNode);
  const selection = {
    name: fieldNode.name.value,
    attributes: [],
    selections: [],
    arguments: [],
    nullable: isNullableNode(astNode),
    list: isListNode(astNode),
    baseType: mapBaseType(typename, typeMap),
    customType: isBaseType(typename) ? undefined : typename,
  };

  for (const directiveNode of fieldNode.directives) {
    selection.attributes.push(directiveNodeToAttribute(directiveNode));
  }

  for (const argumentNode of fieldNode.arguments) {
    selection.arguments.push({
      name: argumentNode.name.value,
      type: argumentNode.value.kind,
      value: argumentNode.value.name.value,
    });
  }

  if (fieldNode.selectionSet) {
    visit(
      fieldNode.selectionSet,
      visitWithTypeInfo(typeInfo, {
        Field(_fieldNode) {
          selection.selections.push(
            fieldNodeToSelection(_fieldNode, typeInfo, typeMap)
          );
          return false;
        },
      })
    );
  }

  return selection;
}

function variableDefNameToFields(
  variableDefName,
  astNode,
  typeMap,
  walkedObjects
) {
  const fields = [];

  const variableDef = astNode.definitions.find(
    (def) =>
      def.kind === "InputObjectTypeDefinition" &&
      def.name.value === variableDefName
  );
  if (!variableDef) {
    throw new Error(`Variable "${variableDefName}" not defined.`);
  }
  for (const fieldNode of variableDef.fields) {
    const typename = getTypename(fieldNode);
    const baseType = mapBaseType(typename, typeMap);
    if (walkedObjects.has(typename)) {
      continue;
    } else if (
      baseType === "InputObject" &&
      !typename.includes("FieldUpdateOperationsInput")
    ) {
      walkedObjects.add(typename);
    }
    fields.push({
      name: fieldNode.name.value,
      baseType,
      customType: isBaseType(typename) ? undefined : typename,
      fields:
        baseType === "InputObject"
          ? variableDefNameToFields(typename, astNode, typeMap, walkedObjects)
          : undefined,
      nullable: isNullableNode(fieldNode),
      list: isListNode(fieldNode),
    });
  }

  return fields;
}

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

function isNullableNode(node) {
  return node.type.kind !== "NonNullType";
}

function isListNode(node) {
  if (node.type.kind === "ListType") {
    return true;
  }
  if (node.type.kind === "NonNullType") {
    return node.type.type.kind === "ListType";
  }
  return false;
}

module.exports = {
  getTypeMap,
  directiveNodeToAttribute,
  fieldNodeToSelection,
  getTypename,
  isNullableNode,
  isListNode,
  isBaseType,
  mapBaseType,
  variableDefNameToFields,
};
