//
//  QHChatBaseBuffer.m
//  QHChatDemo
//
//  Created by Anakin chen on 2020/5/27.
//  Copyright © 2020 Chen Network Technology. All rights reserved.
//

#import "QHChatBaseBuffer.h"

// [iOS 十种线程锁](https://www.jianshu.com/p/7e9dd2cb78a8)

@interface QHChatBaseBuffer ()

@property (nonatomic, strong, readwrite) NSMutableArray<QHChatBaseModel *> *chatDatasArray;
@property (nonatomic, strong, readwrite) NSMutableArray<NSDictionary *> *chatDatasTempArray;

@end

@implementation QHChatBaseBuffer

- (void)dealloc {
#if DEBUG
    NSLog(@"%s", __FUNCTION__);
#endif
    _chatDatasArray = nil;
    _chatDatasTempArray = nil;
}

- (instancetype)init {
    self = [super init];
    if (self) {
        [self p_setup];
    }
    return self;
}

#pragma mark - Public

- (void)append2TempArray:(NSArray<NSDictionary *> *)data {
    [self p_lock:^{
        [self.chatDatasTempArray addObjectsFromArray:data];
        if (self.chatDatasTempArray.count > self.config.chatCountMax) {
            [self.chatDatasTempArray removeObjectsInRange:NSMakeRange(0, self.config.chatCountDelete)];
        }
    }];
}

- (void)append2Array:(QHChatBaseModel *)model {
    [self p_lock:^{
        [self.chatDatasArray addObject:model];
    }];
}

- (QHChatBaseModel *)getChatData:(NSInteger)index {
    __block QHChatBaseModel *data = nil;
    [self p_lock:^{
        if (self.chatDatasArray.count > index) {
            data = self.chatDatasArray[index];
        }
    }];
    return data;
}

- (void)clearTempArray {
    [self p_lock:^{
        [self.chatDatasTempArray removeAllObjects];
    }];
}

- (void)clear {
    [self p_lock:^{
        [self.chatDatasTempArray removeAllObjects];
        [self.chatDatasArray removeAllObjects];
    }];
}

- (void)replaceObjectAtLastIndexWith:(QHChatBaseModel *)model {
    [self p_lock:^{
        [self.chatDatasArray replaceObjectAtIndex:self.chatDatasArray.count - 1 withObject:model];
    }];
}

- (BOOL)removeObjectsInRange {
    __block BOOL bDeleteChatData = NO;
    [self p_lock:^{
        if (self.chatDatasArray.count > self.config.chatCountMax) {
            [self.chatDatasArray removeObjectsInRange:NSMakeRange(0, self.config.chatCountDelete)];
            bDeleteChatData = YES;
        }
    }];
    return bDeleteChatData;
}

#pragma mark - Private

- (void)p_setup {
//    _lock = dispatch_semaphore_create(1);
    
    _chatDatasTempArray = [NSMutableArray new];
    _chatDatasArray = [NSMutableArray new];
}

- (void)p_lock:(void(^)(void))block {
//    if (self.config.hasUnlock == NO) {
//        dispatch_semaphore_wait(_lock, DISPATCH_TIME_FOREVER);
//        block();
//        dispatch_semaphore_signal(_lock);
//    }
//    else {
//        block();
//    }
    block();
}

@end
