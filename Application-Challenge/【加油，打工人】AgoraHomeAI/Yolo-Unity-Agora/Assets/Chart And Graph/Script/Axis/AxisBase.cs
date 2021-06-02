#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.Serialization;
using UnityEngine.UI;

namespace ChartAndGraph
{
    /// <summary>
    /// base class for all axis monobehaviours. This class contains paramets for defining chart axis
    /// </summary>
    public abstract class AxisBase : ChartSettingItemBase, ISerializationCallbackReceiver
    {
#pragma warning disable 0414
        /// <summary>
        /// used internally by the axis inspector
        /// </summary>
        [SerializeField]
        private bool SimpleView = true;
#pragma warning restore 0414
        /// <summary>
        /// the format of the axis labels. This can be either a number, time or date time. If the selected value is either DateTime or Time , user ChartDateUtillity to convert dates to double values that can be set to the chart.
        /// </summary>
        [SerializeField]
        [Tooltip("the format of the axis labels. This can be either a number, time or date time. If the selected value is either DateTime or Time , user ChartDateUtillity to convert dates to double values that can be set to the graph")]
        private AxisFormat format;

        /// <summary>
        /// the format of the axis labels. This can be either a number, time or date time. If the selected value is either DateTime or Time , user ChartDateUtillity to convert dates to double values that can be set to the chart.
        /// </summary>
        public AxisFormat Format
        {
            get { return format; }
            set
            {
                format = value;
                RaiseOnChanged();
            }
        }

        /// <summary>
        /// the depth of the axis reltive to the chart position
        /// </summary>
        [SerializeField]
        [Tooltip("the depth of the axis reltive to the chart position")]
        private AutoFloat depth;
        public AutoFloat Depth
        {
            get { return depth; }
            set { depth = value;
                RaiseOnChanged();
            }
        }

        [SerializeField]
        [Tooltip("The main divisions of the chart axis")]
        [FormerlySerializedAs("MainDivisions")]
        private ChartMainDivisionInfo mainDivisions = new ChartMainDivisionInfo();

        [SerializeField]
        [Tooltip("The sub divisions of each main division")]
        [FormerlySerializedAs("SubDivisions")]
        private ChartSubDivisionInfo subDivisions = new ChartSubDivisionInfo();


        private Dictionary<double, string> mFormats = new Dictionary<double, string>();
        private List<double> mTmpToRemove = new List<double>();
        /// <summary>
        /// the main division properies for this axis
        /// </summary>
        public ChartMainDivisionInfo MainDivisions { get { return mainDivisions; } }
        /// <summary>
        /// the sub division properies for this axis
        /// </summary>
        public ChartDivisionInfo SubDivisions { get { return subDivisions; } }
        public void ClearFormats()
        {
            mFormats.Clear();
        }
        public AxisBase()
        {
            AddInnerItems();
        }
        private void AddInnerItems()
        {
            AddInnerItem(MainDivisions);
            AddInnerItem(SubDivisions);
        }
        /// <summary>
        /// used internally to hold data required for updating the axis lables
        /// </summary>
        internal class TextData
        {
            public ChartDivisionInfo info;
            public float interp;
            public int fractionDigits;
        }

        /// <summary>
        /// checks that all properies of this instance have valid values. This method is used internally and should not be called
        /// </summary>
        public void ValidateProperties()
        {
            mainDivisions.ValidateProperites();
            subDivisions.ValidateProperites();
        }

        /// <summary>
        /// retrieves the begining and end division of the chart
        /// </summary>
        /// <param name="parent"></param>
        /// <param name="orientation"></param>
        /// <param name="total"></param>
        /// <param name="start"></param>
        /// <param name="end"></param>
        private void GetStartEnd(AnyChart parent, ChartOrientation orientation, float total, out float start, out float end)
        {
            start = 0;
            end = total;
            //    if ((orientation == ChartOrientation.Horizontal) ? parent.Frame.Left.Visible : parent.Frame.Bottom.Visible)
            //        ++start;
            //    if ((orientation == ChartOrientation.Horizontal) ? parent.Frame.Right.Visible : parent.Frame.Top.Visible)
            //        --end;
        }

