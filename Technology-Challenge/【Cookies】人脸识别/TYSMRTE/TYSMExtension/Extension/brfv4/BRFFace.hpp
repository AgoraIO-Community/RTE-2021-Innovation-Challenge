#ifndef __brf__BRFFace_hpp
#define __brf__BRFFace_hpp

namespace brf {
	
	class BRFFace {

		public: std::string lastState;
		public: std::string state;
		public: std::string nextState;

		public: std::vector< double >						vertices;
		public: std::vector< int >							triangles;
		public: std::vector< std::shared_ptr<brf::Point> >	points;
		public: brf::Rectangle 								bounds;
		public: brf::Rectangle 								refRect;
		
		public: std::vector< double > 						candideVertices;
		public: std::vector< int > 							candideTriangles;

		public: double scale;
		public: double translationX;
		public: double translationY;
		public: double rotationX;
		public: double rotationY;
		public: double rotationZ;

		public: BRFFace() :

				lastState(	brf::BRFState::RESET),
				state(		brf::BRFState::RESET),
				nextState(	brf::BRFState::RESET),

				vertices(0),
				triangles({
					0, 1, 36,
					0, 17, 36,
					1, 2, 41,
					1, 36, 41,
					2, 3, 31,
					2, 31, 41,
					3, 4, 48,
					3, 31, 48,
					4, 5, 48,
					5, 6, 48,
					6, 7, 59,
					6, 48, 59,
					7, 8, 58,
					7, 58, 59,
					8, 9, 56,
					8, 56, 57,
					8, 57, 58,
					9, 10, 55,
					9, 55, 56,
					10, 11, 54,
					10, 54, 55,
					11, 12, 54,
					12, 13, 54,
					13, 14, 35,
					13, 35, 54,
					14, 15, 46,
					14, 35, 46,
					15, 16, 45,
					15, 45, 46,
					16, 26, 45,
					17, 18, 36,
					18, 19, 37,
					18, 36, 37,
					19, 20, 38,
					19, 37, 38,
					20, 21, 23,
					20, 21, 39,
					20, 38, 39,
					21, 22, 23,
					21, 22, 27,
					21, 27, 39,
					22, 23, 42,
					22, 27, 42,
					23, 24, 43,
					23, 42, 43,
					24, 25, 44,
					24, 43, 44,
					25, 26, 45,
					25, 44, 45,
					27, 28, 39,
					27, 28, 42,
					28, 29, 39,
					28, 29, 42,
					29, 30, 31,
					29, 30, 35,
					29, 31, 40,
					29, 35, 47,
					29, 40, 39,
					29, 42, 47,
					30, 31, 32,
					30, 32, 33,
					30, 33, 34,
					30, 34, 35,
					31, 32, 50,
					31, 40, 41,
					31, 48, 49,
					31, 49, 50,
					32, 33, 51,
					32, 50, 51,
					33, 34, 51,
					34, 35, 52,
					34, 51, 52,
					35, 46, 47,
					35, 52, 53,
					35, 53, 54,
					36, 37, 41,
					37, 38, 40,
					37, 40, 41,
					38, 39, 40,
					42, 43, 47,
					43, 44, 47,
					44, 45, 46,
					44, 46, 47,
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
					59, 60, 67,
					60, 61, 67,
					61, 62, 66,
					61, 66, 67,
					62, 63, 66,
					63, 64, 65,
					63, 65, 66}), //length 339),

				points(0),
				bounds(),
				refRect(),

				candideVertices(0),
				candideTriangles(0),

				scale(1.0),
				translationX(0.0),
				translationY(0.0),
				rotationX(0.0),
				rotationY(0.0),
				rotationZ(0.0)
		{
		}
	};
}

#endif // __brf__BRFFace_hpp
