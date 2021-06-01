//
//  GDataXMLNode+Unisk.m
//
//
//  Created by Guo yu on 3/11/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "GDataXMLNode+Helper.h"

@implementation GDataXMLElement (Unisk)

#pragma mark

- (GDataXMLElement *)elementForName:(NSString *)name
{
    GDataXMLElement *ele = nil;
    NSArray *eles = [self elementsForName:name];
    
#if DEBUG
    if ([eles count] == 0) {
        debugLog(@"!!!elementForName: %@ not find",name);
    }
    //    NSCAssert1([eles count], @"elementForName: %@ not find",
    //               name);
#endif
    
    if ([eles count]) {
        ele = [eles objectAtIndex:0];
    }
    return ele;
}

@end

@implementation GDataXMLNode(Helper)

- (GDataXMLElement *)nodeForXPath:(NSString *)xpath  {
    GDataXMLElement *ele = nil;
    NSArray *eles = [self nodesForXPath:xpath namespaces:nil error:nil];
#if DEBUG
    if ([eles count] == 0) {
        debugLog(@"!!!nodeForXPath: %@ not find",xpath);
    }
    //    NSCAssert1([eles count], @"nodeForXPath: %@ not find",
    //               xpath);
#endif
    if ([eles count]) {
        ele = [eles objectAtIndex:0];
    }
    return ele;
}

- (void) print
{
    static int flagTmp = 0;
    flagTmp++;
    
    NSMutableString* kong = [[NSMutableString alloc] initWithCapacity:4];
    for (int i =0; i<flagTmp; i++)
    {
        [kong appendString:@"   "];
    }
    //Gdata ：每个值都作为一个节点 加入到标签的子树中：所以每个有值的标签的子树数组都有一个name 为@“text”的标签。单标签除外
    if (self.children == nil || self.children.count == 0 || [((GDataXMLNode*)self.children[0]).name isEqualToString:@"text"])
        //    if (self.children)
    {
//        int temInt = flagTmp;
//        NSString* str1 = self.name;
//        NSString* str2 = [self stringValue];
//        debugLog(@"%@ %d---%@ = %@",kong,temInt, str1,str2);
    }
    else
    {
        debugLog(@"%@<%@> %lu",kong,self.name,(unsigned long)self.children.count);
        for (int i=0; i<self.children.count ; i++)
        {
            GDataXMLElement* tempTree = nil;
            tempTree = [self.children objectAtIndex:i];
            [tempTree print];
        }
        debugLog(@"%@</%@>",kong,self.name);
    }

    flagTmp--;
}
#pragma mark - 框架移植相关
//从aTree的子树中查找aTag 返回stringValue,空标签返回@“”
- (NSString*)stringValueWithTag:(NSString*)aTag
{
    GDataXMLElement* element = [self nodeForXPath:aTag];
    NSString* value = [element stringValue];
    if (value.length == 0){//防止因为把返回的nil 加入数组而导致崩溃。
        value = @"";
    }
    return value;
}

//从aTree的子树中查找aTag 返回stringValue,空标签返回@“－－”，用于显示。
-(NSString*) getStringWithTag:(NSString*)aTag
{
    GDataXMLElement* element = [self nodeForXPath:aTag];
    NSString* value = [element stringValue];
    if (value.length == 0){
        value = @"－－";
    }
    return value;
}

//从aTree的子树中查找aTag 返回array
-(NSArray *) getArrayWithTag:(NSString*)aTag
{
    GDataXMLElement* element = [self nodeForXPath:aTag];
    NSArray* arrChildren = [element children];
    //Gdata：所以每个有值的标签的子树数组都有一个name 为@“text”的标签。单标签除外
    if (arrChildren.count == 1 && [((GDataXMLElement*)arrChildren[0]).name isEqualToString:@"text"])
        return nil;
    return arrChildren;
}

