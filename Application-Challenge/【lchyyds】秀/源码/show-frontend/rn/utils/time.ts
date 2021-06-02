/**
 * 
 * @param time 
 * @returns 2021-05-23 03:30
 */
export const getFormattedTime = (time: number | undefined) => {
  let res = '';
  if (time) {
    const theDate = new Date(time);
    const padTime = (t: number) => ('' + t).padStart(2, '0');
    res = `${theDate.getFullYear()}-${padTime(theDate.getMonth() + 1)}-${padTime(theDate.getDate())} ${padTime(theDate.getHours())}:${padTime(theDate.getMinutes())}`;
  }
  return res;
};