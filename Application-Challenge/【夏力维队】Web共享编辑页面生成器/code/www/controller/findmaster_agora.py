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
from random import randint
from AgoraDynamicKey.src.RtcTokenBuilder import RtcTokenBuilder,Role_Attendee



class GetTokenAPIHandler(WebRequest):
    def post(self):
        agora_user_id = self.get_argument("device_user",None)
        aim_id = self.get_argument("aim_id",None)

        appID = settings["AgoraAppID"]
        appCertificate = settings["AgoraAppCertificate"]
        channelName = aim_id
        userAccount = agora_user_id
        expireTimeInSeconds = 3600
        currentTimestamp = int(time.time())
        privilegeExpiredTs = currentTimestamp + expireTimeInSeconds
        token = RtcTokenBuilder.buildTokenWithAccount(appID, appCertificate, channelName, userAccount, Role_Attendee, privilegeExpiredTs)
        self.finish({"info":"ok","about":"agora token success","token":token,"appid":appID})

class GetCustomerTokenAPIHandler(WebRequest):
    @tornado.gen.coroutine
    def post(self):
        self.set_header("Access-Control-Allow-Origin", self.request.headers.get("Origin"))
        self.set_header("Access-Control-Allow-Credentials", "true")
        agora_user_id = self.get_argument("device_user",None)
        aim_id = self.get_argument("aim_id",None)
        appID = self.get_argument("app_id",None)
        appCertificate = self.get_argument("app_certificate",None)
        if not (agora_user_id and aim_id and appID and appCertificate):
            self.finish({"info":"error","about":"error info"})
            return
        channelName = aim_id
        userAccount = agora_user_id
        expireTimeInSeconds = 3600
        currentTimestamp = int(time.time())
        privilegeExpiredTs = currentTimestamp + expireTimeInSeconds
        token = RtcTokenBuilder.buildTokenWithAccount(appID, appCertificate, channelName, userAccount, Role_Attendee, privilegeExpiredTs)
        self.finish({"info":"ok","about":"agora token success","token":token,"appid":appID})






