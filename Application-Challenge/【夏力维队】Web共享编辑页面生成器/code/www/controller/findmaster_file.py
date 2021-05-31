#!/bin/env python
# -*- coding: utf-8 -*-
import sys
import os
import os.path

sys.path.append(os.path.dirname(os.path.abspath(__file__)))
sys.path.append(os.path.dirname(os.path.abspath(__file__)) + '/vendor/')

import re
import uuid
import time
import random
import string
import hashlib
import urllib
import copy
from functools import partial
import logging
import datetime

import markdown
import tornado
import tornado.web
import tornado.escape
import tornado.websocket
import tornado.httpclient
import tornado.gen
from tornado.escape import json_encode, json_decode

import nomagic
import nomagic.auth
import nomagic.block
from nomagic.cache import get_user, get_users, update_user, get_doc, get_docs, update_doc, get_aim, get_aims, update_aim, get_entity, get_entities, update_entity
from nomagic.cache import BIG_CACHE
from setting import settings
from setting import conn

# from user_agents import parse as uaparse #早年KJ用来判断设备使用

from .base import WebRequest
from .base import WebSocket
import pymail

from .data import DataWebSocket

import qiniu

class ImageCheckAPIHandler(WebRequest):
    @tornado.gen.coroutine
    def post(self):
        self.set_header("Access-Control-Allow-Origin", self.request.headers.get("Origin"))
        self.set_header("Access-Control-Allow-Credentials", "true")
        if not self.current_user:
            self.finish({"info": "error","about": "not login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        md5 = self.get_argument("md5",None)
        block = get_aim(block_id)
        permission = block.get("permission","private")

        editors = block.get("editors",[block.get("owner",None)])

        if not block or not md5:
            self.finish({"info": "error","about": "no block or md5"})
            return
        if permission not in ["public"]:
            if user_id not in editors:
                self.finish({"info": "error","about": "not in editors"})
                return
        gallery = block.get("gallery",[])
        if md5 in gallery:
            self.finish({"exists":True})
            return
        q = qiniu.Auth(settings["QiniuAccessKey"], settings["QiniuSecretKey"])
        uptoken = q.upload_token("ofcourse-test-0")
        self.finish({
            "token": uptoken,
            "exists": False
        })
        return
class ImageChangeAPIHandler(WebRequest):
    def post(self):
        pass
class ImageAddAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info": "error","about": "not login"})
            return
        block_id = self.get_argument("aim_id",None)
        user_id = self.current_user["id"]
        md5 = self.get_argument("md5",None)
        block = get_aim(block_id)

        permission = block.get("permission","private")
        editors = block.get("editors",[block.get("owner",None)])


        if not block or not md5:
            self.finish({"info": "error","about": "no block or md5"})
            return
        if permission not in ["public"]:
            if user_id not in editors:
                self.finish({"info": "error","about": "not in editors"})
                return
        gallery = block.get("gallery",[])

        if md5 not in gallery:
            gallery.insert(0,md5)
            block["gallery"]=gallery
            update_aim(block_id,block)
        self.finish({"info":"ok","about":"insert success","md5":md5})
class ImageDelAPIHandler(WebRequest):
    def post(self):
        pass
class VideoCheckAPIHandler(WebRequest):
    @tornado.gen.coroutine
    def post(self):
        self.set_header("Access-Control-Allow-Origin", self.request.headers.get("Origin"))
        self.set_header("Access-Control-Allow-Credentials", "true")
        if not self.current_user:
            self.finish({"info": "error","about": "not login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        md5 = self.get_argument("md5",None)
        block = get_aim(block_id)
        permission = block.get("permission","private")

        editors = block.get("editors",[block.get("owner",None)])

        if not block or not md5:
            self.finish({"info": "error","about": "no block or md5"})
            return
        if permission not in ["public"]:
            if user_id not in editors:
                self.finish({"info": "error","about": "not in editors"})
                return
        videos = block.get("videos",[])
        if md5 in videos:
            self.finish({"exists":True})
            return
        q = qiniu.Auth(settings["QiniuAccessKey"], settings["QiniuSecretKey"])
        uptoken = q.upload_token("ofcourse-test-0")
        self.finish({
            "token": uptoken,
            "exists": False
        })
        return
class VideoChangeAPIHandler(WebRequest):
    def post(self):
        pass
class VideoAddAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info": "error","about": "not login"})
            return
        block_id = self.get_argument("aim_id",None)
        user_id = self.current_user["id"]
        md5 = self.get_argument("md5",None)
        block = get_aim(block_id)

        permission = block.get("permission","private")
        editors = block.get("editors",[block.get("owner",None)])


        if not block or not md5:
            self.finish({"info": "error","about": "no block or md5"})
            return
        if permission not in ["public"]:
            if user_id not in editors:
                self.finish({"info": "error","about": "not in editors"})
                return
        videos = block.get("videos",[])

        if md5 not in videos:
            videos.insert(0,md5)
            block["videos"]=videos
            update_aim(block_id,block)
        self.finish({"info":"ok","about":"insert success","md5":md5})
class VideoDelAPIHandler(WebRequest):
    def post(self):
        pass