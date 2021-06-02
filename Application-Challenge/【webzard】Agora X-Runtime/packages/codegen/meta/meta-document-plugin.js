const { parse, visit, visitWithTypeInfo, TypeInfo } = require("graphql");
const {
  getTypeMap,
  directiveNodeToAttribute,
  fieldNodeToSelection,
  getTypename,
  isNullableNode,
  variableDefNameToFields,
} = require("./common");

module.exports = {
  plugin: (schema, documents, config, info) => {
    const rawSchema = schema.extensions.sources.reduce(
      (prev, cur) => prev + cur.body + "\r\n",
      ""
    );
    const astNode = parse(rawSchema);
    const typeMap = getTypeMap(astNode);

    const types = [];
    const typeInfo = new TypeInfo(schema);

    for (const document of documents) {
      visit(
        parse(document.rawSDL),
        visitWithTypeInfo(typeInfo, {
          OperationDefinition(node) {
            const type = {
              name: node.name.value,
              is: node.operation,
              attributes: [],
              selections: [],
              variables: [],
            };

            for (const directiveNode of node.directives) {
              type.attributes.push(directiveNodeToAttribute(directiveNode));
            }

            for (const variableNode of node.variableDefinitions) {
              const variable = {
                name: variableNode.variable.name.value,
                type: getTypename(variableNode),
                nullable: isNullableNode(variableNode),
                attributes: [],
                fields: [],
                defaultValue: {},
              };

              if (variableNode.defaultValue) {
                for (const field of variableNode.defaultValue.fields) {
                  variable.defaultValue[field.name.value] = field.value.value;
                }
              }

              for (const directiveNode of variableNode.directives) {
                variable.attributes.push(
                  directiveNodeToAttribute(directiveNode)
                );
              }
              let pickList = null;
              const valueAttr = variable.attributes.find(
                (attr) => attr.name === "value"
              );
              const pickArgument =
                valueAttr &&
                valueAttr.arguments.find(
                  (argument) =>
                    argument.name === "pick" && argument.type === "List"
                );
              if (pickArgument) {
                pickList = pickArgument.values.map((item) => item.value);
              }

              const variableDefName = getTypename(variableNode);
              variable.fields = variableDefNameToFields(
                variableDefName,
                astNode,
                typeMap,
                0
              ).filter((field) => {
                return pickList ? pickList.includes(field.name) : true;
              });

              type.variables.push(variable);
            }

            visit(
              node,
              visitWithTypeInfo(typeInfo, {
                Field(fieldNode) {
                  type.selections.push(
                    fieldNodeToSelection(fieldNode, typeInfo, typeMap)
                  );
                  return false;
                },
              })
            );

            types.push(type);
          },
        })
      );
    }

    return JSON.stringify({ types }, null, 2);
  },
};
