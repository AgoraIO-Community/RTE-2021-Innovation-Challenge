
import 'package:flutter/material.dart';

class AmeetSwitch extends StatelessWidget{
  final String title;
  final bool switchSelected;
  final ValueChanged<bool> onSubmitted;

  AmeetSwitch({Key key, this.title, this.switchSelected,this.onSubmitted})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(left: 20,right: 20),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text("$title"),
              Spacer(),
              Switch(
                value: switchSelected,
                onChanged: onSubmitted,
              ),
            ],
          ),
          Container(height: 1,width:MediaQuery.of(context).size.width,color: Colors.blueGrey,)
        ],
      ),
    );
  }

}