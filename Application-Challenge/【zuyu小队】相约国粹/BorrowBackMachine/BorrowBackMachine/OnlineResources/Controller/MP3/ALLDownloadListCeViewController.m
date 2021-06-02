//
//  ALLDownloadListCeViewController.m
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/8/31.
//  Copyright © 2016年 zuyu. All rights reserved.
//
#define WIDTH ([UIScreen mainScreen].bounds.size.width)
#define HEIGHT ([UIScreen mainScreen].bounds.size.height)

#import "ALLDownloadListCeViewController.h"
//#import "DownLoadCeViewController.h"
#import "UIImageView+WebCache.h"
//#import "DownLodeXCeTableViewController.h"
#import "ZFListViewController.h"
#import "ZFDownloadManager.h"
#import "ZFDownloadViewController.h"
#import "AllDownloadingViewController.h"
#import "DetMainSocendTableViewCell.h"
//#import "DownLoadMp3ViewController.h"

@interface ALLDownloadListCeViewController ()<UITableViewDelegate,UITableViewDataSource>
{
    UITableView *_tableView;
    
    UITableView *_deleteTableView;
    
    NSMutableArray *_bookNameArray;
    
    NSMutableArray *_dontRepectBookNameArray;
    
    NSMutableArray *_categoryArray ;
    
    NSMutableArray *_imageUrlArray ;
    
    NSMutableArray *_dontRepectImageUrlArray;
    
    NSMutableArray *_indexArray;
    
    NSString *_isAllSelected;
    
    NSMutableArray *_indexDataArray;
    
    UIButton *_allXuanButton;
    
    UIButton *_deleteButton;
    
    NSMutableArray *_downloadedCountArray;
  
    NSMutableArray *_allCountArray;

}
@property (nonatomic, strong) ZFDownloadManager *downloadManage;

@end

@implementation ALLDownloadListCeViewController
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    _categoryArray = [NSMutableArray array];
    
    _downloadedCountArray = [NSMutableArray array];
    
    _allCountArray = [NSMutableArray array];
    
    [self requestDataForPlist];
    
    [_tableView reloadData];
    
    _indexArray = [NSMutableArray
                   array];
    
    _indexDataArray = [NSMutableArray
                       array];
    
    _isAllSelected = @"0";
    
    self.navigationController.navigationBar.hidden = NO;

    
}


-(void)nodata
{
    UIImageView *imageV = [[UIImageView alloc] initWithFrame:self.view.frame];
    
    imageV.image = [UIImage imageNamed:@"isnot_content_bookinfo"];
    
    
    [self.view addSubview:imageV];
}



- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    _bookNameArray = [NSMutableArray array];
    
    _dontRepectBookNameArray = [NSMutableArray array];
    
    _imageUrlArray = [NSMutableArray array];
    
    _dontRepectImageUrlArray = [NSMutableArray array];
    
    
    [self requestDataForPlist];
    
  
    
    // Do any additional setup after loading the view.
}


