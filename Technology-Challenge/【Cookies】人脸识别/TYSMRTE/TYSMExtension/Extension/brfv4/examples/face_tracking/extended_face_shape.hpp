#ifndef __brf__cpp__BRFCppExample_hpp
#define __brf__cpp__BRFCppExample_hpp

namespace brf {

class BRFCppExample: public BRFBasicCppExample {

public: brf::BRFv4ExtendedFace _extendedShape;

public: BRFCppExample() : BRFBasicCppExample(),
	_extendedShape()
{
}

public: void initCurrentExample(brf::BRFManager& brfManager, brf::Rectangle& resolution) {

	brf::trace("BRFv4 - basic - face tracking - extended face shape" + brf::to_string("\n")+
		"There are 6 additional landmarks for the forehead being calculated from the 68 landmarks.");
}

public: void updateCurrentExample(brf::BRFManager& brfManager, brf::DrawingUtils& draw) {

	brfManager.update();

	draw.clear();

	// Face detection results: a rough rectangle used to start the face tracking.

	draw.drawRects(brfManager.getAllDetectedFaces(),    false, 1.0, 0x00a1ff, 0.5);
	draw.drawRects(brfManager.getMergedDetectedFaces(), false, 2.0, 0xffd200, 1.0);

	// Get all faces. The default setup only tracks one face.

	std::vector< std::shared_ptr<brf::BRFFace> >& faces = brfManager.getFaces();

	for(size_t i = 0; i < faces.size(); i++) {

		brf::BRFFace& face = *faces[i];

		if(	face.state == brf::BRFState::FACE_TRACKING_START ||
				face.state == brf::BRFState::FACE_TRACKING) {

			// The extended face shape is calculated from the usual 68 facial features.
			// The additional landmarks are just estimated, they are not actually tracked.

			_extendedShape.update(face);

			// Then we draw all 74 landmarks of the _extendedShape.

			draw.drawTriangles(	_extendedShape.vertices, _extendedShape.triangles, false, 1.0, 0x00a0ff, 0.4);
			draw.drawVertices(	_extendedShape.vertices, 2.0, false, 0x00a0ff, 0.4);
		}
	}
};

};

}
#endif // __brf__cpp__BRFCppExample_hpp
