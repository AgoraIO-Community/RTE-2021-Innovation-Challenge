import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:styled_widget/styled_widget.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:line_icons/line_icons.dart';

import '../controllers/square_controller.dart';
import 'package:dan_box/app/modules/room/views/room_view.dart';

typedef WH = ScreenUtil;

class SquareView extends GetView<SquareController> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Column(mainAxisAlignment: MainAxisAlignment.start, children: [
      Container(
        // height: WH().setHeight(126),
        width: WH().setWidth(375),
        padding: EdgeInsets.only(
            top: WH().setHeight(72),
            bottom: WH().setHeight(48),
            left: WH().setWidth(24)),
        child: Text('广场圈子')
            .bold()
            .fontSize(WH().setSp(44))
            .height(WH().setHeight(54)),
      ),
      _squareCard()
    ]));
  }

  Widget _squareCard() {
    return Column(
      children: [
        Stack(
          children: [
            Positioned(
              child: Image(image: AssetImage('assets/images/map.png')),
              left: 0,
              top: 0,
            ).constrained(
                width: WH().setWidth(327), height: WH().setHeight(184)),
            Positioned(
              left: WH().setWidth(24),
              top: WH().setHeight(24),
              child: CircleAvatar(
                child: Image(image: AssetImage('assets/images/welcome_01.png')),
              ).constrained(
                  width: WH().setWidth(48), height: WH().setWidth(48)),
            ),
            Positioned(
                child: Text('15个人在跳')
                    .textColor(Colors.white)
                    .padding(all: WH().setWidth(8))
                    .card(color: Colors.black),
                right: WH().setWidth(24),
                top: WH().setHeight(104)),
          ],
        )
            .constrained(width: WH().setWidth(327), height: WH().setHeight(184))
            .padding(
              left: WH().setWidth(24),
              right: WH().setWidth(24),
              top: WH().setWidth(12),
            ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text('创智天地')
                .bold()
                .fontSize(WH().setSp(28))
                .alignment(Alignment.centerLeft),
            Icon(LineIcons.map).padding(all: WH().setWidth(4)).card(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(20),
                  ),
                )
          ],
        ).padding(
          top: WH().setWidth(8),
          left: WH().setWidth(24),
          right: WH().setWidth(24),
        )
      ],
    ).gestures(onTap: (() {
      Get.to(RoomView());
    }));
  }
}
