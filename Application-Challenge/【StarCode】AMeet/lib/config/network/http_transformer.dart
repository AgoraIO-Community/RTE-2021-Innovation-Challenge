import 'package:ameet/config/network/http_response.dart';
import 'package:dio/dio.dart';

/// Response 解析
abstract class HttpTransformer {
  HttpResponse parse(Response response);
}




