const log = console.log.bind(console);

const e = function(selector) {
  const element = document.querySelector(selector);
  if (element === null) {
    const s = `Element not found, selector ${selector} is wrong or js is not in the body`;
    log(s);
    return null;
  } else {
    return element;
  }
};

export { log, e };
