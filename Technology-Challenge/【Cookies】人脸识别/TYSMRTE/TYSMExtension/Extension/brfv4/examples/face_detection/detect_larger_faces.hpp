#ifndef __brf__cpp__BRFCppExample_hpp
#define __brf__cpp__BRFCppExample_hpp

namespace brf {

class BRFCppExample: public BRFBasicCppExample {

public: brf::Rectangle _faceDetectionRoi;

public: BRFCppExample() : BRFBasicCppExample(),
		_faceDetectionRoi()
{
}

public: void initCurrentExample(brf::BRFManager& brfManager, brf::Rectangle& resolution) {

	brf::trace("BRFv4 - basic - face detection - detect large faces" + brf::to_string("\n")+
		"Come closer to the webcam to see detection results.");

	// We explicitly set the mode to run in: BRFMode.FACE_DETECTION.

	brfManager.setMode(brf::BRFMode::FACE_DETECTION);

	// Then we set the face detection region of interest to be
	// most/all of the overall analysed image (green rectangle, 100%).

	_faceDetectionRoi.setTo(
		resolution.width * 0.00, resolution.height * 0.00,
		resolution.width * 1.00, resolution.height * 1.00
	);
	brfManager.setFaceDetectionRoi(&_faceDetectionRoi);

	// We can have either a landscape area (desktop), then choose height or
	// we can have a portrait area (mobile), then choose width as max face size.

	double maxFaceSize = _faceDetectionRoi.height;

	if(_faceDetectionRoi.width < _faceDetectionRoi.height) {
		maxFaceSize = _faceDetectionRoi.width;
	}

	// Merged faces (yellow) will only show up if they are at least 60% of maxFaceSize.
	// So: come really close the your webcam to see a detection result.

	// Default would be 30% of maxFaceSize as minimal value and 90% of maxFaceSize
	// as maximal value, but here we set larger desired sizes.

	brfManager.setFaceDetectionParams(maxFaceSize * 0.60, maxFaceSize * 1.00, 12, 8);
}

public: void updateCurrentExample(brf::BRFManager& brfManager, brf::DrawingUtils& draw) {

	brfManager.update();

	// Drawing the results:

	draw.clear();

	// Show the region of interest (green).

	draw.drawRect(_faceDetectionRoi,					false, 2.0, 0x8aff00, 0.5);

	// Then draw all detected faces (blue).

	draw.drawRects(brfManager.getAllDetectedFaces(),	false, 1.0, 0x00a1ff, 0.5);

	// In the end add the merged detected faces that have at least 12 detected faces
	// in a certain area (yellow).

	draw.drawRects(brfManager.getMergedDetectedFaces(),	false, 2.0, 0xffd200, 1.0);

	// Now print the face sizes:

	printSize(brfManager.getMergedDetectedFaces(), true);
};

public: void printSize(std::vector< std::shared_ptr<brf::Rectangle> >& rects, bool printAlwaysMinMax) {

	int maxWidth = 0;
	int minWidth = 9999;

	for(size_t i = 0, l = rects.size(); i < l; i++) {

		if(rects[i]->width < minWidth) {
			minWidth = rects[i]->width;
		}

		if(rects[i]->width > maxWidth) {
			maxWidth = rects[i]->width;
		}
	}

	if(maxWidth > 0) {

		std::string str = "";

		// One face or same size: name it size, otherwise name it min/max.

		if(minWidth == maxWidth && !printAlwaysMinMax) {
			str = "size: " + brf::to_string(maxWidth);
		} else {
			str = "min: " + brf::to_string(minWidth) + " max: " + brf::to_string(maxWidth);
		}

		brf::trace(str);
	}
}

};

}
#endif // __brf__cpp__BRFCppExample_hpp
