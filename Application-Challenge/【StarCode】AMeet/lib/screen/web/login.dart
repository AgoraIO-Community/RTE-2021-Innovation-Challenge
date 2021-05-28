import 'package:ameet/config/routes/app_pages.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class Login extends StatelessWidget {
  const Login({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Color(0xff10277C),
      body: Container(
        child: Stack(
          children: [
            Align(
              alignment: Alignment.center,
              child: Column(
                children: [
                  SizedBox(height: 117,),
                  Text(
                    "A",
                    style: TextStyle(fontSize: 30, color: Colors.white),
                  ),
                  SizedBox(height: 35,),
                  Card(
                    color: Colors.white, // 背景色
                    shadowColor: Colors.white, // 阴影颜色
                    elevation: 1, // 阴影高度
                    borderOnForeground: false, // 是否在 child 前绘制 border，默认为 true
                    margin: EdgeInsets.fromLTRB(0, 0, 0, 0), // 外边距
                    // 边框
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(40),
                      side: BorderSide(
                        color: Colors.white,
                        width: 1,
                      ),
                    ),
                    child: Container(
                      width: 418,
                      height: 485,
                      child: Column(
                        children: [
                          SizedBox(height: 41,),
                          Text(
                            "登录",
                            style: TextStyle(fontSize: 14, color: Colors.black),
                          ),
                          SizedBox(height: 50,),
                          Row(
                            crossAxisAlignment: CrossAxisAlignment.center,
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Text(
                                "没有账号？",
                                style: TextStyle(fontSize: 14, color: Colors.black),
                              ),
                              Text(
                                "点击注册",
                                style: TextStyle(fontSize: 14, color: Colors.blue),
                              ),
                            ],
                          ),
                          SizedBox(height: 40,),
                          Padding(
                            padding: const EdgeInsets.all(20.0),
                            child: TextField(
                              autofocus: false,
                              cursorColor: Colors.white,
                              decoration: InputDecoration(
                                contentPadding: const EdgeInsets.only(top: 0.0),
                                border: InputBorder.none,
                                hintText: "请输入账号",
                                hintStyle: TextStyle(
                                    fontSize: 17, color: Color.fromARGB(255, 192, 191, 191)),
                              ),
                              style: TextStyle(fontSize: 17),
                            ),
                          ),
                          SizedBox(height: 10,),
                          Padding(
                            padding: const EdgeInsets.all(20.0),
                            child: TextField(
                              autofocus: false,
                              cursorColor: Colors.white,
                              decoration: InputDecoration(
                                contentPadding: const EdgeInsets.only(top: 0.0),
                                border: InputBorder.none,
                                hintText: "请输入密码",
                                hintStyle: TextStyle(
                                    fontSize: 17, color: Color.fromARGB(255, 192, 191, 191)),
                              ),
                              style: TextStyle(fontSize: 17),
                            ),
                          ),
                          SizedBox(height: 30,),
                          Padding(
                            padding: const EdgeInsets.all(20.0),
                            child: Container(
                              width: MediaQuery.of(context).size.width,
                              height: 35,
                              child: RaisedButton(
                                color: Color(0xff1890FF),
                                child: Text("登录",style: TextStyle(fontSize: 14, color: Colors.white)),
                                onPressed: () {
                                  Get.toNamed(Routes.DASHBOARD);
                                },
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
