#ifndef __brf__ImageDataType_hpp
#define __brf__ImageDataType_hpp

namespace brf {

	enum ImageDataType : int {
		UNKNOWN	= -1,
		U8_GRAY	= 0,
//		U8_GRAY_4C = 1,
		U8_BGRA = 1,
		U8_RGBA = 2,
		U8_ARGB	= 3,
		U8_RGB	= 4,
		U8_BGR	= 5

	};
}

#endif // __brf__ImageDataType_hpp
