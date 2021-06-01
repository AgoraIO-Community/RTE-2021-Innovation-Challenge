//
//  LxNoteToXmlHelp.m
//  SmartPiano
//
//  Created by DavinLee on 2018/10/23.
//  Copyright © 2018 Ydtec. All rights reserved.
//

#import "LxNoteToXmlHelp.h"
#import "GDataXMLNode+Helper.h"
#import "CKMakeMusicView.h"
#import "LxMcStaffHeader.h"
#import "LxMcMeasureModel.h"
#import "LxMusicNode.h"
@implementation LxNoteToXmlHelp
/**
 *@description 获取作曲曲谱音符信息保存的xml数据源
 **/
+ (NSData *)lx_xmlTransformWithMameMusicView:(CKMakeMusicView *)view
                                   staffName:(NSString *)staffName;
{
    NSData *xmlData = [NSData dataWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"exampleNodes" ofType:@"xml"]];
    NSError *error;
    GDataXMLDocument *document = [[GDataXMLDocument alloc] initWithData:xmlData options:0 error:&error];
    /** 解析 **/
    GDataXMLElement *rootElement = document.rootElement;
    
    
    GDataXMLNode *oldPart = [rootElement node:@"part"];
    GDataXMLElement *firstMeasure = [[oldPart nodesArray:@"measure"] firstObject];
    /** 暂未发现gdataxml的插入api和操作，此处暂时重新按固定排序添加 **/
    NSArray *childrenElements = [rootElement children];
    for (GDataXMLElement *tempElement in childrenElements) {
        
        NSString *xml = [tempElement XMLString];
        GDataXMLElement *originElementCopy = [[GDataXMLElement alloc] initWithXMLString:xml error:nil];
        [rootElement removeChild:tempElement];
         if ([[tempElement name] isEqualToString:@"credit"]){/** 修改曲谱名称 **/
            [rootElement addChild:[self creditElementWithTitle:staffName]];
        }else if ([[tempElement name] isEqualToString:@"part"]){/** 替换part部分 **/
            [rootElement addChild:[self partNodeWithMusicView:view firstMeasureElement:firstMeasure]];
        }
        else{
            [rootElement addChild:originElementCopy];
        }
    }

    GDataXMLDocument *newDocument = [[GDataXMLDocument alloc] initWithRootElement:rootElement];
    NSData *documentData = [newDocument XMLData];
    
    return documentData;
}

/**
 *@description 获取名称work-node替换
 **/
+ (GDataXMLElement *)workElementWithTitle:(NSString *)title{
    GDataXMLElement *workElement = [GDataXMLElement elementWithName:@"work"];
    GDataXMLElement *workTitleElement = [GDataXMLElement elementWithName:@"work-title" stringValue:title];
    [workElement addChild:workTitleElement];
    return workElement;
}

/** 获取字体名称credit-node替换 **/
+ (GDataXMLElement *)creditElementWithTitle:(NSString *)title{
    GDataXMLElement *creditElement = [GDataXMLElement elementWithName:@"credit"];
    [creditElement addAttribute:[GDataXMLElement attributeWithName:@"page" stringValue:@"1"]];

    GDataXMLElement *wordElement = [GDataXMLElement elementWithName:@"credit-words" stringValue:title];

//    [wordElement addAttribute:[GDataXMLElement attributeWithName:@"default-x" stringValue:@"600"]];
//    [wordElement addAttribute:[GDataXMLElement attributeWithName:@"default-y" stringValue:@"148"]];
    [wordElement addAttribute:[GDataXMLElement attributeWithName:@"font-family" stringValue:@"FreeSerif"]];
    [wordElement addAttribute:[GDataXMLElement attributeWithName:@"font-style" stringValue:@"normal"]];
    [wordElement addAttribute:[GDataXMLElement attributeWithName:@"font-size" stringValue:@"22.0128"]];
    [wordElement addAttribute:[GDataXMLElement attributeWithName:@"font-weight" stringValue:@"normal"]];
    [wordElement addAttribute:[GDataXMLElement attributeWithName:@"default-x" stringValue:@"600"]];
    [wordElement addAttribute:[GDataXMLElement attributeWithName:@"default-y" stringValue:@"148"]];
    [wordElement addAttribute:[GDataXMLElement attributeWithName:@"font-size" stringValue:@"31"]];
    [wordElement addAttribute:[GDataXMLElement attributeWithName:@"justify" stringValue:@"center"]];
    [wordElement addAttribute:[GDataXMLElement attributeWithName:@"valign" stringValue:@"top"]];
    [creditElement addChild:wordElement];
    return creditElement;
    
}

