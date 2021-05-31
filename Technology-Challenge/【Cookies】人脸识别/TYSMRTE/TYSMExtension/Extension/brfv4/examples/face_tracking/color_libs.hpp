#ifndef __brf__cpp__BRFCppExample_hpp
#define __brf__cpp__BRFCppExample_hpp

namespace brf {

class BRFCppExample: public BRFBasicCppExample {

public: void initCurrentExample(brf::BRFManager& brfManager, brf::Rectangle& resolution) {

	brf::trace("BRFv4 - intermediate - face tracking - color libs." + brf::to_string("\n")+
		"Draws triangles with a certain fill color.");
}

public: void updateCurrentExample(brf::BRFManager& brfManager, brf::DrawingUtils& draw) {

	brfManager.update();

	draw.clear();

	// Face detection results: a rough rectangle used to start the face tracking.

	draw.drawRects(brfManager.getAllDetectedFaces(),	false, 1.0, 0x00a1ff, 0.5);
	draw.drawRects(brfManager.getMergedDetectedFaces(),	false, 2.0, 0xffd200, 1.0);

	std::vector< std::shared_ptr<brf::BRFFace> >& faces = brfManager.getFaces(); // default: one face, only one element in that array.

	for(size_t i = 0; i < faces.size(); i++) {

		brf::BRFFace& face = *faces[i];

		if(		face.state == brf::BRFState::FACE_TRACKING_START ||
				face.state == brf::BRFState::FACE_TRACKING) {

			// Face tracking results: 68 facial feature points.

			draw.drawTriangles(	face.vertices, face.triangles, false, 1.0, 0x00a0ff, 0.4);
			draw.drawVertices(	face.vertices, 2.0, false, 0x00a0ff, 0.4);

			// Now just draw all the triangles of the mouth in a certain color.

			draw.fillTriangles(	face.vertices, libTriangles, false, 0xff7900, 0.8);
		}
	}
};

private: inline void setPoint(std::vector< double >& v, int i, brf::Point& p) {
	brf::BRFv4PointUtils::setPoint(v, i, p);
}

public: brf::Point p0;
public: brf::Point p1;
public: brf::Point p2;
public: std::vector<int> libTriangles;

public: BRFCppExample() : BRFBasicCppExample(),
	p0(),
	p1(),
	p2(),
	libTriangles({
		48, 49, 60,
		48, 59, 60,
		49, 50, 61,
		49, 60, 61,
		50, 51, 62,
		50, 61, 62,
		51, 52, 62,
		52, 53, 63,
		52, 62, 63,
		53, 54, 64,
		53, 63, 64,
		54, 55, 64,
		55, 56, 65,
		55, 64, 65,
		56, 57, 66,
		56, 65, 66,
		57, 58, 66,
		58, 59, 67,
		58, 66, 67,
		59, 60, 67
		//,					// mouth whole
		// 60, 61, 67,
		// 61, 62, 66,
		// 61, 66, 67,
		// 62, 63, 66,
		// 63, 64, 65,
		// 63, 65, 66
	})
{
}

};

}
#endif // __brf__cpp__BRFCppExample_hpp
