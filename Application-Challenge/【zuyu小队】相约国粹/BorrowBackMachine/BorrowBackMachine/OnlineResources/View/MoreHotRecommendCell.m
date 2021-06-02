//
//  MoreHotRecommendCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/8.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "MoreHotRecommendCell.h"
#import "OnlineHotCell.h"
@interface MoreHotRecommendCell()<UICollectionViewDelegate,UICollectionViewDataSource>
{
    UICollectionView *_collectionView;
    UILabel          *_titleLabel;
}
@end
@implementation MoreHotRecommendCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}
-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
        
        UIImageView *imageV = [[UIImageView alloc] initWithFrame:CGRectMake(15, 6, 10, 28)];
        
        imageV.image = [UIImage imageNamed:@"shu"];
        
        [self addSubview:imageV];
        
        _titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(33, 5, WIDTH - 200, 30)];
        
        [self addSubview:_titleLabel];
    
        
        UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
        float cellWidth = WIDTH /3 - 1;
        layout.itemSize = CGSizeMake(cellWidth,cellWidth * 1.3 );
        layout.minimumInteritemSpacing = 0;
        layout.minimumLineSpacing = 0;
        layout.sectionInset = UIEdgeInsetsMake(0, 0, 0, 0);
        
        _collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 40, WIDTH, (cellWidth * 1.3 ) * 2 + 10) collectionViewLayout:layout];
        _collectionView.backgroundColor = [UIColor whiteColor];
        [_collectionView registerClass:[OnlineHotCell class] forCellWithReuseIdentifier:@"OnlineHotCell"];
        _collectionView.delegate = self;
        _collectionView.dataSource = self;
        [self addSubview:_collectionView];
        
        [_collectionView registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header"];
        
        _collectionView.scrollEnabled = NO;
        
        _collectionView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"hot_recommended_list_bg"]];
        
        self.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"hot_recommended_list_bg"]];
        
        
        UILabel *lion = [[UILabel alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(_collectionView.frame) + 10, WIDTH, 10)];
        
        lion.backgroundColor = LIONCOLOR;
        
        [self addSubview:lion];
    }
    return self;
}


-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    MainCellListModel *model = _dataArray[indexPath.item];
    
    [self.delegate collectionViewDidSelectItemAtIndexPath:model withType:collectionView.tag - 200];
}

-(void)setIndex:(NSIndexPath *)index
{
    _collectionView.tag = index.row + 200;
}


-(void)setDataArray:(NSArray *)dataArray
{
    _dataArray = dataArray;
    [_collectionView reloadData];
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return _dataArray.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    OnlineHotCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"OnlineHotCell" forIndexPath:indexPath];
    
    MainCellListModel *model = _dataArray[indexPath.item];
    
    cell.bookImage = model.imageName;
    cell.bookNameStr = model.name;
    cell.writerNameStr = model.author;
    
    return cell;
}

-(void)setTitle:(NSString *)title
{
    _titleLabel.text = title;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
