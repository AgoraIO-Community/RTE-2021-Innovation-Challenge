package io.agora.openlive.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.agora.rtm.RtmMessage;

/**
 * Receives and manages messages from RTM engine.
 */
public class RtmMessagePool {
    private Map<String, List<RtmMessage>> mOfflineMessageMap = new HashMap<>();

    void insertOfflineMessage(RtmMessage rtmMessage, String peerId) {
        boolean contains = mOfflineMessageMap.containsKey(peerId);
     //   List<RtmMessage> list = contains ? mOfflineMessageMap.get(peerId) : new ArrayList<>();
        List<RtmMessage> list=null;
if(contains){
    list=mOfflineMessageMap.get(peerId);
}else{
    list=new ArrayList<>();
}

        if (list != null) {
            list.add(rtmMessage);
        }

        if (!contains) {
            mOfflineMessageMap.put(peerId, list);
        }
    }

    List<RtmMessage> getAllOfflineMessages(String peerId) {
  //      return mOfflineMessageMap.containsKey(peerId) ?
  //              mOfflineMessageMap.get(peerId) : new ArrayList<>();
        if(mOfflineMessageMap.containsKey(peerId) ){
            return  mOfflineMessageMap.get(peerId) ;
        }else{
            return  new ArrayList<>();
        }
    }

    void removeAllOfflineMessages(String peerId) {
        mOfflineMessageMap.remove(peerId);
    }
}
