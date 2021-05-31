#ifndef __brf__BRFv4ExtendedFace_hpp
#define __brf__BRFv4ExtendedFace_hpp

namespace brf {
	
	class BRFv4ExtendedFace {

		public: std::vector< double >						vertices;
		public: std::vector< int >							triangles;
		public: std::vector< std::shared_ptr<brf::Point> >	points;
		public: brf::Rectangle 								bounds;
		
		public: brf::Point	 								_tmpPoint0;
		public: brf::Point	 								_tmpPoint1;
		public: brf::Point	 								_tmpPoint2;
		public: brf::Point	 								_tmpPoint3;
		public: brf::Point	 								_tmpPoint4;
		public: brf::Point	 								_tmpPoint5;

		public: BRFv4ExtendedFace() :

				vertices(0),
				triangles(0),

				points(0),
				bounds(),

				_tmpPoint0(),
				_tmpPoint1(),
				_tmpPoint2(),
				_tmpPoint3(),
				_tmpPoint4(),
				_tmpPoint5()
		{
		}

		public: void update(brf::BRFFace& face) {

			size_t i, l;

			for(i = points.size(), l = face.points.size() + 6; i < l; ++i) {
				points.push_back(std::shared_ptr<brf::Point>(new brf::Point(0.0, 0.0)));
			}

			generateExtendedVertices(face);
			generateExtendedTriangles(face);
			updateBounds();
			updatePoints();
		}

		public: void generateExtendedVertices(brf::BRFFace& face) {

			std::vector< double >& v = face.vertices;
			size_t i, l;

			vertices.clear();

			for(i = 0, l = v.size(); i < l; i++) {
				vertices.push_back(v[i]);
			}

			addUpperForeheadPoints(vertices);
		}

		public: void generateExtendedTriangles(brf::BRFFace& face) {
			if(triangles.size() == 0) {

				size_t i, l;

				for(i = 0, l = face.triangles.size(); i < l; i++) {
					triangles.push_back(face.triangles[i]);
				}

				std::vector< int > tmp({
					0, 17, 68,
					17, 18, 68,
					18, 19, 69,
					18, 68, 69,
					19, 20, 69,
					20, 23, 71,
					20, 69, 70,
					20, 70, 71,
					23, 24, 72,
					23, 71, 72,
					24, 25, 72,
					25, 26, 73,
					25, 72, 73,
					16, 26, 73
				});

				for(i = 0, l = tmp.size(); i < l; i++) {
					triangles.push_back(tmp[i]);
				}
			}
		}

		public: void updateBounds() {

			double minX = 0;
			double minY = 0;
			double maxX = 99999;
			double maxY = 99999;

			size_t i, l;
			double value;

			for(i = 0, l = vertices.size(); i < l; i++) {
				value = vertices[i];

				if((i % 2) == 0) {
					if(value < minX) minX = value;
					if(value > maxX) maxX = value;
				} else {
					if(value < minY) minY = value;
					if(value > maxY) maxY = value;
				}
			}

			bounds.x = minX;
			bounds.y = minY;
			bounds.width = maxX - minX;
			bounds.height = maxY - minY;
		}

		public: void updatePoints() {

			size_t i, k, l;
			double x, y;

			for(i = 0, k = 0, l = points.size(); i < l; ++i) {
				x = vertices[k]; k++;
				y = vertices[k]; k++;

				points[i]->x = x;
				points[i]->y = y;
			}
		}

