using UnityEngine;

namespace HI5.VRCalibration
{
	public class VRCalibrationToggle : MonoBehaviour {

		[SerializeField] private KeyCode m_Toggle = KeyCode.Backspace;
		[SerializeField] private MenuStateMachine m_MenuSM;

		void Update () 
		{
			if (Input.GetKeyDown (m_Toggle)) 
			{
				MenuState state = m_MenuSM.State;

				if (state == MenuState.Exit)
                    m_MenuSM.State = MenuState.Main;
				else
                    m_MenuSM.State = MenuState.Exit;
			}
		}
	}
}
