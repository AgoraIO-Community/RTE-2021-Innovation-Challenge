#ifndef __brf__cpp__Basic_Example_hpp
#define __brf__cpp__Basic_Example_hpp

namespace brf {
    
    class BRFBasicCppExample {
        
    public: std::string							_appId;
        
    public: brf::BRFBitmapData					_bmd;
    public: brf::DrawingUtils					_drawing;
        
    public: brf::Rectangle						_brfImageRoi;
    public: brf::Rectangle 						_brfFaceDectionRoi;
    public: std::shared_ptr<brf::BRFManager>	_brfManager;
        
    public: bool 								_initialized;
        
    public: brf::Point                          p0;
    public: brf::Point                          p1;
        
    public: BRFBasicCppExample() :
        
        _appId("com.tastenkunst.brfv4.cpp.examples"), // Choose your own app id. 8 chars minimum.
        
        _bmd(),
        _drawing(),
        
        p0(),
        p1(),
        
        _brfImageRoi(),
        _brfFaceDectionRoi(),
        _brfManager(nullptr),
        
        _initialized(false)
        {
        }
        
    public: virtual ~BRFBasicCppExample() {
        dispose();
    }
        
    public: void dispose() {
        if(_brfManager != nullptr) {
            reset();
            _brfManager = nullptr;
        }
    }
        
    public: void init(unsigned int width, unsigned int height) {
        // Initialize BRF with the image data and the region of interest.
        // It's the same size in this case (-> analyzing the whole image).
        
        _brfManager = brf::BRFManager::getInstance();
        _brfManager->onReady = [this]{
            
            double size = _brfImageRoi.height;
            
            if(_brfImageRoi.width < _brfImageRoi.height) {
                size = _brfImageRoi.width;
            }
            
            _brfManager->setFaceDetectionParams(		size * 0.30, size * 0.90, 12, 8);
            _brfManager->setFaceTrackingStartParams(	size * 0.30, size * 0.90, 22, 26, 22);
            _brfManager->setFaceTrackingResetParams(	size * 0.25, size * 0.90, 40, 55, 32);
            
            _initialized = true;
        };
        
        _brfManager->init(&_bmd, &_brfImageRoi, &_appId);
    }
        
    public: void configBmd(unsigned int width, unsigned int height, brf::ImageDataType type) {
        _bmd.init(width, height, type);
    }
        
    public: void configFaceDetictionRoi(unsigned int width, unsigned height) {
        _brfFaceDectionRoi.setTo(0, 0, width, height);
    }
        
    public: void configImageRoi(unsigned int width, unsigned height) {
        _brfImageRoi.setTo(0, 0, width, height);
    }
        
    public: void configLayout(unsigned int width, unsigned height) {
        _drawing.updateLayout(width, height);
    }
        
    public: void reset() {
        if(_brfManager != nullptr) {
            _brfManager->reset();
        }
    }
        
    public: void update(uint8_t* imageData) {
        _bmd.updateData(imageData);
    }
        
    public: void updateFace() {
        // In a webcam example imageData is the mirrored webcam video feed.
        // In an image example imageData is the (not mirrored) image content.
        
        _brfManager->update();
        
        // Drawing the results:
        
        _drawing.clear();
        
        // Face detection results: a rough rectangle used to start the face tracking.
        
        _drawing.drawRects(_brfManager->getAllDetectedFaces(),    false, 1.0, 0x00a1ff, 0.5);
        _drawing.drawRects(_brfManager->getMergedDetectedFaces(), false, 2.0, 0xffd200, 1.0);
        
        // Get all faces. The default setup only tracks one face.
        
        std::vector< std::shared_ptr<brf::BRFFace> >& faces = _brfManager->getFaces();
        
        for(size_t i = 0; i < faces.size(); i++) {
            
            brf::BRFFace& face = *faces[i];
            
            if(        face.state == brf::BRFState::FACE_TRACKING_START ||
               face.state == brf::BRFState::FACE_TRACKING) {
                
                // Face tracking results: 68 facial feature points.
                
                _drawing.drawTriangles(    face.vertices, face.triangles, false, 1.0, 0x00a0ff, 0.4);
                _drawing.drawVertices(    face.vertices, 2.0, false, 0x00a0ff, 0.4);
            }
        }
    }
        
