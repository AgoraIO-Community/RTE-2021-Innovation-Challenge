import 'package:ameet/config/routes/app_pages.dart';
import 'package:ameet/widgets/ameet_switch.dart';
import 'package:ameet/widgets/ameet_text_from_field.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:permission_handler/permission_handler.dart';

class Join extends StatelessWidget {
  const Join({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 3,
        actions: [
          IconButton(
            icon: Icon(
              Icons.delete,
              color: Colors.black,
            ),
            onPressed: () {

            },
          ),
        ],
      ),
      body: Container(
        child: Column(
          children: [
            //会议号
            AmeetTextFromField(
              title: "会议号",
              hintText: "meet number",
            ),
            //加入名称
            AmeetTextFromField(
              title: "加入名称",
              hintText: "join name",
            ),
            //加入会议
            Padding(
              padding: const EdgeInsets.all(20.0),
              child: Container(
                width: MediaQuery.of(context).size.width,
                height: 35,
                child: RaisedButton(
                  color: Color(0xff1890FF),
                  child: Text("加入会议"),
                  onPressed: () {
                    onJoin();
                  },
                ),
              ),
            ),
            //麦克风
            AmeetSwitch(
              title: "麦克风",
              switchSelected: false,
            ),
            //扬声器
            AmeetSwitch(
              title: "扬声器",
              switchSelected: false,
            ),
            //摄像头
            AmeetSwitch(
              title: "摄像头",
              switchSelected: false,
            ),
          ],
        ),
      ),
    );
  }

  Future<void> onJoin() async {
      // await for camera and mic permissions before pushing video page
      await _handleCameraAndMic(Permission.camera);
      await _handleCameraAndMic(Permission.microphone);
      // push video page with given channel name
      await Get.toNamed(Routes.MEET);
  }

  Future<void> _handleCameraAndMic(Permission permission) async {
    final status = await permission.request();
    print(status);
  }

}
