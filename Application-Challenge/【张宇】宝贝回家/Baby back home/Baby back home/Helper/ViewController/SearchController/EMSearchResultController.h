//
//  EMSearchResultController.h
//  DXStudio
//
//  Created by XieYajie on 22/09/2017.
//  Copyright © 2017 dxstudio. All rights reserved.
//

#import "EMRefreshViewController.h"

@protocol EMSearchControllerDelegate;
@interface EMSearchResultController : EMRefreshViewController

@property (nonatomic, weak) id<EMSearchControllerDelegate> delegate;

@property (nonatomic, strong) UISearchBar *searchBar;

@property (nonatomic, strong) NSString *searchKeyword;

@property (copy) void (^footerBeginRefreshCompletion)(UITableView *tableView);

- (void)cancelSearch;

@property (copy) UITableViewCell * (^cellForRowAtIndexPathCompletion)(UITableView *tableView, NSIndexPath *indexPath);
@property (copy) BOOL (^canEditRowAtIndexPath)(UITableView *tableView, NSIndexPath *indexPath);
@property (copy) void (^commitEditingAtIndexPath)(UITableView *tableView, UITableViewCellEditingStyle editingStyle, NSIndexPath *indexPath);
@property (copy) UISwipeActionsConfiguration * (^trailingSwipeActionsConfigurationForRowAtIndexPath)(UITableView *tableView, NSIndexPath *indexPath);
//@property (copy) CGFloat (^heightForRowAtIndexPathCompletion)(UITableView *tableView, NSIndexPath *indexPath);
@property (copy) void (^didSelectRowAtIndexPathCompletion)(UITableView *tableView, NSIndexPath *indexPath);
@property (copy) void (^didDeselectRowAtIndexPathCompletion)(UITableView *tableView, NSIndexPath *indexPath);
@property (copy) NSInteger (^numberOfSectionsInTableViewCompletion)(UITableView *tableView);
@property (copy) NSInteger (^numberOfRowsInSectionCompletion)(UITableView *tableView, NSInteger section);

@end

@protocol EMSearchControllerDelegate <NSObject>

@optional

- (void)searchBarWillBeginEditing:(UISearchBar *)searchBar;

- (void)searchBarCancelButtonAction:(UISearchBar *)searchBar;

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar;

- (void)searchTextDidChangeWithString:(NSString *)aString;

@end