        /// <summary>
        /// sets the uv of a chart mesh based on the current length and offset
        /// </summary>
        /// <param name="mesh"></param>
        /// <param name="length"></param>
        /// <param name="offset"></param>
        private void SetMeshUv(IChartMesh mesh, float length,float offset)
        {
            if (length < 0)
                offset -= length;
            mesh.Length = length;
            mesh.Offset = offset;
        }

        private void DrawDivisions(double scrollOffset, AnyChart parent, Transform parentTransform, ChartDivisionInfo info, IChartMesh mesh, int group, ChartOrientation orientation, double gap, bool oppositeSide, double mainGap)
        {
            //scrollOffset = -scrollOffset;
            double parentSize = (orientation == ChartOrientation.Vertical) ? ((IInternalUse)parent).InternalTotalHeight : ((IInternalUse)parent).InternalTotalWidth;
            DoubleVector3 startPosition, lengthDirection, advanceDirection;
            GetDirectionVectors(parent, info, orientation, 0f, oppositeSide, out startPosition, out lengthDirection, out advanceDirection);
            double markDepth = ChartCommon.GetAutoDepth(parent, orientation, info);
            double length = ChartCommon.GetAutoLength(parent, orientation, info);
            double backLength = (orientation == ChartOrientation.Vertical) ? ((IInternalUse)parent).InternalTotalWidth : ((IInternalUse)parent).InternalTotalHeight;

            if (info.MarkBackLength.Automatic == false)
                backLength = info.MarkBackLength.Value;

            double totaluv = Math.Abs(length);
            if (backLength != 0 && markDepth > 0)
                totaluv += Math.Abs(backLength) + Math.Abs(markDepth);

            DoubleVector3 halfThickness = advanceDirection * (info.MarkThickness * 0.5f);
            // if (scrollOffset != 0f)
            //     last--;

            bool hasValues = ((IInternalUse)parent).InternalHasValues(this);
            double maxValue = ((IInternalUse)parent).InternalMaxValue(this);
            double minValue = ((IInternalUse)parent).InternalMinValue(this);
            double range = maxValue - minValue;
           
            float AutoAxisDepth = Depth.Value;
//            float scrollFactor = (scrollOffset / (float)(maxValue - minValue));
            //scrollOffset = scrollFactor * parentSize;
                
            if (Depth.Automatic)
            {
                AutoAxisDepth = (float)((((IInternalUse)parent).InternalTotalDepth) - markDepth);
            }

            double startValue = (scrollOffset + minValue);
            double endValue = (scrollOffset + maxValue) + double.Epsilon;
            double direction = 1.0;
            Func<double, double> ValueToPosition = x => ((x - startValue) / range) * parentSize;
            if (startValue > endValue)
            {
                direction = -1.0;
                //ValueToPosition = x => (1.0- ((x - startValue) / range)) * parentSize;
            }
            gap = Math.Abs(gap);
            double fraction = gap - (scrollOffset - Math.Floor((scrollOffset / gap) - double.Epsilon) * gap);
            double mainfraction = -1f;
            double currentMain = 0f; 

            if (mainGap > 0f)
            {
                mainfraction = mainGap - (scrollOffset - Math.Floor((scrollOffset / mainGap) - double.Epsilon) * mainGap);
                currentMain = (scrollOffset + minValue + mainfraction);
            }

            int i = 0;

            mTmpToRemove.Clear();
            double startRange = startValue + fraction;
            foreach (double key in mFormats.Keys)
            {
                if (key* direction > endValue* direction || key* direction < startRange* direction)
                    mTmpToRemove.Add(key);
            }
            for(int k=0; k<mTmpToRemove.Count; k++)
                mFormats.Remove(mTmpToRemove[k]);
            for (double current = startRange; direction*current <= direction*endValue; current += gap*direction)
            {

                ++i;
                if (i > 3000)
                {
                    break;
                }

                if (mainGap > 0.0)
                {
                    if(Math.Abs(current - currentMain) < 0.00001)
                    {
                        currentMain += mainGap;
                        continue;
                    }
                    if(current > currentMain)
                    {
                        currentMain += mainGap;
                    }
                }

                double offset = ValueToPosition(current);
                if (offset < 0 || offset > parentSize)
                    continue;
                DoubleVector3 start = startPosition + advanceDirection * offset;
                DoubleVector3 size = halfThickness + length * lengthDirection;
                start -= halfThickness;
                //size += halfThickness;
                float uvoffset = 0f;

                Rect r = ChartCommon.FixRect(new Rect((float)start.x, (float)start.y, (float)size.x, (float)size.y));

                SetMeshUv(mesh, (float)(-length / totaluv), uvoffset);
                uvoffset += Math.Abs(mesh.Length);

                mesh.AddXYRect(r, group, AutoAxisDepth);
                if (hasValues)
                {
                    double val = Math.Round(current*1000.0)/1000.0;
                    string toSet = "";
                    double keyVal = val;// (int)Math.Round(val);
                    var dic = (orientation == ChartOrientation.Horizontal) ? parent.HorizontalValueToStringMap : parent.VerticalValueToStringMap;
                    if (!(Math.Abs(val - keyVal) < 0.001 && dic.TryGetValue(keyVal, out toSet)))
                    {
                        if (mFormats.TryGetValue(val, out toSet) == false)
                        {
                            if (format == AxisFormat.Number)
                                toSet = ChartAdancedSettings.Instance.FormatFractionDigits(info.FractionDigits, val, parent.CustomNumberFormat);
                            else
                            {
                                DateTime date = ChartDateUtility.ValueToDate(val);
                                if (format == AxisFormat.DateTime)
                                    toSet = ChartDateUtility.DateToDateTimeString(date, parent.CustomDateTimeFormat);
                                else
                                {
                                    if (format == AxisFormat.Date)
                                        toSet = ChartDateUtility.DateToDateString(date);
                                    else
                                        toSet = ChartDateUtility.DateToTimeString(date);
                                }
                            }
                            toSet = info.TextPrefix + toSet + info.TextSuffix;
                            mFormats[val] = toSet;
                            
                        }
                    }
                    else
                    {
                        toSet = info.TextPrefix + toSet + info.TextSuffix;
                    }

                    
                    DoubleVector3 textPos = new DoubleVector3(start.x, start.y);
                    textPos += lengthDirection * info.TextSeperation;
                    TextData userData = new TextData();
                    userData.interp = (float)(offset/parentSize);
                    userData.info = info;
                    userData.fractionDigits = info.FractionDigits;
                    mesh.AddText(parent, info.TextPrefab, parentTransform, info.FontSize, info.FontSharpness, toSet, (float)textPos.x, (float)textPos.y, AutoAxisDepth + info.TextDepth, 0f, userData);
                }

                if (markDepth > 0)
                {
                    if (orientation == ChartOrientation.Horizontal)
                    {
                        SetMeshUv(mesh,(float)( markDepth / totaluv), uvoffset);
                        r = ChartCommon.FixRect(new Rect((float)start.x, AutoAxisDepth, (float)size.x, (float)markDepth));
                        mesh.AddXZRect(r, group, (float)start.y);
                    }
                    else
                    {
                        SetMeshUv(mesh,(float) (-markDepth / totaluv), uvoffset);
                        r = ChartCommon.FixRect(new Rect((float)start.y, AutoAxisDepth, (float)size.y, (float)markDepth));
                        mesh.AddYZRect(r, group, (float)start.x);
                    }

                    uvoffset += Math.Abs(mesh.Length);

                    if (backLength != 0)
                    {
                        SetMeshUv(mesh, (float)(backLength / totaluv), uvoffset);
                        uvoffset += Math.Abs(mesh.Length);
                        DoubleVector3 backSize = halfThickness + backLength * lengthDirection;
                        Rect backR = ChartCommon.FixRect(new Rect((float)start.x, (float)start.y, (float)backSize.x, (float)backSize.y));
                        mesh.AddXYRect(backR, group, (float)(AutoAxisDepth + markDepth));
                    }
                }

            }
         //   Debug.Log("start");
         //   Debug.Log(mFormats.Count);
         //   Debug.Log(cached);
        }

//        /// <summary>
//        /// used internally to draw the division of the axis into a chart mesh
//        /// </summary>
//        /// <param name="parent"></param>
//        /// <param name="parentTransform"></param>
//        /// <param name="info"></param>
//        /// <param name="mesh"></param>
//        /// <param name="group"></param>
//        /// <param name="orientation"></param>
//        /// <param name="totalDivisions"></param>
//        /// <param name="oppositeSide"></param>
//        /// <param name="skip"></param>
//        private void DrawDivisions(float scrollOffset,AnyChart parent, Transform parentTransform, ChartDivisionInfo info, IChartMesh mesh, int group, ChartOrientation orientation, float totalDivisions, bool oppositeSide, int skip)
//        {
//            //scrollOffset = -scrollOffset;
//            float parentSize = (orientation == ChartOrientation.Vertical) ? ((IInternalUse)parent).InternalTotalHeight : ((IInternalUse)parent).InternalTotalWidth;
//            Vector2 startPosition, lengthDirection, advanceDirection;
//            GetDirectionVectors(parent, info, orientation, 0f, oppositeSide, out startPosition, out lengthDirection, out advanceDirection);
//            float markDepth = ChartCommon.GetAutoDepth(parent, orientation, info);
//            float length = ChartCommon.GetAutoLength(parent, orientation, info);
//            float backLength = (orientation == ChartOrientation.Vertical) ? ((IInternalUse)parent).InternalTotalWidth : ((IInternalUse)parent).InternalTotalHeight;

//            if (info.MarkBackLength.Automatic == false)
//                backLength = info.MarkBackLength.Value;

//            float totaluv = Math.Abs(length);
//            if (backLength != 0 && markDepth > 0)
//                totaluv += Math.Abs(backLength) + Math.Abs(markDepth);

//            Vector2 halfThickness = advanceDirection * (info.MarkThickness * 0.5f);
//           // if (scrollOffset != 0f)
//           //     last--;

//            bool hasValues = ((IInternalUse)parent).InternalHasValues(this);
//            double maxValue = ((IInternalUse)parent).InternalMaxValue(this);
//            double minValue = ((IInternalUse)parent).InternalMinValue(this);
//            ChartMainDivisionInfo main = info as ChartMainDivisionInfo;

//            float divisionComplement = 0f;
//            float divisionOffsetComplement = 0f;
//            if (main != null)
//            {
//                if(main.Messure == ChartDivisionInfo.DivisionMessure.DataUnits)
//                {
//                    if (main.UnitsPerDivision <= 0.0001f)
//                        return;
//                    float range = (float)(maxValue - minValue);
//                    totalDivisions = (range / main.UnitsPerDivision) + 1;
//                    divisionComplement = (float)(1f - (main.UnitsPerDivision - Math.Truncate(main.UnitsPerDivision))) / range;
//                    divisionOffsetComplement = (divisionComplement) * parentSize;
//                }
//            }

            
//            float first, last;
//            GetStartEnd(parent, orientation, totalDivisions, out first, out last);

//            float AutoAxisDepth = Depth.Value;
//            float scrollFactor = (scrollOffset / (float)(maxValue - minValue));
//            scrollOffset = scrollFactor * parentSize;

//            if (Depth.Automatic)
//            {
//                AutoAxisDepth = (((IInternalUse)parent).InternalTotalDepth) - markDepth;
//            }

//            float floorLast = Mathf.Floor(last);
//            for (float i = first; i < floorLast; i++)
//            {
//                if (skip != -1 && ((int)i) % skip == 0)
//                    continue;
//                float factor = ((float)i) / (float)(totalDivisions - 1);
//                float offset = -scrollOffset + factor * parentSize;
//                if (scrollOffset != 0f)
//                {
//                    float prevOffs = offset;
//                    offset = offset - Mathf.Floor((offset / parentSize)) * parentSize;
////                    Debug.Log("prev " + prevOffs);
////                    Debug.Log("offs " + offset);
//                    if (offset < prevOffs)
//                    {
//                        offset -= divisionOffsetComplement;
//                    }
//                }
//                if (offset < 0f)
//                    continue;
//                Vector2 start = startPosition + advanceDirection * offset;
//                Vector2 size = halfThickness + length * lengthDirection;
//                start -= halfThickness;
//                //size += halfThickness;
//                float uvoffset = 0f;

//                Rect r = ChartCommon.FixRect(new Rect(start.x, start.y, size.x, size.y));
    
//                SetMeshUv(mesh, -length / totaluv, uvoffset);
//                uvoffset += Math.Abs(mesh.Length);
                        
//                mesh.AddXYRect(r, group, AutoAxisDepth);

//                if (hasValues)
//                {
//                    float valFactor = -scrollFactor + factor;
//                    if (scrollOffset != 0f)
//                    {
//                        float prevFact = valFactor;
//                        valFactor -= Mathf.Floor(valFactor);
//                        if(valFactor < prevFact)
//                        {
//                            valFactor -= divisionComplement;
//                        }
//                    }
//                    //valFactor += scrollFactor;
//                    double offsetPos = (maxValue - minValue) * valFactor;
//                    double scroll = minValue + ((maxValue - minValue) * scrollFactor);
//                    double val = scroll + offsetPos;
//                    string toSet = "";
//                    if (format == AxisFormat.Number)
//                        toSet = ChartAdancedSettings.Instance.FormatFractionDigits(info.FractionDigits, (float)val);
//                    else
//                    {
//                        DateTime date = ChartDateUtility.ValueToDate(val);
//                        if (format == AxisFormat.DateTime)
//                            toSet = ChartDateUtility.DateToDateTimeString(date);
//                        else
//                        {
//                            if (format == AxisFormat.Date)
//                                toSet = ChartDateUtility.DateToDateString(date);
//                            else
//                                toSet = ChartDateUtility.DateToTimeString(date);
//                        }
//                    }

//                    toSet = info.TextPrefix + toSet + info.TextSuffix;
//                    Vector2 textPos = new Vector2(start.x, start.y);
//                    textPos += lengthDirection * info.TextSeperation;
//                    TextData userData = new TextData();
//                    userData.interp = factor;
//                    userData.info = info;
//                    userData.fractionDigits = info.FractionDigits;
//                    mesh.AddText(parent, info.TextPrefab, parentTransform, info.FontSize, info.FontSharpness, toSet, textPos.x, textPos.y, AutoAxisDepth + info.TextDepth,0f, userData);
//                }

//                if (markDepth > 0)
//                {
//                    if (orientation == ChartOrientation.Horizontal)
//                    {
//                        SetMeshUv(mesh, markDepth / totaluv, uvoffset);
//                        r = ChartCommon.FixRect(new Rect(start.x, AutoAxisDepth, size.x, markDepth));
//                        mesh.AddXZRect(r, group, start.y);
//                    }
//                    else
//                    {
//                        SetMeshUv(mesh, -markDepth / totaluv, uvoffset);
//                        r = ChartCommon.FixRect(new Rect(start.y, AutoAxisDepth, size.y, markDepth));
//                        mesh.AddYZRect(r, group, start.x);
//                    }

//                    uvoffset += Math.Abs(mesh.Length);

//                    if (backLength != 0)
//                    {
//                        SetMeshUv(mesh, backLength / totaluv, uvoffset);
//                        uvoffset += Math.Abs(mesh.Length);
//                        Vector2 backSize = halfThickness + backLength * lengthDirection;
//                        Rect backR = ChartCommon.FixRect(new Rect(start.x, start.y, backSize.x, backSize.y));
//                        mesh.AddXYRect(backR, group, AutoAxisDepth + markDepth);
//                    }
//                }
//            }
//        }
        
