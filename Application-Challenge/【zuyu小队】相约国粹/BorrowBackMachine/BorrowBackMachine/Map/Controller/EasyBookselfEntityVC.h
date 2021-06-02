//
//  EasyBookselfEntityVC.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/23.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "BaceViewController.h"
#import "EasyBookselfModel.h"
@interface EasyBookselfEntityVC : BaceViewController
@property(nonatomic,strong) EasyBookselfModel *model;
@property(nonatomic,strong) NSString *bookselfID;
@end
