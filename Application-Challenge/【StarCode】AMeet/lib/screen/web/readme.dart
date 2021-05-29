
import 'package:ameet/config/routes/app_pages.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class Readme extends StatelessWidget {
  const Readme({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Color(0xff10277C),
      body: Container(
        child: Stack(
          children: [
            Positioned(
                top: 100,
                left: 100,
                child: Row(
                  children: [
                    Text("A",style: TextStyle(color: Colors.white,fontSize: 20),),
                    Text("meet",style: TextStyle(color: Colors.white,fontSize: 20),),
                  ],
                )
            ),
            Positioned(
                top: 100,
                right: 100,
                child: InkWell(
                    onTap: (){
                      Get.toNamed(Routes.WEBLOGIN);
                    },
                    child: Text("dashboard",style: TextStyle(color: Colors.white,fontSize: 20),))
            ),
            Positioned(
                top: 200,
                left: 100,
                right: 100,
                child: Text("A-meet 是一个为低代码开发人员提供的，快速聚合Agora的常用SDK功能，并能够对其使用情况进行追踪的平台。用户可以根据自己需要配置相关的声网SDK，而且能够很容易地对其进行调用，增加了研发效率；同时也可以为声网提升了用户规模。",style: TextStyle(color: Colors.white,fontSize: 30),)
            ),
          ],
        ),
      ),
    );
  }
}
