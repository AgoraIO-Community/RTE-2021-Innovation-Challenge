//
//  LxRecordListCell.h
//  LxAudioToStaff
//
//  Created by DavinLee on 2021/5/31.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN
typedef void (^RecordListCellClickDeleteBlock)(NSInteger tag);
@interface LxRecordListCell : UITableViewCell

@property (copy, nonatomic) RecordListCellClickDeleteBlock deleteBlock;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@end

NS_ASSUME_NONNULL_END
