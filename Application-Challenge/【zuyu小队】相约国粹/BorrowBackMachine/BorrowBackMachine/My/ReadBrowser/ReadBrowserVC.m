//
//  ReadBrowserVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/18.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "ReadBrowserVC.h"
#import "zuyu.h"
#import "ReadBrowserCell.h"
#import "ReadBrowserModel.h"
#import "TypeSrerchCell.h"
#import "ClassMainViewController.h"
#import "ZFDownloadManager.h"
#import "TypeSrerchViewController.h"
#import "DownloadCell.h"

@interface ReadBrowserVC ()<NavgationViewDelegate,UITableViewDelegate,UITableViewDataSource,UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout>
{
    UITableView *_tableView;
    NSArray     *_imageArray;
    NSMutableArray *_conutArr;
    NSArray     *_titleArray;

    UICollectionView *_collectionView;
    
    NSMutableArray *_dataImageArray;
    NSMutableArray *_dataTitleArray;
    
    NSInteger _isPush;

    
}
@end

@implementation ReadBrowserVC

- (void)viewDidLoad {
    [super viewDidLoad];
    _isPush = 0;
    
    [self initArray];
    [self getConutOfClass];
    [self createNavgation];
    [self createCollection];
//    [self createTableView];

    // Do any additional setup after loading the view.
}

-(void)initArray
{
    NSArray *image1 = [NSArray arrayWithObjects:@"downloadDJ1",
                       @"downloadDJ2",
                       @"downloadDJ3",nil];
    
    NSArray *image2 = [NSArray arrayWithObjects:@"downloadDZ1",
                       @"downloadDZ2",
                       @"downloadDZ3",
                       @"downloadDZ4",
                       @"downloadDZ5",
                       @"downloadDZ6",
                       @"downloadDZ7",
                       @"downloadDZ10",
                       @"downloadDZ11",
                       @"downloadDZ12",nil];
    
    
    _imageArray = [NSMutableArray arrayWithObjects:image1
                   ,image2, nil];
    
    
    
    NSArray *title1 = [NSArray arrayWithObjects:@"党建课程",
                       @"党课收听",
                       @"党建文库",nil];
    
    
    NSArray *title2 = [NSArray arrayWithObjects:@"有声图书",
                       @"国学分馆",
                       @"书法分馆",
                       @"中华戏曲",
                       @"传世名曲",
                       @"绘画分馆",
                       @"电子图书",
                       @"名师教学",
                       @"种植养殖",
                       @"技工培训",nil];
    
    
    _titleArray = [NSMutableArray arrayWithObjects:title1,title2, nil];
    
    
    
}

#pragma mark - collection
-(void)createCollection
{
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    float cellWidth = WIDTH /4 - 0.1;
    layout.itemSize = CGSizeMake(cellWidth,cellWidth);
    layout.minimumInteritemSpacing = 0;
    layout.minimumLineSpacing = 0;
    layout.sectionInset = UIEdgeInsetsMake(0, 0, 0, 0);
    
    _collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 74) collectionViewLayout:layout];
    _collectionView.backgroundColor = [UIColor whiteColor];
    [_collectionView registerClass:[DownloadCell class] forCellWithReuseIdentifier:@"DownloadCell"];
    _collectionView.delegate = self;
    _collectionView.dataSource = self;
    [self.view addSubview:_collectionView];
    
    [_collectionView registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header"];
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 2;
}
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return [(NSMutableArray *)_imageArray[section] count];
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    DownloadCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"DownloadCell" forIndexPath:indexPath];

    cell.image = _imageArray[indexPath.section][indexPath.item];
    cell.title = _titleArray[indexPath.section][indexPath.item];
    cell.count = _conutArr[indexPath.section][indexPath.item];
    
    return cell;
}
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout referenceSizeForHeaderInSection:(NSInteger)section {
    return CGSizeMake(WIDTH, 40);
}

- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
{
    
    
    UICollectionReusableView *header = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header" forIndexPath:indexPath];
    
    header.backgroundColor = RGBA(235, 235, 235, 1);
    
    if (!_isPush) {
        UIImageView *image = [[UIImageView alloc] initWithFrame:CGRectMake(10, 5, 30, 30)];
        
        UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(45, 0, WIDTH - 60, 40)];
        
        [header addSubview:label];
        
        if (!indexPath.section) {
            image.image = [UIImage imageNamed:@"downloadHeaderDJ"];
            label.text  = @"智慧党建";
            
        }else{
            image.image = [UIImage imageNamed:@"downloadHeaderZZ"];
            label.text = @"电子图书";
        }
        [header addSubview:image];
    }
    
    
    return header;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(nonnull NSIndexPath *)indexPath
{
    _isPush = 1;
    
    ClassMainViewController *vc = [[ClassMainViewController alloc] init];
    
    NSArray *classIDDJArray = [NSArray arrayWithObjects:@"86",
                               @"80",
                               @"79",nil];
    
    NSArray *classIDarray = [NSArray arrayWithObjects:@"55",@"88",@"33",@"44",@"66",@"11",@"22",@"3",@"4",@"5",nil];
    
    NSArray *classIDArray = [NSArray arrayWithObjects:classIDDJArray,classIDarray, nil];
    
   
    vc.titles = _titleArray[indexPath.section][indexPath.item];
    
    vc.classID = classIDArray[indexPath.section][indexPath.item];
    
    vc.isLocal = YES;
    
    [vc setHidesBottomBarWhenPushed:YES];

    [self.navigationController pushViewController:vc animated:YES];
}


- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
//    if (!section ) {
        return UIEdgeInsetsMake(0, WIDTH/24, 0, WIDTH/24);
//    }
//    return UIEdgeInsetsMake(0, 0, 0, 0);
}
#pragma mark - initArray

//-(void)initArray
//{
//    _imageArray = [NSArray arrayWithObjects:
//                   @"loactionType1",
//                   @"loactionType2",
//                   @"loactionType3",
//                   @"loactionType4",
//                   @"loactionType5",
//                   @"loactionType6",
//                   @"loactionType7",
//                   @"loactionType8",nil];
//
//    _conutArr = [NSMutableArray array];
//
//    _titleArray = [NSArray arrayWithObjects:@"电子图书",
//                   @"有声图书",
//                   @"书法分馆",
//                   @"绘画分馆",
//                   @"国学分馆",
//                   @"传世名曲",
//                   @"中华戏曲",
//                   @"数字报纸",nil];
//}

#pragma mark - navgation
-(void)createNavgation
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = NO;
    view.scanHidden = YES;
    view.searchBtnHidden = YES;
    view.createCodeHidden = NO;
    view.titleLableHidden = NO;
    view.rightItmeImage = @"searchForNav";
    view.titleStr = @"我的下载";
    [self.view addSubview:view];
}


-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}

//搜索
-(void)navCreateCode
{
    TypeSrerchViewController *vc = [[TypeSrerchViewController alloc] init];
    [vc setHidesBottomBarWhenPushed:YES];
    [self.navigationController pushViewController:vc animated:YES];
    NSLog(@"search");
}

-(void)searchClick:(UIButton *)button
{
//    TypeSrerchViewController *vc = [[TypeSrerchViewController alloc] init];
//
//    [self.navigationController pushViewController:vc animated:YES];
}



#pragma mark - 拿到每个类里有多少本书.
-(void)getConutOfClass
{
    
    _conutArr = [NSMutableArray array];
    
    NSMutableArray *finishArr = [[NSMutableArray alloc] initWithContentsOfFile:PLIST_PATH];
    
    NSArray *classIDDJArray = [NSArray arrayWithObjects:@"86",
                               @"80",
                               @"79",nil];
    
    
    NSMutableArray *arr1 = [NSMutableArray array];
    NSMutableArray *arr2 = [NSMutableArray array];

    
    for (NSString *classID in classIDDJArray) {
        
        NSMutableArray *bookNameArr = [NSMutableArray array];
        
        for (NSDictionary *dict in finishArr) {
            
            if ([[dict objectForKey:@"ClassID"] isEqualToString:classID]) {
                
                [bookNameArr addObject:[dict objectForKey:@"bookName"]];
            }
        }
        
        NSMutableArray *dataArray = [NSMutableArray array];
        
        for (NSString *str in bookNameArr) {
            if (![dataArray containsObject:str]) {
                [dataArray addObject:str];
            }
        }
        
        
        [arr1 addObject:[NSString stringWithFormat:@"%ld",dataArray.count]];
    }
    
    NSArray *classIDarray = [NSArray arrayWithObjects:@"55",@"88",@"33",@"44",@"66",@"11",@"22",
                             @"1",@"2",@"3",@"4",@"5",nil];
    //
    
    for (NSString *classID in classIDarray) {
        
        NSMutableArray *bookNameArr = [NSMutableArray array];
        
        for (NSDictionary *dict in finishArr) {
            
            if ([[dict objectForKey:@"ClassID"] isEqualToString:classID]) {
                
                [bookNameArr addObject:[dict objectForKey:@"bookName"]];
                
            }
            
        }
        
        NSMutableArray *dataArray = [NSMutableArray array];
        
        for (NSString *str in bookNameArr) {
            if (![dataArray containsObject:str]) {
                [dataArray addObject:str];
            }
        }
        
        [arr2 addObject:[NSString stringWithFormat:@"%ld",dataArray.count]];
    }
    
    [_conutArr addObject:arr1];
    [_conutArr addObject:arr2];
    
    [_collectionView reloadData];
    
}
#pragma mark - nav 处理.
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    if (_isPush) {
        [self getConutOfClass];
        [_collectionView reloadData];
    }
    [self.navigationController setNavigationBarHidden:YES animated:NO];
}


- (void) viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:NO];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