/**
 *@description 获取重新编辑的part曲谱部分的xml
 *@param view  曲谱信息
 *@param firstElement
 **/
+ (GDataXMLElement *)partNodeWithMusicView:(CKMakeMusicView *)view  firstMeasureElement:(GDataXMLElement *)firstMeasureElement{
    NSArray <LxMcMeasureModel *>*measuresArray = view.tabsView.ClefMeasureModelArray;
    GDataXMLElement *partElement = [GDataXMLElement elementWithName:@"part"];
    [partElement addAttribute:[GDataXMLElement attributeWithName:@"id" stringValue:@"P1"]];
    
    NSInteger measureIndex = 1;
    for (LxMcMeasureModel *measure in measuresArray) {
        GDataXMLElement *measureNode = [GDataXMLElement elementWithName:@"measure"];
        
        [measureNode addAttribute:[GDataXMLElement attributeWithName:@"number" stringValue:[NSString stringWithFormat:@"%ld",(long)measureIndex]]];
        if (measureIndex == 1) {
            /** 添加print节点 **/
            NSString *staffCount = view.tabsView.staffClefState == LxMcMcBothClef ? @"2" : @"1";
            GDataXMLElement *printElement = [self printAttributeWithExaPrint:[firstMeasureElement nodeForXPath:@"print"] staffCount:staffCount];
            [measureNode addChild:printElement];
            /** 添加attribute节点 **/
            NSString *beats;
            NSString *beatType;
            switch (view.tabsView.staffBeatsType) {
                case  LxMcStaffBeats2_4:
                    beats = @"2"; beatType = @"4";
                    break;
                case LxMcStaffBeats3_4:
                    beats = @"3"; beatType = @"4";
                    break;
                case LxMcStaffBeats4_4:
                    beats = @"4"; beatType = @"4";
                    break;
                case lxMcStaffBeats3_8:
                    beats = @"3"; beatType = @"8";
                    break;
                default:
                    break;
            }
            GDataXMLElement *attributeElement = [self attributeWithMajorType:view.tabsView.staffManageType
                                                                       beats:beats
                                                                    beatType:beatType
                                                                   staffType:view.tabsView.staffClefState];
            [measureNode addChild:attributeElement];
            /** 加入曲谱节拍速度信息 **/
            GDataXMLElement *direction = [self directionWithPerMinute:@"100" staffType:view.tabsView.staffClefState];
            [measureNode addChild:direction];
        }
        
        for (int i = 0; i < measure.clefUpNoteViewArray.count; i++) {
            LxMcNoteView *noteView = measure.clefUpNoteViewArray[i];
            /** 判断、添加强弱记号 **/
            if (noteView.strength > 0) {
                GDataXMLElement *directionElement = [self directionWithStrength:noteView.strength isUpClef:noteView.isUpClef];
                [measureNode addChild:directionElement];
            }
            
            /** 添加音符 **/
            GDataXMLElement *noteElement = [self nodeElementWithNoteView:noteView highStaff:YES];
            [measureNode addChild:noteElement];
           
        }
        if ([measuresArray indexOfObject:measure] == measuresArray.count - 1) {
            GDataXMLElement *endLine = [GDataXMLElement elementWithName:@"barline"];
            GDataXMLElement *style = [GDataXMLElement elementWithName:@"bar-style" stringValue:@"light-heavy"];
            [endLine addChild:style];
            [measureNode addChild:endLine];
            NSLog(@"添加终止线");
        }
        
        if (measure.clefDoNoteViewArray.count > 0) {
            NSInteger backupDuration = 0;
            NSInteger wholeDuration = 1024;
            switch (view.tabsView.staffBeatsType) {
                case LxMcStaffBeats2_4:
                    backupDuration = 0.5 * wholeDuration;
                    break;
                    case LxMcStaffBeats3_4:
                    backupDuration = 3.f / 4.f * wholeDuration;
                    break;
                    case lxMcStaffBeats3_8:
                    backupDuration = 3.f/ 8.f * wholeDuration;
                    break;
                    case LxMcStaffBeats4_4:
                    backupDuration = wholeDuration;
                    break;
                default:
                    backupDuration = wholeDuration;
                    break;
            }
            GDataXMLElement *backUp = [GDataXMLElement elementWithName:@"backup"];
            GDataXMLElement *backDuration = [GDataXMLElement elementWithName:@"duration" stringValue:@"4"];
            [backUp addChild:backDuration];
//            [backUp addChild:[GDataXMLElement elementWithName:@"duration" stringValue:[NSString stringWithFormat:@"%ld",(long)backupDuration]]];
            
            [measureNode addChild:backUp];
            for (LxMcNoteView *noteView in measure.clefDoNoteViewArray) {
                /** 判断、添加强弱记号 **/
                if (noteView.strength > 0) {
                    GDataXMLElement *directionElement = [self directionWithStrength:noteView.strength isUpClef:noteView.isUpClef];
                    [measureNode addChild:directionElement];
                }
                GDataXMLElement *noteElement = [self nodeElementWithNoteView:noteView highStaff:NO];
                [measureNode addChild:noteElement];
            }
        }
        [partElement addChild:measureNode];
        measureIndex ++;
    }
    return partElement;
}

