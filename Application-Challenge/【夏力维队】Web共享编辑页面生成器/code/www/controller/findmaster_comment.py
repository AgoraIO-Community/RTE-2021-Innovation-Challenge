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
import nomagic.group
import nomagic.comment
from nomagic.cache import get_user, get_users, update_user, get_doc, get_docs, update_doc, get_aim, get_aims, update_aim, get_entity, get_entities, update_entity
from nomagic.cache import BIG_CACHE
from setting import settings
from setting import conn

# from user_agents import parse as uaparse #早年KJ用来判断设备使用

from .base import WebRequest
from .base import WebSocket
import pymail

from .data import DataWebSocket

class AddCommentAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        block = get_aim(block_id)
        #判断是否为编辑者
        if user_id not in block.get("editors",[block.get("owner",None)]):
        # if block.get("owner",None) != user_id:
            self.finish({"info":"error","about":"no in editors"})
            return
        comment_entities = block.get("comment_entities",[])
        group = {
            "owner":block_id,
            "editors":block.get("editors",[]),
            "subtype":"page_comment",
            "owner_type":"page",
        }
        [group_id,group] = nomagic.group.create_chat(group)
        comment_entities.append(group_id)
        block["comment_entities"]=comment_entities
        updatetime = int(time.time())
        block["updatetime"]=updatetime
        update_aim(block_id,block)
        self.finish({"info":"ok","about":"add comment success","comment_entity":group_id})
class GetCommentAPIHandler(WebRequest):
    def get(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        block = get_aim(block_id)
        if user_id not in block.get("editors",[block.get("owner",None)]):
        # if block.get("owner",None) != user_id:
            self.finish({"info":"error","about":"no in editors"})
            return
        comment_entities = block.get("comment_entities",[])
        self.finish({"info":"ok","about":"get comment success","comment_entities":comment_entities})

class CommentSubmitAPIHandler(WebRequest):
    @tornado.gen.coroutine
    def post(self):
        self.set_header("Access-Control-Allow-Origin", self.request.headers.get("Origin"))
        self.set_header("Access-Control-Allow-Credentials", "true")
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        chat_id = self.get_argument("chat_id",None)
        content = self.get_argument("content", None)
        uuid = self.get_argument("uuid",None)
        chat = get_aim(chat_id)
        last_comment_time = 0
        print(chat_id,content)
        print(not chat,content=="",not content)
        comment_push = False
        if not chat or content == "" or not content:
            self.finish({"info":"error","about":"no chat or content"})
            return
        chat["comment_members"]=chat.get("comment_members",[])
        if user_id not in chat["comment_members"]:
            chat["comment_members"].append(user_id)
        comment_type = u"COMMENT"
        chat["comment_ids"] = chat.get("comment_ids", [])
        if not chat["comment_ids"]:
            comments = []
            comment_entity = {
                "owner": chat_id,
                "owner_type": chat["type"],
                "comments": comments,
            }
            [comment_id, comment_entity] = nomagic.comment.create_comment(comment_entity)
            chat["comment_ids"].append(comment_id)
            chat["last_comment_entity"] = comment_entity
            update_aim(chat_id,chat)
            last_comment_time = 0
        else:
            comment_id = chat["comment_ids"][-1]
            if chat.get("last_comment_entity", None):
                comment_entity = chat["last_comment_entity"]
            else:
                comment_entity = get_aim(comment_id)
            comments = comment_entity.get("comments", [])
            if(len(comments)>0):
                last_comment_time = comments[-1][2]
            if(len(comments)>=100):
                update_aim(comment_id,comment_entity)
                comments = []
                comment_entity = {
                    "owner": chat_id,
                    "owner_type": chat["type"],
                    "comments": comments,
                    "type": "comment",
                    "last_comment_id": comment_id
                }
                [comment_id, comment_entity] = nomagic.comment.create_comment(comment_entity)
                chat["comment_ids"].append(comment_id)
        comment_sequence = "".join(random.choice(string.ascii_lowercase+string.digits) for _ in range(6))
        comment_time = time.time()
        comment_push = True
        comment_plus = {}
        comment_del = 0
        comments.append([comment_sequence, user_id, comment_time, comment_type, content, comment_plus, comment_del])
        comment_entity["comments"] = comments
        chat["last_comment_entity"] = comment_entity
        update_aim(chat_id,chat)
        user = get_aim(user_id)
        user_name = user.get("name","")
        user_headimgurl = user.get("headimgurl","/static/img/headimgurl.png")
        msgtype = comment_type
        time_now = int(time.time())

        self.finish({"info": "ok","comment_id":comment_id,"sequence":comment_sequence,"time":time_now,"msgtype":msgtype})

        msg = [msgtype, {
            "content": content,
            "nickname": user_name,
            "headimgurl": user_headimgurl,
            "time": time_now,
            "user_id": user_id,
            "sequence": comment_sequence,
            "comment_id": comment_id,
            "uuid":uuid,
        }, chat_id]
        if not block_id:
            DataWebSocket.send_to_all(json_encode(msg))
        else:
            DataWebSocket.send_to_target_room(json_encode(msg),block_id)

class CommentLoadAPIHandler(WebRequest):
    @tornado.gen.coroutine
    def get(self):
        self.set_header("Access-Control-Allow-Origin", "*")
        chat_id = self.get_argument("chat_id",None)
        comment_id = self.get_argument("comment_id", None)
        chat = get_aim(chat_id)
        if not chat:
            self.finish({"info":"error","about":"no chat"})
            return
        comment_ids = chat.get("comment_ids",[])
        if not comment_id and len(comment_ids)>0 :
            comment_id = comment_ids[-1]
        if comment_id not in comment_ids:
            self.finish({"info":"error","about":"no chat's comment"})
            return
        recent_comment_member_ids = []
        last_comment_id = ""
        if comment_id == comment_ids[-1]:
            comment_entity = chat["last_comment_entity"]
        else:
            comment_entity = get_aim(comment_id)
        last_comment_id = comment_entity.get("last_comment_id", None)
        comments = comment_entity.get("comments",[])
        comments_update = False
        for comment in comments:
            if not type(comment[5]) == dict:
                comments_update = True
                comment_plus = {
                    "content": comment[5]
                }
                comment[5] = comment_plus
            else:
                remarks = comment[5].get("remarks",[])
                for remark in remarks:
                    if len(remark)==3:
                        comments_update = True
                        remark_id = "".join(random.choice(string.ascii_lowercase+string.digits) for _ in range(6))
                        remark.append(remark_id)
                    recent_comment_member_ids.append(remark[0])
                comment[5]["remarks"]=remarks
                likes = comment[5].get("likes",[])
                recent_comment_member_ids = list(set(likes).union(set(recent_comment_member_ids))) #求并集
            recent_comment_member_ids.append(comment[1])
        if comments_update:
            comment_entity["comments"] = comments
            if comment_id == comment_ids[-1]:
                chat["last_comment_entity"] = comment_entity
                update_aim(chat_id,chat)
            else:
                update_aim(comment_id,comment_entity)
        members_ids = set(recent_comment_member_ids)
        members = get_aims(members_ids)
        members_json = {}
        for member in members:
            member_name = member[1].get("name","")
            member_headimgurl = member[1].get("headimgurl","/static/img/headimgurl.png")
            members_json[member[0]] = {"name":member_name ,"headimgurl":member_headimgurl}
        self.finish({"info":"ok","last_comment_id":last_comment_id,"comments":comments,"members":members_json,"comment_id":comment_id,"chat_id":chat_id})



















