import { helper } from '@ember/component/helper';

export default helper(function displayDate([timestamp]) {
  const date = new Date(timestamp);
  return `${date.toLocaleDateString()} At ${date.toLocaleTimeString()}`;
});
