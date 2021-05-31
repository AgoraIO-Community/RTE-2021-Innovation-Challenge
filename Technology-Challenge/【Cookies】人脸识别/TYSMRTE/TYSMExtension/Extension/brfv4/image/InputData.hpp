#ifndef __brf__InputData_hpp
#define __brf__InputData_hpp

namespace brf {

	class InputData {
		
		public: unsigned int width;
		public: unsigned int height;
		public: brf::ImageDataType type;

		public: InputData() : width(0), height(0), type(brf::ImageDataType::UNKNOWN) {}
		public: virtual ~InputData() {}

		public: virtual void init(unsigned int width, unsigned int height, brf::ImageDataType type) = 0;
		public: virtual uint8_t* getData() = 0;
	};
}

#endif // __brf__InputData_hpp
