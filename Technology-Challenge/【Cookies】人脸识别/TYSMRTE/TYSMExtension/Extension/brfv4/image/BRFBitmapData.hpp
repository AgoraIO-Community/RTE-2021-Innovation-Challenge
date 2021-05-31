#ifndef __brf__BRFBitmapData_hpp
#define __brf__BRFBitmapData_hpp

namespace brf {

class BRFBitmapData : public brf::InputData {

	private: unsigned int _numChannels;
	private: uint8_t* _data;

	public: BRFBitmapData();
	public: ~BRFBitmapData();

	public: void dispose();

	public: void init(unsigned int width, unsigned int height, brf::ImageDataType type);
	public: uint8_t* getData();

	public: void updateData(uint8_t* src, bool invertY = false, unsigned int maxHeight = 0);
};

brf::BRFBitmapData::BRFBitmapData():

	InputData(),

	_numChannels(0),
	_data(nullptr) {
}

brf::BRFBitmapData::~BRFBitmapData() {
	dispose();
}

void brf::BRFBitmapData::dispose() {
	if(_data != nullptr) {
		std::free(_data);
		_data = nullptr;
	}
}

void brf::BRFBitmapData::init(unsigned int _width, unsigned int _height, brf::ImageDataType _type) {

	dispose();

	width = _width;
	height = _height;
	type = _type;

	if(type == brf::ImageDataType::U8_BGR ||
			type == brf::ImageDataType::U8_RGB) {
		_numChannels = 3;
	} else if(type == brf::ImageDataType::U8_ARGB ||
			type == brf::ImageDataType::U8_BGRA ||
			type == brf::ImageDataType::U8_RGBA) {
		_numChannels = 4;
	}

	_data = (uint8_t*) std::malloc(width * height * _numChannels * sizeof(uint8_t));
}

void brf::BRFBitmapData::updateData(uint8_t* src, bool invertY, unsigned int maxHeight) {

	if(_data == nullptr) {
		return;
	}

	unsigned int srcHeight = height;
	unsigned int numChannels = _numChannels;

	if(maxHeight > 0) {
		if(maxHeight < height) {
			srcHeight = maxHeight;
		}
	}

	if(!invertY) {
		std::copy(src, src + width * srcHeight * numChannels, _data);
	} else {

		unsigned int i = 0;
		unsigned int k = srcHeight;

		uint8_t* __inputData = nullptr;
		uint8_t* __data = nullptr;

		for(; i < srcHeight; i++) {

			k--;

			__inputData = src + (k * width * numChannels);
			__data = _data + (i * width * numChannels);

			std::copy(__inputData, __inputData + width * numChannels, __data);
		}
	}
}

uint8_t* brf::BRFBitmapData::getData() {
	return _data;
}

}

#endif // __brf__BRFBitmapData_hpp
