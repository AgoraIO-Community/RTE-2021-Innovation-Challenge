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

class UriMappingHandler(WebRequest):
    @tornado.gen.coroutine
    def get(self, app):

        link_list = self.request.uri.split("?")
        uri_mapping = settings["uri_mapping"]
        block_id = uri_mapping.get(link_list[0],None)

        if not self.current_user:
            self.user_id = "no_login:%s"%str(time.time())
        else:
            self.user_id = self.current_user["id"]
        self.time_now = int(time.time())
        self.block_id = block_id
        if not block_id:
            self.render("../template/404.html")
            return
        block = get_aim(block_id)
        if not block:
            self.render("../template/404.html")
            return

        self.permission_btns = {
            "private":"Private·私有",
            "publish":"Publish·公开发表",
            "public":"Public·自由编辑"
        }
        self.permission = block.get("permission","private")
        self.editors = block.get("editors",[block.get("owner",None)])
        self.readers = block.get("readers",[])
        self.blackers = block.get("blackers",[])
        if self.user_id in self.blackers:
            self.render("../template/404.html")
            return
        if self.permission in ["private"]:
            if self.user_id not in self.editors and self.user_id not in self.readers:
                self.render("../template/404.html")
                return


        self.doms = block.get("doms",[])
        websocket_protocol = "ws" if self.request.protocol == "http" else "wss"
        aim_host = self.request.host
        self.websocket_url = "%s://%s/api/data/ws?aim_id=%s" % (websocket_protocol, aim_host, block_id)

        self.title = block.get("title","")
        self.desc = block.get("desc","")
        self.fork_allow = block.get("fork_allow",True)
        self.fork_from = block.get("fork_from","")
        self.fork_entity = block.get("fork_entity",None)
        self.grid_graph = block.get("grid_graph",{})
        self.main_area = block.get("main_area",{})
        self.render("../template/page.html")
class MainHandler(WebRequest):
    @tornado.gen.coroutine
    def get(self, app):
        # self.time_now = int(time.time())
        # self._ = self.locale.translate
        # self.render("../template/index.html")
        block_id = "0b5ee08ed0ed498593306193601680e7"
        if not self.current_user:
            self.user_id = "no_login:%s"%str(time.time())
        else:
            self.user_id = self.current_user["id"]
        self.time_now = int(time.time())
        self.block_id = block_id
        block = get_aim(block_id)
        if not block:
            self.render("../template/404.html")
            return

        self.permission_btns = {
            "private":"Private·私有",
            "publish":"Publish·公开发表",
            "public":"Public·自由编辑"
        }
        self.permission = block.get("permission","private")
        self.editors = block.get("editors",[block.get("owner",None)])
        self.readers = block.get("readers",[])
        self.blackers = block.get("blackers",[])
        if self.user_id in self.blackers:
            self.render("../template/404.html")
            return
        if self.permission in ["private"]:
            if self.user_id not in self.editors and self.user_id not in self.readers:
                self.render("../template/404.html")
                return


        self.doms = block.get("doms",[])
        websocket_protocol = "ws" if self.request.protocol == "http" else "wss"
        aim_host = self.request.host
        self.websocket_url = "%s://%s/api/data/ws?aim_id=%s" % (websocket_protocol, aim_host, block_id)
        
        self.title = block.get("title","")
        self.desc = block.get("desc","")
        self.fork_allow = block.get("fork_allow",True)
        self.fork_from = block.get("fork_from","")
        self.fork_entity = block.get("fork_entity",None)
        self.grid_graph = block.get("grid_graph",{})
        self.main_area = block.get("main_area",{})
        self.render("../template/page.html")

class LoginHandler(WebRequest):
    @tornado.gen.coroutine
    def get(self):
        self.time_now = int(time.time())
        self._ = self.locale.translate
        self.render("../template/login.html")
