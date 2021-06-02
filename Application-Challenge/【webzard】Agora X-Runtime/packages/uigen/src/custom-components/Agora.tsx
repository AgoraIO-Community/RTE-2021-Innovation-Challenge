import React, { useEffect } from "react";
import { Box } from "@chakra-ui/react";

export type AgoraClassRoomProps = {
  id: string;
  name: string;
  role: number;
  roomUuid: string;
  roomName: string;
};
export const AgoraClassRoom: React.FC<AgoraClassRoomProps> = ({
  id,
  name,
  role,
  roomUuid,
  roomName,
}) => {
  useEffect(() => {
    const init = async () => {
      const { token } = await fetch(`/token/${id}`).then((res) => res.json());

      const AgoraEduSDK = (await import("agora-classroom-sdk")).AgoraEduSDK;
      AgoraEduSDK.config({
        appId: import.meta.env.VITE_AGORA_APP_ID as string,
      });
      AgoraEduSDK.launch(
        document.querySelector(".agora-classroom") as HTMLElement,
        {
          rtmToken: token,
          userUuid: id,
          userName: name,
          roomUuid,
          roleType: role,
          roomType: 4,
          roomName,
          pretest: false,
          language: "en",
          startTime: new Date().getTime(),
          duration: 60 * 30,
          courseWareList: [],
          listener: (evt) => {
            console.log("evt", evt);
          },
          recordUrl:
            "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%97%B6%E9%97%B4&fenlei=256&rsv_pq=c0c5e4ff00231557&rsv_t=8220eotGifjO6gjkAs8SB0oOz8HWfy0PjHnip6J0e7fLLY80MdNaUQ0pP04&rqlang=cn&rsv_enter=1&rsv_dl=tb&rsv_sug3=5&rsv_sug1=1&rsv_sug7=100&rsv_sug2=0&rsv_btype=i&prefixsug=%25E6%2597%25B6%25E9%2597%25B4&rsp=5&inputT=947&rsv_sug4=947",
        }
      );
    };
    init();
  }, []);

  return <Box className="agora-classroom"></Box>;
};