#pragma mark - createRigthDeleteBtn And detele Data
-(void)createRightDeleteButton
{
    
    UIBarButtonItem *barItem1 = [[UIBarButtonItem alloc]
                                 
                                 initWithImage:[[UIImage imageNamed:@"delete_all_foueror"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]
                                 
                                 style:UIBarButtonItemStylePlain
                                 
                                 target:self
                                 
                                 action:@selector(deleteDataYa:)];
    
    
    NSArray *arr1=[[NSArray alloc]initWithObjects:barItem1, nil];
    
    self.navigationItem.rightBarButtonItems = arr1;
}




-(void)deleteDataYa:(UIBarButtonItem *)baritem
{
    
    
    if (_allXuanButton.hidden) {
        
        _tableView.hidden = YES;
        _deleteTableView.hidden = NO;
        _allXuanButton.hidden = NO;
        _deleteButton.hidden = NO;
        baritem.image = [UIImage imageNamed:@"delete_all_ok"] ;

        
    }else{
        
        
        _tableView.hidden = NO;
        _deleteTableView.hidden = YES;
        _allXuanButton.hidden = YES;
        _deleteButton.hidden = YES;
        baritem.image = [UIImage imageNamed:@"delete_all_foueror"] ;

        
    }
    [_deleteTableView reloadData];

    NSLog(@"get into deleteDate state");
}




#pragma mark - create deleteDate state TableView
-(void)createdeleteTableViewWithAllXuanAndDeleteButton
{

    _deleteTableView = [[UITableView alloc ] initWithFrame:CGRectMake(0, 64 + 50, WIDTH, HEIGHT - 100 - 64) style:UITableViewStylePlain];
    
    _deleteTableView.delegate = self;
    
    _deleteTableView.dataSource  = self;
    
    _deleteTableView.hidden = YES;
    
    _deleteTableView.rowHeight = 100;
    
    [self.view addSubview:_deleteTableView];
    
    _allXuanButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    _allXuanButton.frame = CGRectMake(0, HEIGHT - 60 ,WIDTH/2,60);
    
    [_allXuanButton addTarget:self action:@selector(Allxuan:) forControlEvents:UIControlEventTouchUpInside];
    
    _allXuanButton.backgroundColor = [UIColor whiteColor];
    
    _allXuanButton.hidden = YES;
    
    UIImageView *allImage = [[UIImageView alloc] initWithFrame:CGRectMake(15, 10, WIDTH/2 - 30, 40)];
    
    allImage.image  = [UIImage imageNamed:@"my_down_check_all"];
    
    [_allXuanButton addSubview:allImage];
    
    [self.view addSubview:_allXuanButton];
    
    _deleteButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    _deleteButton.frame = CGRectMake(WIDTH/2, HEIGHT - 60 ,WIDTH/2,60);
    
    [_deleteButton addTarget:self action:@selector(deleteData:) forControlEvents:UIControlEventTouchUpInside];
    
    _deleteButton.hidden = YES;
    
    _deleteButton.backgroundColor = [UIColor whiteColor];
    
    UIImageView *deleteImage = [[UIImageView alloc] initWithFrame:CGRectMake(15, 10, WIDTH/2 - 30, 40)];
    
    deleteImage.image  = [UIImage imageNamed:@"my_down_delete_all"];
    
    [_deleteButton addSubview:deleteImage];
    
    [self.view addSubview:_deleteButton];
    
}

#pragma mark - AllXuanAndDelete - selector



-(void)Allxuan:(UIButton *)button
{
    
    if ([_isAllSelected isEqualToString:@"0"]) {
        
        _isAllSelected = @"1";
        
        
        
        for (int i = 0; i < _categoryArray.count; i++) {
            
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:i inSection:0];
            [_deleteTableView selectRowAtIndexPath:indexPath animated:YES scrollPosition:UITableViewScrollPositionTop];
            
            if ([_indexArray containsObject:indexPath] == NO) {
                
                [_indexArray addObject:indexPath];
                
            }
            if ([_indexDataArray containsObject:_categoryArray[indexPath.row]] == NO) {
                
                [_indexDataArray addObject:_categoryArray[indexPath.row]];
                
            }
            //            _indexDataArray
            
        }
    } else {
        
        _isAllSelected = @"0";
        
        
        
        [_indexArray removeAllObjects];
        
        [_indexDataArray removeAllObjects];
        
        for (int i = 0; i < _categoryArray.count; i++) {
            
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:i inSection:0];
            
            [_deleteTableView deselectRowAtIndexPath:indexPath animated:YES];
        }
    }
    
    NSLog(@"%@",_indexDataArray);
    
    [_deleteTableView reloadData];
    
}


