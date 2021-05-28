#ifndef __brf__DrawingUtils_hpp
#define __brf__DrawingUtils_hpp

#import <UIKit/UIKit.h>
//RGB color macro
#define UIColorFromRGB(rgbValue) [UIColor 						\
    colorWithRed:((float)((rgbValue & 0xFF0000) >> 16)) / 255.0 \
    green:((float)((rgbValue & 0xFF00) >> 8)) 			/ 255.0 \
    blue:((float)(rgbValue & 0xFF))						/ 255.0 \
    alpha:1.0]

//RGB color macro
#define UIColorFromRGBWithAlpha(rgbValue, a) [UIColor 			\
    colorWithRed:((float)((rgbValue & 0xFF0000) >> 16)) / 255.0 \
    green:((float)((rgbValue & 0xFF00) >> 8)) 			/ 255.0 \
    blue:((float)(rgbValue & 0xFF))						/ 255.0 \
    alpha:a]

namespace brf {

class DrawingUtils {

public: CGContextRef _context;
public: bool _flipped;
public: unsigned int _width;
public: unsigned int _height;

public: DrawingUtils() :
	_context(nullptr),
    _flipped(true),
    _width(480),
    _height(640)
{
}

public: void updateLayout(unsigned int width, unsigned int height) {
	//graphics.create(height, width, CV_8UC4);
    _width = width;
    _height = height;
}

public: void clear() {
}
    
public: double getY(bool flipped, double y) {
    if(flipped) {
        return (double)_height - y;
    } else {
        return y;
    }
}
    
public: void drawVertices(std::vector< double >& vertices, int radius = 2, bool _clear = false,
                                   uint32_t fillColor = 0x00a0ff, double fillAlpha = 1.00) {
    
    if(_context == nullptr) {
        return;
    }
    
    CGColorRef _fillColor = UIColorFromRGBWithAlpha(fillColor, fillAlpha).CGColor;
    
    CGContextRef g = _context;
    
    if(_clear) clear();
    
    CGContextSetFillColorWithColor(g, _fillColor);
    
    int i = 0;
    int l = (int)vertices.size();
    
    CGRect rectangle = CGRectMake(0, 0, radius * 2.0, radius * 2.0);
    
    for(; i < l; i+=2) {
        
        rectangle.origin.x = vertices[i] - radius;
        rectangle.origin.y = getY(_flipped, vertices[i + 1]) - radius;
        
        CGContextAddEllipseInRect(g, rectangle);
    }
    
    CGContextFillPath(g);
}

public: void drawTriangles(std::vector< double >& vertices, std::vector< int >& triangles, bool _clear = false,
		double lineThickness = 1,
		uint32_t lineColor = 0x00a0ff, double lineAlpha = 0.85) {
    
    if(_context == nullptr) {
        return;
    }
    
    CGColorRef _lineColor = UIColorFromRGBWithAlpha(lineColor, lineAlpha).CGColor;
    CGContextRef g = _context;
    
    if(_clear) clear();
    
    CGContextSetStrokeColorWithColor(g, _lineColor);
    CGContextSetLineWidth(g, lineThickness);
    
	int i = 0;
	int l = (int)triangles.size();

	while(i < l) {
		int ti0 = triangles[i];
		int ti1 = triangles[i + 1];
		int ti2 = triangles[i + 2];

		double x0 = vertices[ti0 * 2];
		double y0 = getY(_flipped, vertices[ti0 * 2 + 1]);
		double x1 = vertices[ti1 * 2];
		double y1 = getY(_flipped, vertices[ti1 * 2 + 1]);
		double x2 = vertices[ti2 * 2];
		double y2 = getY(_flipped, vertices[ti2 * 2 + 1]);
        
        
        CGContextMoveToPoint(g, x0, y0);
        CGContextAddLineToPoint(g, x1, y1);
        CGContextAddLineToPoint(g, x2, y2);
        CGContextAddLineToPoint(g, x0, y0);
        
        CGContextStrokePath(g);
        
		i+=3;
	}
}

public: void fillTriangles(std::vector< double >& vertices, std::vector< int >& triangles, bool _clear = false,
                           uint32_t fillColor = 0x00a0ff, double fillAlpha = 0.80) {
    
    if(_context == nullptr) {
        return;
    }
    
    CGColorRef _fillColor = UIColorFromRGBWithAlpha(fillColor, fillAlpha).CGColor;
    CGContextRef g = _context;
    
    if(_clear) clear();
    
    int i = 0;
    int l = (int)triangles.size();
    
    while(i < l) {
        int ti0 = triangles[i];
        int ti1 = triangles[i + 1];
        int ti2 = triangles[i + 2];
        
        double x0 = vertices[ti0 * 2];
        double y0 = getY(_flipped, vertices[ti0 * 2 + 1]);
        double x1 = vertices[ti1 * 2];
        double y1 = getY(_flipped, vertices[ti1 * 2 + 1]);
        double x2 = vertices[ti2 * 2];
        double y2 = getY(_flipped, vertices[ti2 * 2 + 1]);
        
        CGContextSetFillColorWithColor(g, _fillColor);
        
        CGContextMoveToPoint(g, x0, y0);
        CGContextAddLineToPoint(g, x1, y1);
        CGContextAddLineToPoint(g, x2, y2);
        CGContextAddLineToPoint(g, x0, y0);
        
        CGContextClosePath(g);
        CGContextFillPath(g);
        
        i+=3;
    }
}
    
//public: void drawTexture(std::vector< double >& vertices, std::vector< int >& triangles, std::vector< double >& triangles, ? texture) {
//}

public: void drawRect(std::shared_ptr<brf::Rectangle> rect, bool clear = false,
		int lineThickness = 1, uint lineColor = 0x00a0ff, double lineAlpha = 1.00) {

	if(rect != nullptr) {
		drawRect(*rect, clear, lineThickness, lineColor, lineAlpha);
	}
}

public: void drawRect(brf::Rectangle& rect, bool _clear = false,
		double lineThickness = 1, uint lineColor = 0x00a0ff, double lineAlpha = 1.00) {

    if(_context == nullptr) {
        return;
    }
    
    CGColorRef _lineColor = UIColorFromRGBWithAlpha(lineColor, lineAlpha).CGColor;
    CGContextRef g = _context;
    
    if(_clear) clear();
    
    CGContextSetStrokeColorWithColor(g, _lineColor);
    CGContextSetLineWidth(g, lineThickness);
    
    CGRect cgRect = CGRectMake(rect.x, getY(_flipped, rect.y) - rect.height, rect.width, rect.height);
    
    CGContextAddRect(g, cgRect);
    CGContextStrokePath(g);
    
}

public: void drawRects(std::vector< std::shared_ptr<brf::Rectangle> >& rects, bool _clear = false,
		int lineThickness = 1, uint lineColor = 0x00a0ff, double lineAlpha = 1.00) {
    
    if(_context == nullptr) {
        return;
    }
    
    CGColorRef _lineColor = UIColorFromRGBWithAlpha(lineColor, lineAlpha).CGColor;
    CGContextRef g = _context;
    
    if(_clear) clear();
    
    CGContextSetStrokeColorWithColor(g, _lineColor);
    CGContextSetLineWidth(g, lineThickness);
    
    for(size_t i = 0, l = rects.size(); i < l; i++) {
        
        brf::Rectangle& rect = *rects[i];
        
        CGRect cgRect = CGRectMake(rect.x, getY(_flipped, rect.y) - rect.height, rect.width, rect.height);
        
        CGContextAddRect(g, cgRect);
    }
    
    CGContextStrokePath(g);
}

public: void drawPoint(std::shared_ptr<brf::Point> point, int radius, bool _clear = false,
		uint32_t fillColor = 0x00a0ff, double fillAlpha = 1.0) {

    if(_context == nullptr) {
        return;
    }
    
    CGColorRef _fillColor = UIColorFromRGBWithAlpha(fillColor, fillAlpha).CGColor;
    
    CGContextRef g = _context;
    
    if(_clear) clear();
    
    CGContextSetFillColorWithColor(g, _fillColor);
    CGRect rectangle = CGRectMake(0, 0, radius * 2.0, radius * 2.0);
    
    rectangle.origin.x = point->x - radius;
    rectangle.origin.y = getY(_flipped, point->y) - radius;
        
    CGContextAddEllipseInRect(g, rectangle);
    CGContextFillPath(g);
}

public: void drawPoints(std::vector< std::shared_ptr<brf::Point> >& points, int radius, bool _clear = false,
		uint32_t fillColor = 0x00a0ff, double fillAlpha = 1.0) {
    
    if(_context == nullptr) {
        return;
    }
    
    CGColorRef _fillColor = UIColorFromRGBWithAlpha(fillColor, fillAlpha).CGColor;
    
    CGContextRef g = _context;
    
    if(_clear) clear();
    
    CGContextSetFillColorWithColor(g, _fillColor);
    
    int i = 0;
    int l = (int)points.size();
    std::shared_ptr<brf::Point> point;
    
    CGRect rectangle = CGRectMake(0, 0, radius * 2.0, radius * 2.0);
    
    for(; i < l; i+=2) {
        point = points[i];
        
        rectangle.origin.x = point->x - radius;
        rectangle.origin.y = getY(_flipped, point->y) - radius;
        
        CGContextAddEllipseInRect(g, rectangle);
    }
    
    CGContextFillPath(g);
}

};

}

#endif // __brf__DrawingUtils_hpp