class RegisterHandler(WebRequest):
    @tornado.gen.coroutine
    def get(self):
        block_id = "9b30f584a1cf4005996ec4d5e4170cbc"
        if not self.current_user:
            self.user_id = "no_login:%s"%str(time.time())
        else:
            self.user_id = self.current_user["id"]
        self.time_now = int(time.time())
        self.block_id = block_id
        block = get_aim(block_id)
        if not block:
            self.render("../template/404.html")
            return

        self.permission_btns = {
            "private":"Private·私有",
            "publish":"Publish·公开发表",
            "public":"Public·自由编辑"
        }
        self.permission = block.get("permission","private")
        self.editors = block.get("editors",[block.get("owner",None)])
        self.readers = block.get("readers",[])
        self.blackers = block.get("blackers",[])
        if self.user_id in self.blackers:
            self.render("../template/404.html")
            return
        if self.permission in ["private"]:
            if self.user_id not in self.editors and self.user_id not in self.readers:
                self.render("../template/404.html")
                return


        self.doms = block.get("doms",[])
        websocket_protocol = "ws" if self.request.protocol == "http" else "wss"
        aim_host = self.request.host
        self.websocket_url = "%s://%s/api/data/ws?aim_id=%s" % (websocket_protocol, aim_host, block_id)

        self.title = block.get("title","")
        self.desc = block.get("desc","")
        self.fork_allow = block.get("fork_allow",True)
        self.fork_from = block.get("fork_from","")
        self.fork_entity = block.get("fork_entity",None)
        self.grid_graph = block.get("grid_graph",{})
        self.main_area = block.get("main_area",{})
        self.render("../template/page.html")
class MainHomeHandler(WebRequest):
    def get(self):
        self.timer = int(time.time())
        print(self.request)
        self.render("../template/main.html")
class WelcomeHomeHandler(WebRequest):
    def get(self):
        self.timer = int(time.time())
        self.render("../template/welcome.html")
class PagesHomeHandler(WebRequest):
    def get(self):
        self.time_now = int(time.time())
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        user = get_aim(user_id)
        self.pages = user.get("pages",[])
        self.render("../template/pages.html")