		public: void addUpperForeheadPoints(std::vector< double >& v) {

			brf::Point& p0 = _tmpPoint0;
			brf::Point& p1 = _tmpPoint1;
			brf::Point& p2 = _tmpPoint2;
			brf::Point& p3 = _tmpPoint3;
			brf::Point& p4 = _tmpPoint4;
			brf::Point& p5 = _tmpPoint5;

			// base distance

			setPoint(v, 33, p0); // nose top
			setPoint(v, 27, p1); // nose base
			double baseDist = calcDistance(p0, p1) * 1.5;

			// eyes as base line for orthogonal vector

			setPoint(v, 39, p0); // left eye inner corner
			setPoint(v, 42, p1); // right eye inner corner

			double distEyes = calcDistance(p0, p1);

			calcMovementVectorOrthogonalCCW(p4, p0, p1, baseDist / distEyes);

			// orthogonal line for intersection point calculation

			setPoint(v, 27, p2); // nose top
			applyMovementVector(p3, p2, p4, 10.95);
			applyMovementVector(p2, p2, p4, -10.95);

			calcIntersectionPoint(p5, p2, p3, p0, p1);

			// simple head rotation

			double f = 0.5-calcDistance(p0, p5) / distEyes;

			// outer left forehead point

			setPoint(v, 0, p5); // top left outline point
			double dist = calcDistance(p0, p5) * 0.75;

			interpolatePoint(		p2, p0, p1, (dist / -distEyes));
			applyMovementVector(	p3, p2, p4, 0.75);
			addToExtendedVertices(	p3);

			// upper four forehead points

			interpolatePoint(		p2, p0, p1, f - 0.65);
			applyMovementVector(	p3, p2, p4, 1.02);
			addToExtendedVertices(	p3);

			interpolatePoint(		p2, p0, p1, f + 0.0);
			applyMovementVector(	p3, p2, p4, 1.10);
			addToExtendedVertices(	p3);

			interpolatePoint(		p2, p0, p1, f + 1.0);
			applyMovementVector(	p3, p2, p4, 1.10);
			addToExtendedVertices(	p3);

			interpolatePoint(		p2, p0, p1, f + 1.65);
			applyMovementVector(	p3, p2, p4, 1.02);
			addToExtendedVertices(	p3);

			// outer right forehead point

			setPoint(v, 16, p5); // top right outline point
			dist = calcDistance(p1, p5) * 0.75;

			interpolatePoint(		p2, p1, p0, (dist / -distEyes));
			applyMovementVector(	p3, p2, p4, 0.75);
			addToExtendedVertices(	p3);
		}

		private: void addToExtendedVertices(brf::Point& p) {
			vertices.push_back(p.x);
			vertices.push_back(p.y);
		};

		private: inline void setPoint(std::vector< double >& v, int i, brf::Point& p) {
			brf::BRFv4PointUtils::setPoint(v, i, p);
		}

		private: inline double calcDistance(brf::Point& p0, brf::Point& p1) {
			return brf::BRFv4PointUtils::calcDistance(p0, p1);
		}

		private: inline void applyMovementVector(brf::Point& p, brf::Point& p0, brf::Point& pmv, double f) {
			brf::BRFv4PointUtils::applyMovementVector(p, p0, pmv, f);
		}

		private: inline void interpolatePoint(brf::Point& p, brf::Point& p0, brf::Point& p1, double f) {
			brf::BRFv4PointUtils::interpolatePoint(p, p0, p1, f);
		}

		private: inline void calcMovementVector(brf::Point& p, brf::Point& p0, brf::Point& p1, double f) {
			brf::BRFv4PointUtils::calcMovementVector(p, p0, p1, f);
		}

		private: inline void calcMovementVectorOrthogonalCW(brf::Point& p, brf::Point& p0, brf::Point& p1, double f) {
			brf::BRFv4PointUtils::calcMovementVectorOrthogonalCW(p, p0, p1, f);
		}

		private: inline void calcMovementVectorOrthogonalCCW(brf::Point& p, brf::Point& p0, brf::Point& p1, double f) {
			brf::BRFv4PointUtils::calcMovementVectorOrthogonalCCW(p, p0, p1, f);
		}

		private: inline void calcIntersectionPoint(brf::Point& p, brf::Point& pk0, brf::Point& pk1, brf::Point& pg0, brf::Point& pg1) {
			brf::BRFv4PointUtils::calcIntersectionPoint(p, pk0, pk1, pg0, pg1);
		}
	};
}

#endif // __brf__BRFv4ExtendedFace_hpp
