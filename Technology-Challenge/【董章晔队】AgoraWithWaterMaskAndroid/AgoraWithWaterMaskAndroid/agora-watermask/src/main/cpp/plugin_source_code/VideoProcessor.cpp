#include "JniHelper.h"

#include "VideoProcessor.h"

#include <chrono>


#include "../logutils.h"
#include "rapidjson/document.h"
#include "rapidjson/writer.h"
#include "rapidjson/stringbuffer.h"

#include "error_code.h"


#include "opencv2/video/tracking.hpp"
#include "opencv2/videoio.hpp"
#include "opencv2/highgui.hpp"
#include "opencv2/imgproc/types_c.h"



#define CHECK_BEF_AI_RET_SUCCESS(ret, ...) \
if(ret != 0){\
    PRINTF_ERROR(__VA_ARGS__);\
}

namespace agora {
    namespace extension {
        using namespace rapidjson;
        bool WaterMaskProcessor::initOpenGL() {
            const std::lock_guard<std::mutex> lock(mutex_);
#if defined(__ANDROID__) || defined(TARGET_OS_ANDROID)
            if (!eglCore_) {
                eglCore_ = new EglCore();
                //offscreenSurface_ = eglCore_->createOffscreenSurface(640, 320);
                offscreenSurface_ = eglCore_->createOffscreenSurface(600, 270);

            }
            if  (!eglCore_->isCurrent(offscreenSurface_)) {
                eglCore_->makeCurrent(offscreenSurface_);
            }
#endif
            return true;
        }

        bool WaterMaskProcessor::releaseOpenGL() {
            const std::lock_guard<std::mutex> lock(mutex_);

#if defined(__ANDROID__) || defined(TARGET_OS_ANDROID)
            if (eglCore_) {
                if (offscreenSurface_) {
                    eglCore_->releaseSurface(offscreenSurface_);
                }
                delete eglCore_;
                eglCore_ = nullptr;
            }
#endif
            return true;
        }

cv::Mat WaterMaskProcessor::optimizeImageDim(cv::Mat image)
{
	// init
	cv::Mat padded;
	// get the optimal rows size for dft
	int addPixelRows = cv::getOptimalDFTSize(image.rows);
	// get the optimal cols size for dft
	int addPixelCols = cv::getOptimalDFTSize(image.cols);
	// apply the optimal cols and rows size to the image
	cv::copyMakeBorder(image, padded, 0, addPixelRows - image.rows, 0, addPixelCols - image.cols,
		cv::BORDER_CONSTANT, cv::Scalar::all(0));

	return padded;
}

cv::Mat WaterMaskProcessor::antitransformImage()
{
	cv::Mat invDFT = cv::Mat();
	cv::idft(complexImage, invDFT, cv::DFT_SCALE | cv::DFT_REAL_OUTPUT, 0);
	cv::Mat restoredImage = cv::Mat();
	invDFT.convertTo(restoredImage, CV_8U);
	planes.clear();
	return restoredImage;
}
void WaterMaskProcessor::transformImageWithText(cv::Mat image, cv::String watermarkText,
	cv::Point point, double fontSize, cv::Scalar scalar)
{
	// planes.
	if (!planes.empty()) {
		planes.clear();
	}
	if (fontSize == 0.0)
	{
		fontSize = 2.0;
	}
	// optimize the dimension of the loaded image
	cv::Mat padded = optimizeImageDim(image);
	padded.convertTo(padded, CV_32F);
	//printf("padded types %d CV_32FC1 %d\n", padded.type(), CV_32F);
	// prepare the image planes to obtain the complex image
	planes.push_back(padded);
	planes.push_back(cv::Mat::zeros(padded.size(), CV_32F));
	// prepare a complex image for performing the dft
	cv::merge(planes, complexImage);
	//printf("complexImage types %d\n", complexImage.type());
	// dft
	cv::dft(complexImage, complexImage);

	cv::putText(complexImage, watermarkText, point, cv::FONT_HERSHEY_DUPLEX, fontSize, scalar, 3);
	cv::flip(complexImage, complexImage, -1);
	cv::putText(complexImage, watermarkText, point, cv::FONT_HERSHEY_DUPLEX, fontSize, scalar, 3);
	cv::flip(complexImage, complexImage, -1);
	planes.clear();
}
//end

        void WaterMaskProcessor::processEffect(const agora::media::base::VideoFrame &capturedFrame) {
            ///dzy
            cv::Mat oriImg(capturedFrame.height, capturedFrame.yStride, CV_8U,
                         (void*)capturedFrame.yBuffer);
            //cv::blur(img1, img1, cv::Size(3, 3));//进行模糊
            //进行canny边缘检测并显示
            //cv::Canny(img1, img1, 0, 30, 3);
            double frontSize=oriImg.cols / 800;
            cv::Point point(40, 60);
            cv::Scalar scalar(0, 0, 0, 0);
            transformImageWithText(oriImg, wmStr_, point, frontSize, scalar);
            cv::Mat wmImg = antitransformImage();
            int ysize = capturedFrame.yStride * capturedFrame.height;
            memcpy(capturedFrame.yBuffer,wmImg.data, ysize);

            ///
        }


        int WaterMaskProcessor::processFrame(const agora::media::base::VideoFrame &capturedFrame) {
//            PRINTF_INFO("processFrame: w: %d,  h: %d,  r: %d", capturedFrame.width, capturedFrame.height, capturedFrame.rotation);
            const std::lock_guard<std::mutex> lock(mutex_);

            if (wmEffectEnabled_) {
                processEffect(capturedFrame);
            }

            return 0;
        }


        int WaterMaskProcessor::releaseEffectEngine() {
            const std::lock_guard<std::mutex> lock(mutex_);

            wmEffectEnabled_ = false;
            wmStr_="wm";
            if (yuvBuffer_) {
                free(yuvBuffer_);
                yuvBuffer_ = nullptr;
            }


            return 0;
        }

        int WaterMaskProcessor::setParameters(std::string parameter) {
            const std::lock_guard<std::mutex> lock(mutex_);
            Document d;
            d.Parse(parameter.c_str());
            if (d.HasParseError()) {
                return -ERROR_INVALID_JSON;
            }


            if (d.HasMember("plugin.watermask.wmEffectEnabled")) {
                Value& enabled = d["plugin.watermask.wmEffectEnabled"];
                if (!enabled.IsBool()) {
                    return -ERROR_INVALID_JSON_TYPE;
                }
                wmEffectEnabled_ = enabled.GetBool();
            }

            if (d.HasMember("plugin.watermask.wmStr")) {
                Value& wmStr = d["plugin.watermask.wmStr"];
                if (!wmStr.IsString()) {
                    return -ERROR_INVALID_JSON_TYPE;
                }
                wmStr_ = wmStr.GetString();
            }

            return 0;
        }

        std::thread::id WaterMaskProcessor::getThreadId() {
            std::thread::id id = std::this_thread::get_id();
            return id;
        }

    }
}
