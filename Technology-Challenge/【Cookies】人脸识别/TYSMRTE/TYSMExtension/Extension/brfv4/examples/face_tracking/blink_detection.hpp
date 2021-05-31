#ifndef __brf__cpp__BRFCppExample_hpp
#define __brf__cpp__BRFCppExample_hpp

namespace brf {

class BRFCppExample: public BRFBasicCppExample {

public: std::vector< double > _oldFaceShapeVertices;
public: bool _blinked;
public: int _timeOut;

public: BRFCppExample() : BRFBasicCppExample(),
	_oldFaceShapeVertices(0),
	_blinked(false),
	_timeOut(-1)
{
}

public: void initCurrentExample(brf::BRFManager& brfManager, brf::Rectangle& resolution) {

	brf::trace("BRFv4 - advanced - face tracking - simple blink detection." + brf::to_string("\n")+
		"Detects a blink of the eyes.");
}

public: void updateCurrentExample(brf::BRFManager& brfManager, brf::DrawingUtils& draw) {

	brfManager.update();

	draw.clear();

	// Face detection results: a rough rectangle used to start the face tracking.

	draw.drawRects(brfManager.getAllDetectedFaces(),    false, 1.0, 0x00a1ff, 0.5);
	draw.drawRects(brfManager.getMergedDetectedFaces(), false, 2.0, 0xffd200, 1.0);

	std::vector< std::shared_ptr<brf::BRFFace> >& faces = brfManager.getFaces(); // default: one face, only one element in that array.

	for(size_t i = 0; i < faces.size(); i++) {

		brf::BRFFace& face = *faces[i];

		if(face.state == brf::BRFState::FACE_TRACKING) {

			// simple blink detection

			// A simple approach with quite a lot false positives. Fast movement can't be handled
			// properly. This code is quite good when it comes to starring contest apps though.

			// It basically compares the old positions of the eye points to the current ones.
			// If rapid movement of the current points was detected it's considered a blink.

			std::vector< double >& v = face.vertices;

			if(_oldFaceShapeVertices.size() == 0) storeFaceShapeVertices(v);

			size_t k, l;
			double yLE, yRE;

			for(k = 36, l = 41, yLE = 0; k <= l; k++) {
				yLE += v[k * 2 + 1] - _oldFaceShapeVertices[k * 2 + 1];
			}
			yLE /= 6.0;

			for(k = 42, l = 47, yRE = 0; k <= l; k++) {
				yRE += v[k * 2 + 1] - _oldFaceShapeVertices[k * 2 + 1];
			}

			yRE /= 6.0;

			double yN = 0.0;

			yN += v[27 * 2 + 1] - _oldFaceShapeVertices[27 * 2 + 1];
			yN += v[28 * 2 + 1] - _oldFaceShapeVertices[28 * 2 + 1];
			yN += v[29 * 2 + 1] - _oldFaceShapeVertices[29 * 2 + 1];
			yN += v[30 * 2 + 1] - _oldFaceShapeVertices[30 * 2 + 1];
			yN /= 4.0;

			double blinkRatio = brf::BRFv4PointUtils::abs((yLE + yRE) / yN);

			if((blinkRatio > 12 && (yLE > 0.4 || yRE > 0.4))) {
				brf::trace("blink " +
						brf::to_string(blinkRatio) + " " +
						brf::to_string(yLE) + " " +
						brf::to_string(yRE) + " " +
						brf::to_string(yN));
				blink();
			}

			uint32_t color = 0x00a0ff;

			if(_blinked) {
				color = 0xffd200;
			}

			// Face Tracking results: 68 facial feature points.

			draw.drawTriangles(	face.vertices, face.triangles, false, 1.0, color, 0.4);
			draw.drawVertices(	face.vertices, 2.0, false, color, 0.4);

			brf::trace("blink: " + brf::to_string((_blinked ? "Yes" : "No")));

			storeFaceShapeVertices(v);
		}
	}

	resetBlink();
};

private: void blink() {
	_blinked = true;
	_timeOut = 10;
}

private: void resetBlink() {
	_timeOut--;
	if(_timeOut < 0) {
		_blinked = false;
		_timeOut = -1;
	}
}

private: void storeFaceShapeVertices(std::vector< double >& vertices) {
	_oldFaceShapeVertices.resize(vertices.size());
	for(size_t i = 0, l = vertices.size(); i < l; i++) {
		_oldFaceShapeVertices[i] = vertices[i];
	}
}

};

}
#endif // __brf__cpp__BRFCppExample_hpp
