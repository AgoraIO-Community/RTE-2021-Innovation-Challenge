//
//  ClassMainViewController.m
//  CNCLibraryScan
//
//  Created by zuyu on 2017/9/14.
//  Copyright © 2017年 zuyu. All rights reserved.
//

#import "ClassMainViewController.h"
#import "MainSocendTableViewCell.h"
#import "TMyDownloadViewController.h"
#import "LocalMP3PlayerViewController.h"
#import "DownLoadMp3ViewController.h"
#import "DownloadData.h"
#import "ScanDownloadViewController.h"
#import "LocalPDFViewController.h"
#import "ZFDownloadManager.h"
#import "MainMP4ViewController.h"
#import "MainSecondViewClassCellModel.h"
#import "MainThirdMP3ViewController.h"
#import "MainWebViewController.h"
#import "zuyu.h"
#import "ZFDownloadManager.h"





@interface ClassMainViewController ()<UITableViewDelegate,UITableViewDataSource,NavgationViewDelegate>
{
    UITableView *_tableView;
    
    UITableView *_networkTableView;

    NSMutableArray *_dataArray;
    
    NSMutableArray *_classIDArray;
    
    NSMutableArray *_countArray;
    
    NSMutableArray *_classListArray;
    
    NSInteger _isPush;
    
}
@property (nonatomic, strong) ZFDownloadManager *downloadManage;
@end

@implementation ClassMainViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _isPush = 0;
    
    self.view.backgroundColor = [UIColor whiteColor];    
    
    [self createNavgation];
    
    [self initClass];
    
    [self reouceData];
    
    [self createTableView];
    
    // Do any additional setup after loading the view.
}

#pragma mark - navgation
-(void)createNavgation
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = NO;
    view.titleStr = self.titles;
    [self.view addSubview:view];
}


-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - init class

-(void)initClass
{
    _dataArray = [NSMutableArray array];
    _classIDArray = [NSMutableArray array];
    _countArray = [NSMutableArray array];
    _classListArray = [NSMutableArray array];

}


#pragma mark - reouceData
-(void)reouceData
{
    NSMutableArray *finishArr = [[NSMutableArray alloc] initWithContentsOfFile:PLIST_PATH];
    
    NSMutableArray *bookNameArr = [NSMutableArray array];
    
    for (NSDictionary *dict in finishArr) {
        
        if ([[dict objectForKey:@"ClassID"] isEqualToString:self.classID]) {
          
            [bookNameArr addObject:[dict objectForKey:@"bookName"]];

        }
        
    }
    
    for (NSString *str in bookNameArr) {
        if (![_dataArray containsObject:str]) {
            [_dataArray addObject:str];
        }
    }
    
    for (int i = 0; i<_dataArray.count; i++) {
        
        NSString *classID;
        
        NSMutableArray *rongqi = [NSMutableArray array];
        
        for (NSDictionary *dict in finishArr) {
            
            if ([[dict objectForKey:@"bookName"] isEqualToString:_dataArray[i]]) {
                
                classID = [dict objectForKey:@"ClassID"];
                
                [rongqi addObject:classID];
            }
            
        }
        
        [_classIDArray addObject:classID];
        
        [_countArray addObject:rongqi];
        
    }
    
    [_tableView reloadData];

}


#pragma mark - createTableview
-(void)createTableView
{
    _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 50 - 74) style:UITableViewStylePlain];
    
    _tableView.delegate = self;
    
    _tableView.dataSource = self;
    
    _tableView.rowHeight = 100;
    
    _tableView.separatorStyle = NO;
    
    [self.view addSubview:_tableView];
    
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
    
    UIAlertView *alert = [UIAlertView showWithTitle:@"是否确认删除该资源" message:@"" cancelButtonTitle:@"取消" otherButtonTitles:@[@"删除"] tapBlock:^(UIAlertView * _Nonnull alertView, NSInteger buttonIndex) {
        if (buttonIndex) {
            [self deleteCe:_dataArray[indexPath.row] index:indexPath];
        }
    }];
}


