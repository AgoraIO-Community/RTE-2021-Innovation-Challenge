//
//  BookselfEntityVC.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/9.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "BaceViewController.h"
#import "BookselfModel.h"

@interface BookselfEntityVC : BaceViewController
@property(nonatomic,strong) BookselfModel *model;
@property(nonatomic,strong) NSString *bookselfID;

@end
