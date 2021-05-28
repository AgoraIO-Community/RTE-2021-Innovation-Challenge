#!/bin/env python
#coding=utf-8
import sys
import os

sys.path.append(os.path.dirname(os.path.abspath(__file__)))
sys.path.append(os.path.dirname(os.path.abspath(__file__)) + '/vendor/')
#os.chdir(os.path.dirname(os.path.abspath(__file__)))

import logging
import uuid

settings = {
    "static_path": os.path.join(os.path.dirname(__file__),"static"),
    "demos_path": os.path.join(os.path.dirname(__file__),"demos"),
    "cookie_secret": "hotpoorinchina",
    "cookie_domain": "",
    "hotpoor_developers": [""],
    "QiniuAccessKey": "oAUZmGaVbXLBrfWW5MykZ2_Wr9kmS4FB_1Ij3mpo",
    "QiniuSecretKey": "lOzMXu2LZkfHOvIarlFpQ5WaOYoxqRuXX0aK_aqs",
    "QQMailSecretKey":"5TJmH7bYPgg9ejDM",
    "QQMailSecretKey1":"ChBBiPZy2ebawGJW",
    "MPWeixinID":"gh_78ae44eccd32",
    "MPWeixinAppID":"wx774240ec4bf19fe4",
    "MPWeixinAppSecret":"9690417e6754f06062a26e1c2b1105c4",


    "AgoraAppID":"8b92e0b356c2411a8dc4f8ac28c78690",
    "AgoraAppCertificate":"ad58e10b3df9432c8c5b633069c90ec7",
    # "BaiduYuyinAppID": "9082071",
    # "BaiduYuyinAPIKey": "PXirZpvwwZ9hsKqaLYcLXLzq",
    # "BaiduYuyinSecretKey":"ef6788d39df2bb689437d0cb9b6dbda6",
    "debug": True,
    # "debug": False,
    "wss_port":8201,
    "LoginCode":"automove",
    "developers":[]
}

try:
    import torndb as database
    conn = database.Connection("127.0.0.1:3306", "findmaster", "root", "root")
    conn1 = database.Connection("127.0.0.1:3306", "findmaster1", "root", "root")
    conn2 = database.Connection("127.0.0.1:3306", "findmaster2", "root", "root")
    ring = [conn1, conn2]
except:
    pass
