import 'package:ameet/config/network/http_response.dart';
import 'package:dio/dio.dart';
import 'http_transformer.dart';

class DefaultHttpTransformer extends HttpTransformer {
// 假设接口返回类型
//   {
//     "code": 100,
//     "data": {},
//     "message": "success"
// }
  @override
  HttpResponse parse(Response response) {
    // if (response.data["code"] == 100) {
    //   return HttpResponse.success(response.data["data"]);
    // } else {
    // return HttpResponse.failure(errorMsg:response.data["message"],errorCode: response.data["code"]);
    // }
    return HttpResponse.success(response.data["data"]);
  }

  /// 单例对象
  static DefaultHttpTransformer _instance = DefaultHttpTransformer._internal();

  /// 内部构造方法，可避免外部暴露构造函数，进行实例化
  DefaultHttpTransformer._internal();

  /// 工厂构造方法，这里使用命名构造函数方式进行声明
  factory DefaultHttpTransformer.getInstance() => _instance;
}
