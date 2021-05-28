import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class Console extends StatelessWidget {
  const Console({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {

    return Stack(
      children: [
        Container(
          height: Get.height,
          width: Get.width,
          child: Padding(
            padding: const EdgeInsets.only(
                left: 120, right: 120, top: 50, bottom: 50),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  "整体数据",
                  style: TextStyle(fontSize: 20, color: Colors.black),
                ),
                Row(
                  children: [
                    cardData("总剩余时长", "12345"),
                    cardData("总剩余人数", "20"),
                    cardData("总参与次数", "435"),
                    cardData("总参与时长", "2234"),
                  ],
                ),
                Text(
                  "应用趋势",
                  style: TextStyle(fontSize: 20, color: Colors.black),
                ),
                Container(
                  height: 200,
                  child: Padding(
                    padding: const EdgeInsets.only(top: 20,bottom: 20),
                    child: LineChart(
                        chartData(),
                    ),
                  ),
                ),
                Text(
                  "会议列表",
                  style: TextStyle(fontSize: 20, color: Colors.black),
                ),
                SizedBox(height: 20,),
                Container(
                  height: 300,
                  width: Get.width,
                  child: ListView.builder(
                      itemCount:8,
                      itemBuilder: (index,v){
                      return Row(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Row(
                            children: [
                              Checkbox(value: false, onChanged: (v){}),
                              SizedBox(width: 10,),
                              Text(
                                "123456@163.com",
                                style: TextStyle(fontSize: 14, color: Colors.black),
                              ),
                            ],
                          ),
                          Text(
                            "Mango",
                            style: TextStyle(fontSize: 14, color: Colors.black),
                          ),
                          Text(
                            "API",
                            style: TextStyle(fontSize: 14, color: Colors.black),
                          ),
                          Text(
                            "04-09 20:00",
                            style: TextStyle(fontSize: 14, color: Colors.black),
                          ),
                        ],
                      );
                  }
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
            child: Padding(
              padding: const EdgeInsets.only(left: 120, right: 120),
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

  Widget cardData(title, subTitle) {
    return Card(
      color: Colors.white,
      // 背景色
      shadowColor: Colors.white,
      // 阴影颜色
      elevation: 0,
      // 阴影高度
      borderOnForeground: false,
      // 是否在 child 前绘制 border，默认为 true
      margin: EdgeInsets.fromLTRB(0, 10, 20, 10),
      // 外边距
      // 边框
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(10),
        side: BorderSide(
          color: Colors.black12,
          width: 1,
        ),
      ),
      child: Container(
        width: 246,
        height: 84,
        alignment: Alignment.centerLeft,
        child: Padding(
          padding: const EdgeInsets.only(left: 10),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                "$title",
                style: TextStyle(fontSize: 10, color: Colors.grey),
              ),
              SizedBox(
                height: 10,
              ),
              Text(
                "$subTitle",
                style: TextStyle(fontSize: 12, color: Colors.black),
              ),
            ],
          ),
        ),
      ),
    );
  }

  LineChartData chartData() {
    return LineChartData(
      gridData: FlGridData(
        show: true,
        drawVerticalLine: true,
        getDrawingHorizontalLine: (value) {
          return FlLine(
            color: const Color(0xff37434d),
            strokeWidth: 1,
          );
        },
        getDrawingVerticalLine: (value) {
          return FlLine(
            color: const Color(0xff37434d),
            strokeWidth: 1,
          );
        },
      ),
      titlesData: FlTitlesData(
        show: true,
        bottomTitles: SideTitles(
          showTitles: true,
          reservedSize: 22,
          getTextStyles: (value) =>
          const TextStyle(color: Color(0xff68737d), fontWeight: FontWeight.bold, fontSize: 16),
          getTitles: (value) {
            if(value.toInt()%2==0){
              return value.toInt().toString()+'月';
            }
            return '';
          },
          margin: 8,
        ),
        leftTitles: SideTitles(
          showTitles: true,
          getTextStyles: (value) => const TextStyle(
            color: Color(0xff67727d),
            fontWeight: FontWeight.bold,
            fontSize: 15,
          ),
          getTitles: (value) {
            switch (value.toInt()) {
              case 1:
                return '10k';
              case 3:
                return '30k';
              case 5:
                return '50k';
            }
            return '';
          },
          reservedSize: 28,
          margin: 12,
        ),
      ),
      borderData:
      FlBorderData(show: true, border: Border.all(color: const Color(0xff37434d), width: 1)),
      minX: 0,
      maxX: 11,
      minY: 0,
      maxY: 6,
      lineBarsData:linesBarData1(),
    );
  }

  List<LineChartBarData> linesBarData1() {

    List<Color> gradientColors = [
      const Color(0xff23b6e6),
      const Color(0xff02d39a),
    ];

    final LineChartBarData lineChartBarData1 = LineChartBarData(
      spots: [
        FlSpot(0, 3),
        FlSpot(2.6, 2),
        FlSpot(4.9, 5),
        FlSpot(6.8, 3.1),
        FlSpot(8, 4),
        FlSpot(9.5, 3),
        FlSpot(11, 4),
      ],
      isCurved: true,
      colors: gradientColors,
      barWidth: 5,
      isStrokeCapRound: true,
      dotData: FlDotData(
        show: false,
      ),
      belowBarData: BarAreaData(
        show: true,
        colors: gradientColors.map((color) => color.withOpacity(0.3)).toList(),
      ),
    );
    return [lineChartBarData1];
  }

}
