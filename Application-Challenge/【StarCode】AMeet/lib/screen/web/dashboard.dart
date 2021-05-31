import 'package:ameet/screen/web/dash/appcenter.dart';
import 'package:ameet/screen/web/dash/console.dart';
import 'package:ameet/screen/web/dash/help.dart';
import 'package:flutter/material.dart';

class Dashboard extends StatefulWidget {
  const Dashboard({Key key}) : super(key: key);

  @override
  _DashboardState createState() => _DashboardState();
}

class _DashboardState extends State<Dashboard> with
    AutomaticKeepAliveClientMixin<Dashboard>,
    SingleTickerProviderStateMixin<Dashboard> {

  List<Tab> _title = [
    Tab(child:Row(children:[Icon(Icons.dashboard,size: 20,), SizedBox(width: 10,),Text('控制台')])),
    Tab(child:Row(children:[Icon(Icons.directions_transit,size: 20,), SizedBox(width: 10,),Text('我的应用')])),
    Tab(child:Row(children:[ Icon(Icons.help,size: 20,),SizedBox(width: 10,),Text('帮助中心')])),
    ];
  List<Widget> _pages = [Console(),AppCenter(),Help()];
  TabController _tabController;

  @override
  void initState() {
    super.initState();
    if (_tabController == null) {
      _tabController = TabController(length: _pages.length, vsync: this);
    }
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(
        backgroundColor: Color(0xff10277C),
        leading: Center(
          child: Text(
            "A",
            style: TextStyle(fontSize: 30, color: Colors.white),
          ),
        ),
        centerTitle: true,
        title: Container(
          width: 500,
          child: TabBar(
            indicator: const BoxDecoration(),
            tabs: _title,
            controller: _tabController,
          ),
        ),
        actions: [
          Icon(Icons.notification_important,color: Colors.grey,),
          SizedBox(width: 10,),
          ClipOval(
            child: new FadeInImage.assetNetwork(
              placeholder: "assets/images/ic_default_avator.jpeg",//预览图
              fit: BoxFit.contain,
              image:"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=3463668003,3398677327&fm=58",
              width: 30.0,
              height: 30.0,
            ),
          ),
          SizedBox(width: 10,),
        ],
      ),
      body: TabBarView(
        children: _pages,
        controller: _tabController,
      ),
    );
  }

  @override
  bool get wantKeepAlive => true;

  @override
  void dispose() {
    super.dispose();
    _tabController.dispose();
  }

}

