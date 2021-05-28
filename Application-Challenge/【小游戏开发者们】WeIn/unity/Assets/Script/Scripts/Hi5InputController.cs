using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Hi5_Interaction_Interface;

namespace Hi5_Interaction_Core
{

    public class Hi5InputController : MonoBehaviour
    {

        private Hi5_Object_JudgeMent hi5_object_judgeMent_1;
        private Hi5_Object_JudgeMent hi5_object_judgeMent_2;
        public Hi5_Glove_Interaction_Hand HI5_Left_Human_Collider;
        public Hi5_Glove_Interaction_Hand HI5_Right_Human_Collider;
        private Hi5_Glove_Interaction_State hi5_Glove_Interaction_State_1;
        private Hi5_Glove_Interaction_State hi5_Glove_Interaction_State_2;
        public Hi5Button World_Menu_Button;
        public Hi5Button Switch_Mode_Button;
        public Hi5ButtonRect ButtonCreate;
        public Hi5ButtonRect ButtonSwitch;
        public Hi5ButtonRect ButtonSave;
        public Hi5ButtonRect ButtonLoad;
        public Hi5ButtonRect ButtonAnd;
        public Hi5ButtonRect ButtonOr;
        public Hi5ButtonRect ButtonNot;
        public WorldOptions worldOptions;
        public MergeOptions mergeOptions;
        public HelpOptions helpOptions;
        public GameObject userGuide;
        public GameObject objectMode;
        public GameObject voxelMode;
        public GameObject faceMode;
        public GameObject systemHandle;
        public ObjectManipulator objectManipulator;
        public int nowAcitonIndex = 0;
        public int lastAcitonIndex = 0;
        public int selectObjectInput_stateMonitor = 0;
        public int createObjectInput_stateMonitor = 0;
        public int deleteObjectInput_stateMonitor = 0;
        public int copyObjectInput_stateMonitor = 0;
        public int moveObjectInput_stateMonitor = 0;
        public int combineObjectInput_stateMonitor = 0;
        public int createVoxelInput_stateMonitor = 0;
        public int deleteVoxelInput_stateMonitor = 0;
        public int selectVoxelInput_stateMonitor = 0;
        public int switchModeInput_stateMonitor = 0;
        public int worldMenuInput_stateMonitor = 0;
        public int teleportInput_stateMonitor = 0;
        public int selectFaceInput_stateMonitor = 0;
        public int pullFaceInput_stateMonitor = 0;
        public int laserObjectInput_stateMonitor = 0;
        public int helpImageInput_stateMonitor = 0;
        public int deleteFaceInput_stateMonitor = 0;
        public int upUp_stateMonitor = 0;
        public int downDown_stateMonitor = 0;
        public int changeSize_stateMonitor = 0;



