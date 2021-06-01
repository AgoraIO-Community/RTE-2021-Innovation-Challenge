<?php
/**
 *  AgoraRTCClient 接口
 */

//获取token
include './lib/AgoraDynamicKey/DynamicKey4.php';

$appID = '8e0fc13fc5ac4f6b87ad8edc21bdffd5';
$appCertificate = 'ed029976e9294fd099ba5f155e2485a6';
$channelName = "demo_channel";
$ts = (string)time();
$randomInt = rand(100000000, 999999999);
$uid = 0;
$expiredTs = 0;

//$recordingKey = generateRecordingKey($appID, $appCertificate, $channelName, $ts, $randomInt, $uid, $expiredTs);
//echo('recordingKey : ' . $recordingKey . '<br /><br />');

$mediaChannelKey = generateMediaChannelKey($appID, $appCertificate, $channelName, $ts, $randomInt, $uid, $expiredTs);
header('content-type:application/json');
echo json_encode([
    "errcode"=>200,
    "info"=>"ok",
    "data"=>[
        "appid"=>$appID,
        "channel"=>$channelName,
        "token"=>$mediaChannelKey
    ]
],256);

?>