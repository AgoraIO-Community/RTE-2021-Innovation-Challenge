//
//  LxRecordListVc.m
//  LxAudioToStaff
//
//  Created by DavinLee on 2021/5/31.
//

#import "LxRecordListVc.h"
#import "LxRecordListCell.h"
@interface LxRecordListVc ()<UITableViewDelegate,UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *tableViewTopOffsetY;

@property (strong, nonatomic) NSMutableArray *recordFileNames;

@end

@implementation LxRecordListVc

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupDefault];
    // Do any additional setup after loading the view from its nib.
}

- (void)setupDefault{
    
    self.recordFileNames = [NSMutableArray arrayWithArray:[mUserDefaults objectForKey:@"recordNames"]];
    if (self.recordFileNames.count < 1) {
        [MBProgressHUD lx_showHudWithTitle:@"无记录，快去保存吧！！！" hideCompletion:^{
            [self dismissViewControllerAnimated:YES completion:nil];
        }];
        self.block = nil;
    }
    
    self.tableView.backgroundColor = [UIColor clearColor];
    
    [self.tableView registerNib:[UINib nibWithNibName:@"LxRecordListCell" bundle:[NSBundle mainBundle]] forCellReuseIdentifier:@"cell"];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    
    
}


- (void)deleteRecordWithIndex:(NSInteger)index{
    if (self.recordFileNames.count > index) {
        
        [mUserDefaults removeObjectForKey:self.recordFileNames[index]];
        [self.recordFileNames removeObjectAtIndex:index];
        [self.tableView reloadData];
        [mUserDefaults setObject:self.recordFileNames forKey:@"recordNames"];
        [mUserDefaults synchronize];
    }
}

- (void)setBlock:(RecordListVcSelectedName)block{
    _block = [block copy];
}

#pragma mark - ************************tableViewDelegate************************
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.recordFileNames.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    LxRecordListCell *cell = (LxRecordListCell *)[tableView dequeueReusableCellWithIdentifier:@"cell"];
    cell.tag = indexPath.row;
    WF;
    [cell setDeleteBlock:^(NSInteger tag) {
        [weakSelf deleteRecordWithIndex:tag];
    }];
    cell.nameLabel.text = self.recordFileNames[indexPath.row];
    
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (self.block) {
        self.block(self.recordFileNames[indexPath.row]);
        self.block = nil;
    }
    [self dismissViewControllerAnimated:YES completion:nil];
}
    
    - (void)touchesEnded:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
        self.block = nil;
        [self dismissViewControllerAnimated:YES completion:nil];
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
