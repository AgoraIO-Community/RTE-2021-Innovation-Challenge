#ifndef __brf__StringUtils_hpp
#define __brf__StringUtils_hpp

namespace brf {

	void trace(std::string msg);

	template < typename T >
	std::string to_string(const T& n, long p = 6) {
		std::ostringstream strStream;
		strStream.precision(p);
		strStream << n;
		return strStream.str();
	}
}

#endif // __brf__StringUtils_hpp