-(void)deleteData:(UIButton *)button
{
//    
    for (NSString *string in _indexDataArray) {
        
        NSMutableArray *downladed = self.downloadManage.finishedlist;
        NSMutableArray *downloading = self.downloadManage.downinglist;
        
        NSMutableArray *downladedClass = @[].mutableCopy;
        
        for ( ZFFileModel *fileInfo in downladed) {
            
            if ([fileInfo.bookName isEqualToString:string] ) {
                [downladedClass addObject:fileInfo];
            }
            //
        }
        
        for (ZFFileModel *fileInfo in downladedClass) {
            [self.downloadManage deleteFinishFile:fileInfo];
            [self deletePlistWithName:[self  decodeString: fileInfo.fileName ]];
        }
        
        
        NSMutableArray *downloadingClass = @[].mutableCopy;
        
        
        for (ZFHttpRequest *request in downloading) {
            
            ZFFileModel *fileInfo = [request.userInfo objectForKey:@"File"];
            
            if ([fileInfo.bookName isEqualToString:string]) {
                [downloadingClass addObject:request];
            }
        }
        
         
        for (ZFHttpRequest *request in  downloadingClass) {
            
            [self.downloadManage deleteRequest:request];
            
            ZFFileModel *fileInfo = [request.userInfo objectForKey:@"File"];
            
            [self deletePlistWithName:[self decodeString: fileInfo.fileName ]];
        }
        
        [_categoryArray removeObject:string];
        
        
    }
     
    [_indexArray removeAllObjects];
    
    
    [_deleteTableView reloadData];
    
    [_tableView reloadData];
    
}



#pragma mark - decodeURI
-(NSString *)decodeString:(NSString*)encodedString

{
    //NSString *decodedString = [encodedString stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding ];
    
    NSString *decodedString  = (__bridge_transfer NSString *)CFURLCreateStringByReplacingPercentEscapesUsingEncoding(NULL,
                                                                                                                     (__bridge CFStringRef)encodedString,
                                                                                                                     CFSTR(""),
                                                                                                                     CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding));
    return decodedString;
}



#pragma mark - 未下载已下载按钮创建.