-(void)deleteCe:(NSString *)bookName index:(NSIndexPath *)indexPath
{
    NSMutableArray *downladed = self.downloadManage.finishedlist;
    NSMutableArray *downloading = self.downloadManage.downinglist;
    //先删除已经下载好的
    NSMutableArray *downladedClass = @[].mutableCopy;
    for (ZFFileModel *fileInfo in downladed) {
        if ([fileInfo.bookName isEqualToString:bookName] ) {
            [downladedClass addObject:fileInfo];
        }
    }
    
    for (ZFFileModel *fileInfo in downladedClass) {
        [self.downloadManage deleteFinishFile:fileInfo];
    }
    
    
    //在删除正在下载中的
    NSMutableArray *downloadingClass = @[].mutableCopy;

    for (ZFHttpRequest *request in downloading) {
        
        ZFFileModel *fileInfo = [request.userInfo objectForKey:@"File"];
        
        if ([fileInfo.bookName isEqualToString:bookName]) {
            [downloadingClass addObject:request];
        }
    }
    
    for (ZFHttpRequest *request in  downloadingClass) {
        [self.downloadManage deleteRequest:request];
        
    }
    
    [_dataArray removeObject:bookName];

    [_tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationBottom];

    
}





#pragma mark - tableView datasouce

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
        return 2;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    if (!self.isLocal) {
        
        if (section) {
            return _classListArray.count;
        }
        return 0;
    }else{
        if (section) {
            return 0;
        }
        return _dataArray.count;
    }
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section) {
        return HEIGHT/6;
    }else{
        
        return 100;
    }
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.section == 0) {
        
        static NSString *identifier = @"cell";
        
        MainSocendTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        
        if (cell == nil) {
            cell = [[MainSocendTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        }
        
        
        
        cell.nameLable.text = [NSString stringWithFormat:@"%@",_dataArray[indexPath.row]];
        
        cell.nameLable.frame = CGRectMake(100, 0, self.view.frame.size.width/2, 60);

        
        cell.countLable.text = [NSString stringWithFormat:@"本地共%@节 已下载%@节",[NSString stringWithFormat:@"%ld",[_countArray[indexPath.row] count]], [NSString stringWithFormat:@"%ld",[_countArray[indexPath.row] count]]];
        
        
         
        cell.lionLabel.frame = CGRectMake(0, 100-0.8, WIDTH, 0.8);;
        
        if (WIDTH == 320) {
            cell.nameLable.frame = CGRectMake(100, 5, self.view.frame.size.width/3, 60);
            cell.nameLable.numberOfLines = 2;
            cell.countLable.frame = CGRectMake(100, 45, self.view.frame.size.width/3, 60);
            cell.nameLable.font = [UIFont systemFontOfSize:14];
            cell.countLable.font = [UIFont systemFontOfSize:14];
            cell.countLable.numberOfLines = 0;
            
        }
        
        
        cell.nameLable.numberOfLines = 0;
        
        cell.collectLable.hidden = YES;
        
        cell.countryLable.hidden = YES;
        
        cell.image.image = [UIImage imageNamed:@"wenjianjia"];
        
        cell.image.frame = CGRectMake(20, 20, 50, 60);
        
        
        UIButton *weiButton = [self createCelldownLoadLgButton:indexPath.row];
        
        [cell addSubview:weiButton];
        //
        
        
        NSString *typeString = _classIDArray[indexPath.row];
        
        if ([typeString isEqualToString:@"88"]||[typeString isEqualToString:@"66"]||[typeString isEqualToString:@"44"]||[typeString isEqualToString:@"55"]) {
            
            weiButton.hidden = YES;
            
            [cell addSubview: [self createCellmp3Button:indexPath.row]];
            
        }else {
            
            weiButton.hidden = YES;
            
            if ([typeString isEqualToString:@"22"]||[typeString isEqualToString:@"77"]||[typeString isEqualToString:@"99"]) {
                
                [cell addSubview: [self createCellmp4Button:indexPath.row WithPDForMp4:0]];
                
            }else{
                
                [cell addSubview: [self createCellmp4Button:indexPath.row WithPDForMp4:1]];
                
            }
            
        }
        
        return cell;
        
    }else{
        
        MainSocendTableViewCell *mCell =(MainSocendTableViewCell*)[tableView dequeueReusableCellWithIdentifier:@"MainSocendTableViewCell"];
        
        if (mCell==nil) {
            
            mCell = [[MainSocendTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault
                                                   reuseIdentifier:@"MainSocendTableViewCell"];
            mCell.selectionStyle = UITableViewCellSelectionStyleNone;
            
        }
        if (_classListArray.count) {
            
            
            MainSecondViewClassCellModel *model = _classListArray[indexPath.row];
            
            mCell.nameLable.text = model.name;
            
            mCell.countryLable.text = model.author;
            
            mCell.collectLable.text = [NSString stringWithFormat:@"%@人气",model.viewCount];
            
            mCell.summaryLable.text = model.Summary;
            
            
            CGSize size = [self getAttributeSizeWithText: model.name  fontSize:17];
            
            if (WIDTH == 320 ) {
                size = [self getAttributeSizeWithText: model.name  fontSize:14];
                
                mCell.nameLable.font = [UIFont systemFontOfSize:14.0];
                
                mCell.collectLable.font = [UIFont systemFontOfSize:14.0];
                
                mCell.summaryLable.font = [UIFont systemFontOfSize:14.0];
                
            }
            
            
            CGSize sizes = [[UIScreen mainScreen] bounds].size;

            mCell.nameLable.frame = CGRectMake(sizes.width/4, 15, size.width + 5, 20);
            
            
            mCell.classTypeSign.frame = CGRectMake(CGRectGetWidth(mCell.nameLable.bounds) + sizes.width/4  , 15, 35, size.height);
            
            
            mCell.collectButton.tag = 5000 + indexPath.row;
            
            NSString * ResourceType = [NSString stringWithFormat:@"%@",model.ResourceType];
            
            if ([ResourceType isEqualToString:@"0"]) {
                
                mCell.classTypeSign.image = [UIImage imageNamed:@"img_mp3"];
                
            }else if ([ResourceType isEqualToString:@"2"]){
                
                mCell.classTypeSign.image = [UIImage imageNamed:@"img_book_img"];
                
            }else if ([ResourceType isEqualToString:@"3"]){
                
                mCell.classTypeSign.image = [UIImage imageNamed:@"img_text_pdf"];
                
            }else if([ResourceType isEqualToString:@"4"]){
                
                mCell.classTypeSign.image = [UIImage imageNamed:@"img_text_t"];
                
            }else{
                mCell.classTypeSign.image = [UIImage imageNamed:@"img_mp4"];
                
            }
        }
        
        return mCell;
        
        
    }
   
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];

    _isPush = 1;
    
    if (indexPath.section == 0) {
        
        if (_dataArray.count) {
            TMyDownloadViewController *vc = [[TMyDownloadViewController alloc] init];
            
            vc.bookName = _dataArray[indexPath.row];
            
            vc.type = self.classID;
            
            [vc setHidesBottomBarWhenPushed:YES];
            
            [self.navigationController pushViewController:vc animated:YES];
        }
        
  
    
    }else{
   
        if (_classListArray.count) {
            
        }
        
        MainSecondViewClassCellModel *model = _classListArray[indexPath.row];

        NSString *dataType  =[NSString stringWithFormat:@"%@",model.ResourceType]  ;
        
        if ([dataType isEqualToString:@"0"]) {
            //音
            
            MainThirdMP3ViewController *thirdMp3Vc = [[MainThirdMP3ViewController alloc] init];
            
            thirdMp3Vc.Summary = model.Summary;
            
            thirdMp3Vc.tableListID = model.parameterID;
            
            thirdMp3Vc.MenuID = model.MenuID;
            
            thirdMp3Vc.butTag = indexPath.row % 8 + 100;
            
            thirdMp3Vc.thirdTitle = model.name;
            
            thirdMp3Vc.writer = model.author;
            
            thirdMp3Vc.viewCount = model.viewCount;
            
            thirdMp3Vc.imageName = model.imageName;
            
            thirdMp3Vc.BookTypeName = model.bookTypeName;
            
            thirdMp3Vc.VolumeCount = model.VolumeCount;
            
            [thirdMp3Vc setHidesBottomBarWhenPushed:YES];
            
            [self.navigationController pushViewController:thirdMp3Vc animated:YES];
            
        }else if ([dataType isEqualToString:@"6"]){
            //视
            MainMP4ViewController *vc = [[MainMP4ViewController alloc] init];
            
            vc.BookID = model.parameterID;
                        
            [self.navigationController pushViewController:vc animated:YES];
            
        }else{
            
                MainWebViewController *vc = [[MainWebViewController alloc] init];
            vc.name = model.name;
                vc.webUrl = [NSString  stringWithFormat:@"%@",model.ResourceUrl];
            
                [self.navigationController pushViewController:vc animated:YES];
                
            
        }
        
    }
    
}

#pragma mark - cell .button.
-(UIImageView *)createCellmp3Button:(NSInteger )buttonTag
{
    UIImageView *listionButton = [[UIImageView alloc] init];
    
    listionButton.frame =  CGRectMake(WIDTH/2 + 120 , 35/2, 65, 65);
    
    if (WIDTH == 320) {
        listionButton.frame =  CGRectMake(WIDTH/2 + 65 , 35/2, 65, 65);
        
    }
    
    listionButton.image = [UIImage imageNamed:@"img_mp3_seeing"];
    
//    [listionButton addTarget:self action:@selector(mp3ButtonClick:) forControlEvents:UIControlEventTouchUpInside];
    
    return listionButton;
    
}

-(UIButton *)createCellmp4Button:(NSInteger )buttonTag WithPDForMp4:(NSUInteger )pom
{
    
    
        UIButton *listionButton = [UIButton buttonWithType:UIButtonTypeCustom];
        
        listionButton.tag = buttonTag;
        
        listionButton.frame =  CGRectMake(WIDTH/2 + 120 , 35/2, 65, 65);
        
        if (WIDTH == 320) {
            listionButton.frame =  CGRectMake(WIDTH/2 + 65 , 35/2, 65, 65);
            
        }
    
    
    if (pom) {
//        [listionButton addTarget:self action:@selector(mp4ButtonClick:) forControlEvents:UIControlEventTouchUpInside];
        
        [listionButton setImage:[UIImage imageNamed:@"img_mp4_seeing"] forState:UIControlStateNormal];
    }else{
//        [listionButton addTarget:self action:@selector(pdfButtonClick:) forControlEvents:UIControlEventTouchUpInside];
        
        [listionButton setImage:[UIImage imageNamed:@"img_mp4_reading"] forState:UIControlStateNormal];
    }
    
    
        
        return listionButton;
   
    
}


-(UIButton *)createCelldownLoadLgButton:(NSInteger )buttonTag
{
    UIButton *listionButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    listionButton.tag = buttonTag;
    
    listionButton.frame =  CGRectMake(WIDTH/2 + 120 , 65/2, 65, 35);
    
//    [listionButton setImage:[UIImage imageNamed:@"img_indownfile"] forState:UIControlStateNormal];
    
    
    if (WIDTH == 320) {
        listionButton.frame =  CGRectMake(WIDTH/2 + 65 , 65/2, 65, 35);
        
    }
    return listionButton;
}



-(void)mp3ButtonClick:(UIButton *)button
{
    
    
    
    NSMutableArray *urlArr = @[].mutableCopy;
    NSMutableArray *nameArr = @[].mutableCopy;
    
    NSMutableArray *downladed = self.downloadManage.finishedlist;
    NSMutableArray *downladedClass = @[].mutableCopy;
    
    for ( ZFFileModel *fileInfo in downladed) {
        
        if ([fileInfo.bookName isEqualToString:[NSString stringWithFormat:@"%@",_dataArray[button.tag]]] ) {
            [downladedClass addObject:fileInfo];
        }
        //
    }
    
    
    
    for (ZFFileModel *fileInfo in downladedClass) {
        
        [urlArr addObject:FILE_PATH(fileInfo.fileName)];
        
        [nameArr addObject:fileInfo.fileName];
    }
    
    LocalMP3PlayerViewController *vc = [[LocalMP3PlayerViewController alloc] init];
    
    [vc setHidesBottomBarWhenPushed:YES];
    
    vc.URLArray = urlArr;
    
    vc.indexRow = 0;
    
    vc.nameArray = nameArr;
    
    [self.navigationController pushViewController:vc animated:YES];
    
    
    
}
-(void)mp4ButtonClick:(UIButton *)button
{
    
    
    
    NSMutableArray *urlArr = @[].mutableCopy;
    NSMutableArray *nameArr = @[].mutableCopy;
    
    NSMutableArray *downladed = self.downloadManage.finishedlist;
    NSMutableArray *downladedClass = @[].mutableCopy;
    
    for ( ZFFileModel *fileInfo in downladed) {
        
        if ([fileInfo.bookName isEqualToString:[NSString stringWithFormat:@"%@",_dataArray[button.tag]]] ) {
            [downladedClass addObject:fileInfo];
        }
        //
    }
    
    
    
    for (ZFFileModel *fileInfo in downladedClass) {
        
        [urlArr addObject:FILE_PATH(fileInfo.fileName)];
        
        [nameArr addObject:fileInfo.fileName];
    }
    
    DownLoadMp3ViewController *vc = [[DownLoadMp3ViewController alloc] init];
    
    vc.dataArray = urlArr;
    
    vc.nameArray = nameArr;
    
    vc.chooseID = 0;
    
    [vc setHidesBottomBarWhenPushed:YES];
    
    [self.navigationController pushViewController:vc animated:YES];
}

-(void)pdfButtonClick:(UIButton *)button
{
    
    NSMutableArray *urlArr = @[].mutableCopy;
    NSMutableArray *nameArr = @[].mutableCopy;
    
    NSMutableArray *downladed = self.downloadManage.finishedlist;
    NSMutableArray *downladedClass = @[].mutableCopy;
    
    for ( ZFFileModel *fileInfo in downladed) {
        
        if ([fileInfo.bookName isEqualToString:[NSString stringWithFormat:@"%@",_dataArray[button.tag]]] ) {
            [downladedClass addObject:fileInfo];
        }
        //
    }
    
    
    
    for (ZFFileModel *fileInfo in downladedClass) {
        
        [urlArr addObject:FILE_PATH(fileInfo.fileName)];
        
        [nameArr addObject:fileInfo.fileName];
    }
    
    LocalPDFViewController *vc = [[LocalPDFViewController alloc] init];
    
    [vc setHidesBottomBarWhenPushed:YES];
    
    vc.urlString = urlArr[0];
    
    [self.navigationController pushViewController:vc animated:YES];
    
    
}

#pragma mark - downloadManage
- (ZFDownloadManager *)downloadManage
{
    if (!_downloadManage) {
        _downloadManage = [ZFDownloadManager sharedDownloadManager];
    }
    return _downloadManage;
}



- (CGSize )getAttributeSizeWithText:(NSString *)text fontSize:(int)fontSize
{
    CGSize size=[text sizeWithAttributes:@{NSFontAttributeName: [UIFont systemFontOfSize:fontSize]}];
    
    NSAttributedString *attributeSting = [[NSAttributedString alloc] initWithString:text attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:fontSize]}];
    size = [attributeSting size];
    
    return size;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark - nav 处理.
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:NO];
    
    if (_isPush) {
        
        [_dataArray removeAllObjects];
        [self initClass];
        [self reouceData];
        [_tableView reloadData];
        
    }
}


- (void) viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:NO];
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
