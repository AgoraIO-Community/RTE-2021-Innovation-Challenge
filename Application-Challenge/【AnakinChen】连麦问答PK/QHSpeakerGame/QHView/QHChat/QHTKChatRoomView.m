//
//  QHTKChatRoomView.m
//  QHChatDemo
//
//  Created by Anakin chen on 2019/11/8.
//  Copyright © 2019 Chen Network Technology. All rights reserved.
//

#import "QHTKChatRoomView.h"

#import "QHChatBaseUtil.h"
#import "QHTKRoomChatVIewUtil.h"
#import "QHTKRoomChatContentViewCell.h"

#import "QHTKRoomChatVIewUtil+Game.h"
#import "QHTKRoomChatVIewUtil+Room.h"

#import "QHHyphenateChatManagerDefine.h"

#define kQHCHAT_TK_CONTENT_CELLIDENTIFIER @"TKQHChatLCContentCellIdentifier"

NSString *const kTKChatOpKey = @"op";

@interface QHTKChatRoomView () <QHChatBaseViewCellDelegate>

@end

@implementation QHTKChatRoomView

#pragma mark - QHChatBaseViewProtocol

- (void)qhChatCustomChatViewSetup {
}

- (void)qhChatAddCell2TableView:(UITableView *)tableView {
    [tableView registerClass:[QHTKRoomChatContentViewCell class] forCellReuseIdentifier:kQHCHAT_TK_CONTENT_CELLIDENTIFIER];
}

- (NSMutableAttributedString *)qhChatAnalyseContent:(NSDictionary *)data {
    NSInteger op = [data[kTKChatOpKey] integerValue];
    NSMutableAttributedString *content = nil;
    switch (op) {
        case QHHCActionChat:
            content = [QHTKRoomChatVIewUtil toHello:data];
            break;
        case QHHCActionSystem:
            content = [QHTKRoomChatVIewUtil toNotice:data];
            break;
        case QHHCActionWelcome:
            content = [QHTKRoomChatVIewUtil toWelcome:data];
            break;
        case QHHCActionJoin:
            content = [QHTKRoomChatVIewUtil toJoinGame:data];
            break;
        case QHHCActionBegan:
            content = [QHTKRoomChatVIewUtil toBeganGame:data];
            break;
        case QHHCActionNext:
            content = [QHTKRoomChatVIewUtil toNextPlayerGame:data];
            break;
        case QHHCActionResult:
            content = [QHTKRoomChatVIewUtil toResultGame:data];
            break;
        case QHHCActionEnd:
            content = [QHTKRoomChatVIewUtil toEndGame:data];
            break;
        default:
            break;
    }
    return content;
}

- (CGFloat)qhChatAnalyseHeight:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    QHChatBaseModel *model = [self.buffer getChatData:indexPath.row];
    if (model.chatAttributedText == nil) {
        return 0;
    }
    UIEdgeInsets contentEI = TKQHCHAT_LC_CONTENT_EDGEINSETS;
    UIEdgeInsets contentTextEI = TKQHCHAT_LC_CONTENT_TEXT_EDGEINSETS;
    
    CGFloat w = self.config.cellConfig.cellWidth - contentEI.left - contentEI.right - contentTextEI.left - contentTextEI.right;
    NSStringDrawingOptions options = NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading;
    CGRect rect = [model.chatAttributedText boundingRectWithSize:CGSizeMake(w, CGFLOAT_MAX) options:options context:nil];
    CGFloat h = rect.size.height + self.config.cellConfig.cellLineSpacing + contentEI.top + contentEI.bottom + contentTextEI.top + contentTextEI.bottom;
    return h;
}

- (UITableViewCell *)qhChatChatView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *chatCell = nil;
    QHChatBaseModel *model = [self.buffer getChatData:indexPath.row];
    if (model.chatAttributedText != nil) {
        QHTKRoomChatContentViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kQHCHAT_TK_CONTENT_CELLIDENTIFIER];
        cell.contentL.attributedText = model.chatAttributedText;
        cell.delegate = self;
//        [cell cellUpdateConstraints];
        chatCell = cell;
    }
    
    return chatCell;
}

- (void)qhChatAddCellDefualAttributes:(NSMutableAttributedString *)attr {
    if (attr == nil) {
        return;
    }
    NSMutableParagraphStyle *paragraphStyle = [NSMutableParagraphStyle new];
    paragraphStyle.lineSpacing = self.config.cellConfig.cellLineSpacing;
    paragraphStyle.lineBreakMode = NSLineBreakByCharWrapping;
    [attr addAttributes:@{NSFontAttributeName: [UIFont boldSystemFontOfSize:self.config.cellConfig.fontSize], NSParagraphStyleAttributeName: paragraphStyle} range:NSMakeRange(0, attr.length)];
}

- (void)qhlongPressAction:(UILongPressGestureRecognizer *)gec {
}

@end
