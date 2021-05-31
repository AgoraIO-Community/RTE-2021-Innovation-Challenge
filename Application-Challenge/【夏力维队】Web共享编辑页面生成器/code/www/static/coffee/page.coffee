root = exports ? this
# !!!! Hotpoor root object
root.Hs or= {}
Hs = root.Hs
Hs.DEVICE_USER = null
hotpoor_ws = null
hotpoor_timestamp = null
hotpoor_ws_device = null
WS_DEVICE =
    UNKNOWN  : 0
    READY    : 1
    OPEN     : 2
    POST     : 3
    BAD      : 4
formatDate = (now) ->
    now_date = new Date(now)
    audio_list_time_now = new Date()
    year = now_date.getFullYear()
    month = now_date.getMonth()+1
    date = now_date.getDate()
    hour = now_date.getHours()
    minute = now_date.getMinutes()
    if hour < 10
        hour = "0"+hour
    if minute < 10
        minute = "0"+minute

    if audio_list_time_now.getFullYear() == year && audio_list_time_now.getMonth()+1 == month && audio_list_time_now.getDate() == date
        return  hour+":"+minute
    if audio_list_time_now.getFullYear() == year
        # return  month+"月"+date+"日 "+hour+":"+minute
        return  "#{month}/#{date}/#{year}"
    # return  year+"年"+month+"月"+date+"日 "+hour+":"+minute
    return  "#{month}/#{date}/#{year}"
formatDateAll = (now) ->
    now_date = new Date(now)
    audio_list_time_now = new Date()
    year = now_date.getFullYear()
    month = now_date.getMonth()+1
    date = now_date.getDate()
    hour = now_date.getHours()
    minute = now_date.getMinutes()
    if hour < 10
        hour = "0"+hour
    if minute < 10
        minute = "0"+minute

    if audio_list_time_now.getFullYear() == year && audio_list_time_now.getMonth()+1 == month && audio_list_time_now.getDate() == date
        return  hour+":"+minute
    if audio_list_time_now.getFullYear() == year
        return  month+"月"+date+"日 "+hour+":"+minute
        # return  "#{month}/#{date}/#{year}"
    return  year+"年"+month+"月"+date+"日 "+hour+":"+minute
    # return  "#{month}/#{date}/#{year}"

