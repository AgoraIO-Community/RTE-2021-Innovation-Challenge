QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

CONFIG += c++11

# The following define makes your compiler emit warnings if you use
# any Qt feature that has been marked deprecated (the exact warnings
# depend on your compiler). Please consult the documentation of the
# deprecated API in order to know how to port your code away from it.
DEFINES += QT_DEPRECATED_WARNINGS

DEFINES +=  CURL_STATICLIB \
    BUILD_LIBCURL \
    HTTP_ONLY \
    CURL_DISABLE_LDAP \


# You can also make your code fail to compile if it uses deprecated APIs.
# In order to do so, uncomment the following line.
# You can also select to disable deprecated APIs only up to a certain version of Qt.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

SOURCES += \
    Demo_IntegratedClass.cpp \
    ViolationObserver.cpp \
    dialog.cpp \
    main.cpp \
    mainwindow.cpp

HEADERS += \
    Demo_IntegratedClass.h \
    ViolationObserver.h \
    dialog.h \
    mainwindow.h

FORMS += \
    dialog.ui \
    mainwindow.ui

# Default rules for deployment.
qnx: target.path = /tmp/$${TARGET}/bin
else: unix:!android: target.path = /opt/$${TARGET}/bin
!isEmpty(target.path): INSTALLS += target


LIBS += -lAdvapi32\
    -lWs2_32\
    -lWldap32\
    -lwinmm\
    -lCrypt32\
    -lNormaliz\





unix:!macx|win32: LIBS += -L$$PWD/lib_/Releasex64_lib/libjpegturbo/ -ljpeg

INCLUDEPATH += $$PWD/lib_/Releasex64_lib/libjpegturbo
DEPENDPATH += $$PWD/lib_/Releasex64_lib/libjpegturbo

win32:!win32-g++: PRE_TARGETDEPS += $$PWD/lib_/Releasex64_lib/libjpegturbo/jpeg.lib
else:unix:!macx|win32-g++: PRE_TARGETDEPS += $$PWD/lib_/Releasex64_lib/libjpegturbo/libjpeg.a

unix:!macx|win32: LIBS += -L$$PWD/lib_/Releasex64_lib/curl/ -llibcurl_a

INCLUDEPATH += $$PWD/lib_/Releasex64_lib/curl
DEPENDPATH += $$PWD/lib_/Releasex64_lib/curl

win32:!win32-g++: PRE_TARGETDEPS += $$PWD/lib_/Releasex64_lib/curl/libcurl_a.lib
else:unix:!macx|win32-g++: PRE_TARGETDEPS += $$PWD/lib_/Releasex64_lib/curl/liblibcurl_a.a

unix:!macx|win32: LIBS += -L$$PWD/lib_/Releasex64_lib/freeImage/ -lFreeImage

INCLUDEPATH += $$PWD/lib_/Releasex64_lib/freeImage
DEPENDPATH += $$PWD/lib_/Releasex64_lib/freeImage

win32:!win32-g++: PRE_TARGETDEPS += $$PWD/lib_/Releasex64_lib/freeImage/FreeImage.lib
else:unix:!macx|win32-g++: PRE_TARGETDEPS += $$PWD/lib_/Releasex64_lib/freeImage/libFreeImage.a


unix:!macx|win32: LIBS += -L$$PWD/lib_/Releasex64_lib/x86_64/ -lagora_rtc_sdk

INCLUDEPATH += $$PWD/lib_/Releasex64_lib/x86_64
DEPENDPATH += $$PWD/lib_/Releasex64_lib/x86_64

win32:!win32-g++: PRE_TARGETDEPS += $$PWD/lib_/Releasex64_lib/x86_64/agora_rtc_sdk.lib
else:unix:!macx|win32-g++: PRE_TARGETDEPS += $$PWD/lib_/Releasex64_lib/x86_64/libagora_rtc_sdk.a
