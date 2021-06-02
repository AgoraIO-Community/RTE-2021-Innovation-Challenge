//
//  OnlineCollctionHeaderButtonsView.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/20.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "OnlineCollctionHeaderButtonsView.h"
#import "HeaderButtonsCell.h"
@interface OnlineCollctionHeaderButtonsView()<UICollectionViewDataSource,UICollectionViewDelegate>
{
    UICollectionView *_collectionView;
    UILabel          *_scrollLabel;
    NSArray *_imageArray;
}
@end
@implementation OnlineCollctionHeaderButtonsView


-(instancetype)init
{
    self = [super init];
    
    if (self) {
    
        _imageArray = [NSArray arrayWithObjects:@"onlineIcon1",
                       @"onlineIcon2",
                       @"onlineIcon3",
                       @"onlineIcon4",
                       @"onlineIcon5",
                       @"onlineIcon6",
                       @"onlineIcon7",
                       @"onlineIcon8",
                       @"onlineIcon9",
                       @"onlineIcon10",nil];
        
        self.frame = CGRectMake(0, 160, WIDTH, 170);
        
        UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
        float cellWidth = WIDTH/5 - 1;
        layout.itemSize = CGSizeMake(cellWidth,80);
        layout.minimumInteritemSpacing = 0;
        layout.minimumLineSpacing = 0;
        layout.sectionInset = UIEdgeInsetsMake(0, 0, 0, 0);
        layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
        _collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 160) collectionViewLayout:layout];
        _collectionView.backgroundColor = [UIColor whiteColor];
            [_collectionView registerClass:[HeaderButtonsCell class] forCellWithReuseIdentifier:@"HeaderButtonsCell"];
        _collectionView.delegate = self;
        _collectionView.dataSource = self;
        _collectionView.scrollsToTop = NO;
        _collectionView.showsVerticalScrollIndicator = NO;
        _collectionView.showsHorizontalScrollIndicator = NO;
        _collectionView.bounces = NO;
        [self addSubview:_collectionView];
        
        UILabel *lable = [[UILabel alloc] initWithFrame:CGRectMake(0, 160, WIDTH, 20)];
        
        lable.backgroundColor = [UIColor whiteColor];
        
        [self addSubview:lable];
        
        UILabel *Bglable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH * 0.45, 165, WIDTH * 0.1, 4)];
        
        Bglable.backgroundColor = [UIColor lightGrayColor];
        
        [self addSubview:Bglable];
        
        _scrollLabel = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH * 0.45, 165, WIDTH * 0.05, 4)];
        
        _scrollLabel.backgroundColor = [UIColor orangeColor];
        
        [self addSubview:_scrollLabel];
    }
    
    return self;
}


- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return _dataArray.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    HeaderButtonsCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"HeaderButtonsCell" forIndexPath:indexPath];
    
    ResouceClassModel *model = _dataArray[indexPath.item];

    cell.imageName = model.image;
    cell.title     = model.title;
    
    return cell;
}


-(void)setDataArray:(NSArray *)dataArray
{
    _dataArray = dataArray;
    
    [_collectionView reloadData];
    
}
- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    
    
    float c = scrollView.contentSize.width - scrollView.frame.size.width;
    float l = WIDTH *0.1 - WIDTH * 0.05;
    
    float cc = scrollView.contentOffset.x;
    
    float r = cc * l / c;
    
    
//    float bodong =  WIDTH*0.1;
    
    _scrollLabel.frame = CGRectMake(WIDTH * 0.45 + r, 165, WIDTH * 0.05, 4);
//    NSLog(@"%f",scrollView.contentOffset.x);
}
 
-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    [self.delegate HeaderButtonsClick:_dataArray[indexPath.item]];
}
 
 
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
