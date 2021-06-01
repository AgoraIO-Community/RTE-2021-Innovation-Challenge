//
//  BBHomeMessageTableViewCell.m
//  Baby back home
//
//  Created by zhangyu on 2021/5/14.
//

#import "BBHomeMessageTableViewCell.h"
#import "MessageDBManager.h"
@interface BBHomeMessageTableViewCell ()
@property(weak, nonatomic)UIImageView * img;
@property(weak, nonatomic)UILabel * titleLB;
@property(weak, nonatomic)UILabel * locationLB;
@property(weak, nonatomic)UILabel * timeLB;

@end

@implementation BBHomeMessageTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
        [self buildSubViews];
    }
    
    return self;
}

- (void)buildSubViews{
    UIImageView * img = [[UIImageView alloc] initWithFrame:(CGRectMake(10, 10, 80, 80))];
    [self.contentView addSubview:img];
    img.contentMode = UIViewContentModeScaleAspectFit;
    self.img = img;
    
    UILabel * titleLB = [[UILabel alloc] initWithFrame:(CGRectMake(100, 20, self.width - 100, 20))];
    titleLB.textColor = UIColor.blackColor;
    titleLB.font = [UIFont systemFontOfSize:19];
    self.titleLB = titleLB;
    [self.contentView addSubview:titleLB];
    
    
    UILabel * locationLB = [[UILabel alloc] initWithFrame:(CGRectMake(100, 60, self.width - 100, 20))];
    locationLB.textColor = KFontColor;
    locationLB.font = [UIFont systemFontOfSize:16];
    self.locationLB = locationLB;
    [self.contentView addSubview:locationLB];
    
    UILabel * timeLB = [[UILabel alloc] initWithFrame:(CGRectMake(KScreenWidth - 100, 40,  90, 20))];
    timeLB.textColor = KFontColor;
    timeLB.font = [UIFont systemFontOfSize:14];
    self.timeLB = timeLB;
    [self.contentView addSubview:timeLB];
    
//    UIView * view = [[UIView alloc] initWithFrame:(CGRectMake(20, 99, KScreenWidth - 40, 1))];
//    view.backgroundColor = [UIColor colorWithWhite:0.9 alpha:1];
//    [self.contentView addSubview:view];
}

- (void)setModel:(BBMessageModel *)model{
    if (_model != model) {
        self.titleLB.text = model.title;
        self.timeLB.text = [self timestampSwitchTime:model.timestamp andFormatter:@"YYYY-MM-dd"];
        self.locationLB.text = model.address;
        NSArray * picArr = [model.imgName componentsSeparatedByString:@","];
        self.img.image = [self detectFace:[[MessageDBManager sharedInstance] getCacheImageUseImagePath:picArr[0]]];
        if ([self detectFace:[[MessageDBManager sharedInstance] getCacheImageUseImagePath:picArr[0]]] == nil) {
            self.img.image = [self detectFace:[UIImage imageNamed:model.imgName]];
        }
        
    }
}

- (NSString *)timestampSwitchTime:(NSInteger)timestamp andFormatter:(NSString *)format{
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateStyle:NSDateFormatterMediumStyle];
    [formatter setTimeStyle:NSDateFormatterShortStyle];
    [formatter setDateFormat:format];
    NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"Asia/Beijing"];
    [formatter setTimeZone:timeZone];
    
    NSDate *confromTimesp = [NSDate dateWithTimeIntervalSince1970:timestamp];
    
    NSString *confromTimespStr = [formatter stringFromDate:confromTimesp];
    if ([format isEqual:@"HH:mm:ss"] && confromTimespStr.length<8) {
        confromTimespStr = @"00:00:00";
    }
    if ([format isEqual:@"YYYY-MM-dd HH:mm:ss"] && confromTimespStr.length<18) {
        confromTimespStr = @"2000-01-01 00:00:00"; //默认返回
    }
    return confromTimespStr;
}

- (UIImage *)detectFace:(UIImage*)image{
    UIImage *resultImage;
    if (image) {
        CIImage *cgImage = [[CIImage alloc] initWithImage:image];
        CIContext *context = [CIContext contextWithOptions:nil];
        CIDetector *faceDetector = [CIDetector detectorOfType:CIDetectorTypeFace context:context options:@{CIDetectorAccuracy:CIDetectorAccuracyHigh}];
        //检测到的人脸数组
        NSArray *faceArray = [faceDetector featuresInImage:cgImage];
        if (faceArray.count > 0) {
            //检测到人脸时获取最后一次监测到的人脸
            CIFeature *faceFeature = [faceArray lastObject];
            CGRect faceBounds = faceFeature.bounds;
            //cgImage计算的尺寸是像素，需要与空间的尺寸做个计算
            //下面几句是为了获取到额头部位做的处理，如果只需要定位到五官可直接取faceBounds的值
//            CGFloat scale = cgImage.extent.size.width/KScreenWidth;
//            CGFloat offsetY = fabs(self.img.frame.origin.y*scale - faceBounds.origin.y);
            CGFloat offsetY = fabs(faceBounds.size.height/2.0);
            CGFloat offsetX = fabs(faceBounds.size.width/2.0);
            faceBounds.size.height +=  offsetY;
            faceBounds.origin.x -= offsetX/2;
            faceBounds.size.width +=  offsetX;
            //这种裁剪方法在低头时和抬头时会截取不到完整的脸部，但是可以定位全脸位置更精确
//            CGImageRef newImage = CGImageCreateWithImageInRect(self.displayImageView.image.CGImage, faceBounds);
//            resultImage = [[UIImage alloc] initWithCGImage:newImage];
//            CGImageRelease(newImage);
            
            //这种裁剪方法不会出现脸部裁剪不到的情况，但是会裁剪到脖子的位置
            CIImage *faceImage = [cgImage imageByCroppingToRect:faceBounds];
            resultImage = [UIImage imageWithCGImage:[context createCGImage:faceImage fromRect:faceImage.extent]];
            return resultImage;
        }
    }
    return image;
}




- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