    public: double updateSmille() {
        _brfManager->update();
        
        _drawing.clear();
        
        // Face detection results: a rough rectangle used to start the face tracking.
        
        _drawing.drawRects(_brfManager->getAllDetectedFaces(),    false, 1.0, 0x00a1ff, 0.5);
        _drawing.drawRects(_brfManager->getMergedDetectedFaces(),    false, 2.0, 0xffd200, 1.0);
        
        std::vector< std::shared_ptr<brf::BRFFace> >& faces = _brfManager->getFaces(); // default: one face, only one element in that array.
        
        for(size_t i = 0; i < faces.size(); i++) {
            
            brf::BRFFace& face = *faces[i];
            
            if(        face.state == brf::BRFState::FACE_TRACKING_START ||
               face.state == brf::BRFState::FACE_TRACKING) {
                
                // Smile Detection
                
                setPoint(face.vertices, 48, p0); // mouth corner left
                setPoint(face.vertices, 54, p1); // mouth corner right
                
                double mouthWidth = calcDistance(p0, p1);
                
                setPoint(face.vertices, 39, p1); // left eye inner corner
                setPoint(face.vertices, 42, p0); // right eye outer corner
                
                double eyeDist = calcDistance(p0, p1);
                double smileFactor = mouthWidth / eyeDist;
                
                smileFactor -= 1.40; // 1.40 - neutral, 1.70 smiling
                
                if(smileFactor > 0.25) smileFactor = 0.25;
                if(smileFactor < 0.00) smileFactor = 0.00;
                
                smileFactor *= 4.0;
                
                if(smileFactor < 0.0) { smileFactor = 0.0; }
                if(smileFactor > 1.0) { smileFactor = 1.0; }
                
                // Let the color show you how much you are smiling.
                
                uint32_t color =
                ((((uint32_t)(0xff * (1.0 - smileFactor)) & 0xff) << 16)) +
                (((uint32_t)(0xff * smileFactor) & 0xff) << 8);
                
                // Face Tracking results: 68 facial feature points.
                
                _drawing.drawTriangles(    face.vertices, face.triangles, false, 1.0, color, 0.4);
                _drawing.drawVertices(    face.vertices, 2.0, false, color, 0.4);
                
                return smileFactor;
            }
        }
        return -1.0;
    }
        
    public: double updateYawn() {
        _brfManager->update();
        
        _drawing.clear();
        
        // Face detection results: a rough rectangle used to start the face tracking.
        
        _drawing.drawRects(_brfManager->getAllDetectedFaces(),    false, 1.0, 0x00a1ff, 0.5);
        _drawing.drawRects(_brfManager->getMergedDetectedFaces(),    false, 2.0, 0xffd200, 1.0);
        
        std::vector< std::shared_ptr<brf::BRFFace> >& faces = _brfManager->getFaces(); // default: one face, only one element in that array.
        
        for(size_t i = 0; i < faces.size(); i++) {
            
            brf::BRFFace& face = *faces[i];
            
            if(        face.state == brf::BRFState::FACE_TRACKING_START ||
               face.state == brf::BRFState::FACE_TRACKING) {
                
                // Yawn Detection - Or: How wide open is the mouth?
                
                setPoint(face.vertices, 39, p1); // left eye inner corner
                setPoint(face.vertices, 42, p0); // right eye outer corner
                
                double eyeDist = calcDistance(p0, p1);
                
                setPoint(face.vertices, 62, p0); // mouth corner left
                setPoint(face.vertices, 66, p1); // mouth corner right
                
                double mouthOpen = calcDistance(p0, p1);
                double yawnFactor = mouthOpen / eyeDist;
                
                yawnFactor -= 0.35; // remove smiling
                
                if(yawnFactor < 0) yawnFactor = 0;
                
                yawnFactor *= 2.0; // scale up a bit
                
                if(yawnFactor > 1.0) yawnFactor = 1.0;
                
                if(yawnFactor < 0.0) { yawnFactor = 0.0; }
                if(yawnFactor > 1.0) { yawnFactor = 1.0; }
                
                // Let the color show you how much you yawn.
                
                uint32_t color =
                ((((uint32_t)(0xff * (1.0 - yawnFactor)) & 0xff) << 16)) +
                (((uint32_t)(0xff * yawnFactor) & 0xff) << 8);
                
                // Face Tracking results: 68 facial feature points.
                
                _drawing.drawTriangles(    face.vertices, face.triangles, false, 1.0, color, 0.4);
                _drawing.drawVertices(    face.vertices, 2.0, false, color, 0.4);
                
                //                brf::trace(brf::to_string((int)(yawnFactor * 100)) + "%");
                
                return yawnFactor;
                
            }
        }
        return -1.0;
    }
        
    private: inline void setPoint(std::vector< double >& v, int i, brf::Point& p) {
        brf::BRFv4PointUtils::setPoint(v, i, p);
    }
        
    private: inline double calcDistance(brf::Point& p0, brf::Point& p1) {
        return brf::BRFv4PointUtils::calcDistance(p0, p1);
    }
        
    };
    
}

#endif // __brf__cpp__Basic_Example_hpp
