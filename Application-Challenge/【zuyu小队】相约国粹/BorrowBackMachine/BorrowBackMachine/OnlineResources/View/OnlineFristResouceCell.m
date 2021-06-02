//
//  OnlineFristResouceCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/7.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "OnlineFristResouceCell.h"
#import "OnlineFristCollectionCell.h"
#import "LoadErrorCellView.h"
@interface OnlineFristResouceCell()<UICollectionViewDelegate,UICollectionViewDataSource>
{
    UICollectionView *_collectionView;
    UIButton         *_button;
    UILabel          *_titleLabel;
    NSInteger        _page;
    float            _contentX;
    LoadErrorCellView *_errorView;
    


}
@end
@implementation OnlineFristResouceCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
        
        _page = 0;
        _contentX = 0.0;
        
        UIImageView *imageV = [[UIImageView alloc] initWithFrame:CGRectMake(15, 6, 10, 28)];
        
        imageV.image = [UIImage imageNamed:@"shu"];
        
        [self.contentView addSubview:imageV];
        
        _titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(33, 5, WIDTH - 200, 30)];
        
        [self.contentView addSubview:_titleLabel];
        
        _titleLabel.text = @"热门推荐";
        
        _button = [UIButton buttonWithType:UIButtonTypeCustom];
        
        _button.frame = CGRectMake(WIDTH - 120, 0, 120, 40);
        
        [_button setTitle:@"查看更多" forState: UIControlStateNormal];
        
        [_button setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
        
        [_button addTarget:self action:@selector(moreClick:) forControlEvents:UIControlEventTouchUpInside];
        [self.contentView addSubview:_button];
        
        
        UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
        float cellWidth = WIDTH *0.9;
        layout.itemSize = CGSizeMake(cellWidth,160);
        layout.minimumInteritemSpacing = 0;
        layout.minimumLineSpacing = 0;
        layout.sectionInset = UIEdgeInsetsMake(0, 0, 0, 0);
        layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;

        _collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 40, WIDTH, 165) collectionViewLayout:layout];
        _collectionView.backgroundColor = [UIColor whiteColor];
        [_collectionView registerClass:[OnlineFristCollectionCell class] forCellWithReuseIdentifier:@"OnlineFristCollectionCell"];
        _collectionView.delegate = self;
        _collectionView.dataSource = self;
        
        _collectionView.bounces = NO;
        [self.contentView addSubview:_collectionView];
        _collectionView.showsVerticalScrollIndicator = FALSE;
        
        _collectionView.showsHorizontalScrollIndicator = FALSE;
        
        _collectionView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"hot_recommended_list_bg"]];
        
        self.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"hot_recommended_list_bg"]];
        
        _pageControl = [[UIPageControl alloc] initWithFrame:CGRectMake(-33 ,205,WIDTH,20)];
        
        
        
        //选中颜色
        _pageControl.currentPageIndicatorTintColor = [UIColor redColor];
        //默认颜色
        _pageControl.pageIndicatorTintColor = [UIColor blueColor];

//        [_pageControl setValue:[UIImage imageNamed:@"pagecontroller1"] forKeyPath:@"_pageImage"];
//
//        [_pageControl setValue:[UIImage imageNamed:@"pagecontroller2"] forKeyPath:@"_currentPageImage"];

        
        _pageControl.numberOfPages = 4;
//        _pageControl.currentPage = 4;
        
        [self.contentView addSubview:_pageControl];
        
        
        
        _errorView = [[LoadErrorCellView alloc] initWithFrame:CGRectMake(0, 40, WIDTH, CGRectGetMaxY(_pageControl.frame) - 40)];
        _errorView.hidden = YES;
        
        
        UITapGestureRecognizer *tapGesturRecognizer=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(errorViewTouchUpInsind:)];
        
        [_errorView addGestureRecognizer:tapGesturRecognizer];
        [self.contentView addSubview:_errorView];
        

    }
    
    return self;
}


-(void)errorViewTouchUpInsind:(id)tap
{
    [self.delegate fristCellLoadResouceError:0];
}


 - (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    
    float width = WIDTH * 0.8;
    
    if (scrollView.contentOffset.x > width*3) {
        _pageControl.currentPage = 3;
    }else if (scrollView.contentOffset.x > width*2) {
        _pageControl.currentPage = 2;
    }else if (scrollView.contentOffset.x > width) {
        _pageControl.currentPage = 1;
    }else{
        _pageControl.currentPage = 0;
    }
    
}


-(void)moreClick:(UIButton *)button
{
    [self.delegate hotRecommendMoreClick];
}


-(void)setDataArray:(NSArray *)dataArray
{
    _dataArray = dataArray;
    if (dataArray.count) {
        _errorView.hidden = YES;
        [_collectionView reloadData];
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
    OnlineFristCollectionCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"OnlineFristCollectionCell" forIndexPath:indexPath];
    
    MainCellListModel *model = _dataArray[indexPath.item];

    cell.bookImage = model.imageName;
    cell.bookNameStr = model.name;
    cell.writerNameStr = model.author;
    cell.summerStr   = model.Summary;
    
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


#pragma mark - UIScrollViewDelegate 方法
- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate{
    
    if (_contentX < scrollView.contentOffset.x) {
        _page = _page + 1;
    }else if (_contentX > scrollView.contentOffset.x){
        _page = _page - 1;
    }

    if (_page<0) {
        _page = 0;
    }else if (_page > 3){
        _page = 3;
    }

    [self layoutIfNeeded];
    [_collectionView scrollToItemAtIndexPath:[NSIndexPath indexPathForRow:_page inSection:0] atScrollPosition:UICollectionViewScrollPositionCenteredHorizontally animated:YES];

    _contentX =  WIDTH * 0.9 * _page;
    
}



-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    MainCellListModel *model = _dataArray[indexPath.item];

    [self.delegate hotRecommendCollectionDidSelect:model];
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
