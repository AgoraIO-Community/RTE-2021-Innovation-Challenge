#!/usr/bin/python3

import leancloud

appid = ''
appkey = ''

isInit = False


def init():
    global isInit
    isInit = False
    if appid == None or len(appid) <= 0:
        print("appid is empty")
        return

    if appkey == None or len(appkey) <= 0:
        print("appkey is empty")
        return

    leancloud.init(appid, appkey)
    isInit = True


def createTable():
    if (isInit == False):
        return

    # User
    USER = leancloud.Object.extend('USER')
    user = USER()
    user.set('name', 'TestUser')
    user.set('avatar', '1')
    user.save()

    # Room
    ROOM = leancloud.Object.extend('ROOM')
    room = ROOM()
    room.set('anchorId', user)
    room.set('channelName', "TestName")
    room.save()

    # Member
    MEMBER = leancloud.Object.extend('MEMBER')
    member = MEMBER()
    member.set('isMuted', 0)
    member.set('isSelfMuted', 0)
    member.set('isSpeaker', 0)
    member.set('roomId', room)
    member.set('streamId', 10000)
    member.set("userId", user)
    member.save()

    # Action
    ACTION = leancloud.Object.extend('ACTION')
    action = ACTION()
    action.set('action', 1)
    action.set('memberId', member)
    action.set('roomId', room)
    action.set('status', 1)
    action.save()

    user.destroy()
    room.destroy()
    member.destroy()
    action.destroy()
    print("数据库创建成功。")


init()
createTable()
