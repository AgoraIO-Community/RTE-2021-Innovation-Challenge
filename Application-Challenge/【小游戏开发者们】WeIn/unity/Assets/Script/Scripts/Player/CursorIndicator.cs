using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CursorIndicator : MonoBehaviour
{
    public HitPointReader hitPointReader;

    private MeshRenderer m_renderer;

    // Start is called before the first frame update
    void Start()
    {
        m_renderer = GetComponent<MeshRenderer>();
    }

    // Update is called once per frame
    void Update()
    {
        /*if (ToolManager.Instance.Tmode == ToolManager.ToolMode.FaceStretch)
        {
            m_renderer.enabled = false;
            if (hitPointReader.hitting)
            {
                m_renderer.enabled = true;
                float s = WorldDataManager.Instance.ActiveWorld.worldSize;
                transform.localScale = new Vector3(s, s, s);
                transform.position = hitPointReader.hitPoint.position
                    - hitPointReader.hitPoint.normal / 2 * WorldDataManager.Instance.ActiveWorld.worldSize
                    + new Vector3(0.05f, 0.05f, 0.05f) * WorldDataManager.Instance.ActiveWorld.worldSize; //Mesh offset
            }
        }
        else
        {
            // TODO：有待优化
            m_renderer.enabled = true;
            float s = WorldDataManager.Instance.ActiveWorld.worldSize;
            transform.localScale = new Vector3(s, s, s);
            
            transform.position = hitPointReader.hitPoint.position
                - hitPointReader.hitPoint.normal / 2 * WorldDataManager.Instance.ActiveWorld.worldSize
                + new Vector3(0.05f, 0.05f, 0.05f) * WorldDataManager.Instance.ActiveWorld.worldSize; //Mesh offset
        }*/
        
    }
}
