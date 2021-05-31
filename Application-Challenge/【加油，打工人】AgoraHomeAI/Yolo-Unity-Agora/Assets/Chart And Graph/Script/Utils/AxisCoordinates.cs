#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class AxisCoordinates : MonoBehaviour
{
    public string TextFormat = "{0} : {1}";
    public Text Coordinates;
    public RectTransform Prefab;

    RectTransform mVertical;
    RectTransform mHorizontal;

	// Use this for initialization

	void Start()
    {
	    if(Prefab != null)
        {
            mVertical = GameObject.Instantiate(Prefab);
            mHorizontal = GameObject.Instantiate(Prefab);
            mVertical.gameObject.SetActive(false);
            mHorizontal.gameObject.SetActive(false);
        }
	}


	void Update () {
	
	}
}
