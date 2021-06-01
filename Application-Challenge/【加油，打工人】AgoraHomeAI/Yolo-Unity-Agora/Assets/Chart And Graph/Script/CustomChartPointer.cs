#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

namespace ChartAndGraph
{
    class CustomChartPointer : MaskableGraphic ,IPointerEnterHandler,IPointerExitHandler,IPointerDownHandler,IPointerUpHandler,IPointerClickHandler
    {
        public Vector2 ScreenPosition;
        public bool IsMouseDown;
        public bool Click;

        protected override void Awake()
        {
            color = new Color(0f, 0f, 0f, 0f);
        }
        public void OnPointerClick(PointerEventData eventData)
        {
            Click = true;
        }

        public void OnPointerDown(PointerEventData eventData)
        {
            IsMouseDown = true;
        }

        public void OnPointerEnter(PointerEventData eventData)
        {
            
        }

        public void OnPointerExit(PointerEventData eventData)
        {
            IsMouseDown = false;
        }

        public void OnPointerUp(PointerEventData eventData)
        {
            IsMouseDown = false;
        }

        public override bool Raycast(Vector2 sp, Camera eventCamera)
        {
            ScreenPosition = sp;
            return true;
        }
        void LateUpdate()
        {
            Click = false;

        }
    }
}
