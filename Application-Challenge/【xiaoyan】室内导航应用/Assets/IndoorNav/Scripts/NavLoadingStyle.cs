using UnityEngine;

namespace indoorNav
{
    public class NavLoadingStyle: MonoBehaviour
    {
        public void SetStyle(string prefabToLoad)
        {
            indoorNav.NavLoadingScreen.prefabName = prefabToLoad;
        }
    }
}