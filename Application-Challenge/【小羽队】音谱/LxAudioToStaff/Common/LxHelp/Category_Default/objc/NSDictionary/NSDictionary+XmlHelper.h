//
//  NSDictionary+XmlHelper.h
//  LxHelp
//
//  Created by DavinLee on 2017/9/22.
//  Copyright © 2017年 DavinLee. All rights reserved.
//

#import <Foundation/Foundation.h>
#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wobjc-missing-property-synthesis"


NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, XMLDictionaryAttributesMode)
{
    XMLDictionaryAttributesModePrefixed = 0, //default
    XMLDictionaryAttributesModeDictionary,
    XMLDictionaryAttributesModeUnprefixed,
    XMLDictionaryAttributesModeDiscard
};


typedef NS_ENUM(NSInteger, XMLDictionaryNodeNameMode)
{
    XMLDictionaryNodeNameModeRootOnly = 0, //default
    XMLDictionaryNodeNameModeAlways,
    XMLDictionaryNodeNameModeNever
};


static NSString *const XMLDictionaryAttributesKey   = @"__attributes";
static NSString *const XMLDictionaryCommentsKey     = @"__comments";
static NSString *const XMLDictionaryTextKey         = @"__text";
static NSString *const XMLDictionaryNodeNameKey     = @"__name";
static NSString *const XMLDictionaryAttributePrefix = @"_";

@interface XMLDictionaryParser : NSObject <NSCopying>

+ (XMLDictionaryParser *)sharedInstance;

@property (nonatomic, assign) BOOL collapseTextNodes; // defaults to YES
@property (nonatomic, assign) BOOL stripEmptyNodes;   // defaults to YES
@property (nonatomic, assign) BOOL trimWhiteSpace;    // defaults to YES
@property (nonatomic, assign) BOOL alwaysUseArrays;   // defaults to NO
@property (nonatomic, assign) BOOL preserveComments;  // defaults to NO
@property (nonatomic, assign) BOOL wrapRootNode;      // defaults to NO

@property (nonatomic, assign) XMLDictionaryAttributesMode attributesMode;
@property (nonatomic, assign) XMLDictionaryNodeNameMode nodeNameMode;

- (nullable NSDictionary<NSString *, id> *)dictionaryWithParser:(NSXMLParser *)parser;
- (nullable NSDictionary<NSString *, id> *)dictionaryWithData:(NSData *)data;
- (nullable NSDictionary<NSString *, id> *)dictionaryWithString:(NSString *)string;
- (nullable NSDictionary<NSString *, id> *)dictionaryWithFile:(NSString *)path;

@end


@interface NSDictionary (XmlHelper)

+ (nullable NSDictionary<NSString *, id> *)dictionaryWithXMLParser:(NSXMLParser *)parser;
+ (nullable NSDictionary<NSString *, id> *)dictionaryWithXMLData:(NSData *)data;
+ (nullable NSDictionary<NSString *, id> *)dictionaryWithXMLString:(NSString *)string;
+ (nullable NSDictionary<NSString *, id> *)dictionaryWithXMLFile:(NSString *)path;

@property (nonatomic, readonly, copy, nullable) NSDictionary<NSString *, NSString *> *attributes;
@property (nonatomic, readonly, copy, nullable) NSDictionary<NSString *, id> *childNodes;
@property (nonatomic, readonly, copy, nullable) NSArray<NSString *> *comments;
@property (nonatomic, readonly, copy, nullable) NSString *nodeName;
@property (nonatomic, readonly, copy, nullable) NSString *innerText;
@property (nonatomic, readonly, copy) NSString *innerXML;
@property (nonatomic, readonly, copy) NSString *XMLString;

- (nullable NSArray *)arrayValueForKeyPath:(NSString *)keyPath;
- (nullable NSString *)stringValueForKeyPath:(NSString *)keyPath;
- (nullable NSDictionary<NSString *, id> *)dictionaryValueForKeyPath:(NSString *)keyPath;

@end


@interface NSString (XMLDictionary)

@property (nonatomic, readonly, copy) NSString *XMLEncodedString;


@end



NS_ASSUME_NONNULL_END


#pragma GCC diagnostic pop
