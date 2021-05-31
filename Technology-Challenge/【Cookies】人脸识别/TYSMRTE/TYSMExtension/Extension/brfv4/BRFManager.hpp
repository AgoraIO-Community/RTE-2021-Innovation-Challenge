#ifndef __brf_BRFManager_hpp
#define __brf_BRFManager_hpp

#include <cstdint>
#include <cstdlib>
#include <cmath>

#include <vector>
#include <memory>
#include <functional>

#include <string>
#include <sstream>
#include <iostream>

#include "brfv4/utils/StringUtils.hpp"

#include "brfv4/geom/Point.hpp"
#include "brfv4/geom/Rectangle.hpp"

#include "brfv4/image/ImageDataType.hpp"
#include "brfv4/image/InputData.hpp"

#include "brfv4/BRFMode.hpp"
#include "brfv4/BRFState.hpp"
#include "brfv4/BRFFace.hpp"

namespace brf {

class BRFManager {

	public: std::function< void() > onReady;

	public: BRFManager();
	public: virtual ~BRFManager() = 0;

	public: virtual void init(brf::InputData* src, brf::Rectangle* imageRoi, std::string* appId) = 0;

	public: virtual void update() = 0;
	public: virtual void reset() = 0;

	public: virtual std::string& getMode() = 0;
	public: virtual void setMode(std::string mode) = 0;

	public: virtual void setFaceDetectionRoi(brf::Rectangle* faceDetectionRoi) = 0;
	public: virtual void setFaceDetectionParams(int minFaceSize, int maxFaceSize, int stepSize, int minMergeNeighbors) = 0;
	public: virtual std::vector< std::shared_ptr<brf::Rectangle> >& getAllDetectedFaces() = 0;
	public: virtual std::vector< std::shared_ptr<brf::Rectangle> >& getMergedDetectedFaces() = 0;

	public: virtual void setNumFacesToTrack(unsigned int numFaces) = 0;
	public: virtual void setFaceTrackingStartParams(double startMinFaceWidth, double startMaxFaceWidth, double startRotationX, double startRotationY, double startRotationZ) = 0;
	public: virtual void setFaceTrackingResetParams(double resetMinFaceWidth, double resetMaxFaceWidth, double resetRotationX, double resetRotationY, double resetRotationZ) = 0;
	public: virtual std::vector< std::shared_ptr<brf::BRFFace> >& getFaces() = 0;

	public: virtual void setOpticalFlowParams(int patchSize, int numLevels, int numIterations, double error) = 0;
	public: virtual void addOpticalFlowPoints(std::vector< std::shared_ptr<brf::Point> >& pointsToAdd) = 0;

	public: virtual std::vector< std::shared_ptr<brf::Point> >& getOpticalFlowPoints() = 0;
	public: virtual std::vector< bool >& getOpticalFlowPointStates() = 0;

	public: virtual bool getOpticalFlowCheckPointsValidBeforeTracking() = 0;
	public: virtual void setOpticalFlowCheckPointsValidBeforeTracking(bool checkPointsValidBeforeTracking) = 0;

	public: static std::shared_ptr<brf::BRFManager> getInstance();

}; // class BRFManager
	
} // namespace brf

#endif // __brf_BRFManager_hpp
