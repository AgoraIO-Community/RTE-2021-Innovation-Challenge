//
//  LxRecordListCell.m
//  LxAudioToStaff
//
//  Created by DavinLee on 2021/5/31.
//

#import "LxRecordListCell.h"
@interface LxRecordListCell()




@end

@implementation LxRecordListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    self.backgroundColor = [UIColor clearColor];
    self.contentView.backgroundColor =[UIColor clearColor];
    self.selectionStyle = UITableViewCellSelectionStyleNone;
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}
- (void)setDeleteBlock:(RecordListCellClickDeleteBlock)deleteBlock{
    _deleteBlock = [deleteBlock copy];
}

- (IBAction)clickDeleteBtn:(id)sender {
    if (self.deleteBlock) {
        self.deleteBlock(self.tag);
    }
}

@end
