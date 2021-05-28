#include <stdlib.h>

#include <opencv2/core/utility.hpp>
#include <opencv2/video/tracking.hpp>
#include <opencv2/videoio.hpp>
#include <opencv2/highgui.hpp>

#include "opencv2/imgproc/types_c.h"

std::vector<cv::Mat> planes;
cv::Mat complexImage;
void shiftDFT(cv::Mat image)
{
	image = image(cv::Rect(0, 0, image.cols & -2, image.rows & -2));
	int cx = image.cols / 2;
	int cy = image.rows / 2;

	cv::Mat q0 = cv::Mat(image, cv::Rect(0, 0, cx, cy));
	cv::Mat q1 = cv::Mat(image, cv::Rect(cx, 0, cx, cy));
	cv::Mat q2 = cv::Mat(image, cv::Rect(0, cy, cx, cy));
	cv::Mat q3 = cv::Mat(image, cv::Rect(cx, cy, cx, cy));

	cv::Mat tmp = cv::Mat();
	q0.copyTo(tmp);
	q3.copyTo(q0);
	tmp.copyTo(q3);

	q1.copyTo(tmp);
	q2.copyTo(q1);
	tmp.copyTo(q2);
}

cv::Mat optimizeImageDim(cv::Mat image)
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
cv::Mat createOptimizedMagnitude(cv::Mat complexImage)
{
	// init
	std::vector<cv::Mat> newPlanes;
	cv::Mat mag = cv::Mat();
	// split the comples image in two planes
	cv::split(complexImage, newPlanes);
	// compute the magnitude
	cv::magnitude(newPlanes[0], newPlanes[1], mag);

	// move to a logarithmic scale
	cv::add(cv::Mat::ones(mag.size(), CV_32F), mag, mag);
	cv::log(mag, mag);
	// optionally reorder the 4 quadrants of the magnitude image
	shiftDFT(mag);
	// normalize the magnitude image for the visualization since both JavaFX
	// and OpenCV need images with value between 0 and 255
	// convert back to CV_8UC1
	mag.convertTo(mag, CV_8UC1);
	cv::normalize(mag, mag, 0, 255, cv::NORM_MINMAX, CV_8UC1);

	return mag;
}

cv::Mat transformImage(cv::Mat image)
{
	// planes.
	if (!planes.empty()) {
		planes.clear();
	}
	// optimize the dimension of the loaded image
	cv::Mat padded = optimizeImageDim(image);
	padded.convertTo(padded, CV_32F);
	// prepare the image planes to obtain the complex image
	planes.push_back(padded);
	planes.push_back(cv::Mat::zeros(padded.size(), CV_32F));
	// prepare a complex image for performing the dft
	cv::merge(planes, complexImage);
	// dft
	printf("complexImage types %d\n", complexImage.type());
	cv::dft(complexImage, complexImage);

	// optimize the image resulting from the dft operation
	cv::Mat magnitude = createOptimizedMagnitude(complexImage);
	planes.clear();
	return magnitude;
}

int main(int argc, char* argv[])
{
	if (argc < 2)
	{
		printf("please input picture_name [e.g.1.jpg]\n");
		return 1;
	}
	printf("read file %s\n", argv[1]);
	cv::Mat img1 = cv::imread(argv[1], cv::IMREAD_GRAYSCALE);
	cv::Mat img2 = transformImage(img1);

	cv::namedWindow("decodeWaterMask", cv::WINDOW_AUTOSIZE);
	cv::imshow("decodeWaterMask", img2);
	cv::imwrite("decodeWaterMask.jpg", img2);
	
	cv::waitKey(0);
	cv::destroyAllWindows();
	return 0;
}