#ifndef __brf__cpp__BRFCppExample_hpp
#define __brf__cpp__BRFCppExample_hpp

namespace brf {

class BRFCppExample: public BRFBasicCppExample {

public: int numFacesToTrack;

public: BRFCppExample() : BRFBasicCppExample(),
		numFacesToTrack(2)
{
}

public: void initCurrentExample(brf::BRFManager& brfManager, brf::Rectangle& resolution) {

	brf::trace("BRFv4 - basic - face tracking - track multiple faces" + brf::to_string("\n")+
			"Detect and track " + brf::to_string(numFacesToTrack) + " faces and draw their 68 facial landmarks.");

	// By default everything necessary for a single face tracking app
	// is set up for you in brfManager.init.

	// But here we tell BRFv4 to track multiple faces. In this case two.

	// While the first face is getting tracked the face detection
	// is performed in parallel and is looking for a second face.

	brfManager.setNumFacesToTrack(numFacesToTrack);

	// Relax starting conditions to eventually find more faces.

	double maxFaceSize = resolution.height;

	if(resolution.width < resolution.height) {
		maxFaceSize = resolution.width;
	}

	brfManager.setFaceDetectionParams(		maxFaceSize * 0.40, maxFaceSize * 1.00, 12, 8);
	brfManager.setFaceTrackingStartParams(	maxFaceSize * 0.40, maxFaceSize * 1.00, 32, 35, 32);
	brfManager.setFaceTrackingResetParams(	maxFaceSize * 0.30, maxFaceSize * 1.00, 40, 55, 32);
}

public: void updateCurrentExample(brf::BRFManager& brfManager, brf::DrawingUtils& draw) {

	brfManager.update();

	// Drawing the results:

	draw.clear();

	// Get all faces. We get numFacesToTrack faces in that array.

	std::vector< std::shared_ptr<brf::BRFFace> >& faces = brfManager.getFaces();

	for(size_t i = 0; i < faces.size(); i++) {

		brf::BRFFace& face = *faces[i];

		// Every face has it's own states.
		// While the first face might already be tracking,
		// the second face might just try to detect a face.

		if(face.state == brf::BRFState::FACE_DETECTION) {

			// Face detection results: a rough rectangle used to start the face tracking.

			draw.drawRects(brfManager.getMergedDetectedFaces(), false, 2.0, 0xffd200, 1.0);

		} else if(	face.state == brf::BRFState::FACE_TRACKING_START ||
					face.state == brf::BRFState::FACE_TRACKING) {

			// Face tracking results: 68 facial feature points.

			draw.drawTriangles(	face.vertices, face.triangles, false, 1.0, 0x00a0ff, 0.4);
			draw.drawVertices(	face.vertices, 2.0, false, 0x00a0ff, 0.4);
		}
	}
};

};

}
#endif // __brf__cpp__BRFCppExample_hpp
