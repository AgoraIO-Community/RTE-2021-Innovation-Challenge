const { parse, visit } = require("graphql");
const {
  getTypeMap,
  directiveNodeToAttribute,
  getTypename,
  isListNode,
  isNullableNode,
  isBaseType,
  mapBaseType,
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
            nullable: isNullableNode(fieldNode),
            list: isListNode(fieldNode),
            attributes: [],
          };

          for (const directiveNode of fieldNode.directives) {
            field.attributes.push(directiveNodeToAttribute(directiveNode));
          }

          type.fields.push(field);
        }
        types.push(type);
      },
    });

    return JSON.stringify({ types }, null, 2);
  },
};
