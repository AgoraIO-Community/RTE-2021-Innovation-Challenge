using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;

public class CountDown : MonoBehaviour
{
    public event Action OnCountDwonStart;
    public event Action OnCountDownComplete;

    public float Count { get { return m_Count; } set { m_Count = Count; } }
    private float m_Count = 3f;

    public int Timer
    {
        get
        {
            return timerInt;
        }
    }
    private int timerInt = -1;

    public void StartCD()
    {
        StartCoroutine(DoCountDown());
        if (OnCountDwonStart != null)
            OnCountDwonStart();
    }

    public void StopCD()
    {
        StopCoroutine(DoCountDown());
    }

    IEnumerator DoCountDown()
    {
        float timer = m_Count;

        while (timer > 0)
        {
            timer -= Time.deltaTime;
            timerInt = Mathf.CeilToInt(timer);
            yield return null;
        }

        if (OnCountDownComplete != null)
            OnCountDownComplete();
    }

}