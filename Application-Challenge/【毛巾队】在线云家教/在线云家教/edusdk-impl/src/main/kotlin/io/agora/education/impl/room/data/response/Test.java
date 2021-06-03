package io.agora.education.impl.room.data.response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.agora.education.impl.ResponseBody;

public class Test {
    private static String json = "{\"msg\":\"Success\",\"code\":0,\"ts\":1600942240276,\"data\":{\"room\":{\"roomInfo\":{\"roomName\":\"123544\",\"roomUuid\":\"1235440\"},\"roomState\":{\"state\":0,\"muteChat\":{},\"muteVideo\":{},\"muteAudio\":{}},\"roomProperties\":{\"board\":{\"info\":{\"boardId\":\"03d84230fe4e11ea8a6a89c586a4dabc\",\"boardToken\":\"WHITEcGFydG5lcl9pZD0xTnd5aDBsMW9ZazhaRWNuZG1kaWgwcmJjVWVsQnE1UkpPMVMmc2lnPTIxNWJmYjdjMDczYTkwYjUwN2U2N2U4MzgyYWU4Y2EyOWRiMmNkODk6YWs9MU53eWgwbDFvWWs4WkVjbmRtZGloMHJiY1VlbEJxNVJKTzFTJmNyZWF0ZV90aW1lPTE2MDA5NDIxNjE2NDAmZXhwaXJlX3RpbWU9MTYzMjQ3ODE2MTY0MCZub25jZT0xNjAwOTQyMTYxNjQwMDAmcm9sZT1yb29tJnJvb21JZD0wM2Q4NDIzMGZlNGUxMWVhOGE2YTg5YzU4NmE0ZGFiYyZ0ZWFtSWQ9NjQ2\"},\"state\":{\"follow\":0,\"grantUsers\":[]}}}},\"user\":{\"userName\":\"123\",\"userUuid\":\"1232\",\"role\":\"broadcaster\",\"muteChat\":0,\"userProperties\":{},\"updateTime\":1600942240110,\"streamUuid\":\"3323113882\",\"userToken\":\"eyJhbGciOiJIUzI1NiJ9.eyJwcmltYXJ5U3RyZWFtVXVpZCI6IjMzMjMxMTM4ODIiLCJhcHBJZCI6ImY0ODg0OTNkMTg4NjQzNWY5NjNkZmIzZDk1OTg0ZmQ0IiwidXNlclV1aWQiOiIxMjMyIiwicm9vbVV1aWQiOiIxMjM1NDQwIiwidXNlcklkIjoiNWY2YzcwNWQxMmEwZmMxMDk2OGQ2MTVjIiwicm9vbUlkIjoiNWY2YzcwNGUxMmEwZmMxMDk2OGQ2MTU3IiwiaWF0IjoxNjAwOTQyMjQwfQ.yIOrWgdHyTy5-PPky7tmgOTAOQQSJO7JHWCEwVDx5kw\",\"rtmToken\":\"006f488493d1886435f963dfb3d95984fd4IAAvh3J/Jp1wprgxiomA6fLyRgW57sP2ml2zebre4KSEZpZFgHIAAAAAIgCPEYIDIMJtXwQAAQD/////AgD/////AwD/////BAD/////\",\"rtcToken\":\"006f488493d1886435f963dfb3d95984fd4IAAHKJ6PXu4+hc7u7+Ow9uNlv5wkfoYtuiIHuf3abNG+ieyTL0W3DVhtIgDLI0cAIMJtXwQAAQD/////AgD/////AwD/////BAD/////\",\"state\":2,\"streams\":[{\"streamUuid\":\"3323113882\",\"videoSourceType\":1,\"audioSourceType\":1,\"videoState\":1,\"audioState\":1,\"updateTime\":1600942240125,\"rtcToken\":\"006f488493d1886435f963dfb3d95984fd4IAAHKJ6PXu4+hc7u7+Ow9uNlv5wkfoYtuiIHuf3abNG+ieyTL0W3DVhtIgDLI0cAIMJtXwQAAQD/////AgD/////AwD/////BAD/////\",\"state\":1}]},\"sysConfig\":{\"sequenceTimeout\":300}}}";

    public static void main(String[] argv) {
        ResponseBody<EduEntryRes> res = new Gson().fromJson(json,
                new TypeToken<ResponseBody<EduEntryRes>>(){}.getType());
        System.out.println("qqq");
    }
}
