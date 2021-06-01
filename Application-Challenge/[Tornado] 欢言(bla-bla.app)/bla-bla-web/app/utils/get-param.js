export default function getParam() {
  const urlParam = new URL(location.href).searchParams;

  return {
    roomId: urlParam.get('invite'),
    username: urlParam.get('user'),
  };
}
