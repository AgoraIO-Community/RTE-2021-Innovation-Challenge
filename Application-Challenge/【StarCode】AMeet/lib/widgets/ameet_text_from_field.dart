
import 'package:flutter/material.dart';

class AmeetTextFromField extends StatelessWidget{
  final String title;
  final String hintText;
  final VoidCallback onTab;
  final ValueChanged<String> onSubmitted;

  AmeetTextFromField({Key key, this.title, this.hintText, this.onTab,this.onSubmitted})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text("$title"),
            SizedBox(width: 10,),
            SizedBox(
              width: MediaQuery.of(context).size.width-100,
              child: TextField(
                onSubmitted: onSubmitted,
                onTap: onTab,
                autofocus: false,
                cursorColor: Colors.white,
                decoration: InputDecoration(
                    contentPadding: const EdgeInsets.only(top: 0.0),
                    border: InputBorder.none,
                    hintText: hintText,
                    hintStyle: TextStyle(
                        fontSize: 17, color: Color.fromARGB(255, 192, 191, 191)),
                   ),
                style: TextStyle(fontSize: 17),
              ),
            ),
          ],
        ),
        Padding(
          padding: const EdgeInsets.only(left: 20,right: 20),
          child: Container(height: 1,width:MediaQuery.of(context).size.width,color: Colors.blueGrey,),
        )
      ],
    );
  }

}