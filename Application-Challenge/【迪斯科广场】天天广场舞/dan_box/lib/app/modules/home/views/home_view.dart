import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:styled_widget/styled_widget.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:line_icons/line_icons.dart';

import '../controllers/home_controller.dart';
import 'package:dan_box/app/modules/square/views/square_view.dart';

typedef WH = ScreenUtil;

class HomeView extends GetView<HomeController> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        children: [
          Container(
            // height: WH().setHeight(126),
            width: WH().setWidth(375),
            padding: EdgeInsets.only(
                top: WH().setHeight(72),
                bottom: WH().setHeight(48),
                left: WH().setWidth(24)),
            child: Text('天天广场舞')
                .bold()
                .fontSize(WH().setSp(44))
                .height(WH().setHeight(54)),
          ),
          _searchBar(),
          ListView(
            scrollDirection: Axis.horizontal,
            children: [
              _typeBox('广场舞', Colors.orange.shade300, Colors.yellow.shade100),
              _typeBox('宅舞', Colors.red.shade300, Colors.pink.shade100),
              _typeBox('蹦迪', Colors.green.shade300, Colors.blue.shade100),
              _typeBox('街舞', Colors.orange.shade300, Colors.yellow.shade100),
              _typeBox('拉丁舞', Colors.red.shade300, Colors.pink.shade100),
              _typeBox('交际舞', Colors.green.shade300, Colors.blue.shade100),
            ],
          ).constrained(width: WH().setWidth(375), height: WH().setHeight(160)),
          _activity(),
          ListView(
            scrollDirection: Axis.horizontal,
            children: [
              _shopCard('夏日T恤', 'assets/images/Thunder T-Shirt.png',
                  Colors.orange.shade600),
              _shopCard(
                  '犀利眼镜', 'assets/images/Glasses.png', Colors.green.shade600)
            ],
          ).constrained(width: WH().setWidth(375), height: WH().setHeight(160)),
        ],
      ),
    );
  }

  Widget _searchBar() {
    final _widgetStyle = ({required Widget child}) => child
        .padding(
          left: WH().setWidth(16),
          right: WH().setWidth(16),
        )
        .decorated(
            borderRadius: BorderRadius.circular(12),
            color: Colors.grey.shade100,
            border: Border.all(color: Colors.black54, width: 2))
        // .constrained(width: 400)
        .alignment(Alignment.center)
        .elevation(
          25,
          borderRadius: BorderRadius.circular(25),
          shadowColor: Colors.blueGrey.shade50,
        );

    return Container(
      width: WH().setWidth(327),
      height: WH().setHeight(56),
      child: Row(
        children: [
          TextField(
            decoration: InputDecoration(
                border: InputBorder.none,
                focusedBorder: InputBorder.none,
                enabledBorder: InputBorder.none,
                errorBorder: InputBorder.none,
                disabledBorder: InputBorder.none,
                hintText: '寻找广场舞'),
          )
              .width(WH().setWidth(259))
              .height(WH().setHeight(53))
              .padding(right: WH().setWidth(12)),
          Icon(LineIcons.search).constrained(width: WH().setWidth(24)).gestures(
              onTap: () {
            Get.snackbar('搜索', '在这里搜索广场');
          })
        ],
      ).parent(_widgetStyle),
    );
  }

  Widget _typeBox(String title, Color starColor, Color cardColor) {
    return Column(
      children: [
        Icon(
          Icons.star,
          color: starColor,
          size: WH().setWidth(40),
        )
            .padding(all: WH().setWidth(16))
            .card(
              color: cardColor,
              elevation: 10,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(20),
              ),
            )
            .alignment(Alignment.center)
            .gestures(onTap: () {
          Get.to(SquareView());
        }),
        Text(title)
            .fontSize(WH().setSp(24))
            .bold()
            .padding(top: WH().setWidth(4))
      ],
    ).padding(left: WH().setWidth(16), top: WH().setHeight(32));
  }

  Widget _activity() {
    return Stack(
      children: [
        Positioned(
            child: Text('首充即送\n夏日迪斯科套装').bold().fontSize(WH().setSp(20)),
            left: WH().setWidth(24),
            top: WH().setHeight(24)),
        Positioned(
            child: ElevatedButton(
              style: ElevatedButton.styleFrom(primary: Colors.black),
              child: Text('立即充值'),
              onPressed: () {
                Get.snackbar('活动', '夏日充值活动');
              },
            ),
            left: WH().setWidth(24),
            top: WH().setHeight(104)),
        Positioned(
          child: Image(image: AssetImage('assets/images/activity_01.png')),
          right: -10,
          bottom: -15,
        )
      ],
    )
        .card(
          color: Colors.orange[200],
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(20),
          ),
        )
        .constrained(width: WH().setWidth(327), height: WH().setHeight(184))
        .padding(all: WH().setWidth(24));
  }

  Widget _shopCard(String title, String imagePath, Color cardColor) {
    return Column(
      children: [
        Image(image: AssetImage(imagePath))
            .constrained(width: WH().setWidth(64), height: WH().setWidth(64))
            .padding(all: WH().setWidth(16))
            .card(
              color: cardColor,
              elevation: 2,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(20),
              ),
            )
            .alignment(Alignment.center),
        Text(title)
            .fontSize(WH().setSp(12))
            // .bold()
            .padding(top: WH().setWidth(4))
      ],
    ).padding(left: WH().setWidth(16), top: WH().setHeight(4));
  }
}