-(NSString*) deepValue:(NSString*) tag
{
    GDataXMLNode* xmlNode = [self deepXMLNode:tag];
    if (xmlNode.children == nil || xmlNode.children.count == 0 || (((GDataXMLNode*)xmlNode.children[0]).children.count == 0))
    {//值
        return [xmlNode stringValue];
    }
    else
    {//数组
#if DEBUG
        debugLog(@"!!!deepValue: %@ is a arrarXML",tag);
        //        NSCAssert1(0, @"deepValue: %@ is a arrarXML",
        //                   tag);
#endif
    }
    return nil;
}

-(NSArray*) deepArray:(NSString*) tag
{
    GDataXMLNode* xmlNode = [self deepXMLNode:tag];
    if (xmlNode.children == nil || xmlNode.children.count == 0 || (((GDataXMLNode*)xmlNode.children[0]).children.count == 0))
    {//值
#if DEBUG
        debugLog(@"!!!deepArray: %@ is a stringXML",tag);
        //        NSCAssert1(0, @"deepArray: %@ is a stringXML",
        //                   tag);
#endif
    }
    else
    {//数组
        return [xmlNode children];
    }
    return nil;
}

-(GDataXMLNode*) deepXMLNode:(NSString*) tag
{
    GDataXMLNode* xmlNode = nil;
    [self _deepXMLNode:tag to:&xmlNode];
#if DEBUG
    if (!xmlNode) {
        debugLog(@"!!!deepXMLNode: %@ not find",tag);
    }
    //    NSCAssert1(xmlNode != nil, @"deepXMLNode: %@ not find",
    //               tag);
#endif
    return xmlNode;
}

-(void) _deepXMLNode:(NSString*) tag to:(GDataXMLNode**) aFoundNode
{
    if ([self.name isEqualToString:tag])
    {
        *aFoundNode = self;
        return;
    }
    for (int i=0; i<self.children.count ; i++)
    {
        GDataXMLElement* tempTree = nil;
        tempTree = [self.children objectAtIndex:i];
        [tempTree _deepXMLNode:tag to:aFoundNode];
    }
}

#pragma mark -xpath
-(NSArray*) nodesArray:(NSString*) aTag {
    
    NSArray* array = [self nodesForXPath:aTag error:nil];
    
    //Gdata：所以每个有值的标签的子树数组都有一个name 为@“text”的标签。单标签除外
    if (array.count == 0) {
#if DEBUG
        debugLog(@"!!!nodesArray: %@  not found",aTag);
        //        NSCAssert1(0, @"deepValue: %@ is a arrarXML",
        //                   tag);
#endif
    }
    
    return array;
}

-(NSString*) nodeString:(NSString*) aXPath {
    NSArray* array = [self nodesForXPath:aXPath error:nil];
    
    //Gdata：所以每个有值的标签的子树数组都有一个name 为@“text”的标签。单标签除外
    if (array.count == 0) {
#if DEBUG
        debugLog(@"!!!nodeString: %@  not found",aXPath);
        //        NSCAssert1(0, @"deepValue: %@ is a arrarXML",
        //                   tag);
        return nil;
#endif
    } else {
        GDataXMLElement* xmlNode = array[0];
        return [xmlNode stringValue];
    }
    return @"";
}

-(NSString*) nodeStringNotPrint:(NSString*) aXPath {
    NSArray* array = [self nodesForXPath:aXPath error:nil];
    
    //Gdata：所以每个有值的标签的子树数组都有一个name 为@“text”的标签。单标签除外
    if (array.count == 0) {
    } else {
        GDataXMLElement* xmlNode = array[0];
        return [xmlNode stringValue];
    }
    return @"";
}

-(GDataXMLNode*) node:(NSString*) aXPath {
    NSArray* array = [self nodesForXPath:aXPath error:nil];
    //Gdata：所以每个有值的标签的子树数组都有一个name 为@“text”的标签。单标签除外
    if (array.count == 0) {
#if DEBUG
        debugLog(@"!!!node: %@  not found",aXPath);
        return nil;
#endif
    } else {
        GDataXMLNode* xmlNode = array[0];
        return xmlNode;
    }
    return nil;
}

@end
