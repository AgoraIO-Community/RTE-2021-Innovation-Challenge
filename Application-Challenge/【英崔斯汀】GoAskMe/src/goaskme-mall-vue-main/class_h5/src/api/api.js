import http from "../../utils/http";

let resquest = process.env.VUE_APP_API_BASE_URL;
/**
 * 获取课程列表
 */
export function getClassList(params) {
  return http.get(`${resquest}index`, params);
}
/**
 * 获取详情
 */
export function getClass(data) {
  return http.get(`${resquest}classes/${data}`);
}
/**
 * 登陆
 */
export function login(data) {
  return http.post("user/login", data);
}
/**
 * 搜索
 */
export function search(data) {
  return http.get("classes/search", data);
}
/**
 * 获取导航
 */
export function getclassescate() {
  return http.get("classescate");
}
/**
 * 发布课程
 */
export function addClass(data){
  return http.post("classes/add",data)
}
/**
 * 兑换课程
 */
export function exchange(data){
  return http.post("user/exchange",data)
}