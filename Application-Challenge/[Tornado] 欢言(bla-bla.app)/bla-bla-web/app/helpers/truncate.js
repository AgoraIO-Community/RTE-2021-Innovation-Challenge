import { helper } from '@ember/component/helper';

export default helper(function truncate([str, length]) {
  return str?.slice(0, length);
});
