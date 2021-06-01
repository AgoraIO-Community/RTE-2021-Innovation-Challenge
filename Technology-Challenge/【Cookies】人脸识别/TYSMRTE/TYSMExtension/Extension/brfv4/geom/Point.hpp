#ifndef __brf__Point_hpp
#define __brf__Point_hpp

namespace brf {

	class Point {
		
		public: double x;
		public: double y;
		
		public: Point(double x = 0.0, double y = 0.0) :
			x(x), y(y) {
		}

		public: void setTo(double _x = 0.0, double _y = 0.0) {
			x = _x;
			y = _y;
		}
	};
}

#endif // __brf__Point_hpp
