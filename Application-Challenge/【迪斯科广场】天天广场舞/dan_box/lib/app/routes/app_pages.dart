import 'package:get/get.dart';

import 'package:dan_box/app/modules/home/bindings/home_binding.dart';
import 'package:dan_box/app/modules/home/views/home_view.dart';
import 'package:dan_box/app/modules/room/bindings/room_binding.dart';
import 'package:dan_box/app/modules/room/views/room_view.dart';
import 'package:dan_box/app/modules/square/bindings/square_binding.dart';
import 'package:dan_box/app/modules/square/views/square_view.dart';
import 'package:dan_box/app/modules/video/bindings/video_binding.dart';
import 'package:dan_box/app/modules/video/views/video_view.dart';

part 'app_routes.dart';

class AppPages {
  AppPages._();

  static const INITIAL = Routes.HOME;

  static final routes = [
    GetPage(
      name: _Paths.HOME,
      page: () => HomeView(),
      binding: HomeBinding(),
    ),
    GetPage(
      name: _Paths.SQUARE,
      page: () => SquareView(),
      binding: SquareBinding(),
    ),
    GetPage(
      name: _Paths.ROOM,
      page: () => RoomView(),
      binding: RoomBinding(),
    ),
    GetPage(
      name: _Paths.VIDEO,
      page: () => VideoView(),
      binding: VideoBinding(),
    ),
  ];
}
