import 'package:get/get.dart';

import '../controllers/square_controller.dart';

class SquareBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut<SquareController>(
      () => SquareController(),
    );
  }
}