        public int selectObjectInput()
        {
            int state = 0;
            if (hi5_object_judgeMent_1.IsHandIndexPoint() && hi5_object_judgeMent_2.IsHandIndexPoint())
            {
                nowAcitonIndex = 1;
                if (nowAcitonIndex != lastAcitonIndex && selectObjectInput_stateMonitor == 0)
                {
                    selectObjectInput_stateMonitor = 1;
                    state = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && selectObjectInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && selectObjectInput_stateMonitor == 1)
            {
                selectObjectInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int createObjectInput()
        {
            int state = 0;
            if (hi5_object_judgeMent_1.IsFingerPlane() && hi5_object_judgeMent_2.IsFingerPlane())
            {
                nowAcitonIndex = 2;
                if (nowAcitonIndex != lastAcitonIndex && createObjectInput_stateMonitor == 0)
                {
                    createObjectInput_stateMonitor = 1;
                    state = 1;
                    lastAcitonIndex = nowAcitonIndex;


                }
                else if (nowAcitonIndex == lastAcitonIndex && createObjectInput_stateMonitor == 1)
                {
                    state = 2;


                }
            }
            if (state == 0 && createObjectInput_stateMonitor == 1)
            {
                createObjectInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;

            }
            return state;
        }

        public int deleteObjectInput()
        {
            int state = 0;
            if (hi5_object_judgeMent_1.IsOK() && hi5_object_judgeMent_2.IsOK())
            {
                nowAcitonIndex = 3;
                if (nowAcitonIndex != lastAcitonIndex && deleteObjectInput_stateMonitor == 0)
                {
                    state = 1;
                    deleteObjectInput_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && deleteObjectInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && deleteObjectInput_stateMonitor == 1)
            {
                deleteObjectInput_stateMonitor = 0;
                state = 3;
                lastAcitonIndex = 0;
            }
            return state;
        }


        public int copyObjectInput()
        {
            int state = 0;
                if (hi5_object_judgeMent_1.IsHandFist() && hi5_object_judgeMent_2.IsHandFist())
            {
                nowAcitonIndex = 4;
                if (nowAcitonIndex != lastAcitonIndex && copyObjectInput_stateMonitor == 0)
                {
                    state = 1;
                    copyObjectInput_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && copyObjectInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && copyObjectInput_stateMonitor == 1)
            {
                copyObjectInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int moveObjectInput()
        {
            int state = 0;
                if (hi5_object_judgeMent_1.IsHandFist() && hi5_object_judgeMent_2.IsHandIndexPoint())
            {
                nowAcitonIndex = 5;
                if (nowAcitonIndex != lastAcitonIndex && moveObjectInput_stateMonitor == 0)
                {
                    state = 1;
                    moveObjectInput_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && moveObjectInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && moveObjectInput_stateMonitor == 1)
            {
                moveObjectInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int combineObjectInput()
        {
            int state = 0;
                if (hi5_object_judgeMent_1.IsFingerPlane() && hi5_object_judgeMent_2.IsHandFist())
            {
                nowAcitonIndex = 6;
                if (nowAcitonIndex != lastAcitonIndex && combineObjectInput_stateMonitor == 0)
                {
                    state = 1;
                    combineObjectInput_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && combineObjectInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && combineObjectInput_stateMonitor == 1)
            {
                combineObjectInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int createVoxelInput()
        {
            int state = 0;
                if (hi5_object_judgeMent_1.IsFingerPlane() && hi5_object_judgeMent_2.IsHandIndexPoint())
            {
                nowAcitonIndex = 7;
                if (nowAcitonIndex != lastAcitonIndex && createVoxelInput_stateMonitor == 0)
                {
                    state = 1;
                    createVoxelInput_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && createVoxelInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && createVoxelInput_stateMonitor == 1)
            {
                createVoxelInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int deleteVoxelInput()
        {
            int state = 0;
                if (hi5_object_judgeMent_1.IsOK() && hi5_object_judgeMent_2.IsHandIndexPoint())
            {
                nowAcitonIndex = 8;
                if (nowAcitonIndex != lastAcitonIndex && deleteVoxelInput_stateMonitor == 0)
                {
                    state = 1;
                    deleteVoxelInput_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && deleteVoxelInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && deleteVoxelInput_stateMonitor == 1)
            {
                deleteVoxelInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int selectVoxelInput()
        {
            int state = 0;
                if (hi5_object_judgeMent_1.IsHandFist() && hi5_object_judgeMent_2.IsHandIndexPoint())
            {
                nowAcitonIndex = 9;
                if (nowAcitonIndex != lastAcitonIndex && selectVoxelInput_stateMonitor == 0)
                {
                    state = 1;
                    selectVoxelInput_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && selectVoxelInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && selectVoxelInput_stateMonitor == 1)
            {
                selectVoxelInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int selectFaceInput()
        {
            int state = 0;
                if (hi5_object_judgeMent_1.IsFingerPlane() && hi5_object_judgeMent_2.IsHandIndexPoint())
            {
                nowAcitonIndex = 14;
                if (nowAcitonIndex != lastAcitonIndex && selectFaceInput_stateMonitor == 0)
                {
                    state = 1;
                    selectFaceInput_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && selectFaceInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && selectFaceInput_stateMonitor == 1)
            {
                selectFaceInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int pullFaceInput()
        {
            int state = 0;
            if (hi5_object_judgeMent_1.IsFingerPlane() && hi5_object_judgeMent_2.IsFingerPlane())
            {
                nowAcitonIndex = 13;
                if (nowAcitonIndex != lastAcitonIndex && pullFaceInput_stateMonitor == 0)
                {
                    state = 1;
                    pullFaceInput_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && pullFaceInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && pullFaceInput_stateMonitor == 1)
            {
                pullFaceInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int switchModeInput()
        {
            int state = 0;

            if (Switch_Mode_Button.IsTouch())
                 {
                nowAcitonIndex = 10;
                if (nowAcitonIndex != lastAcitonIndex && switchModeInput_stateMonitor == 0)
                {
                    state = 1;
                    switchModeInput_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && switchModeInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && switchModeInput_stateMonitor == 1)
            {
                switchModeInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int worldMenuInput()
        {
            int state = 0;
                if (World_Menu_Button.IsTouch())
            {
                nowAcitonIndex = 11;
                if (nowAcitonIndex != lastAcitonIndex && worldMenuInput_stateMonitor == 0)
                {
                    state = 1;
                    worldMenuInput_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && worldMenuInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && worldMenuInput_stateMonitor == 1)
            {
                worldMenuInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int teleportInput()
        {
            int state = 0;

                if (hi5_object_judgeMent_1.IsFly())
            {
                nowAcitonIndex = 12;
                if (nowAcitonIndex != lastAcitonIndex && teleportInput_stateMonitor == 0)
                {
                    state = 1;
                    teleportInput_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && teleportInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && teleportInput_stateMonitor == 1)
            {
                teleportInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int laserObjectInput()
        {
            int state = 0;
            if (hi5_object_judgeMent_1.IsOK() && hi5_object_judgeMent_2.IsHandIndexPoint())
            {
                nowAcitonIndex = 15;
                if (nowAcitonIndex != lastAcitonIndex && laserObjectInput_stateMonitor == 0)
                {
                    state = 1;
                    laserObjectInput_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && laserObjectInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && laserObjectInput_stateMonitor == 1)
            {
                laserObjectInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int helpImageInput()
        {
            int state = 0;
            if (hi5_object_judgeMent_2.IsFly())
            {
                nowAcitonIndex = 16;
                if (nowAcitonIndex != lastAcitonIndex && helpImageInput_stateMonitor == 0)
                {
                    state = 1;
                    helpImageInput_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && helpImageInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && helpImageInput_stateMonitor == 1)
            {
                helpImageInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int deleteFaceInput()
        {
            int state = 0;
            if (hi5_object_judgeMent_1.IsOK() && hi5_object_judgeMent_2.IsOK())
            {
                nowAcitonIndex = 17;
                if (nowAcitonIndex != lastAcitonIndex && deleteFaceInput_stateMonitor == 0)
                {
                    state = 1;
                    deleteFaceInput_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && deleteFaceInput_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && deleteFaceInput_stateMonitor == 1)
            {
                deleteFaceInput_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int upUp()
        {
            int state = 0;
            if (hi5_object_judgeMent_1.IsThree() && hi5_object_judgeMent_2.IsThree())
            {
                nowAcitonIndex = 18;
                if (nowAcitonIndex != lastAcitonIndex && upUp_stateMonitor == 0)
                {
                    state = 1;
                    upUp_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && upUp_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && upUp_stateMonitor == 1)
            {
                upUp_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int downDown()
        {
            int state = 0;
            if (hi5_object_judgeMent_1.IsTwo() && hi5_object_judgeMent_2.IsTwo())
            {
                nowAcitonIndex = 19;
                if (nowAcitonIndex != lastAcitonIndex && downDown_stateMonitor == 0)
                {
                    state = 1;
                    downDown_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && downDown_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && downDown_stateMonitor == 1)
            {
                downDown_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int changeSize()
        {
            int state = 0;
            if (hi5_object_judgeMent_1.IsHandFist() && hi5_object_judgeMent_2.IsOK())
            {
                nowAcitonIndex = 20;
                if (nowAcitonIndex != lastAcitonIndex && changeSize_stateMonitor == 0)
                {
                    state = 1;
                    changeSize_stateMonitor = 1;
                    lastAcitonIndex = nowAcitonIndex;
                }
                else if (nowAcitonIndex == lastAcitonIndex && changeSize_stateMonitor == 1)
                {
                    state = 2;
                }
            }
            else if (state == 0 && changeSize_stateMonitor == 1)
            {
                changeSize_stateMonitor = 0;
                lastAcitonIndex = 0;
                state = 3;
            }
            return state;
        }

        public int LeftHandFist()
        {
            int state = 0;
            if(hi5_object_judgeMent_1.IsHandFist())
            {
                state = 1;
            }
            return state;
        }

        private void Awake()
        {
        }

        private void Start()
        {
            hi5_object_judgeMent_1 = new Hi5_Object_JudgeMent();
            hi5_object_judgeMent_2 = new Hi5_Object_JudgeMent();
            HI5_Left_Human_Collider = GameObject.Find("HI5_Left_Human_Collider").GetComponent<Hi5_Glove_Interaction_Hand>();
            HI5_Right_Human_Collider = GameObject.Find("HI5_Right_Human_Collider").GetComponent<Hi5_Glove_Interaction_Hand>();
            hi5_Glove_Interaction_State_1 = null;
            hi5_Glove_Interaction_State_2 = null;
            hi5_object_judgeMent_1.mStateManager = hi5_Glove_Interaction_State_1;
            hi5_object_judgeMent_2.mStateManager = hi5_Glove_Interaction_State_2;
            hi5_object_judgeMent_1.Hand = HI5_Left_Human_Collider;
            hi5_object_judgeMent_2.Hand = HI5_Right_Human_Collider;
            World_Menu_Button = GameObject.Find("World_Menu_Button").GetComponent<Hi5Button>();
            Switch_Mode_Button = GameObject.Find("Switch_Mode_Button").GetComponent<Hi5Button>();
            Switch_Mode_Button = GameObject.Find("Switch_Mode_Button").GetComponent<Hi5Button>();
            ButtonCreate = GameObject.Find("ButtonCreate").GetComponent<Hi5ButtonRect>();
            ButtonSwitch = GameObject.Find("ButtonSwitch").GetComponent<Hi5ButtonRect>();
            ButtonSave = GameObject.Find("ButtonSave").GetComponent<Hi5ButtonRect>();
            ButtonLoad = GameObject.Find("ButtonLoad").GetComponent<Hi5ButtonRect>();
            ButtonAnd = GameObject.Find("ButtonAnd").GetComponent<Hi5ButtonRect>();
            ButtonOr = GameObject.Find("ButtonOr").GetComponent<Hi5ButtonRect>();
            ButtonNot = GameObject.Find("ButtonNot").GetComponent<Hi5ButtonRect>();
            mergeOptions = GameObject.Find("MergeMenu").GetComponent<MergeOptions>();
            worldOptions = GameObject.Find("WorldMenu").GetComponent<WorldOptions>();
            helpOptions = GameObject.Find("HelpMenu").GetComponent<HelpOptions>();
            objectManipulator = GameObject.Find("ObjectManipulator").GetComponent<ObjectManipulator>();
            userGuide = GameObject.Find("UserGuide");
            objectMode = GameObject.Find("ObjectMode");
            voxelMode = GameObject.Find("VoxelMode");
            faceMode = GameObject.Find("FaceMode");
            systemHandle = GameObject.Find("SystemHandle");
            objectMode.SetActive(false);
            voxelMode.SetActive(false);
            faceMode.SetActive(false);
            systemHandle.SetActive(false);
            mergeOptions.gameObject.SetActive(false);
            worldOptions.gameObject.SetActive(false);

        }

        // Update is called once per frame
        private void Update()
        {

        }

        internal void WorldChange()
        {
            if (worldOptions.gameObject.activeSelf)
            {
                ButtonCreate.setButtonPosition(0.1f);
                if ((ButtonCreate.IsTouch() && hi5_object_judgeMent_2.IsHandIndexPoint()) || hi5_object_judgeMent_1.IsHandIndexPoint())
                {
                    worldOptions.OnPressForCreate();
                }
                ButtonSwitch.setButtonPosition(0.0f);
                if ((ButtonSwitch.IsTouch() && hi5_object_judgeMent_2.IsHandIndexPoint()) || hi5_object_judgeMent_1.IsTwo())
                {
                    worldOptions.OnPressForSwitch();
                }
                ButtonSave.setButtonPosition(-0.1f);
                if ((ButtonSave.IsTouch() && hi5_object_judgeMent_2.IsHandIndexPoint()) || hi5_object_judgeMent_1.IsThree())
                {
                    worldOptions.OnPressForSave();
                }
                ButtonLoad.setButtonPosition(-0.2f);
                if ((ButtonLoad.IsTouch() && hi5_object_judgeMent_2.IsHandIndexPoint()) || hi5_object_judgeMent_1.IsFingerPlane())
                {
                    worldOptions.OnPressForLoad();
                }
                if(hi5_object_judgeMent_1.IsHandFist())
                {
                    worldOptions.gameObject.SetActive(false);
                }
            }
            else if (mergeOptions.gameObject.activeSelf)
            {
                ButtonAnd.setButtonPosition(0.0f);
                if ((ButtonAnd.IsTouch() && hi5_object_judgeMent_2.IsHandIndexPoint()) || hi5_object_judgeMent_1.IsHandIndexPoint())
                {
                    mergeOptions.OnPressForAdd();
                }
                ButtonOr.setButtonPosition(-0.1f);
                if ((ButtonOr.IsTouch() && hi5_object_judgeMent_2.IsHandIndexPoint()) || hi5_object_judgeMent_1.IsTwo())
                {
                    mergeOptions.OnPressForOr();
                }
                ButtonNot.setButtonPosition(-0.2f);
                if ((ButtonNot.IsTouch() && hi5_object_judgeMent_2.IsHandIndexPoint()) || hi5_object_judgeMent_1.IsThree())
                {
                    mergeOptions.OnPressForNot();
                }
                if (hi5_object_judgeMent_1.IsHandFist())
                {
                    mergeOptions.gameObject.SetActive(false);
                }
            }
            else if(helpOptions.gameObject.activeSelf)
            {
                if(hi5_object_judgeMent_1.IsHandIndexPoint() && objectMode.activeSelf is false)
                {
                    helpOptions.turnToObject();
                }
                if (hi5_object_judgeMent_1.IsTwo() && voxelMode.activeSelf is false)
                {
                    helpOptions.turnToVoxel();
                }
                if (hi5_object_judgeMent_1.IsThree() && faceMode.activeSelf is false)
                {
                    helpOptions.turnToFace();
                }
                if (hi5_object_judgeMent_1.IsFingerPlane() && systemHandle.activeSelf is false)
                {
                    helpOptions.turnToSystem();
                }
                if (hi5_object_judgeMent_1.IsHandFist())
                {
                    helpOptions.closeHelpMenu();
                }
            }
        }
    }
}
