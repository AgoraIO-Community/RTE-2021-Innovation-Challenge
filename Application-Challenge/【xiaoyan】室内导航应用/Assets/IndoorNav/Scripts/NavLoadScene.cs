using UnityEngine;
using UnityEngine.SceneManagement;

namespace indoorNav
{
    public class NavLoadScene: MonoBehaviour
    {
        public void ChangeToScene(string sceneName)
        {
            indoorNav.NavLoadingScreen.LoadScene(sceneName);
        }
    }
}