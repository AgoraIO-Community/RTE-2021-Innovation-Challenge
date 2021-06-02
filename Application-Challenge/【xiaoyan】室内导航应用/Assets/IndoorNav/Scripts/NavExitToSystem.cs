using UnityEngine;

namespace indoorNav
{
    public class NavExitToSystem : MonoBehaviour
    {
        public void ExitGame()
        {
            Debug.Log("Exit system");
            Application.Quit();
        }
    }
}