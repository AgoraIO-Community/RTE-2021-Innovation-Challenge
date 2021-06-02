package com.example.jialichun.client;




import com.example.jialichun.client.model.ChatModel;
import com.example.jialichun.client.model.ItemModel;

import java.util.ArrayList;

/**
 * Created by：Administrator on 2015/12/21 16:43
 */
public class TestData {

    public static ArrayList<ItemModel> getTestAdData() {
        ArrayList<ItemModel> models = new ArrayList<>();
        ChatModel model = new ChatModel();
        model.setContent("你好？我们交个朋友吧！");
        model.setIcon(R.drawable.touxiang2);
        models.add(new ItemModel(ItemModel.CHAT_A, model));
        ChatModel model2 = new ChatModel();
        model2.setContent("我是隔壁小王，你是谁？");
        model2.setIcon(R.drawable.touxiang);
        models.add(new ItemModel(ItemModel.CHAT_B, model2));
        ChatModel model3 = new ChatModel();
        model3.setContent("我是二狗子");
        model3.setIcon(R.drawable.touxiang2);
        models.add(new ItemModel(ItemModel.CHAT_A, model3));
        return models;
    }
    public static ArrayList<ItemModel> getTestMSData() {
        ArrayList<ItemModel> models = new ArrayList<>();
        ChatModel model = new ChatModel();
        model.setContent("你好！\n您有一位新访客申请访问\n姓名：二狗子\n访问时间：2018年3月19日-2018年4月20日\n访客照片：\n同意请回复y，不同意请回复n");
        model.setIcon(R.drawable.qiao);
        models.add(new ItemModel(ItemModel.CHAT_A, model));
        return models;
    }

}
