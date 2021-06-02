#!/bin/env python
#!/bin/env python
#coding=utf-8
import sys
import os

sys.path.append(os.path.dirname(os.path.abspath(__file__)))
sys.path.append(os.path.dirname(os.path.abspath(__file__)) + '/vendor/')
#os.chdir(os.path.dirname(os.path.abspath(__file__)))
import ssl
import time

import tornado.options
import tornado.ioloop
import tornado.web
import tornado.websocket
import tornado.template
import tornado.auth
import tornado.locale
from tornado import gen
from tornado.escape import json_encode, json_decode

from setting import settings
from setting import conn

from controller import auth
from controller import findmaster
from controller import findmaster_permission
from controller import findmaster_file
from controller import findmaster_tool
from controller import findmaster_fork
from controller import findmaster_comment
from controller import findmaster_agora
from controller import data
from controller.base import WebRequest

tornado.httpclient.AsyncHTTPClient.configure("tornado.curl_httpclient.CurlAsyncHTTPClient")

application = tornado.web.Application([
    (r"/home/main",findmaster.MainHomeHandler),
    (r"/home/welcome",findmaster.WelcomeHomeHandler),

    # (r"/login",findmaster.LoginHandler),
    # (r"/register",findmaster.RegisterHandler),

    (r"/home/pages",findmaster.PagesHomeHandler),
    # (r"/home/page/fix/(.*)",findmaster.PageFixHomeHandler),
    (r"/home/page/edit/(.*)",findmaster.PageEditHomeHandler),
    (r"/home/page/(.*)",findmaster.PageHomeHandler),

    (r"/api/pages/list",findmaster.PagesListAPIHandler),

    (r"/api/page/add",findmaster.PageAddAPIHandler),
    (r"/api/page/add_dom",findmaster.PageAddDomAPIHandler),
    (r"/api/page/copy_doms",findmaster.PageCopyDomsAPIHandler),
    (r"/api/page/copy_dom",findmaster.PageCopyDomAPIHandler),
    (r"/api/page/del_dom",findmaster.PageDelDomAPIHandler),
    (r"/api/page/update_title",findmaster.PageUpdateTitleAPIHandler),
    (r"/api/page/update_desc",findmaster.PageUpdateDescAPIHandler),
    (r"/api/page/update_grid_graph",findmaster.PageUpdateGridGraphAPIHandler),
    (r"/api/page/update_main_area",findmaster.PageUpdateMainAreaAPIHandler),
    (r"/api/page/update_dom",findmaster.PageUpdateDomAPIHandler),
    (r"/api/page/update_doms",findmaster.PageUpdateDomsAPIHandler),
    (r"/api/page/update_dom_video",findmaster.PageUpdateDomVideoAPIHandler),
    (r"/api/page/update_dom_content",findmaster.PageUpdateDomContentAPIHandler),
    (r"/api/page/update_dom_iframe",findmaster.PageUpdateDomIframeAPIHandler),

    # public publish private
    (r"/api/page/set_permission",   findmaster_permission.PageSetPermissionAPIHandler),
    (r"/api/page/editors",          findmaster_permission.PageEditorsAPIHandler),
    (r"/api/page/readers",          findmaster_permission.PageReadersAPIHandler),
    (r"/api/page/members",          findmaster_permission.PageMembersAPIHandler),
    (r"/api/page/blackers",         findmaster_permission.PageBlackersAPIHandler),
    (r"/api/page/rules",            findmaster_permission.PageRulesAPIHandler),
    (r"/api/page/add_editor",       findmaster_permission.PageAddEditorAPIHandler),
    (r"/api/page/del_editor",       findmaster_permission.PageDelEditorAPIHandler),
    (r"/api/page/add_reader",       findmaster_permission.PageAddReaderAPIHandler),
    (r"/api/page/del_reader",       findmaster_permission.PageDelReaderAPIHandler),
    (r"/api/page/add_blacker",      findmaster_permission.PageAddBlackerAPIHandler),
    (r"/api/page/del_blacker",      findmaster_permission.PageDelBlackerAPIHandler),

    (r"/api/image/check",   findmaster_file.ImageCheckAPIHandler),
    (r"/api/image/change",  findmaster_file.ImageChangeAPIHandler),
    (r"/api/image/add",     findmaster_file.ImageAddAPIHandler),
    (r"/api/image/del",     findmaster_file.ImageDelAPIHandler),

    (r"/api/video/check",   findmaster_file.VideoCheckAPIHandler),
    (r"/api/video/change",  findmaster_file.VideoChangeAPIHandler),
    (r"/api/video/add",     findmaster_file.VideoAddAPIHandler),
    (r"/api/video/del",     findmaster_file.VideoDelAPIHandler),

    (r"/api/page/count_connect_num",findmaster_tool.CountConnectNumAPIHandler),
    (r"/api/page/fork",findmaster_fork.ForkPageAPIHandler),
    (r"/api/page/add_to_top",findmaster_tool.AddToTopPageAPIHandler),
    (r"/api/page/remove_from_top",findmaster_tool.RemoveFromTopPageAPIHandler),
    (r"/api/page/remove_to_trash",findmaster_tool.RemoveToTrashAPIHandler),

    (r"/api/page/add_comment",findmaster_comment.AddCommentAPIHandler),
    (r"/api/page/get_comment",findmaster_comment.GetCommentAPIHandler),
    (r"/api/page/comment/submit",findmaster_comment.CommentSubmitAPIHandler),
    (r"/api/page/comment/load",findmaster_comment.CommentLoadAPIHandler),

    (r"/api/page/agora/get_token",findmaster_agora.GetTokenAPIHandler),
    (r"/api/page/agora/get_customer_token",findmaster_agora.GetCustomerTokenAPIHandler),

    # (r"/start",auth.StartHomeHandler),

    (r"/api/data/ws",data.DataWebSocket),

    (r"/api/register",auth.RegisterAPIHandler),
    (r"/api/login",auth.LoginAPIHandler),
    (r"/api/logout",auth.LogoutAPIHandler),
    (r"/logout",auth.LogoutHandler),
    (r"/api/get_vcode",auth.GetVcodeAPIHandler),
    (r"/api/reset_password",auth.ResetPasswordAPIHandler),
    (r"/api/get_login",auth.GetLoginAPIHandler),

    # (r"/(.*)", findmaster.MainHandler),
    (r"/(.*)", findmaster.UriMappingHandler),
    ],**settings)

if __name__ == "__main__":
    tornado.options.define("port", default=8201, help="Run server on a specific port", type=int)
    tornado.options.parse_command_line()

    i18n_path = os.path.join(os.path.dirname(__file__), "locales")
    # tornado.locale.load_gettext_translations(i18n_path, 'en_US')
    tornado.locale.load_translations(i18n_path)
    tornado.locale.set_default_locale('zh_CN')

    application_server = tornado.httpserver.HTTPServer(application, xheaders=True)
    application_server.listen(tornado.options.options.port)
    application_server.start()
    tornado.ioloop.IOLoop.instance().start()