$ ->
    console.log "page"

    if BLOCK_ID?
        hotpoor_ws_device = WS_DEVICE.UNKNOWN
        hotpoor_ws_device_open = false
        hotpoor_ws_timer = null
        restart_ws_connection = () ->
            console.log "restart_ws_connection"
            on_message = (params) ->
                # console.log params
                m_type = params[0]
                content_json = params[1]
                m_plus = ""
                m_aim = params[2]
                # console.log "=== on_message ==="
                # console.log content_json["content"]
                if m_type == "COMMENTPAGEUPDATEDOM"
                    page_dom(content_json)
                else if m_type == "COMMENTPAGEUPDATEDOMS"
                    page_doms(content_json)
                else if m_type == "COMMENTPAGEUPDATEDOMCONTENT"
                    page_dom_content(content_json)
                else if m_type == "COMMENTPAGEUPDATEDOMVIDEO"
                    page_dom_video(content_json)
                else if m_type == "COMMENTPAGEADDDOM"
                    page_dom_add(content_json)
                else if m_type == "COMMENTPAGEDELDOM"
                    page_dom_del(content_json)
                else if m_type == "COMMENTPAGEPERMISSION"
                    page_permission(content_json)
                else if m_type in ["COMMENTPAGEADDEDITOR","COMMENTPAGEDELEDITOR"]
                    page_editor_change(content_json)
                else if m_type == "COMMENTPAGEUPDATEDOMIFRAME"
                    page_dom_iframe(content_json)
                else if m_type == "COMMENTPAGEUPDATETITLE"
                    page_title(content_json)
                else if m_type == "COMMENTPAGEUPDATEDESC"
                    page_desc(content_json)
                else if m_type == "COMMENTPAGEGRIDGRAPH"
                    page_grid_graph(content_json)
                else if m_type == "COMMENTPAGEMAINAREA"
                    page_main_area(content_json)
                else if m_type == "COMMENTPAGECOPYDOM"
                    page_dom_copy(content_json)
                else if m_type == "COMMENTPAGECOPYDOMS"
                    page_doms_copy(content_json)

                # Hs.video_list.push video_uri
                # console.log Hs.video_list

            if "WebSocket" of window and hotpoor_ws_device == WS_DEVICE.UNKNOWN
                hotpoor_ws.close() if hotpoor_ws?
                hotpoor_ws = new WebSocket WEBSOCKET_URL
                hotpoor_ws_device = WS_DEVICE.READY
                hotpoor_ws.onopen = () ->
                    if hotpoor_ws_device != WS_DEVICE.POST
                        hotpoor_ws_device = WS_DEVICE.OPEN
                        hotpoor_ws_device_open = true
                        # load_chat_list() #开启hotpoor_ws成功，加载列表页
                        console.log "开启hotpoor_ws成功，加载列表页"                        
                hotpoor_ws.onmessage = (evt) ->
                    if hotpoor_ws_device != WS_DEVICE.POST
                        params = JSON.parse(evt.data)
                        on_message(params)
                    console.log "ws 收到消息"
                hotpoor_ws.onclose = () ->
                    console.log "hotpoor_ws_device:#{hotpoor_ws_device}"
                    if hotpoor_ws_device != WS_DEVICE.POST
                        if hotpoor_ws_device == WS_DEVICE.OPEN or hotpoor_ws_device == WS_DEVICE.READY
                            hotpoor_ws_device = WS_DEVICE.UNKNOWN
                            console.log "wait"
                            clearTimeout hotpoor_ws_timer if hotpoor_ws_timer
                            hotpoor_ws_timer = setTimeout restart_ws_connection(), 500 if USER_ID
                        else
                            hotpoor_ws_device = WS_DEVICE.BAD
                hotpoor_ws.onerror = () ->
                    console.log "ws error"
                clearTimeout hotpoor_ws_timer if hotpoor_ws_timer
                hotpoor_ws_timer = setTimeout restart_ws_connection, 3000 if USER_ID
            else
                if hotpoor_ws_device_open
                    clearTimeout hotpoor_ws_timer if hotpoor_ws_timer
                    hotpoor_ws_timer = setTimeout restart_ws_connection, 3000 if USER_ID
                    return
        restart_ws_connection()
    root.page_dom_content_self = (content_json,key,key_item)->
        # console.log content_json[key]
        # console.log key
        # console.log key_item
        if typeof(content_json[key])=="string"
            # uuid2_now = JSON.parse(content_json[key])[key_item]
            try
                uuid2_now = JSON.parse(content_json[key])[key_item]
            catch d
                return false

        else
            uuid2_now = content_json[key][key_item]
        # console.log "uuid2_now:",uuid2_now
        if uuid2_now in root.uuid2s
            root.uuid2s.pop uuid2_now
            return true
        return false
    root.page_doms_copy = (content_jsons)->
        # console.log content_jsons["content"]
        page_doms_copy_items = []
        for content_json_content in content_jsons["content"]
            page_doms_copy_items.push content_json_content["dom_current"]
            do (content_json_content)->
                console.log "page_doms_copy"
                new_content_json = content_jsons
                new_content_json["content"]=content_json_content
                page_dom_copy(new_content_json)
        action_ids = "##{page_doms_copy_items.join(",#")}"
        $(".card_more_selected").removeClass("card_more_selected")
        $(action_ids).find(".card_more_select").click()
    root.page_dom_copy = (content_json)->
        page_dom_add content_json, true, (content_json)->
            do (content_json)->
                main_content = content_json["content"]
                if main_content["dom_type"] == "iframe"
                    page_dom_iframe content_json,true,(content_json)->
                        page_dom(content_json)
                else if main_content["dom_type"] == "text"
                    page_dom_content content_json,true,(content_json)->
                        page_dom(content_json)
                else if main_content["dom_type"] == "video"
                    page_dom_video content_json,true,(content_json)->
                        page_dom(content_json)
                else
                    page_dom(content_json)
    root.page_doms = (content_json)->
        if page_dom_content_self(content_json["content"],"dom_content","uuid")
            console.log "self"
        main_content = content_json["content"]
        console.log main_content
        for update in main_content["updates"]
            $("##{update["dom_id"]}").animate
                "left":update["x"]
                "top":update["y"]
    root.page_dom = (content_json)->
        console.log "page_dom"
        console.log content_json
        main_content = content_json["content"]
        dom_id = main_content["dom_current"]
        dom_x = main_content["dom_position_x"]
        dom_y = main_content["dom_position_y"]
        dom_w = main_content["dom_position_w"]
        dom_h = main_content["dom_position_h"]
        dom_z = main_content["dom_position_z"]
        dom_s = main_content["dom_scroll"]
        dom = $("##{dom_id}")
        if $("##{dom_id}").hasClass "dom_video"
            $("#player_#{dom_id}").css
                "width":dom_w
                "height":dom_h
        else if $("##{dom_id}").hasClass "dom_iframe"
            $("##{dom_id}>.sections>.section>iframe").css
                "width":dom_w
                "height":dom_h
        if dom.hasClass("self-editing")
            return
        if dom_s == ""
            if $("##{dom_id}").hasClass("dom_scroll_auto")
                $("##{dom_id}").removeClass("dom_scroll_auto")
        else
            $("##{dom_id}").addClass(dom_s)
        $("##{dom_id}").css
            "width":dom_w
            "height":dom_h
        $("##{dom_id}").animate
                "left":dom_x
                "top":dom_y
                "zIndex":dom_z
            ,500
        $("##{dom_id}>.z_index_num").text dom_z
        
    root.page_dom_content = (content_json,copy=false,callback=null)->
        if page_dom_content_self(content_json["content"],"dom_content","uuid")
            console.log "self"
        else
            if copy
                b = content_json["content"]["dom_content"]
                b_html = b
                b["dom_id"]=content_json["content"]["dom_current"]
            else    
                b = JSON.parse(content_json["content"]["dom_content"])
                b_html = b["text"]

            a=$("##{b["dom_id"]}>.sections").html(b_html)
            # a=$("##{b["dom_id"]}>.sections>.section")[0]
            
            # if b["end_node"]==b["start_node"] and b["end"]>b["start"]
            #     # 增加
            #     if b["text"]!=""
            #         a.childNodes[b["start_node"]].insertData(b["start"],b["text"])
            # else if b["end_node"]==b["start_node"] and b["end"]<=b["start"]
            #     if b["text"]!=""
            #         # 删除
            #         a.childNodes[b["start_node"]].deleteData(b["end"],b["start"]-b["end"])
            # else if b["end_node"]==-1
            #     a.insertBefore(document.createElement("br"),null)
            # a.normalize()
        if copy
            callback(content_json)
    root.page_dom_video = (content_json,copy=false,callback=null)->
        main_content = content_json["content"]
        if copy
            b = content_json["content"]["dom_content"]
            b["dom_id"]=main_content["dom_current"]
            b["text"]=
                "src":b["src"]
                "type":b["type"]
                "poster":b["poster"]
        else
            b = JSON.parse(content_json["content"]["dom_content"])
        # b = JSON.parse(content_json["content"]["dom_content"])
        w = $("##{b["dom_id"]}").width()
        h = $("##{b["dom_id"]}").height()
        $("#player_#{b["dom_id"]}").remove()
        vt = uuid2(6,null)
        $(".novideospan").remove()
        $("##{b['dom_id']}>.sections>.section").prepend """
            <video id="player_#{b["dom_id"]}" data-vt="#{vt}" width="#{w}" height="#{h}" class="video-js vjs-default-skin" poster="#{b["text"]["poster"]}"
                    controls
                    webkit-playsinline="true"
                    playsinline="true">
                <source src="#{b["text"]["src"]}" type="#{b["text"]["type"]}">
                <!-- .m3u8 application/x-mpegURL -->
            </video>
        """
        videojs("#player_#{b["dom_id"]}[data-vt=#{vt}]");

        if PAGE_TYPE == "page_edit"
            $("##{b['dom_id']}>.sections>.section>div>.video_src").val(b["text"]["src"])
            $("##{b['dom_id']}>.sections>.section>div>.video_type").val(b["text"]["type"])
            $("##{b['dom_id']}>.sections>.section>div>.video_poster").val(b["text"]["poster"])
        if copy
            callback(content_json)
    root.page_dom_iframe = (content_json,copy=false,callback=null)->
        main_content = content_json["content"]
        if copy
            console.log main_content
            console.log "========"
            b = main_content["dom_content"]
            iframe_html = b["html"]
            b["dom_id"]=main_content["dom_current"]
            console.log b["dom_id"]
        else
            b = JSON.parse(main_content["dom_content"])
            iframe_html = b["text"]["html"]
        console.log iframe_html
        # b = JSON.parse(content_json["content"]["dom_content"])
        w = $("##{b["dom_id"]}").width()
        h = $("##{b["dom_id"]}").height()

        if PAGE_TYPE == "page_edit"
            iframe_tools = $("##{b['dom_id']}>.sections>.section>.iframe_tool")
        $("##{b['dom_id']}>.sections>.section>iframe").remove()
        $("##{b['dom_id']}>.sections>.section").empty()
        $("##{b['dom_id']}>.sections>.section").prepend """
            #{iframe_html}
        """
        $("##{b['dom_id']}>.sections>.section>iframe").css
            "width":w
            "height":h

        if PAGE_TYPE == "page_edit"
            $("##{b['dom_id']}>.sections>.section").append iframe_tools
            console.log "===iframe_html 1==="
            console.log iframe_html
            console.log "===iframe_html 2==="
            console.log "##{b['dom_id']}>.sections>.section>div>.iframe_html"
            $("##{b['dom_id']}>.sections>.section>div>.iframe_html").val(iframe_html)
        if copy
            callback(content_json)
    root.page_main_area=(content_json)->
        main_content = content_json["content"]
        if page_dom_content_self(main_content,"dom_content","uuid")
            console.log "self"
            return
        if typeof(content_json["content"]["dom_content"]) == "string"
            b = JSON.parse(content_json["content"]["dom_content"])
        else
            b = content_json["content"]["dom_content"]
        # b = JSON.parse(content_json["content"]["dom_content"])
        _w = parseInt(b["text"]["w"])
        _h = parseInt(b["text"]["h"])

        if PAGE_TYPE in ["page_edit","page"]
            $(".main_area").css
                "width":"#{_w}px"
                "height":"#{_h}px"
    root.page_grid_graph=(content_json)->
        main_content = content_json["content"]
        if page_dom_content_self(main_content,"dom_content","uuid")
            console.log "self"
            return
        if typeof(content_json["content"]["dom_content"]) == "string"
            b = JSON.parse(content_json["content"]["dom_content"])
        else
            b = content_json["content"]["dom_content"]
        # b = JSON.parse(content_json["content"]["dom_content"])
        _w = parseInt(b["text"]["w"])*2
        _h = parseInt(b["text"]["h"])*2

        if PAGE_TYPE == "page_edit"
            $("body").css
                "backgroundSize":"#{_w}px #{_h}px"
    root.page_title=(content_json)->
        main_content = content_json["content"]
        if page_dom_content_self(main_content,"dom_content","uuid")
            console.log "self"
            return
        b = JSON.parse(content_json["content"]["dom_content"])
        title = b["title"]

        if PAGE_TYPE == "page_edit"
            $(".top_area_info_title").val(title)
            title_tag = "Page Edit |"
        else
            title_tag = "Page |"
        title_list = window.document.title.split(title_tag)
        title_list[0]="#{title} | "
        window.document.title = title_list.join(title_tag)
    root.page_desc=(content_json)->
        main_content = content_json["content"]
        if page_dom_content_self(main_content,"dom_content","uuid")
            console.log "self"
            return
        b = JSON.parse(content_json["content"]["dom_content"])
        desc = b["desc"]

        if PAGE_TYPE == "page_edit"
            block_desc_content_old = desc
            $(".block_desc_content").val(desc)
    root.page_dom_add = (content_json,copy=false,callback=null)->
        main_content = content_json["content"]
        dom_current = main_content["dom_current"]
        dom_content = main_content["dom_content"]
        dom_x = main_content["dom_position_x"]
        dom_y = main_content["dom_position_y"]
        dom_w = main_content["dom_position_w"]
        dom_h = main_content["dom_position_h"]
        dom_z = main_content["dom_position_z"]
        dom_type = main_content["dom_type"]

        if PAGE_TYPE == "page_edit"
            if dom_type == "video"
                dom_content = """
                <div class="section" contenteditable="false">
                    <div>video_src:<input class="video_src"></div>
                    <div>video_type:<input class="video_type"></div>
                    <div>video_poster:<input class="video_poster"></div>
                    <div>
                        <button class="video_add">upload video</button>
                        <button class="video_save">save</button>
                    </div>
                </div>
                """
            else if dom_type == "iframe"
                dom_content = """
                <div class="section" contenteditable="false">
                    <div class="iframe_tool"><textarea class="iframe_html" placeholder="/* html code */"></textarea></div>
                    <div class="iframe_tool"><button class="iframe_save">save</button></div>
                </div>
                """
            html_control = """
            <div class="card_move"></div>
            <div class="card_more_select"></div>
            <div class="card_copy"></div>
            <div class="card_del"></div>
            <div class="resize_btn"></div>
            <div class="card_z_index z_index_max"></div>
            <div class="card_z_index z_index_up"></div>
            <div class="card_z_index z_index_down"></div>
            <div class="card_z_index z_index_min"></div>
            <div class="card_z_index z_index_num">#{dom_z}</div>
            <div class="card_scroll_auto">
                <div class="card_scroll_auto_svg"></div>
            </div>
            <div class="card_text_align text_align_left"></div>
            <div class="card_text_align text_align_center"></div>
            <div class="card_text_align text_align_right"></div>
            <div class="card_font_size font_size_big"></div>
            <div class="card_font_size font_size_small"></div>
            <div class="card_font_color">
                <div class="card_font_color_demo">
                </div>
            </div>
            """
            html = """
                <div    id="#{dom_current}" class="card dom dom_#{dom_type}" 
                            data-tree="#{dom_current}" 
                            style="
                            left: #{dom_x}px;
                            top: #{dom_y}px;
                            width: #{dom_w}px;
                            height: #{dom_h}px;
                            z-index: #{dom_z};
                            ">
                            <style></style>
                        <div class="sections" contenteditable="true">
                            #{dom_content}
                        </div>
                        #{html_control}
                </div>
            """
        else
            if dom_type == "video"
                dom_content = """
                    <div class="section" contenteditable="false"><span class="novideospan">视频未设置</span></div>
                """
            else if dom_type == "iframe"
                dom_content = """
                    <div class="section" contenteditable="false">iframe暂未设置</div>
                """
            html_control = """
            <div class="card_move hide"></div>
            <div class="card_more_select hide"></div>
            <div class="card_copy hide"></div>
            <div class="card_del hide"></div>
            <div class="resize_btn hide"></div>
            """
            html = """
                <div    id="#{dom_current}" class="card dom dom_#{dom_type}" 
                            data-tree="#{dom_current}" 
                            style="
                            left: #{dom_x}px;
                            top: #{dom_y}px;
                            width: #{dom_w}px;
                            height: #{dom_h}px;
                            z-index: #{dom_z};
                            ">
                            <style></style>
                        <div class="sections">
                            #{dom_content}
                        </div>
                        #{html_control}
                </div>
            """
        $(".main_area").append html
        if copy
            callback(content_json)
    root.page_dom_del = (content_json)->
        main_content = content_json["content"]
        dom_current = main_content["dom_current"]
        $("##{dom_current}").remove()
    root.page_permission = (content_json)->
        main_content = content_json["content"]
        if main_content["action"] == "reload page"
            window.location.reload()
    root.page_editor_change = (content_json)->
        main_content = content_json["content"]
        if main_content["aim_user_id"] == USER_ID
            window.location.reload()
        else
            if main_content["action"] == "add editor"
                load_users(["editors"])
            else if main_content["action"] == "del editor"
                load_users(["editors"])

    root.load_users = (groups)->
        for group in groups
            do (group)->
                $.ajax
                    url:"/api/page/#{group}"
                    data:
                        block_id: BLOCK_ID
                    dataType: 'json'
                    type: 'GET'
                    success:(data)->
                        console.log JSON.stringify(data)
                        $(".block_#{group}_list").text data[group]
                    error:(data)->
                        console.log data
    root.load_users(["editors","readers","blackers","members"])

    device_uuid = uuid2(6,null)
    Hs.DEVICE_USER = "#{USER_ID}_#{device_uuid}"
    console.log Hs.DEVICE_USER
