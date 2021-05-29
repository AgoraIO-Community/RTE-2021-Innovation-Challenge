const pluralize = require("pluralize");

function capitalize(str) {
  return str[0].toUpperCase() + str.slice(1);
}

function lowerCase(name) {
  return name.substring(0, 1).toLowerCase() + name.substring(1);
}

function capitalizeFirst(str) {
  return str[0].toUpperCase() + str.slice(1).toLowerCase();
}

module.exports = {
  capitalize,
  capitalizeFirst,
  lowerCase,
  pluralize,
};