        /// <summary>
        /// used internally to determine drawing direction of the each chart division
        /// </summary>
        /// <param name="parent"></param>
        /// <param name="info"></param>
        /// <param name="orientation"></param>
        /// <param name="oppositeSide"></param>
        /// <param name="startPosition"></param>
        /// <param name="lengthDirection"></param>
        /// <param name="advanceDirection"></param>
        private void GetDirectionVectors(AnyChart parent, ChartDivisionInfo info, ChartOrientation orientation,float scrollOffset, bool oppositeSide, out DoubleVector3 startPosition, out DoubleVector3 lengthDirection, out DoubleVector3 advanceDirection)
        {
            if (orientation == ChartOrientation.Horizontal)
            {
                advanceDirection = new DoubleVector3(1f, 0f);
                if (oppositeSide)
                {
                    startPosition = new DoubleVector3(scrollOffset, ((IInternalUse)parent).InternalTotalHeight);
                    lengthDirection = new DoubleVector3(0f, -1f);
                    return;
                }
                startPosition = new DoubleVector3(0f, 0f);
                lengthDirection = new DoubleVector3(0f, 1f);
                return;
            }
            advanceDirection = new DoubleVector3(0f, 1f);
            if (oppositeSide)
            {
                startPosition = new DoubleVector3(0f, 0f);
                lengthDirection = new DoubleVector3(1f, 0f);
                return;
            }
            startPosition = new DoubleVector3(((IInternalUse)parent).InternalTotalWidth, scrollOffset);
            lengthDirection = new DoubleVector3(-1f, 0f);
        }