-(void)createButton
{
    CGSize sizes = [UIScreen mainScreen].bounds.size;
    
    UIButton *but1  = [UIButton  buttonWithType:UIButtonTypeCustom];
    
    but1.backgroundColor = [UIColor whiteColor];
    
    but1.frame = CGRectMake(0, 64, sizes.width/2, 50);
    
    but1.backgroundColor = [UIColor whiteColor];
    //    [but1 setTitle:@"已下载" forState:UIControlStateNormal];
    
    [but1 setImage:[UIImage imageNamed:@"select_down_ok"] forState:UIControlStateNormal];
    
    [but1 setTitleColor:[UIColor orangeColor] forState:UIControlStateNormal];
    
    [self.view addSubview:but1];
    
    UIButton *but2  = [UIButton  buttonWithType:UIButtonTypeCustom];
    
    but2.frame = CGRectMake(sizes.width/2, 64, sizes.width/2, 50);
    //
    but2.backgroundColor = [UIColor whiteColor];

    //    [but2 setTitle:@"正在下载" forState:UIControlStateNormal];
    
    but2.backgroundColor = [UIColor whiteColor];

    [but2 setImage:[UIImage imageNamed:@"downloading"] forState:UIControlStateNormal];
    
    [but2 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    
    [but2 addTarget:self action:@selector(pushViewController:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:but2];
    
}

-(void)pushViewController:(UIButton *)button
{
    
    AllDownloadingViewController *vc = [[AllDownloadingViewController alloc] init];
    
    [vc setHidesBottomBarWhenPushed:YES];
    
    [self.navigationController pushViewController:vc animated:NO];

}
- (ZFDownloadManager *)downloadManage
{
    if (!_downloadManage) {
        _downloadManage = [ZFDownloadManager sharedDownloadManager];
    }
    return _downloadManage;
}
-(void)initData:(NSInteger )indexrow
{
    NSMutableArray *downladed = self.downloadManage.finishedlist;
    NSMutableArray *downloading = self.downloadManage.downinglist;
    
    NSMutableArray *downladedClass = @[].mutableCopy;
    
    for ( ZFFileModel *fileInfo in downladed) {
        
        if ([fileInfo.bookName isEqualToString:_categoryArray[indexrow]]) {
            [downladedClass addObject:fileInfo];
        }
        
        
    }
    
    
    for (ZFHttpRequest *request in downloading) {
        
        ZFFileModel *fileInfo = [request.userInfo objectForKey:@"File"];
        
        if ([fileInfo.bookName isEqualToString:_categoryArray[indexrow]]) {
            //            NSLog(@"fileInfo.bookName = %@",fileInfo.bookName);
            
            [downladedClass addObject:fileInfo];
            
            
        }
        
        
    }
    
    
    
}


-(void)createTableView
{
    _tableView = [[UITableView alloc ] initWithFrame:CGRectMake(0, 50 + 64, WIDTH, HEIGHT - 50 - 64) style:UITableViewStylePlain];
    
    _tableView.delegate = self;
    
    _tableView.dataSource  = self;
    
    _tableView.rowHeight = 100;
    
    [self.view addSubview:_tableView];
    
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
        return _categoryArray.count;
  
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    return nil;
    
}


-(UIButton *)createCelldownLoadLgButton:(NSInteger )buttonTag
{
    UIButton *listionButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    listionButton.tag = buttonTag;
    
    listionButton.frame =  CGRectMake(self.view.frame.size.width/2 + 120 , 65/2, 65, 35);
    
    if (WIDTH == 320) {
        listionButton.frame =  CGRectMake(WIDTH/2 + 65 , 65/2, 65, 35);
        
    }
    
    [listionButton setImage:[UIImage imageNamed:@"img_indownfile"] forState:UIControlStateNormal];
    
    [listionButton addTarget:self action:@selector(downLoadLgButtonClick:) forControlEvents:UIControlEventTouchUpInside];
    
    return listionButton;
}

-(UIButton *)createCellmp3Button:(NSInteger )buttonTag
{
    UIButton *listionButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    listionButton.tag = buttonTag;

    listionButton.frame =  CGRectMake(self.view.frame.size.width/2 + 120 , 65/2, 65, 35);
    
    if (WIDTH == 320) {
        listionButton.frame =  CGRectMake(WIDTH/2 + 65 , 65/2, 65, 35);
        
    }
    
    [listionButton setImage:[UIImage imageNamed:@"img_mp3_seeing"] forState:UIControlStateNormal];
    
    [listionButton addTarget:self action:@selector(mp3ButtonClick:) forControlEvents:UIControlEventTouchUpInside];
  
    return listionButton;
    
}

-(UIButton *)createCellmp4Button:(NSInteger )buttonTag
{
    UIButton *listionButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    listionButton.tag = buttonTag;

    listionButton.frame =  CGRectMake(self.view.frame.size.width/2 + 120 , 65/2, 65, 35);
    
    if (WIDTH == 320) {
     
        listionButton.frame =  CGRectMake(WIDTH/2 + 65 , 65/2, 65, 35);
        
    }
    
    [listionButton setImage:[UIImage imageNamed:@"img_mp4_seeing"] forState:UIControlStateNormal];
    
    [listionButton addTarget:self action:@selector(mp4ButtonClick:) forControlEvents:UIControlEventTouchUpInside];

    return listionButton;
    
}

-(void)mp3ButtonClick:(UIButton *)button
{
    
    
    
    NSMutableArray *urlArr = @[].mutableCopy;
    NSMutableArray *nameArr = @[].mutableCopy;
    
    NSMutableArray *downladed = self.downloadManage.finishedlist;
    NSMutableArray *downladedClass = @[].mutableCopy;
    
    for ( ZFFileModel *fileInfo in downladed) {
        
        if ([fileInfo.bookName isEqualToString:[NSString stringWithFormat:@"%@",_categoryArray[button.tag]]] ) {
            [downladedClass addObject:fileInfo];
        }
        //
    }
    
    
    
    for (ZFFileModel *fileInfo in downladedClass) {
        
        [urlArr addObject:FILE_PATH(fileInfo.fileName)];
        
        [nameArr addObject:fileInfo.fileName];
    }
    
    
    
    
}
-(void)mp4ButtonClick:(UIButton *)button
{
    
    
    
    NSMutableArray *urlArr = @[].mutableCopy;
    NSMutableArray *nameArr = @[].mutableCopy;
    
    NSMutableArray *downladed = self.downloadManage.finishedlist;
    NSMutableArray *downladedClass = @[].mutableCopy;
    
    for ( ZFFileModel *fileInfo in downladed) {
        
        if ([fileInfo.bookName isEqualToString:[NSString stringWithFormat:@"%@",_categoryArray[button.tag]]] ) {
            [downladedClass addObject:fileInfo];
        }
        //
    }
    
    
    
    for (ZFFileModel *fileInfo in downladedClass) {
        
        [urlArr addObject:FILE_PATH(fileInfo.fileName)];
        
        [nameArr addObject:fileInfo.fileName];
    }
//    
//    DownLoadMp3ViewController *vc = [[DownLoadMp3ViewController alloc] init];
//    
//    vc.dataArray = urlArr;
//    
//    vc.nameArray = nameArr;
//    
//    vc.chooseID = 0;
    
//    [self.navigationController pushViewController:vc animated:YES];
}



-(void)downLoadLgButtonClick:(UIButton *)button
{
    
    
    
    
    
    
    
    
    
    ZFDownloadViewController *vc = [[ZFDownloadViewController alloc] init];
    
    [self.navigationController pushViewController:vc animated:YES];
}






-(void)requestDataForPlist
{
    
    
    NSArray *patharray = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    
    NSString *path =  [patharray objectAtIndex:0];
    
    NSString *filepath=[path stringByAppendingPathComponent:@"downtest4.plist"];
    
    NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithContentsOfFile:filepath];
    
    for (NSString *key in dic) {
        
        
        if ([key isEqualToString:@"Xuan"] == YES) {
            
            
            
        }else{
            
            
                [_bookNameArray addObject:[NSString stringWithFormat:@"%@",[dic objectForKey:key][2]]];
                
            
            
            for (unsigned i = 0; i < [_bookNameArray count]; i++){
                
                if ([_categoryArray containsObject:[_bookNameArray objectAtIndex:i]] == NO){
                    
                    [_categoryArray addObject:[_bookNameArray objectAtIndex:i]];
                    
                    
                }
            }
            
        }//else end
        
        
    }
    
    if (_categoryArray.count) {
        
        [self requestConut];

        
        [self UIBarButtonBackItemSet];
        
        [self createButton];
        
        [self createRightDeleteButton];
        
        [self createdeleteTableViewWithAllXuanAndDeleteButton];
    }else{
        [self nodata];
    }
    
    
    [_tableView reloadData];
    
}



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    if (tableView == _tableView) {
        
        [self initData:indexPath.row];
        
        ZFDownloadViewController *vc = [[ZFDownloadViewController alloc] init];
        
        
        [self.navigationController pushViewController:vc animated:YES];
        
    }else{
    
        if ([_indexArray containsObject:indexPath]) {
            
            [_indexArray removeObject:indexPath];
            
        }else{
            
            [_indexArray addObject:indexPath];
            
        }
        
        if ([_indexDataArray containsObject:_categoryArray[indexPath.row]]) {
            
            [_indexDataArray removeObject:_categoryArray[indexPath.row]];
            
        }else{
            
            [_indexDataArray addObject:_categoryArray[indexPath.row]];
            
        }
        
        [_deleteTableView reloadData];
    }
    //
    
}