#pragma mark - ********************  获取  ********************


/** 获取音符node **/
+ (GDataXMLElement *)nodeElementWithNoteView:(LxMcNoteView *)noteView highStaff:(BOOL)highStaff
{
    GDataXMLElement *node = [GDataXMLElement elementWithName:@"note"];
    
    [node addAttribute:[GDataXMLElement attributeWithName:@"color" stringValue:@"#000000"]];
    /** 休止符 **/
    if (noteView.isRest) {
        [node addChild:[GDataXMLElement elementWithName:@"rest"]];
    }else{
        GDataXMLElement *pitch = [GDataXMLElement elementWithName:@"pitch"];
        [pitch addChild:[GDataXMLElement elementWithName:@"step" stringValue:[LxMusicNode stepWithMiditag:noteView.miditag]]];
        if (noteView.alter != 0) {/** 大调、小调添加属性alter **/
            [pitch addChild:[GDataXMLElement elementWithName:@"alter" stringValue:[NSString stringWithFormat:@"%ld",(long)noteView.alter]]];
        }
        [pitch addChild:[GDataXMLElement elementWithName:@"octave" stringValue:[NSString stringWithFormat:@"%ld",(long)[LxMusicNode octaveWithMidiTag:noteView.miditag]]]];
        [node addChild:pitch];
    }
    if (noteView.isDot) {
        GDataXMLElement *dot = [GDataXMLElement elementWithName:@"dot"];
        [node addChild:dot];
    }
    /** 计算时值 **/
    NSInteger wholeDuration = 4;
    NSInteger duration = wholeDuration / noteView.noteType;
    if (noteView.isDot) {
        duration *= 1.5;
    }
    [node addChild:[GDataXMLElement elementWithName:@"duration" stringValue:[NSString stringWithFormat:@"%ld",(long)duration]]];
    
    
    /** 音符所在曲谱 **/
    if (highStaff) {
        [node addChild:[GDataXMLElement elementWithName:@"voice" stringValue:@"1"]];
    }else{
        [node addChild:[GDataXMLElement elementWithName:@"voice" stringValue:@"5"]];
    }
//    GDataXMLElement *instrument = [GDataXMLElement elementWithName:@"instrument"];
//    [instrument addAttribute:[GDataXMLElement attributeWithName:@"id" stringValue:@"P1-I1"]];
//    [node addChild:instrument];
    
    
    /** 音符类型 **/
    [node addChild:[GDataXMLElement elementWithName:@"type" stringValue:[noteView nodeTypeString]]];
    /** 符干朝向 **/
    if (noteView.headDirection == LxMcNoteHead_left_up ||
        noteView.headDirection == LxMcNoteHead_right_up) {
        [node addChild:[GDataXMLElement elementWithName:@"stem" stringValue:@"down"]];
    }else{
        [node addChild:[GDataXMLElement elementWithName:@"stem" stringValue:@"up"]];
    }
    /** 音符所在曲谱 **/
    if (highStaff) {
        [node addChild:[GDataXMLElement elementWithName:@"staff" stringValue:@"1"]];
    }else{
        [node addChild:[GDataXMLElement elementWithName:@"staff" stringValue:@"2"]];
    }
    
    /** 添加力度 **/
//    [node addChild:[GDataXMLElement elementWithName:@"dynamics" stringValue:[NSString stringWithFormat:@"%ld",(long)(noteView.strength == 0 ? 80  : noteView.strength)]]];
    
    return node;
}

