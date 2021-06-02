//
//  OnlineResouceCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/1.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "OnlineResouceCell.h"
#import "OnlineHotCell.h"
#import "LoadErrorCellView.h"
@interface OnlineResouceCell()<UICollectionViewDelegate,UICollectionViewDataSource>
{
    UICollectionView *_collectionView;
    UILabel          *_titleLabel;
    NSArray          *_titleArray;
    UIButton         *_button;
    UIButton         *_pageButton;
    LoadErrorCellView *_errorView;
} 
@end
@implementation OnlineResouceCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}




-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];

    if (self) {
        
        _titleArray = [NSArray arrayWithObjects:@"热门推荐",
                               @"电子图书",
                               @"有声图书",
                               @"",
                               @"国学分馆",
                               @"传世名曲",
                       @"",
                       @"",
                       @"",
                       @"",nil];
         
        UIImageView *imageV = [[UIImageView alloc] initWithFrame:CGRectMake(15, 6, 10, 28)];
        
        imageV.image = [UIImage imageNamed:@"shu"];
        
        [self.contentView addSubview:imageV];
        
        _titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(33, 5, WIDTH - 200, 30)];
        
        [self.contentView addSubview:_titleLabel];
        
        
        _button = [UIButton buttonWithType:UIButtonTypeCustom];
        
        _button.frame = CGRectMake(WIDTH - 120, 0, 120, 40);
        
        [_button setTitle:@"查看更多" forState: UIControlStateNormal];
        
        [_button setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
        
        [_button addTarget:self action:@selector(moreClick:) forControlEvents:UIControlEventTouchUpInside];
        [self.contentView addSubview:_button];
        
        
        UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
        float cellWidth = WIDTH /3 - 1;
        layout.itemSize = CGSizeMake(cellWidth,cellWidth * 1.3 );
        layout.minimumInteritemSpacing = 0;
        layout.minimumLineSpacing = 0;
        layout.sectionInset = UIEdgeInsetsMake(0, 0, 0, 0);
        
        _collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 40, WIDTH, (cellWidth * 1.3 )  + 10) collectionViewLayout:layout];
        _collectionView.backgroundColor = [UIColor whiteColor];
        [_collectionView registerClass:[OnlineHotCell class] forCellWithReuseIdentifier:@"OnlineHotCell"];
        _collectionView.delegate = self;
        _collectionView.dataSource = self;
        [self.contentView addSubview:_collectionView];
        
        [_collectionView registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header"];
        
        _collectionView.scrollEnabled = NO;
        
        _collectionView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"hot_recommended_list_bg"]];
        
        self.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"hot_recommended_list_bg"]];
        
       
        
        
        _pageButton = [UIButton buttonWithType:UIButtonTypeCustom];
        
        _pageButton.frame = CGRectMake(WIDTH/2 - 70, CGRectGetMaxY(_collectionView.frame), 140, 40);
        
        [_pageButton setImage:[UIImage imageNamed:@"pageButton"] forState:UIControlStateNormal];
        
        
        [_pageButton addTarget:self action:@selector(pageClick:) forControlEvents:UIControlEventTouchUpInside];
        
        [self.contentView addSubview:_pageButton];

        UILabel *lion = [[UILabel alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(_pageButton.frame) + 10, WIDTH, 10)];
        
        lion.backgroundColor = LIONCOLOR;
        
        [self.contentView addSubview:lion];
        
        _errorView = [[LoadErrorCellView alloc] initWithFrame:CGRectMake(0, 40, WIDTH, CGRectGetMaxY(lion.frame) - 40)];
        _errorView.hidden = YES;
        
        
        UITapGestureRecognizer *tapGesturRecognizer=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(errorViewTouchUpInsind:)];
        
        [_errorView addGestureRecognizer:tapGesturRecognizer];
        [self.contentView addSubview:_errorView];
    }
    return self;
}


-(void)errorViewTouchUpInsind:(id)tap
{
    [self.delegate resouceLoadErrorTouchUpInsind:_errorView.tag - 300];
}


-(void)pageClick:(UIButton *)button
{
    [self.delegate pageClickThing:button.tag - 100];
}

-(void)moreClick:(UIButton *)button
{
    [self.delegate moreClickThing:button.tag];
}

-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    MainCellListModel *model = _dataArray[indexPath.item];

    [self.delegate collectionViewDidSelectItemAtIndexPath:model withType:collectionView.tag - 200];
}

-(void)setIndex:(NSIndexPath *)index
{
    _titleLabel.text = _titleArray[index.row];
    _pageButton.tag = index.row + 100;
    _button.tag  = index.row;
    _errorView.tag = index.row + 300;
    _collectionView.tag = index.row + 200;
}


-(void)setDataArray:(NSArray *)dataArray
{
    _dataArray = dataArray;
    if (dataArray.count) {
        [_collectionView reloadData];
        _errorView.hidden = YES;
    }else{
        _errorView.hidden = NO;
    }
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
    
    NSString *dataType = [NSString stringWithFormat:@"%@",model.ResourceType];
    
    if ([dataType isEqualToString:@"0"]) {
        //音
        cell.typeImage.image = [UIImage imageNamed:@"mp3Type"];
    }else if ([dataType isEqualToString:@"1"]||[dataType isEqualToString:@"6"] || [dataType isEqualToString:@"10"]){
        //视
        cell.typeImage.image = [UIImage imageNamed:@"mp4Type"];
        
    }else{ 
        //web
        cell.typeImage.image = [UIImage imageNamed:@"webType"];
        
    }
    return cell;
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