#pragma mark - 删除

- (NSString *)tableView:(UITableView *)tableView titleForDeleteConfirmationButtonForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return @"删除";
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}

-(void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    NSString *string = _categoryArray[indexPath.row];
    
    NSMutableArray *downladed = self.downloadManage.finishedlist;
    NSMutableArray *downloading = self.downloadManage.downinglist;
    
    NSMutableArray *downladedClass = @[].mutableCopy;
    
    for ( ZFFileModel *fileInfo in downladed) {
        
        if ([fileInfo.bookName isEqualToString:string] ) {
            [downladedClass addObject:fileInfo];
        }
        //
    }
    
    for (ZFFileModel *fileInfo in downladedClass) {
        [self.downloadManage deleteFinishFile:fileInfo];
        [self deletePlistWithName:[self  decodeString: fileInfo.fileName ]];
    }
    
    
    NSMutableArray *downloadingClass = @[].mutableCopy;
    
    
    for (ZFHttpRequest *request in downloading) {
        
        ZFFileModel *fileInfo = [request.userInfo objectForKey:@"File"];
        
        if ([fileInfo.bookName isEqualToString:string]) {
            [downloadingClass addObject:request];
        }
    }
    
    for (ZFHttpRequest *request in  downloadingClass) {
        
        [self.downloadManage deleteRequest:request];
        
        ZFFileModel *fileInfo = [request.userInfo objectForKey:@"File"];
        
        [self deletePlistWithName:[self decodeString: fileInfo.fileName ]];
    }
    
    [_categoryArray removeObject:string];
    
    [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationBottom];
    
    [_tableView reloadData];
    
}