class PagesListAPIHandler(WebRequest):
    def get(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        user = get_aim(user_id)

        pages_ids = user.get("pages",[])
        pages = get_aims(pages_ids)
        result = []
        for page in pages:
            result_item = {
                "block_id":page[0],
                "title":page[1].get("title","new page"),
                "desc":page[1].get("desc","this is a new page"),
                "fork_allow":page[1].get("fork_allow",True),
                "fork_from":page[1].get("fork_from",None),
            }
            result.append(result_item)
        pages_top_ids = user.get("pages_top",[])
        
        self.finish({"info":"ok","result":result,"pages_top_ids":pages_top_ids})

class PageAddAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        title = self.get_argument("title","new page")
        desc = self.get_argument("desc","this is a new page")
        user = get_aim(user_id)
        pages = user.get("pages",[])
        block = {
            "owner":user_id,
            "subtype":"page",
            "title":title,
            "desc":desc,
            "doms":[],
            "history":[],
            "updatetime":int(time.time())
        }
        [block_id,block]=nomagic.block.create_block(block)
        pages.insert(0,block_id)
        user["pages"]=pages
        update_aim(user_id,user)
        self.finish({"info":"ok","about":"create new page success","block_id":block_id})
class PageHomeHandler(WebRequest):
    def get(self,block_id):
        # self.set_header("Access-Control-Allow-Origin", self.request.headers.get("Origin","*"))
        # self.set_header("Access-Control-Allow-Credentials", "true")
        if not self.current_user:
            self.user_id = "no_login:%s"%str(time.time())
        else:
            self.user_id = self.current_user["id"]
        self.time_now = int(time.time())
        self.block_id = block_id
        block = get_aim(block_id)
        if not block:
            self.render("../template/404.html")
            return

        self.permission_btns = {
            "private":"Private·私有",
            "publish":"Publish·公开发表",
            "public":"Public·自由编辑"
        }
        self.permission = block.get("permission","private")
        self.editors = block.get("editors",[block.get("owner",None)])
        self.readers = block.get("readers",[])
        self.blackers = block.get("blackers",[])
        if self.user_id in self.blackers:
            self.render("../template/404.html")
            return
        if self.permission in ["private"]:
            if self.user_id not in self.editors and self.user_id not in self.readers:
                self.render("../template/404.html")
                return


        self.doms = block.get("doms",[])
        websocket_protocol = "ws" if self.request.protocol == "http" else "wss"
        aim_host = self.request.host
        self.websocket_url = "%s://%s/api/data/ws?aim_id=%s" % (websocket_protocol, aim_host, block_id)
        
        self.title = block.get("title","")
        self.desc = block.get("desc","")
        self.fork_allow = block.get("fork_allow",True)
        self.fork_from = block.get("fork_from","")
        self.fork_entity = block.get("fork_entity",None)
        self.grid_graph = block.get("grid_graph",{})
        self.main_area = block.get("main_area",{})
        self.render("../template/page.html")
class PageEditHomeHandler(WebRequest):
    def get(self,block_id):
        # self.set_header("Access-Control-Allow-Origin", self.request.headers.get("Origin","*"))
        # self.set_header("Access-Control-Allow-Credentials", "true")
        if not self.current_user:
            self.user_id = "no_login:%s"%str(time.time())
        else:
            self.user_id = self.current_user["id"]
        self.time_now = int(time.time())
        self.block_id = block_id
        block = get_aim(block_id)
        if not block:
            self.render("../template/404.html")
            return
        self.doms = block.get("doms",[])
        websocket_protocol = "ws" if self.request.protocol == "http" else "wss"
        aim_host = self.request.host
        self.websocket_url = "%s://%s/api/data/ws?aim_id=%s" % (websocket_protocol, aim_host, block_id)

        self.permission_btns = {
            "private":"Private·私有",
            "publish":"Publish·公开发表",
            "public":"Public·自由编辑"
        }
        self.permission = block.get("permission","private")
        self.editors = block.get("editors",[block.get("owner",None)])
        self.readers = block.get("readers",[])
        self.blackers = block.get("blackers",[])
        self.members = block.get("members",[])
        self.stars = block.get("stars",[])

        if self.user_id in self.blackers:
            self.render("../template/404.html")
            return

        if self.permission not in ["public"]:
            if self.user_id not in self.editors:
                self.redirect("/home/page/%s"%block_id)
                return
        self.title = block.get("title","")
        self.desc = block.get("desc","")
        self.fork_allow = block.get("fork_allow",True)
        self.fork_from = block.get("fork_from","")
        self.fork_entity = block.get("fork_entity",None)
        self.grid_graph = block.get("grid_graph",{})
        self.main_area = block.get("main_area",{})
        self.comment_entities = block.get("comment_entities",[])
        
        self.render("../template/page_edit.html")
class PageFixHomeHandler(WebRequest):
    def get(self,block_id):
        # self.set_header("Access-Control-Allow-Origin", self.request.headers.get("Origin","*"))
        # self.set_header("Access-Control-Allow-Credentials", "true")
        if not self.current_user:
            self.user_id = "no_login:%s"%str(time.time())
        else:
            self.user_id = self.current_user["id"]
        self.time_now = int(time.time())
        self.block_id = block_id
        block = get_aim(block_id)
        if not block:
            self.render("../template/404.html")
            return
        self.doms = block.get("doms",[])
        websocket_protocol = "ws" if self.request.protocol == "http" else "wss"
        aim_host = self.request.host
        self.websocket_url = "%s://%s/api/data/ws?aim_id=%s" % (websocket_protocol, aim_host, block_id)

        self.permission = block.get("permission","private")
        self.editors = block.get("editors",[block.get("owner",None)])
        self.readers = block.get("readers",[])
        self.blackers = block.get("blackers",[])
        self.members = block.get("members",[])

        if self.user_id in self.blackers:
            self.render("../template/404.html")
            return

        if self.permission not in ["public"]:
            if self.user_id not in self.editors:
                self.redirect("/home/page/%s"%block_id)
                return
        self.render("../template/page_fix.html")
class PageCopyDomAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        aim_id = self.get_argument("aim_id",None)
        aim_dom_id = self.get_argument("aim_dom_id",None)
        dom_owner = self.get_argument("dom_owner",None)
        dom_type = self.get_argument("dom_type","text")
        dom_position_x = int(float(self.get_argument("dom_position_x","0")))
        dom_position_y = int(float(self.get_argument("dom_position_y","0")))
        # if dom_type not in ["text","img","video","canvas","input","button","textarea"]:
        if dom_type not in ["domcopy"]:
            self.finish({"info":"error","about":"not allow dom type"})
            return
        block = get_aim(block_id)
        aim = get_aim(aim_id)
        #判断是否为编辑者
        if user_id not in block.get("editors",[block.get("owner",None)]):
        # if block.get("owner",None) != user_id:
            self.finish({"info":"error","about":"no in editors"})
            return
        doms = block.get("doms",[])
        dom_sequence = "".join(random.choice(string.ascii_lowercase+string.digits) for _ in range(6))
        dom_sequence_same = False
        while True:
            for dom_item in doms:
                if dom_item[0] == dom_sequence:
                    dom_sequence_same = True
                    break
            if dom_sequence_same:
                dom_sequence = "".join(random.choice(string.ascii_lowercase+string.digits) for _ in range(6))
                dom_sequence_same = False
            else:
                break
        copy_dom = None
        for aim_dom in aim.get("doms",[]):
            if aim_dom[0]==aim_dom_id:
                copy_dom = copy.deepcopy(aim_dom)
        if not copy_dom:
            self.finish({"info":"error","about":"copy dom is removed or none"})
            return
        updatetime = int(time.time())
        copy_dom[0]=dom_sequence
        # copy_dom[2]["x"]=dom_position_x
        # copy_dom[2]["y"]=dom_position_y
        copy_dom[6]=updatetime
        # dom = [dom_sequence,dom_type,dom_position,dom_content,dom_css,dom_children,updatetime]
        dom = copy_dom
        doms.append(dom)
        block["doms"] = doms
        block["updatetime"] = updatetime
        update_aim(block_id,block)
        self.finish({"info":"ok","dom_sequence":dom_sequence})
        [dom_sequence,dom_type,dom_position,dom_content,dom_css,dom_children,updatetime] = dom
        content_data = {
            "dom_current":dom_sequence,
            "dom_content":dom_content,
            "dom_position_x":dom_position["x"],
            "dom_position_y":dom_position["y"],
            "dom_position_w":dom_position["w"],
            "dom_position_h":dom_position["h"],
            "dom_position_z":dom_position["z"],
            "dom_scroll":dom_position.get("s",""),
            "dom_type":dom_type,
        }
        msgtype = "COMMENTPAGECOPYDOM"
        msg = [msgtype, {
            "content": content_data,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))
class PageCopyDomsAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        # aim_id = self.get_argument("aim_id",None)
        # aim_dom_id = self.get_argument("aim_dom_id",None)
        current_copy = self.get_argument("current_copy","[]")
        dom_owner = self.get_argument("dom_owner",None)
        dom_type = self.get_argument("dom_type","text")
        dom_position_x = int(float(self.get_argument("dom_position_x","0")))
        dom_position_y = int(float(self.get_argument("dom_position_y","0")))
        # if dom_type not in ["text","img","video","canvas","input","button","textarea"]:
        if dom_type not in ["domcopy"]:
            self.finish({"info":"error","about":"not allow dom type"})
            return
        block = get_aim(block_id)
        #判断是否为编辑者
        if user_id not in block.get("editors",[block.get("owner",None)]):
        # if block.get("owner",None) != user_id:
            self.finish({"info":"error","about":"no in editors"})
            return
        aim_ids = []
        aim_dom_ids = []
        current_copy_items = json_decode(current_copy)
        for current_copy_item in current_copy_items:
            current_copy_item_list = current_copy_item.split("COPYDOM//")
            [a_aim_id,a_aim_dom_id]=current_copy_item_list[1].split(",")
            aim_ids.append(a_aim_id)
            aim_dom_ids.append([a_aim_id,a_aim_dom_id])
        aim_ids = list(set(aim_ids))
        aims = get_aims(aim_ids)
        aims_json = {}
        for aim in aims:
            aims_json[aim[0]]=aim[1]
        dom_sequences=[]
        doms = block.get("doms",[])
        content_datas = []
        for [aim_id,aim_dom_id] in aim_dom_ids:
            aim = aims_json[aim_id]
            dom_sequence = "".join(random.choice(string.ascii_lowercase+string.digits) for _ in range(6))
            dom_sequence_same = False
            while True:
                for dom_item in doms:
                    if dom_item[0] == dom_sequence:
                        dom_sequence_same = True
                        break
                if dom_sequence_same:
                    dom_sequence = "".join(random.choice(string.ascii_lowercase+string.digits) for _ in range(6))
                    dom_sequence_same = False
                else:
                    break
            copy_dom = None
            for aim_dom in aim.get("doms",[]):
                if aim_dom[0]==aim_dom_id:
                    copy_dom = copy.deepcopy(aim_dom)
            if not copy_dom:
                self.finish({"info":"error","about":"copy dom is removed or none"})
                return
            updatetime = int(time.time())
            copy_dom[0]=dom_sequence
            # copy_dom[2]["x"]=dom_position_x
            # copy_dom[2]["y"]=dom_position_y
            copy_dom[6]=updatetime
            # dom = [dom_sequence,dom_type,dom_position,dom_content,dom_css,dom_children,updatetime]
            dom = copy_dom
            doms.append(dom)
            dom_sequences.append(dom_sequence)
            [dom_sequence,dom_type,dom_position,dom_content,dom_css,dom_children,updatetime] = dom
            content_data = {
                "dom_current":dom_sequence,
                "dom_content":dom_content,
                "dom_position_x":dom_position["x"],
                "dom_position_y":dom_position["y"],
                "dom_position_w":dom_position["w"],
                "dom_position_h":dom_position["h"],
                "dom_position_z":dom_position["z"],
                "dom_scroll":dom_position.get("s",""),
                "dom_type":dom_type,
            }
            content_datas.append(content_data)
        block["doms"] = doms
        block["updatetime"] = updatetime
        update_aim(block_id,block)
        self.finish({"info":"ok","dom_sequences":dom_sequences})
        msgtype = "COMMENTPAGECOPYDOMS"
        msg = [msgtype, {
            "content": content_datas,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))
class PageAddDomAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        dom_owner = self.get_argument("dom_owner",None)
        dom_type = self.get_argument("dom_type","text")
        dom_position_x = int(float(self.get_argument("dom_position_x","0")))
        dom_position_y = int(float(self.get_argument("dom_position_y","0")))
        dom_scroll = self.get_argument("dom_scroll","")
        # if dom_type not in ["text","img","video","canvas","input","button","textarea"]:
        if dom_type not in ["text","img","video","iframe"]:
            self.finish({"info":"error","about":"not allow dom type"})
            return
        block = get_aim(block_id)
        #判断是否为编辑者
        if user_id not in block.get("editors",[block.get("owner",None)]):
        # if block.get("owner",None) != user_id:
            self.finish({"info":"error","about":"no in editors"})
            return
        doms = block.get("doms",[])
        dom_sequence = "".join(random.choice(string.ascii_lowercase+string.digits) for _ in range(6))
        dom_sequence_same = False
        while True:
            for dom_item in doms:
                if dom_item[0] == dom_sequence:
                    dom_sequence_same = True
                    break
            if dom_sequence_same:
                dom_sequence = "".join(random.choice(string.ascii_lowercase+string.digits) for _ in range(6))
                dom_sequence_same = False
            else:
                break
        dom_position = {
            "x":dom_position_x,
            "y":dom_position_y,
            "w":100,
            "h":40,
            "z":0,
            "s":dom_scroll,
        }
        dom_css = ""
        dom_content = ""
        dom_children = []
        updatetime = int(time.time())
        dom = [dom_sequence,dom_type,dom_position,dom_content,dom_css,dom_children,updatetime]
        doms.append(dom)
        block["doms"] = doms
        block["updatetime"] = updatetime
        update_aim(block_id,block)
        self.finish({"info":"ok","dom_sequence":dom_sequence})

        if dom_type in ["text"]:
            dom_content = """
            <div class="section">text</div>
            """
        elif dom_type in ["img"]:
            dom_content = """
            <div class="section"><img src="/static/img/need_add_img.png"></div>
            """
        elif dom_type in ["video"]:
            dom_content = """
            <div class="section" contenteditable="false"><span class="novideospan">视频未设置</span></div>
            """
        elif dom_type in ["iframe"]:
            dom_content = """
            <div class="section" contenteditable="false">iframe暂未设置</div>
            """
        content_data = {
            "dom_current":dom_sequence,
            "dom_content":dom_content,
            "dom_position_x":dom_position["x"],
            "dom_position_y":dom_position["y"],
            "dom_position_w":dom_position["w"],
            "dom_position_h":dom_position["h"],
            "dom_position_z":dom_position["z"],
            "dom_scroll":dom_position["s"],
            "dom_type":dom_type,
        }
        msgtype = "COMMENTPAGEADDDOM"
        msg = [msgtype, {
            "content": content_data,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))
class PageDelDomAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        dom_owner = self.get_argument("dom_owner",None)
        dom_current = self.get_argument("dom_current",None)

        block = get_aim(block_id)
        #判断是否为编辑者
        if user_id not in block.get("editors",[block.get("owner",None)]):
        # if block.get("owner",None) != user_id:
            self.finish({"info":"error","about":"no in editors"})
            return
        doms = block.get("doms",[])
        dom_tree = dom_current.split("_")
        dom_aim = None
        doms = block.get("doms",[])

        _doms = doms
        for dom in dom_tree:
            _doms = _doms
            for _dom in _doms:
                if dom == _dom[0]:
                    if dom_owner == _dom[0]:
                        dom_aim = _dom
                        _doms.remove(_dom)
                        break
                    _doms = _dom[5]
                    break
        if not dom_aim:
            self.finish({"info":"error","about":"no dom"})
            return
        dom_content = ""
        updatetime = int(time.time())
        block["doms"] = doms
        block["updatetime"] = updatetime
        update_aim(block_id,block)
        self.finish({"info":"ok",})
        content_data = {
            "dom_current":dom_current,
            "dom_content":dom_content,
        }
        msgtype = "COMMENTPAGEDELDOM"
        msg = [msgtype, {
            "content": content_data,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))
class PageUpdateTitleAPIHandler(WebRequest):
    # @tornado.gen.coroutine
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        dom_content = self.get_argument("dom_content",None)
        
        title = json_decode(dom_content).get("title","")
        updatetime = int(time.time())

        block = get_aim(block_id)

        #判断是否为编辑者
        if user_id not in block.get("editors",[block.get("owner",None)]):
        # if block.get("owner",None) != user_id:
            self.finish({"info":"error","about":"no in editors"})
            return
        old_title = block.get("title","")
        if title == old_title:
            self.finish({"info":"ok","about":"same title"})
            return
        block["title"]=title
        block["updatetime"]=updatetime
        update_aim(block_id,block)
        self.finish({"info":"ok"})
        content_data = {
            "dom_content":dom_content,
        }
        msgtype = "COMMENTPAGEUPDATETITLE"
        msg = [msgtype, {
            "content": content_data,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))
class PageUpdateDescAPIHandler(WebRequest):
    # @tornado.gen.coroutine
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        dom_content = self.get_argument("dom_content",None)
        
        desc = json_decode(dom_content).get("desc","")
        updatetime = int(time.time())

        block = get_aim(block_id)

        #判断是否为编辑者
        if user_id not in block.get("editors",[block.get("owner",None)]):
        # if block.get("owner",None) != user_id:
            self.finish({"info":"error","about":"no in editors"})
            return
        old_desc = block.get("desc","")
        if desc == old_desc:
            self.finish({"info":"ok","about":"same desc"})
            return
        block["desc"]=desc
        block["updatetime"]=updatetime
        update_aim(block_id,block)
        self.finish({"info":"ok"})
        content_data = {
            "dom_content":dom_content,
        }
        msgtype = "COMMENTPAGEUPDATEDESC"
        msg = [msgtype, {
            "content": content_data,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))
class PageUpdateMainAreaAPIHandler(WebRequest):
    # @tornado.gen.coroutine
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        dom_content = self.get_argument("dom_content",None)
        
        w = int(json_decode(dom_content).get("text").get("w","1024"))
        h = int(json_decode(dom_content).get("text").get("h","0"))
        updatetime = int(time.time())
        
        block = get_aim(block_id)
        
        #判断是否为编辑者
        if user_id not in block.get("editors",[block.get("owner",None)]):
        # if block.get("owner",None) != user_id:
            self.finish({"info":"error","about":"no in editors"})
            return
        old_main_area = block.get("main_area",{})
        if w == int(old_main_area.get("w","1024")) and h == int(old_main_area.get("h","0")):
            self.finish({"info":"ok","about":"same main_area"})
            return
        block["updatetime"]=updatetime
        block["main_area"]={
            "w":w,
            "h":h,
        }
        update_aim(block_id,block)
        self.finish({"info":"ok"})
        content_data = {
            "dom_content":dom_content,
        }
        msgtype = "COMMENTPAGEMAINAREA"
        msg = [msgtype, {
            "content": content_data,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))
class PageUpdateGridGraphAPIHandler(WebRequest):
    # @tornado.gen.coroutine
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        dom_content = self.get_argument("dom_content",None)
        
        w = int(json_decode(dom_content).get("text").get("w","30"))
        h = int(json_decode(dom_content).get("text").get("h","30"))
        updatetime = int(time.time())
        
        block = get_aim(block_id)
        
        #判断是否为编辑者
        if user_id not in block.get("editors",[block.get("owner",None)]):
        # if block.get("owner",None) != user_id:
            self.finish({"info":"error","about":"no in editors"})
            return
        old_grid_graph = block.get("grid_graph",{})
        if w == int(old_grid_graph.get("w","30")) and h == int(old_grid_graph.get("h","30")):
            self.finish({"info":"ok","about":"same grid graph"})
            return
        block["updatetime"]=updatetime
        block["grid_graph"]={
            "w":w,
            "h":h,
        }
        update_aim(block_id,block)
        self.finish({"info":"ok"})
        content_data = {
            "dom_content":dom_content,
        }
        msgtype = "COMMENTPAGEGRIDGRAPH"
        msg = [msgtype, {
            "content": content_data,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))
class PageUpdateDomsAPIHandler(WebRequest):
    @tornado.gen.coroutine
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        updates = self.get_argument("updates","[]")
        if updates == "[]":
            self.finish({"info":"error","about":"no update"})
            return
        dom_content = self.get_argument("dom_content",None)
        updates = json_decode(updates)
        updatetime = int(time.time())
        block = get_aim(block_id)
        #判断是否为编辑者
        if user_id not in block.get("editors",[block.get("owner",None)]):
        # if block.get("owner",None) != user_id:
            self.finish({"info":"error","about":"no in editors"})
            return
        doms = block.get("doms",[])
        for dom in doms:
            for update in updates:
                if dom[0] == update["dom_id"]:
                    dom[2]["x"]=update["x"]
                    dom[2]["y"]=update["y"]
        block["doms"]=doms
        block["updatetime"]=updatetime
        update_aim(block_id,block)
        self.finish({"info":"ok"})
        content_data = {
            "updates":updates,
            "dom_content":{
                "uuid":dom_content
            },
        }
        msgtype = "COMMENTPAGEUPDATEDOMS"
        msg = [msgtype, {
            "content": content_data,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))

class PageUpdateDomAPIHandler(WebRequest):
    @tornado.gen.coroutine
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        dom_owner = self.get_argument("dom_owner",None)
        dom_current = self.get_argument("dom_current",None)
        dom_position_x = int(float(self.get_argument("dom_position_x","0")))
        dom_position_y = int(float(self.get_argument("dom_position_y","0")))
        dom_position_w = int(float(self.get_argument("dom_position_w","0")))
        dom_position_h = int(float(self.get_argument("dom_position_h","0")))
        dom_position_z = int(float(self.get_argument("dom_position_z","0")))
        dom_scroll = self.get_argument("dom_scroll","")
        
        updatetime = int(time.time())
        
        block = get_aim(block_id)
        
        #判断是否为编辑者
        if user_id not in block.get("editors",[block.get("owner",None)]):
        # if block.get("owner",None) != user_id:
            self.finish({"info":"error","about":"no in editors"})
            return
        dom_tree = dom_current.split("_")
        dom_aim = None
        doms = block.get("doms",[])

        _doms = doms
        for dom in dom_tree:
            _doms = _doms
            for _dom in _doms:
                if dom == _dom[0]:
                    if dom_owner == _dom[0]:
                        dom_aim = _dom
                        dom_aim[2]={
                            "x":dom_position_x,
                            "y":dom_position_y,
                            "w":dom_position_w,
                            "h":dom_position_h,
                            "z":dom_position_z,
                            "s":dom_scroll,
                        }
                        dom_aim[6]=updatetime
                        break
                    _doms = _dom[5]
                    break
        block["doms"]=doms
        block["updatetime"]=updatetime
        update_aim(block_id,block)
        self.finish({"info":"ok"})
        content_data = {
            "dom_current":dom_current,
            "dom_position_x":dom_position_x,
            "dom_position_y":dom_position_y,
            "dom_position_w":dom_position_w,
            "dom_position_h":dom_position_h,
            "dom_position_z":dom_position_z,
            "dom_scroll":dom_scroll,
        }

        msgtype = "COMMENTPAGEUPDATEDOM"
        msg = [msgtype, {
            "content": content_data,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))
class PageUpdateDomVideoAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        dom_owner = self.get_argument("dom_owner",None)
        dom_current = self.get_argument("dom_current",None)
        dom_content = self.get_argument("dom_content",None)
        
        updatetime = int(time.time())
        
        block = get_aim(block_id)
        #判断是否为编辑者
        if user_id not in block.get("editors",[block.get("owner",None)]):
            self.finish({"info":"error","about":"no in editors"})
            return
        dom_tree = dom_current.split("_")
        dom_aim = None
        doms = block.get("doms",[])

        _doms = doms
        for dom in dom_tree:
            _doms = _doms
            for _dom in _doms:
                if dom == _dom[0]:
                    if dom_owner == _dom[0]:
                        dom_aim = _dom
                        dom_aim[3] = json_decode(dom_content).get("text",{})
                        dom_aim[6] =updatetime
                        break
                    _doms = _dom[5]
                    break
        block["doms"]=doms
        block["updatetime"]=updatetime

        update_aim(block_id,block)
        self.finish({"info":"ok"})
        content_data = {
            "dom_current":dom_current,
            "dom_content":dom_content,
        }
        msgtype = "COMMENTPAGEUPDATEDOMVIDEO"
        msg = [msgtype, {
            "content": content_data,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))
class PageUpdateDomContentAPIHandler(WebRequest):
    # @tornado.gen.coroutine
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        dom_owner = self.get_argument("dom_owner",None)
        dom_current = self.get_argument("dom_current",None)
        dom_content = self.get_argument("dom_content",None)
        
        updatetime = int(time.time())
        
        block = get_aim(block_id)
        #判断是否为编辑者
        if user_id not in block.get("editors",[block.get("owner",None)]):
            self.finish({"info":"error","about":"no in editors"})
            return
        dom_tree = dom_current.split("_")
        dom_aim = None
        doms = block.get("doms",[])

        _doms = doms
        for dom in dom_tree:
            _doms = _doms
            for _dom in _doms:
                if dom == _dom[0]:
                    if dom_owner == _dom[0]:
                        dom_aim = _dom
                        dom_aim[3] = json_decode(dom_content).get("text","")
                        dom_aim[6] =updatetime
                        break
                    _doms = _dom[5]
                    break
        block["doms"]=doms
        block["updatetime"]=updatetime

        update_aim(block_id,block)
        self.finish({"info":"ok"})
        content_data = {
            "dom_current":dom_current,
            "dom_content":dom_content,
        }
        msgtype = "COMMENTPAGEUPDATEDOMCONTENT"
        msg = [msgtype, {
            "content": content_data,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))
class PageUpdateDomIframeAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        dom_owner = self.get_argument("dom_owner",None)
        dom_current = self.get_argument("dom_current",None)
        dom_content = self.get_argument("dom_content",None)
        
        updatetime = int(time.time())
        
        block = get_aim(block_id)
        #判断是否为编辑者
        if user_id not in block.get("editors",[block.get("owner",None)]):
            self.finish({"info":"error","about":"no in editors"})
            return
        dom_tree = dom_current.split("_")
        dom_aim = None
        doms = block.get("doms",[])

        _doms = doms
        for dom in dom_tree:
            _doms = _doms
            for _dom in _doms:
                if dom == _dom[0]:
                    if dom_owner == _dom[0]:
                        dom_aim = _dom
                        dom_aim[3] = json_decode(dom_content).get("text",{})
                        dom_aim[6] =updatetime
                        break
                    _doms = _dom[5]
                    break
        block["doms"]=doms
        block["updatetime"]=updatetime
        update_aim(block_id,block)
        self.finish({"info":"ok"})
        content_data = {
            "dom_current":dom_current,
            "dom_content":dom_content,
        }
        msgtype = "COMMENTPAGEUPDATEDOMIFRAME"
        msg = [msgtype, {
            "content": content_data,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))
