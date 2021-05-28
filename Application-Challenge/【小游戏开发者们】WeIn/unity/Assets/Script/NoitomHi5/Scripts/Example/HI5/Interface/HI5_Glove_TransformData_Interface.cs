using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace HI5
{
    public class HI5_Glove_TransformData_Interface : MonoBehaviour
    {
        public static HI5_Glove_TransformData_Interface Instance = null;
        public enum EHi5_Glove_TransformData_Bones
        {
            /// <summary>
            /// The hand joint.
            /// </summary>
            Hand = 0,
            /// <summary>
            /// The metacarpal joint of thumb finger.
            /// </summary>
            HandThumb1,
            /// <summary>
            /// The proximal joint of thumb finger.
            /// </summary>
            HandThumb2,
            /// <summary>
            /// The distal joint of thumb finger.
            /// </summary>
            HandThumb3,
            /// <summary>
            /// The metacarpal joint of index finger.
            /// </summary>
            InHandIndex,
            /// <summary>
            /// The proximal joint of index finger.
            /// </summary>
            HandIndex1,
            /// <summary>
            /// The middle joint of index finger.
            /// </summary>
            HandIndex2,
            /// <summary>
            /// The distal joint of index finger.
            /// </summary>
            HandIndex3,
            /// <summary>
            /// The metacarpal joint of middle finger.
            /// </summary>
            InHandMiddle,
            /// <summary>
            /// The proximal joint of middle finger.
            /// </summary>
            HandMiddle1,
            /// <summary>
            /// The middle joint of middle finger.
            /// </summary>
            HandMiddle2,
            /// <summary>
            /// The distal joint of middle finger.
            /// </summary>
            HandMiddle3,
            /// <summary>
            /// The metacarpal joint of ring finger.
            /// </summary>
            InHandRing,
            /// <summary>
            /// The proximal joint of ring finger.
            /// </summary>
            HandRing1,
            /// <summary>
            /// The middle joint of ring finger.
            /// </summary>
            HandRing2,
            /// <summary>
            /// The distal joint of ring finger.
            /// </summary>
            HandRing3,
            /// <summary>
            /// The metacarpal joint of pinky finger.
            /// </summary>
            InHandPinky,
            /// <summary>
            /// The proximal joint of pinky finger.
            /// </summary>
            HandPinky1,
            /// <summary>
            /// The middle joint of pinky finger.
            /// </summary>
            HandPinky2,
            /// <summary>
            /// The distal joint of pinky finger.
            /// </summary>
            HandPinky3,
            /// <summary>
            /// The number of joints of Hi5 bones.
            /// </summary>
            NumOfHI5Bones,
        }
        private Dictionary<EHi5_Glove_TransformData_Bones,Transform> LeftHandBones;//= new Transform[(int)EHi5_Glove_TransformData_Bones.NumOfHI5Bones];
        private Dictionary<EHi5_Glove_TransformData_Bones, Transform> RightHandBones; //= new Transform[(int)EHi5_Glove_TransformData_Bones.NumOfHI5Bones];
        private HI5_VIVEInstance mLeftHand, mRightHand;


        public Dictionary<EHi5_Glove_TransformData_Bones, Transform> GetLeftHandTransform( )
        {
            return LeftHandBones;
        }

        public Dictionary<EHi5_Glove_TransformData_Bones, Transform> GetRightHandTransform()
        {
            return RightHandBones;
        }

        private void Awake()
        {
            HI5_Glove_TransformData_Interface.Instance = this;
            HI5_VIVEInstance[] hands = gameObject.GetComponentsInChildren<HI5_VIVEInstance > ();
            if (hands != null && hands.Length == 2)
            {
                if (hands[0].HandType == Hand.LEFT)
                {
                    mRightHand = hands[1];
                    mLeftHand = hands[0];
                }
                else
                {
                    mLeftHand = hands[1];
                    mRightHand = hands[0];
                }
            }
        }

        private void Update()
        {
            if (LeftHandBones == null && mLeftHand != null)
            {
                LeftHandBones = new Dictionary<EHi5_Glove_TransformData_Bones, Transform>();
                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.Hand, mLeftHand.HandBones[(int)Bones.Hand]);

                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.HandThumb1, mLeftHand.HandBones[(int)Bones.HandThumb1]);
                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.HandThumb2, mLeftHand.HandBones[(int)Bones.HandThumb2]);
                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.HandThumb3, mLeftHand.HandBones[(int)Bones.HandThumb3]);

                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.InHandIndex, mLeftHand.HandBones[(int)Bones.InHandIndex]);
                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.HandIndex1, mLeftHand.HandBones[(int)Bones.HandIndex1]);
                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.HandIndex2, mLeftHand.HandBones[(int)Bones.HandIndex2]);
                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.HandIndex3, mLeftHand.HandBones[(int)Bones.HandIndex3]);

                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.InHandMiddle, mLeftHand.HandBones[(int)Bones.InHandMiddle]);
                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.HandMiddle1, mLeftHand.HandBones[(int)Bones.HandMiddle1]);
                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.HandMiddle2, mLeftHand.HandBones[(int)Bones.HandMiddle2]);
                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.HandMiddle3, mLeftHand.HandBones[(int)Bones.HandMiddle3]);

                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.InHandRing, mLeftHand.HandBones[(int)Bones.InHandRing]);
                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.HandRing1, mLeftHand.HandBones[(int)Bones.HandRing1]);
                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.HandRing2, mLeftHand.HandBones[(int)Bones.HandRing2]);
                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.HandRing3, mLeftHand.HandBones[(int)Bones.HandRing3]);

                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.InHandPinky, mLeftHand.HandBones[(int)Bones.InHandPinky]);
                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.HandPinky1, mLeftHand.HandBones[(int)Bones.HandPinky1]);
                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.HandPinky2, mLeftHand.HandBones[(int)Bones.HandPinky2]);
                LeftHandBones.Add(EHi5_Glove_TransformData_Bones.HandPinky3, mLeftHand.HandBones[(int)Bones.HandPinky3]);

            }
            if (RightHandBones == null && mRightHand != null)
            {
                RightHandBones = new Dictionary<EHi5_Glove_TransformData_Bones, Transform>();
                RightHandBones.Add(EHi5_Glove_TransformData_Bones.Hand, mRightHand.HandBones[(int)Bones.Hand]);

                RightHandBones.Add(EHi5_Glove_TransformData_Bones.HandThumb1, mRightHand.HandBones[(int)Bones.HandThumb1]);
                RightHandBones.Add(EHi5_Glove_TransformData_Bones.HandThumb2, mRightHand.HandBones[(int)Bones.HandThumb2]);
                RightHandBones.Add(EHi5_Glove_TransformData_Bones.HandThumb3, mRightHand.HandBones[(int)Bones.HandThumb3]);

                RightHandBones.Add(EHi5_Glove_TransformData_Bones.InHandIndex, mRightHand.HandBones[(int)Bones.InHandIndex]);
                RightHandBones.Add(EHi5_Glove_TransformData_Bones.HandIndex1, mRightHand.HandBones[(int)Bones.HandIndex1]);
                RightHandBones.Add(EHi5_Glove_TransformData_Bones.HandIndex2, mRightHand.HandBones[(int)Bones.HandIndex2]);
                RightHandBones.Add(EHi5_Glove_TransformData_Bones.HandIndex3, mRightHand.HandBones[(int)Bones.HandIndex3]);

                RightHandBones.Add(EHi5_Glove_TransformData_Bones.InHandMiddle, mRightHand.HandBones[(int)Bones.InHandMiddle]);
                RightHandBones.Add(EHi5_Glove_TransformData_Bones.HandMiddle1, mRightHand.HandBones[(int)Bones.HandMiddle1]);
                RightHandBones.Add(EHi5_Glove_TransformData_Bones.HandMiddle2, mRightHand.HandBones[(int)Bones.HandMiddle2]);
                RightHandBones.Add(EHi5_Glove_TransformData_Bones.HandMiddle3, mRightHand.HandBones[(int)Bones.HandMiddle3]);

                RightHandBones.Add(EHi5_Glove_TransformData_Bones.InHandRing, mRightHand.HandBones[(int)Bones.InHandRing]);
                RightHandBones.Add(EHi5_Glove_TransformData_Bones.HandRing1, mRightHand.HandBones[(int)Bones.HandRing1]);
                RightHandBones.Add(EHi5_Glove_TransformData_Bones.HandRing2, mRightHand.HandBones[(int)Bones.HandRing2]);
                RightHandBones.Add(EHi5_Glove_TransformData_Bones.HandRing3, mRightHand.HandBones[(int)Bones.HandRing3]);

                RightHandBones.Add(EHi5_Glove_TransformData_Bones.InHandPinky, mRightHand.HandBones[(int)Bones.InHandPinky]);
                RightHandBones.Add(EHi5_Glove_TransformData_Bones.HandPinky1, mRightHand.HandBones[(int)Bones.HandPinky1]);
                RightHandBones.Add(EHi5_Glove_TransformData_Bones.HandPinky2, mRightHand.HandBones[(int)Bones.HandPinky2]);
                RightHandBones.Add(EHi5_Glove_TransformData_Bones.HandPinky3, mRightHand.HandBones[(int)Bones.HandPinky3]);

            }
            //if (mLeftHand != null)
            //{
            //    SetTransformData(mLeftHand, LeftHandBones, Bones.Hand, EHi5_Glove_TransformData_Bones.Hand);

            //    SetTransformData(mLeftHand, LeftHandBones, Bones.HandThumb1, EHi5_Glove_TransformData_Bones.HandThumb1);
            //    SetTransformData(mLeftHand, LeftHandBones, Bones.HandThumb2, EHi5_Glove_TransformData_Bones.HandThumb2);
            //    SetTransformData(mLeftHand, LeftHandBones, Bones.HandThumb3, EHi5_Glove_TransformData_Bones.HandThumb3);

            //    SetTransformData(mLeftHand, LeftHandBones, Bones.InHandIndex, EHi5_Glove_TransformData_Bones.InHandIndex);
            //    SetTransformData(mLeftHand, LeftHandBones, Bones.HandIndex1, EHi5_Glove_TransformData_Bones.HandIndex1);
            //    SetTransformData(mLeftHand, LeftHandBones, Bones.HandIndex2, EHi5_Glove_TransformData_Bones.HandIndex2);
            //    SetTransformData(mLeftHand, LeftHandBones, Bones.HandIndex3, EHi5_Glove_TransformData_Bones.HandIndex3);

            //    SetTransformData(mLeftHand, LeftHandBones, Bones.InHandMiddle, EHi5_Glove_TransformData_Bones.InHandMiddle);
            //    SetTransformData(mLeftHand, LeftHandBones, Bones.HandMiddle1, EHi5_Glove_TransformData_Bones.HandMiddle1);
            //    SetTransformData(mLeftHand, LeftHandBones, Bones.HandMiddle2, EHi5_Glove_TransformData_Bones.HandMiddle2);
            //    SetTransformData(mLeftHand, LeftHandBones, Bones.HandMiddle3, EHi5_Glove_TransformData_Bones.HandMiddle3);

            //    SetTransformData(mLeftHand, LeftHandBones, Bones.InHandRing, EHi5_Glove_TransformData_Bones.InHandRing);
            //    SetTransformData(mLeftHand, LeftHandBones, Bones.HandRing1, EHi5_Glove_TransformData_Bones.HandRing1);
            //    SetTransformData(mLeftHand, LeftHandBones, Bones.HandRing2, EHi5_Glove_TransformData_Bones.HandRing2);
            //    SetTransformData(mLeftHand, LeftHandBones, Bones.HandRing3, EHi5_Glove_TransformData_Bones.HandRing3);

            //    SetTransformData(mLeftHand, LeftHandBones, Bones.InHandPinky, EHi5_Glove_TransformData_Bones.InHandPinky);
            //    SetTransformData(mLeftHand, LeftHandBones, Bones.HandPinky1, EHi5_Glove_TransformData_Bones.HandPinky1);
            //    SetTransformData(mLeftHand, LeftHandBones, Bones.HandPinky2, EHi5_Glove_TransformData_Bones.HandPinky2);
            //    SetTransformData(mLeftHand, LeftHandBones, Bones.HandPinky3, EHi5_Glove_TransformData_Bones.HandPinky3);
            //}
            //if (mRightHand != null)
            //{
            //    SetTransformData(mRightHand, RightHandBones, Bones.Hand, EHi5_Glove_TransformData_Bones.Hand);

            //    SetTransformData(mRightHand, RightHandBones, Bones.HandThumb1, EHi5_Glove_TransformData_Bones.HandThumb1);
            //    SetTransformData(mRightHand, RightHandBones, Bones.HandThumb2, EHi5_Glove_TransformData_Bones.HandThumb2);
            //    SetTransformData(mRightHand, RightHandBones, Bones.HandThumb3, EHi5_Glove_TransformData_Bones.HandThumb3);

            //    SetTransformData(mRightHand, RightHandBones, Bones.InHandIndex, EHi5_Glove_TransformData_Bones.InHandIndex);
            //    SetTransformData(mRightHand, RightHandBones, Bones.HandIndex1, EHi5_Glove_TransformData_Bones.HandIndex1);
            //    SetTransformData(mRightHand, RightHandBones, Bones.HandIndex2, EHi5_Glove_TransformData_Bones.HandIndex2);
            //    SetTransformData(mRightHand, RightHandBones, Bones.HandIndex3, EHi5_Glove_TransformData_Bones.HandIndex3);

            //    SetTransformData(mRightHand, RightHandBones, Bones.InHandMiddle, EHi5_Glove_TransformData_Bones.InHandMiddle);
            //    SetTransformData(mRightHand, RightHandBones, Bones.HandMiddle1, EHi5_Glove_TransformData_Bones.HandMiddle1);
            //    SetTransformData(mRightHand, RightHandBones, Bones.HandMiddle2, EHi5_Glove_TransformData_Bones.HandMiddle2);
            //    SetTransformData(mRightHand, RightHandBones, Bones.HandMiddle3, EHi5_Glove_TransformData_Bones.HandMiddle3);

            //    SetTransformData(mRightHand, RightHandBones, Bones.InHandRing, EHi5_Glove_TransformData_Bones.InHandRing);
            //    SetTransformData(mRightHand, RightHandBones, Bones.HandRing1, EHi5_Glove_TransformData_Bones.HandRing1);
            //    SetTransformData(mRightHand, RightHandBones, Bones.HandRing2, EHi5_Glove_TransformData_Bones.HandRing2);
            //    SetTransformData(mRightHand, RightHandBones, Bones.HandRing3, EHi5_Glove_TransformData_Bones.HandRing3);

            //    SetTransformData(mRightHand, RightHandBones, Bones.InHandPinky, EHi5_Glove_TransformData_Bones.InHandPinky);
            //    SetTransformData(mRightHand, RightHandBones, Bones.HandPinky1, EHi5_Glove_TransformData_Bones.HandPinky1);
            //    SetTransformData(mRightHand, RightHandBones, Bones.HandPinky2, EHi5_Glove_TransformData_Bones.HandPinky2);
            //    SetTransformData(mRightHand, RightHandBones, Bones.HandPinky3, EHi5_Glove_TransformData_Bones.HandPinky3);
            //}
        }

        private void SetTransformData(HI5_VIVEInstance handOriginal,
                                        List<Transform> hands,
                                        Bones boneOriginalType,
                                        EHi5_Glove_TransformData_Bones boneType)
        {
            if (hands[(int)boneType] == null)
                Debug.Log("boneType"+ (int)boneType);
            if (handOriginal.HandBones[(int)boneOriginalType] == null)
                Debug.Log("boneOriginalType" + (int)boneOriginalType);
            hands[(int)boneType].position = handOriginal.HandBones[(int)boneOriginalType].position;
            hands[(int)boneType].rotation = handOriginal.HandBones[(int)boneOriginalType].rotation;
        }

    }
}

