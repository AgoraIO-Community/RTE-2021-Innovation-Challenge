using UnityEngine;
using System.Collections;

public class DrawRectangle : MonoBehaviour
{

    public Color rectColor = Color.green;


    public Material rectMat = null;//画线的材质 不设定系统会用当前材质画线 结果不可控

    // Use this for initialization
    void Start()
    {

        rectMat.hideFlags = HideFlags.HideAndDontSave;

        rectMat.shader.hideFlags = HideFlags.HideAndDontSave;

    }


    void Update()
    {
    }



    void OnPostRender()
    {//画线这种操作推荐在OnPostRender（）里进行 而不是直接放在Update，所以需要标志来开启

        Rect rect0 = new Rect(10, 10, 120, 220);
        drawRect(rect0);

    }



    void drawRect(Rect rect0)
    {
        if (!rectMat)
            return;
        GL.PushMatrix();//保存摄像机变换矩阵
        rectMat.SetPass(0);

        GL.LoadPixelMatrix();//设置用屏幕坐标绘图

        // GL.Begin(GL.QUADS);

        // GL.Color( new Color(rectColor.r,rectColor.g,rectColor.b,0.1f) );//设置颜色和透明度，方框内部透明

        // GL.Vertex3( 0,0,0);

        // GL.Vertex3( Screen.width/2,0,0);

        // GL.Vertex3( Screen.width/2,Screen.height/2,0 );

        // GL.Vertex3( 0,Screen.height/2,0 );

        // GL.End();
        float startX = rect0.x;
        float startY = rect0.y;
        float endX = rect0.xMax;
        float endY = rect0.yMax;

        GL.Begin(GL.LINES);

        GL.Color(rectColor);//设置方框的边框颜色 边框不透明

        GL.Vertex3(startX, startY, 0);
        GL.Vertex3(endX, startY, 0);

        GL.Vertex3(endX, startY, 0);
        GL.Vertex3(endX, endY, 0);

        GL.Vertex3(endX, endY, 0);
        GL.Vertex3(startX, endY, 0);

        GL.Vertex3(startX, endY, 0);
        GL.Vertex3(startX, startY, 0);

        GL.End();
        // GL.Begin(GL.LINES);
        // GL.Vertex3(0, 0, 0);
        // GL.Vertex3(Screen.width, Screen.height, 0);
        // GL.End(); 
        GL.PopMatrix();//恢复摄像机投影矩阵
    }


    void drawRects(Rect[] rects)
    {
        if (!rectMat)
            return;
        GL.PushMatrix();//保存摄像机变换矩阵
        rectMat.SetPass(0);

        GL.LoadPixelMatrix();//设置用屏幕坐标绘图

        // GL.Begin(GL.QUADS);

        // GL.Color( new Color(rectColor.r,rectColor.g,rectColor.b,0.1f) );//设置颜色和透明度，方框内部透明

        // GL.Vertex3( 0,0,0);

        // GL.Vertex3( Screen.width/2,0,0);

        // GL.Vertex3( Screen.width/2,Screen.height/2,0 );

        // GL.Vertex3( 0,Screen.height/2,0 );

        // GL.End();
        GL.Begin(GL.LINES);
        for (int i = 0; i < rects.Length; i++)
        {

            Rect rect0 = rects[i];
            //Debug.Log(rect0);
            float startX = rect0.x;
            float startY = rect0.y;
            float endX = rect0.xMax;
            float endY = rect0.yMax;



            GL.Color(rectColor);//设置方框的边框颜色 边框不透明

            GL.Vertex3(startX, startY, 0);
            GL.Vertex3(endX, startY, 0);

            GL.Vertex3(endX, startY, 0);
            GL.Vertex3(endX, endY, 0);

            GL.Vertex3(endX, endY, 0);
            GL.Vertex3(startX, endY, 0);

            GL.Vertex3(startX, endY, 0);
            GL.Vertex3(startX, startY, 0);


        }
        GL.End();
        // GL.Begin(GL.LINES);
        // GL.Vertex3(0, 0, 0);
        // GL.Vertex3(Screen.width, Screen.height, 0);
        // GL.End(); 
        GL.PopMatrix();//恢复摄像机投影矩阵

    }
}