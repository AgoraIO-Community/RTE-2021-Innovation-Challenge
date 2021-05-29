import 'package:ameet/screen/web/dash/appcenter.dart';
import 'package:ameet/screen/web/dash/console.dart';
import 'package:ameet/screen/web/dash/help.dart';
import 'package:ameet/screen/web/dashboard.dart';
import 'package:ameet/screen/join.dart';
import 'package:ameet/screen/web/login.dart';
import 'package:ameet/screen/meet.dart';
import 'package:ameet/screen/web/readme.dart';
import 'package:flutter/foundation.dart';
import 'package:get/get.dart';

part 'app_routes.dart';

class AppPages {
  static const INITIAL = kIsWeb == true ? Routes.README : Routes.JOIN;

  static final routes = (kIsWeb == true
      ? [
          //web
          GetPage(name: Routes.README, page: () => Readme(), children: []),
          GetPage(name: Routes.WEBLOGIN, page: () => Login(), children: []),
          GetPage(name: Routes.DASHBOARD, page: () => Dashboard(), children: []),
          GetPage(name: Routes.CONSOLE, page: () => Console(), children: []),
          GetPage(name: Routes.APPCENTER, page: () => AppCenter(), children: []),
          GetPage(name: Routes.HELP, page: () => Help(), children: []),
        ]
      : [
          //android ios other
          GetPage(name: Routes.JOIN, page: () => Join(), children: []),
          GetPage(name: Routes.MEET, page: () => Meet(), children: []),
        ]);
}