        /// <summary>
        /// used internally , adds the axis sub divisions to the chart mesh
        /// </summary>
        /// <param name="parent"></param>
        /// <param name="parentTransform"></param>
        /// <param name="mesh"></param>
        /// <param name="orientation"></param>
        internal void AddSubdivisionToChartMesh(double scrollOffset,AnyChart parent, Transform parentTransform, IChartMesh mesh, ChartOrientation orientation)
        {
            int total = SubDivisions.Total;
            if (total <= 1) // no need for more
                return;
            double maxValue = ((IInternalUse)parent).InternalMaxValue(this);
            double minValue = ((IInternalUse)parent).InternalMinValue(this);
            double range = maxValue - minValue;
            double? gap = GetMainGap(parent, range);
            if (gap.HasValue == false)
                return;
            double subGap = gap.Value / (total);
            mesh.Tile = ChartCommon.GetTiling(SubDivisions.MaterialTiling);
            if ((SubDivisions.Alignment & ChartDivisionAligment.Opposite) == ChartDivisionAligment.Opposite)
                DrawDivisions(scrollOffset,parent, parentTransform, SubDivisions, mesh, 0, orientation, subGap, false, gap.Value);
            if ((SubDivisions.Alignment & ChartDivisionAligment.Standard) == ChartDivisionAligment.Standard)
                DrawDivisions(scrollOffset,parent, parentTransform, SubDivisions, mesh, 0, orientation, subGap, true, gap.Value);
        }

