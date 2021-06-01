package com.qgmodel.qggame.entity;

import android.util.Log;

public class MyCardSystem {
    private static final String TAG = "MyCard";

    private String[] originalCards = new String[]{"说数字", "说为什么", "说英文", "说成语", "说脏话", "说不要", "说好的", "说我没有", "说对不起", "说女朋友",
            "说男朋友", "说谢谢", "说考试", "说年龄", "说钱", "说别动", "说明星名", "说歌曲名", "说我不说", "说怕", "说凭什么",
            "说没有", "说方言", "说好难", "说该谁了", "说轮到你了", "说不会", "说人名", "说再见", "说回家", "称赞别人", "夸自己", "手托腮",
            "回头看", "大叫", "回忆", "向上看", "叫人名", "摸头发", "拒绝别人", "点头", "摸鼻子", "表示同意", "大笑", "回答问题", "提问",
            "打赌", "摇头", "怀疑别人", "摸嘴唇", "使眼色", "摸耳垂", "聊工作", "伸舌头","聊家人","聊猫","聊星座","说可爱","说真的","陷害别人",
            "唱歌", "讨论玩法", "说听不清", "说没事", "安慰别人", "说不怕", "捂嘴","说电影名字","说刚才"};

    private String[] cards = new String[originalCards.length];
    private String[] resultCards;
    //记录下标的数组
    private int[] primes = {2,3,5,7,11,13,17,19};
    private int[] indexes = new int[originalCards.length];
    private int[] mark = new int[originalCards.length];
    private int count = 0;
    private int userNum = 4;
    private int userCard = 10;
    private int distributeCount = 0;
    private int reset = 0;

    public MyCardSystem(int userNum, int userCard){
        this.userNum = userNum;
        this.userCard = userCard;
        reset =(int) Math.floor((double) originalCards.length/userCard);
        Log.d(TAG, "=== reset--> "+reset);
    }

    private void shuffle(){
        count++;
        if (count == 1){
            for(int i = 0; i< indexes.length; i++) {
                indexes[i] = i;
            }
        }


        //对坐标数组用洗牌算法
        for (int i =  originalCards.length-1; i >0; i--) {
            int j = rand(0, i);
            indexes[i] = indexes[i] + indexes[j];
            indexes[j] = indexes[i] - indexes[j];
            indexes[i] = indexes[i] - indexes[j];
        }
        for (int i = 0;i<cards.length;i++){
            cards[i] = originalCards[indexes[i]];
        }

    }

    private int rand(int start,int end){
        int ret = (int)(Math.random()*(end - start)+start);
        return ret;
    }

    public String[] getResultCards() {
        //外部每次获取前重新分配
        distribute();
        return resultCards;
    }


    public void distribute(){
        //每次分配都要先洗牌
        shuffle();
        distributeCount++;
        if (distributeCount % reset ==0){
            clearMark();
        }
        resultCards = new String[userNum*userCard];
        //selected表示已算好的卡牌的数量
        int selected = 0;
        //userSign作为不同玩家的标志，均为质数，用于判断某牌是不是被该玩家抽中过
        int userSign = primes[0];
        int j = 1;
        for (int i = 0;selected < userNum*userCard;i++){
            if (mark[indexes[i % cards.length]]!=0&&mark[indexes[i % cards.length]] % userSign == 0){
                //该牌已被该玩家抽到过
            }else{
                if (mark[indexes[i % cards.length]]==0){
                    //该牌没被任何玩家抽到过
                    mark[indexes[i % cards.length]]+=userSign;
                }else if (mark[indexes[i % cards.length]] % userSign != 0){
                    //该牌没被 当前玩家 抽到过
                    mark[indexes[i % cards.length]]*=userSign;
                }
                resultCards[selected] = cards[i%cards.length];
                selected++;
                if (selected%userCard == 0 && j<userNum){
                    //如果当前玩家已分配好牌，为下一个玩家分配
                    userSign = primes[j++];
                }

            }
//            Log.d(TAG, "=== i--> "+i);

        }
//        for (int i = 0;i<resultCards.length;i++)
//        {
//            Log.d(TAG, "=== resultCards--> "+resultCards[i]);
//        }
//        Log.d(TAG, "=== ************************************");

    }


    private void clearMark(){
        for (int i = 0;i<mark.length;i++){
            mark[i] = 0;
        }
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public int getUserCard() {
        return userCard;
    }

    public void setUserCard(int userCard) {
        this.userCard = userCard;
    }
}
