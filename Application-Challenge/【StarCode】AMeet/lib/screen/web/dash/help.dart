
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class Help extends StatelessWidget {
  const Help({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Container(
          height: Get.height,
          width: Get.width,
          child: Padding(
            padding: const EdgeInsets.only(left: 120,right: 120,top: 50,bottom: 50),
            child: Column(
              children: [
                Text(
                  "开发中",
                  style: TextStyle(fontSize: 50, color: Colors.black),
                ),
              ],
            ),
          ),
        ),
        Positioned(
          bottom: 0,
          child: Container(
            height: 68,
            width: Get.width,
            child:Padding(
              padding: const EdgeInsets.only(left: 120,right: 120),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    "@ 2021 Ameeting",
                    style: TextStyle(fontSize: 14, color: Colors.black),
                  ),
                  Text(
                    "联系我们",
                    style: TextStyle(fontSize: 14, color: Colors.blue),
                  ),
                ],
              ),
            ),
          ),
        ),
      ],
    );
  }
}