+ (GDataXMLElement *)directionWithStrength:(NSInteger)strength isUpClef:(BOOL)isUpClef
{
    NSString *dynamics = @"f";
    switch (strength) {
        case MusicNodeStrengthPP:
            dynamics = @"pp";
            break;
            case MusicNodeStrengthP:
            dynamics = @"p";
            break;
            case MusicNodeStrengthMP:
            dynamics = @"mp";
            break;
            case MusicNodeStrengthMF:
            dynamics = @"mf";
            break;
            case MusicNodeStrengthF:
            dynamics = @"f";
            break;
            case MusicNodeStrengthFF:
            dynamics = @"ff";
            break;
        default:
            break;
    }
    GDataXMLElement *directionElement = [GDataXMLElement elementWithName:@"direction"];
    
    GDataXMLElement *directionTypeElement = [GDataXMLElement elementWithName:@"direction-type"];
    
    GDataXMLElement *dynamicElement = [GDataXMLElement elementWithName:@"dynamics"];
    [dynamicElement addAttribute:[GDataXMLElement attributeWithName:@"default-x" stringValue:@"106"]];
    [dynamicElement addAttribute:[GDataXMLElement attributeWithName:@"default-y" stringValue:@"-70"]];
    [dynamicElement addAttribute:[GDataXMLElement attributeWithName:@"color" stringValue:@"#000000"]];
    [dynamicElement addAttribute:[GDataXMLElement attributeWithName:@"font-family" stringValue:@"SimHei"]];
    [dynamicElement addAttribute:[GDataXMLElement attributeWithName:@"font-style" stringValue:@"italic"]];
    [dynamicElement addAttribute:[GDataXMLElement attributeWithName:@"font-size" stringValue:@"11.9365"]];
    [dynamicElement addAttribute:[GDataXMLElement attributeWithName:@"font-weight" stringValue:@"bold"]];
    [dynamicElement addChild:[GDataXMLElement elementWithName:dynamics]];
    
    [directionTypeElement addChild:dynamicElement];
    [directionElement addChild:directionTypeElement];
    [directionElement addChild:[GDataXMLElement elementWithName:@"voice" stringValue:isUpClef ? @"1" : @"2"]];
    [directionElement addChild:[GDataXMLElement elementWithName:@"staff" stringValue:isUpClef ? @"1" : @"2"]];
    return directionElement;
}
/** 获取曲谱速度信息 **/
+ (GDataXMLElement *)directionWithPerMinute:(NSString *)perMinute staffType:(LxMcState )staffType{
    GDataXMLElement *direction = [GDataXMLElement elementWithName:@"direction"];
    /** 速度展示 **/
    GDataXMLElement *type = [GDataXMLElement elementWithName:@"direction-type"];
    GDataXMLElement *metronome = [GDataXMLElement elementWithName:@"metronome"];
    [metronome addAttribute:[GDataXMLElement attributeWithName:@"default-y" stringValue:@"30"]];
    [metronome addAttribute:[GDataXMLElement attributeWithName:@"color" stringValue:@"#000000"]];
    [metronome addAttribute:[GDataXMLElement attributeWithName:@"font-family" stringValue:@"Opus Text Std"]];
    [metronome addAttribute:[GDataXMLElement attributeWithName:@"font-style" stringValue:@"normal"]];
    [metronome addAttribute:[GDataXMLElement attributeWithName:@"font-size" stringValue:@"11.1614"]];
    [metronome addAttribute:[GDataXMLElement attributeWithName:@"font-weight" stringValue:@"normal"]];
    [metronome addChild:[GDataXMLElement elementWithName:@"beat-unit" stringValue:@"quarter"]];
    [metronome addChild:[GDataXMLElement elementWithName:@"per-minute" stringValue:perMinute]];
    [type addChild:metronome];
   
    [direction addChild:type];
//    [direction addChild:[GDataXMLElement elementWithName:@"voice" stringValue:@"1"]];
    [direction addChild:[GDataXMLElement elementWithName:@"staff" stringValue:@"1"]];
    
    return direction;
}

/**
 *@description 获取曲谱print信息
 *@param exaPrint 范例print节点
 *@param staffCount  曲谱无线谱数量 （1/2）
 **/
+ (GDataXMLElement *)printAttributeWithExaPrint:(GDataXMLElement *)exaPrint staffCount:(NSString *)staffCount{
    GDataXMLElement *print = [GDataXMLElement elementWithName:@"print"];
    [print addAttribute:[GDataXMLElement attributeWithName:@"new-page" stringValue:@"yes"]];
    
    
    GDataXMLNode *sysLayout = [exaPrint node:@"system-layout"];
    [print addChild:sysLayout];
    
    GDataXMLElement *stfLayout = [GDataXMLElement elementWithName:@"staff-layout"];
    GDataXMLNode *stfLayoutAtt = [GDataXMLNode attributeWithName:@"number" stringValue:staffCount];
    [stfLayout addAttribute:stfLayoutAtt];
    [stfLayout addChild:[GDataXMLElement elementWithName:@"staff-distance" stringValue:@"55"]];
    [print addChild:stfLayout];
    return print;
}

