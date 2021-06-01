//
//  GDataXMLNode+Unisk.h
//
//
//  Created by Guoyu on 3/11/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "GDataXMLNode.h"

@interface GDataXMLElement(Unisk)

- (GDataXMLElement *)elementForName:(NSString *)name;

@end


@interface GDataXMLNode(Helper)
- (GDataXMLElement *)nodeForXPath:(NSString *)xpath;
- (void) print;

//从aTree的子树中查找aTag 返回stringValue
- (NSString*)stringValueWithTag:(NSString*)aTag;//空标签返回@“”
-(NSString*) getStringWithTag:(NSString*)aTag;//空标签返回@“－－”，用于显示。

//从aTree的子树中查找aTag 返回children array
-(NSArray *) getArrayWithTag:(NSString*)aTag;

//深度遍历，返回遇到的第一个值
-(NSString*) deepValue:(NSString*) tag;
//深度遍历，返回遇到的第一个数组
-(NSArray*) deepArray:(NSString*) tag;
//深度遍历，返回遇到的第一个element
-(GDataXMLNode*) deepXMLNode:(NSString*) tag;


//xpath(以node开头)
//返回遇到的所有nodes 支持xpath
-(NSArray*) nodesArray:(NSString*) aXPath;
//深度遍历,返回遇到的所有nodes 支持xpath
-(NSString*) nodeString:(NSString*) aXPath;
-(NSString*) nodeStringNotPrint:(NSString*) aXPath;
-(GDataXMLNode*) node:(NSString*) aXPath;
@end;