        double? GetMainGap(AnyChart parent, double range)
        {
            double gap = ((ChartMainDivisionInfo)MainDivisions).UnitsPerDivision;
            if (((ChartMainDivisionInfo)MainDivisions).Messure == ChartDivisionInfo.DivisionMessure.TotalDivisions)
            {
                int total = ((ChartMainDivisionInfo)MainDivisions).Total;
                if (total <= 0)
                    return null;
                gap = (range / (double)(total));
            }
            return gap;
        }
        /// <summary>
        /// used internally , adds the axis main divisions to the chart mesh
        /// </summary>
        /// <param name="parent"></param>
        /// <param name="parentTransform"></param>
        /// <param name="mesh"></param>
        /// <param name="orientation"></param>

        internal void AddMainDivisionToChartMesh(double scrollOffset,AnyChart parent, Transform parentTransform, IChartMesh mesh, ChartOrientation orientation)
        {
            double maxValue = ((IInternalUse)parent).InternalMaxValue(this);
            double minValue = ((IInternalUse)parent).InternalMinValue(this);
            double range = maxValue - minValue;
            double? gap = GetMainGap(parent, range);
            if (gap.HasValue == false)
                return;
            mesh.Tile = ChartCommon.GetTiling(MainDivisions.MaterialTiling);
            if ((MainDivisions.Alignment & ChartDivisionAligment.Opposite) == ChartDivisionAligment.Opposite)
                DrawDivisions(scrollOffset,parent, parentTransform, MainDivisions, mesh, 0, orientation,gap.Value, false, -1);
            if ((MainDivisions.Alignment & ChartDivisionAligment.Standard) == ChartDivisionAligment.Standard)
                DrawDivisions(scrollOffset,parent, parentTransform, MainDivisions, mesh, 0, orientation, gap.Value, true, -1);
        }

        void ISerializationCallbackReceiver.OnBeforeSerialize()
        {

        }

        void ISerializationCallbackReceiver.OnAfterDeserialize()
        {
            AddInnerItems();
        }
    }
}
