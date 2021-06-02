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
          recordUrl: "https://cn.bing.com/recordUrl",
        }
      );
    };
    init();
  }, []);

  return <Box className="agora-classroom"></Box>;
};
