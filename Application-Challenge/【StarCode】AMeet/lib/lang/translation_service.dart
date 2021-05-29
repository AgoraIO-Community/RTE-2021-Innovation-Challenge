import 'package:ameet/lang/en_us.dart';
import 'package:ameet/lang/zh_cn.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class TranslationService extends Translations {
  static Locale get locale => Get.deviceLocale;
  static final fallbackLocale = Locale('zh', 'cn');
  @override
  Map<String, Map<String, String>> get keys => {
    'zh_cn': zh_cn,
    'en_us': en_us,
  };
}