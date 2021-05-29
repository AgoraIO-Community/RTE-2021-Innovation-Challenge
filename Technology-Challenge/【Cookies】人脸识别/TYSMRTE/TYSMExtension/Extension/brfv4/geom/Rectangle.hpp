#ifndef __brf__Rectangle_hpp
#define __brf__Rectangle_hpp

namespace brf {
	
	class Rectangle {
		
		public: double x;
		public: double y;
		public: double width;
		public: double height;

		public: Rectangle(double x = 0.0, double y = 0.0, double width = 0.0, double height = 0.0) :
			x(x), y(y), width(width), height(height) {
		}

		public: void setTo(double _x = 0.0, double _y = 0.0, double _width = 0.0, double _height = 0.0) {
			x = _x;
			y = _y;
			width = _width;
			height = _height;
		}
		
		/**
		 * Floors x and y to multiple of the raster parameter.
		 * Floors width and height to multiple of the raster parameter and adds value of raster to them.
		 *
		 * @param raster - a choosen raster, e.g. 4.0 or 0.1.
		 */
		public: void rasterBy(double raster) {

			if(raster < 0) raster = -raster;

			double rf = 1;

			while(raster < 1) {
				raster *= 10;
				rf *= 10;
			}

			long rRaster = (long)raster;

			long rx = (long)floor(x * rf);
			long ry = (long)floor(y * rf);
			long rw = (long)floor(width * rf);
			long rh = (long)floor(height * rf);

			x		= rx - (rx % rRaster);
			y		= ry - (ry % rRaster);
			width	= rw - (rw % rRaster) + rRaster;
			height 	= rh - (rh % rRaster) + rRaster;

			x /= rf;
			y /= rf;
			width /= rf;
			height /= rf;
		}

		public: bool contains(double _x, double _y) {
			if(_x >= x && _x <= x + width && _y >= y && _y <= y + height) {
				return true;
			}
			return false;
		}
	};
}

#endif // __brf__Rectangle_hpp
