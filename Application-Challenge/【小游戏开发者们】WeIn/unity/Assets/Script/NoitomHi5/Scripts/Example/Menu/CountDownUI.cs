using System.Collections;
using UnityEngine;

[RequireComponent(typeof(SpriteRenderer))]
public class CountDownUI : CountDown
{
    [SerializeField]
    private SpriteRenderer m_SpriteRenderer;
    [SerializeField]
    private Sprite[] m_Sprites;

    private void OnEnable()
    {
        base.OnCountDwonStart += HandleCountDownStart;
        base.OnCountDownComplete += HandleCountDownComplete;

        m_SpriteRenderer.sprite = null;
    }

    private void OnDisable()
    {
        base.OnCountDwonStart -= HandleCountDownStart;
        base.OnCountDownComplete -= HandleCountDownComplete;
    }

    private void HandleCountDownStart()
    {
        StartCoroutine(UpdateSpriteRender());
    }

    private void HandleCountDownComplete()
    {
        StopCoroutine(UpdateSpriteRender());
    }

    IEnumerator UpdateSpriteRender()
    {
        while (true)
        {
            ActiveNumber(Timer);
            yield return null;
        }
    }

    private void ActiveNumber(int index)
    {
        if (index < 1 || index > m_Sprites.Length)
            m_SpriteRenderer.sprite = null;
        else
            m_SpriteRenderer.sprite = m_Sprites[index - 1];
    }
}