-(void)deletePlistWithName:(NSString *)name
{
    NSArray *patharray = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    
    NSString *path =  [patharray objectAtIndex:0];
    
    NSString *filepath=[path stringByAppendingPathComponent:@"downtest4.plist"];
    
    NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithContentsOfFile:filepath];
    
    [dic removeObjectForKey:name];
    
    [dic writeToFile:filepath atomically:YES];
    
    
}




-(void)requestConut
{
    for (NSString *string in _categoryArray) {
        
        NSMutableArray *downladed = self.downloadManage.finishedlist;
        
        NSMutableArray *downloading = self.downloadManage.downinglist;
        
        NSMutableArray *downladedClass = @[].mutableCopy;
        
        for ( ZFFileModel *fileInfo in downladed) {
            
            if ([fileInfo.bookName isEqualToString:string] ) {
                [downladedClass addObject:fileInfo];
            }
            //
        }
        
        
        NSMutableArray *downloadingClass = @[].mutableCopy;
        
        
        for (ZFHttpRequest *request in downloading) {
            
            ZFFileModel *fileInfo = [request.userInfo objectForKey:@"File"];
            
            if ([fileInfo.bookName isEqualToString:string]) {
                [downloadingClass addObject:request];
            }
       
        }
        
        [_downloadedCountArray addObject:[NSString stringWithFormat:@"%ld",[downladedClass count]]];
        
        
        NSInteger allCount = downladedClass.count + downloadingClass.count;
        
        [_allCountArray addObject:[NSString stringWithFormat:@"%d",allCount]];

        NSLog(@"%@  :  已下载完成%ld册, 正在下载%ld册",string,[downladedClass count],[downloadingClass count]);
        
    }
    
    [self createTableView];
    
}

#pragma mark - 导航栏返回按钮颜色设置 及 文字变成<
//  导航栏返回按钮颜色设置 及 文字变成<
-(void)UIBarButtonBackItemSet
{
    UIBarButtonItem *btn_back = [[UIBarButtonItem alloc]init];
    
    btn_back.title = @"";
    
    self.navigationItem.backBarButtonItem = btn_back;
    
    self.navigationController.navigationBar.barStyle = UIStatusBarStyleDefault;
    
    [self.navigationController.navigationBar setTintColor:[UIColor whiteColor]];

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
