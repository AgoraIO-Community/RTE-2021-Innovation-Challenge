
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class AppCenter extends StatelessWidget {
  const AppCenter({Key key}) : super(key: key);

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
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  "我的应用",
                  style: TextStyle(fontSize: 20, color: Colors.black),
                ),
                Container(
                  width: Get.width,
                  height: 475,
                  child: Stack(
                    children: [
                      Column(
                        children: [
                          SizedBox(height: 100,),
                          Row(
                            children: [
                              Text(
                                "名称",
                                style: TextStyle(fontSize: 14, color: Colors.grey),
                              ),
                              SizedBox(width: 50,),
                              SizedBox(
                                width: 300,
                                child: TextField(
                                  autofocus: false,
                                  cursorColor: Colors.white,
                                  decoration: InputDecoration(
                                    contentPadding: const EdgeInsets.only(top: 0.0),
                                    border: InputBorder.none,
                                    hintText: "plese enter name",
                                    hintStyle: TextStyle(
                                        fontSize: 14, color: Color.fromARGB(255, 192, 191, 191)),
                                  ),
                                  style: TextStyle(fontSize: 14),
                                ),
                              ),
                            ],
                          ),
                          SizedBox(height: 30,),
                          Row(
                            children: [
                              Text(
                                "ID",
                                style: TextStyle(fontSize: 14, color: Colors.grey),
                              ),
                              SizedBox(width: 50,),
                              Text(
                                "345673",
                                style: TextStyle(fontSize: 14, color: Colors.black),
                              ),
                              SizedBox(width: 50,),
                              Text(
                                "复制",
                                style: TextStyle(fontSize: 14, color: Colors.blue),
                              ),
                            ],
                          ),
                          SizedBox(height: 50,),
                          Row(
                            children: [
                              Text(
                                "密钥",
                                style: TextStyle(fontSize: 14, color: Colors.grey),
                              ),
                              SizedBox(width: 50,),
                              Text(
                                "hgffj7676wgd34",
                                style: TextStyle(fontSize: 14, color: Colors.black),
                              ),
                              SizedBox(width: 50,),
                              Text(
                                "复制",
                                style: TextStyle(fontSize: 14, color: Colors.blue),
                              ),
                            ],
                          ),
                          SizedBox(height: 50,),
                          Row(
                            children: [
                              Text(
                                "默认配置",
                                style: TextStyle(fontSize: 14, color: Colors.grey),
                              ),
                              SizedBox(width: 50,),
                              Checkbox(value: false, onChanged: (v){

                              }),
                              SizedBox(width: 50,),
                              Text(
                                "视频SDK",
                                style: TextStyle(fontSize: 14, color: Colors.blue),
                              ),
                            ],
                          ),
                        ],
                      ),
                      Positioned(
                        bottom: 0,
                        right: 20,
                        child: Container(
                          height: 68,
                          width: 200,
                          child:Row(
                            children: [
                              RaisedButton(
                                color: Color(0xffffffff),
                                child: Text("取消",style: TextStyle(fontSize: 14, color: Colors.black)),
                                onPressed: () {

                                },
                              ),
                              SizedBox(width: 30,),
                              RaisedButton(
                                color: Color(0xff1890FF),
                                child: Text("确定",style: TextStyle(fontSize: 14, color: Colors.white)),
                                onPressed: () {

                                },
                              ),
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                )
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