/**
 *@description 获取曲谱节拍、大小调等信息
 **/
+ (GDataXMLElement *)attributeWithMajorType:(LxMcStaffManageType )staffMajorType
                                      beats:(NSString *)beats
                                   beatType:(NSString *)beatType
                                  staffType:(LxMcState )staffType{
    GDataXMLElement *attribute = [GDataXMLElement elementWithName:@"attributes"];
    
    GDataXMLElement *divisions = [GDataXMLElement elementWithName:@"divisions" stringValue:@"1"];
    [attribute addChild:divisions];
    
    NSString *fifths;
//    NSString *mode = @"major";
    switch (staffMajorType) {
        case LxMcStaffManageNormal:
            fifths = @"0";
            break;
            case LxMcStaffManageGmajor:
            fifths = @"1";
            break;
            case LxMcStaffManageDmajor:
            fifths = @"2";
            break;
            case LxMcStaffManageFmajor:
            fifths = @"-1";
            break;
            case LxMcStaffManageAchordMajor:
            fifths = @"0";   //fifths = @"0";
//            mode = @"major";  //mode = @"minor";
            break;
        case LxMcStaffManageEchordMajor:
             fifths = @"1";  //fifths = @"1";
//            mode = @"major"; // mode = @"minor";
            break;
        default:
            break;
    }
    /** 升降调 **/
    GDataXMLElement *key = [GDataXMLElement elementWithName:@"key"];
    GDataXMLElement *keyColor = [GDataXMLElement attributeWithName:@"color" stringValue:@"#000000"];
    [key addAttribute:keyColor];
    [key addChild:[GDataXMLElement elementWithName:@"fifths" stringValue:fifths]];
//    [key addChild:[GDataXMLElement elementWithName:@"mode" stringValue:mode]];
    [attribute addChild:key];
    /** 节拍信息 **/
    GDataXMLElement *time = [GDataXMLElement elementWithName:@"time"];
//    GDataXMLElement *timeColor = [GDataXMLElement attributeWithName:@"color" stringValue:@"#000000"];
//    [time addAttribute:timeColor];
    GDataXMLElement *beatsNode = [GDataXMLElement elementWithName:@"beats" stringValue:beats];
    GDataXMLElement *beatTypeNode = [GDataXMLElement elementWithName:@"beat-type" stringValue:beatType];
    [time addChild:beatsNode];
    [time addChild:beatTypeNode];
    [attribute addChild:time];
    /** 曲谱数量 **/
    NSString *staffCount = staffType == LxMcMcBothClef ? @"2" : @"1";
    GDataXMLElement *stave = [GDataXMLElement elementWithName:@"staves" stringValue:staffCount];
    [attribute addChild:stave];
    /** 未知-分高低音谱**/
    GDataXMLElement *clef1 = [GDataXMLElement elementWithName:@"clef"];
    GDataXMLElement *clef1Index = [GDataXMLElement attributeWithName:@"number" stringValue:@"1"];
    [clef1 addAttribute:clef1Index];
    [clef1 addAttribute:keyColor];
    [clef1 addChild:[GDataXMLElement elementWithName:@"sign" stringValue:@"G"]];
    [clef1 addChild:[GDataXMLElement elementWithName:@"line" stringValue:@"2"]];
    
    GDataXMLElement *clef2 = [GDataXMLElement elementWithName:@"clef"];
    GDataXMLElement *clef2Index = [GDataXMLElement attributeWithName:@"number" stringValue:@"2"];
    [clef2 addAttribute:clef2Index];
    [clef2 addAttribute:keyColor];
    [clef2 addChild:[GDataXMLElement elementWithName:@"sign" stringValue:@"F"]];
    [clef2 addChild:[GDataXMLElement elementWithName:@"line" stringValue:@"4"]];
    switch (staffType) {
        case LxMcMcBothClef:
            [attribute addChild:clef1];
            [attribute addChild:clef2];
            break;
        case LxMcMcHighClef:
            [attribute addChild:clef1];
            break;
            case LxMcMcLowClef:
            [attribute addChild:clef2];
        default:
            break;
    }
    /** 曲谱数量 **/
//    GDataXMLElement *staffDetails = [GDataXMLElement elementWithName:@"staff-details"];
//    [staffDetails addAttribute:[GDataXMLElement attributeWithName:@"number" stringValue:staffType == LxMcMcBothClef ? @"2" : @"1"]];
//    [staffDetails addAttribute:[GDataXMLElement attributeWithName:@"print-object" stringValue:@"yes"]];
//    [attribute addChild:staffDetails];
    return attribute;
}
@end
