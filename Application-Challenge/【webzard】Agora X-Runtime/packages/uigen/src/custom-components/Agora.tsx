import React, { useEffect } from "react";
import { Box } from "@chakra-ui/react";

export const AgoraClassroom: React.FC<{
  id: string;
  name: string;
}> = ({ id, name }) => {
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
          roomUuid: "122",
          roleType: parseInt(id),
          roomType: 4,
          roomName: "demo-class",
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
