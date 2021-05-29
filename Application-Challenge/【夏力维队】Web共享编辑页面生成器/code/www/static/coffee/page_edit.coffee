root = exports ? this
# !!!! Hotpoor root object
root.Hs or= {}
Hs = root.Hs
root.page_edit = true
root.symbol_window_select_allow = false
$ ->

    send_data_now = false
    send_data =
        "dom_id":null
        "range":null
        "start":null
        "start_node":null
        "end":null
        "end_node":null
        "text":null
        "uuid":null
        "base_text":null
    send_data_node_num_check = (section,node)->
        for i in [0..section.childNodes.length-1]
            if section.childNodes[i] == node
                return i
        return -1

    dom_current = ""
    console.log "page edit"

    editing_now_timeout = null
    editing_now = (dom)->
        if editing_now_timeout != null
            clearTimeout editing_now_timeout
        dom.addClass("self-editing")
        # do (dom,editing_now_timeout)->
        editing_now_timeout = setTimeout ()->
                console.log "editing_now_timeout"
                dom.removeClass("self-editing")
            ,1500
    save_dom_current_timeout = null
    update_dom_content_timeout = null
    update_dom_video_timeout = null
    update_dom_iframe_timeout = null
    update_title_timeout = null
    update_desc_timeout = null

    update_grid_graph_timeout = null
    update_main_area_timeout = null

    root.update_dom_content_img = (sections)->
        dom = sections.parents(".dom")
        send_data["dom_id"]=dom.attr("id")
        html = sections.html()
        send_data["text"] = html

        if editing_now_timeout != null
            clearTimeout update_dom_content_timeout
        update_dom_content_timeout = setTimeout ()->
                update_dom_content(dom,send_data)
            ,500
    update_dom_content = (dom,adata)->
        dom_tree = dom.attr("data-tree")
        dom_tree_list = dom_tree.split("_")
        dom_owner = dom_tree_list[dom_tree_list.length-1]
        # $(".dom_owner").val(dom_owner)
        dom_current = dom_tree
        adata["uuid"]=uuid2(6,null)
        uuid2s.push adata["uuid"]
        if typeof(adata["text"]) == "undefined"
            console.log "undefined",adata
            send_data_now = false
            return
        dom_content = JSON.stringify(adata)
        send_data_now = false

        $(".network_now").text "保存中"
        $.ajax
            url:"/api/page/update_dom_content"
            data:
                block_id: BLOCK_ID
                dom_owner: dom_owner
                dom_current: dom_current
                dom_content: dom_content
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
                if data.info == "ok"
                    $(".network_now").text "已保存"
                    setTimeout ()->
                        $(".network_now").remove()
                    ,500
                else if data.info == "error"
                    $(".network_now").text data.about
            error:(data)->
                console.log data
                $(".network_now").text "保存失败"
    update_dom_video = (dom,adata)->
        dom_tree = dom.attr("data-tree")
        dom_tree_list = dom_tree.split("_")
        dom_owner = dom_tree_list[dom_tree_list.length-1]
        # $(".dom_owner").val(dom_owner)
        dom_current = dom_tree
        adata["uuid"]=uuid2(6,null)
        uuid2s.push adata["uuid"]
        if typeof(adata["text"]) == "undefined"
            console.log "undefined",adata
            send_data_now = false
            return
        dom_content = JSON.stringify(adata)
        send_data_now = false
        $.ajax
            url:"/api/page/update_dom_video"
            data:
                block_id: BLOCK_ID
                dom_owner: dom_owner
                dom_current: dom_current
                dom_content: dom_content
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
            error:(data)->
                console.log data
    update_dom_iframe = (dom,adata)->
        dom_tree = dom.attr("data-tree")
        dom_tree_list = dom_tree.split("_")
        dom_owner = dom_tree_list[dom_tree_list.length-1]
        # $(".dom_owner").val(dom_owner)
        dom_current = dom_tree
        adata["uuid"]=uuid2(6,null)
        uuid2s.push adata["uuid"]
        if typeof(adata["text"]) == "undefined"
            console.log "undefined",adata
            send_data_now = false
            return
        dom_content = JSON.stringify(adata)
        send_data_now = false
        $.ajax
            url:"/api/page/update_dom_iframe"
            data:
                block_id: BLOCK_ID
                dom_owner: dom_owner
                dom_current: dom_current
                dom_content: dom_content
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
            error:(data)->
                console.log data
    # send_data = {"dom_id","range","start","end","text","uuid"}

    $("body").on "keydown", ".iframe_tool>textarea", (evt)->
        dom = $(this)
        if evt.key == "Tab"
            a = this
            s = a.selectionStart
            str_0 = a.value.substring(0,s)
            str_1 = a.value.substring(s,a.value.length)
            str = "#{str_0}    #{str_1}"
            a.value = str
            a.selectionStart = s+4
            a.selectionEnd = s+4
            # evt.stopPropagation()
            # evt.preventDefault()
            return false
    
    $("body").on "keydown",".dom.dom_text,.dom.dom_img",(evt)->
        if not send_data_now
            send_data["dom_id"]=$(this).attr("id")
            # range = window.getSelection().getRangeAt(0);
            # section = range.commonAncestorContainer.parentElement
            # # send_data[1]=range.commonAncestorContainer
            # console.log range
            # send_data["start"]=range.startOffset
            # send_data["start_node"]=send_data_node_num_check(section,range.startContainer)
            # send_data["base_text"]=range.commonAncestorContainer.textContent
            send_data_now = true

    $("body").on "keyup",".dom.dom_text,.dom.dom_img",(evt)->
        if send_data_now
            range = window.getSelection().getRangeAt(0);
            section = range.commonAncestorContainer.parentElement
            # text = range.commonAncestorContainer.textContent
            # send_data["end"]=range.endOffset
            # send_data["end_node"]=send_data_node_num_check(section,range.endContainer)
            # if send_data["end"]>send_data["start"]
            #     if evt.keyCode not in [33,34,39,40]
            #         text = text.substring(send_data["start"],send_data["end"])
            #     else
            #         text = ""
            # else if send_data["end"]<=send_data["start"]
            #     if evt.keyCode in [8]
            #         text = send_data["base_text"].substring(send_data["end"],send_data["start"])
            #     else
            #         text = ""
            if $(section).hasClass("sections")
                # console.log "111"
                if evt.keyCode in [8]
                    # console.log "del",$(section).find(".section").length,
                    if $(section).find(".section").length < 1
                        $(section).append """
                            <div class="section"><br></div>
                        """
                        
                html = $(section).html()
            else
                # console.log "222"
                sections = $(section).parents(".sections")
                html = sections.html()
                # console.log section,sections

                if $(section).hasClass("dom")
                    $(section).find(".sections").append """
                        <div class="section"><br></div>
                    """

                # console.log html
            send_data["text"] = html

            # console.log section
            # console.log evt.keyCode,evt
        dom = $(this)
        editing_now(dom)
        if editing_now_timeout != null
            clearTimeout update_dom_content_timeout
        if $(".network_now").length < 1
            $("body").append """
            <div class="network_now">正在编辑</div>
            """
        update_dom_content_timeout = setTimeout ()->
                update_dom_content(dom,send_data)
            ,500
    iframe_add_iframe_pin_timout = null
    iframe_add_iframe_pin = (dom_sequence)->
        clearTimeout iframe_add_iframe_pin_timout
        iframe_add_iframe_pin_timout = setTimeout ()->
                if $("##{dom_sequence}").length < 1
                    return iframe_add_iframe_pin(dom_sequence)
                iframe_pin = """
                <div class="add_iframe_pin_hover_info">
                <p>input some hover words here</p>
                </div>
                <style>
                .add_iframe_pin_hover_info{
                        width:30px;
                        height:20px;
                }
                .add_iframe_pin_hover_info>p{}
                .add_hover_info:hover{}
                </style>
                """
                $("##{dom_sequence}").find(".iframe_html").val(iframe_pin)
                $("##{dom_sequence}").find(".iframe_save").click()
            ,1000

        
    $("body").on "click",".page_edit_tools>div>.add",(evt)->
        dom_type_old = $(this).attr("data-type")
        dom_type = dom_type_old
        dom_current = $(".dom_current").val()
        _hbase = $(window).scrollTop()
        _wbase = $(window).scrollLeft()
        _w = parseInt($(window).width()/7*3-$(".main_area").offset()["left"])
        _h = parseInt($(window).height()/7*3)
        uri = "/api/page/add_dom"
        if dom_type in ["pin"]
            dom_type = "iframe"
            ajax_data =
                block_id: BLOCK_ID
                dom_owner: dom_current
                dom_type: dom_type
                dom_position_x: _wbase + _w
                dom_position_y: _hbase + _h
        else if dom_type in ["domcopy"]
            current_copy = localStorage.getItem("current_copy")
            if current_copy == null
                return
            current_copy = JSON.parse(current_copy)
            if current_copy.length > 1
                uri = "/api/page/copy_doms"
                ajax_data =
                    block_id: BLOCK_ID
                    dom_owner: dom_current
                    dom_type: dom_type
                    dom_position_x: _wbase + _w
                    dom_position_y: _hbase + _h
                    current_copy: JSON.stringify(current_copy)
            else
                uri = "/api/page/copy_dom"
                current_copy_list = current_copy[0].split("COPYDOM//")
                [aim_id,aim_dom_id]=current_copy_list[1].split(",")
                ajax_data =
                    block_id: BLOCK_ID
                    dom_owner: dom_current
                    dom_type: dom_type
                    dom_position_x: _wbase + _w
                    dom_position_y: _hbase + _h
                    aim_id:aim_id
                    aim_dom_id:aim_dom_id
        else if dom_type in ["more_select"]
            $(".card_more_select").removeClass("card_more_selected")
            return
        else if dom_type in ["area_select"]
            if $(".add_btn_area_select").hasClass("show")
                $(".add_btn_area_select").removeClass("show")
                root.symbol_window_select_allow = false
            else
                $(".add_btn_area_select").addClass("show")
                root.symbol_window_select_allow = true
            return
        else
            ajax_data =
                block_id: BLOCK_ID
                dom_owner: dom_current
                dom_type: dom_type
                dom_position_x: _wbase + _w
                dom_position_y: _hbase + _h
        $.ajax
            url:uri
            data:ajax_data
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
                if data.info == "error"
                    alert data.about
                if data.info == "ok"
                    if dom_type_old in ["pin"]
                        iframe_add_iframe_pin(data.dom_sequence)
            error:(data)->
                console.log data
    $("body").on "click",".card>.card_del",(evt)->
        dom = $(this)
        card = dom.parents(".card")
        dom_tree = card.attr("data-tree")
        dom_tree_list = dom_tree.split("_")
        dom_owner = dom_tree_list[dom_tree_list.length-1]
        dom_current = dom_tree
        $.ajax
            url:"/api/page/del_dom"
            data:
                block_id: BLOCK_ID
                dom_owner: dom_owner
                dom_current: dom_current
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
            error:(data)->
                console.log data
    root.page_edit_current = (dom,save=false,callback=null)->
        # console.log dom
        if dom == null
            dom_owner = ""
            $(".dom_owner").val(dom_owner)
            dom_current = ""
            $(".dom_current").val(dom_current)
        else
            if dom.hasClass("just_tool_move")
                fix_page_edit_tools_list_cover()
                return
            block_width = $(".main_area").width()
            block_height = $(".main_area").height()
            $("input.block_width").val(block_width)
            $("input.block_height").val(block_height)
            x=dom.position().left
            y=dom.position().top
            $("input.dom_position_x").val(x)
            $("input.dom_position_y").val(y)
            w=dom.width()
            h=dom.height()
            $("input.dom_position_w").val(w)
            $("input.dom_position_h").val(h)
            z = dom.css("zIndex")
            if z == "auto"
                z = "0"
            z = parseInt(z)
            # console.log "z",z
            $("input.dom_position_z").val(z)
            dom_tree = dom.attr("data-tree")
            dom_tree_list = dom_tree.split("_")
            dom_owner = dom_tree_list[dom_tree_list.length-1]
            $(".dom_owner").val(dom_owner)
            dom_current = dom_tree
            $(".dom_current").val(dom_current)
            if save
                if callback == null
                    save_dom_current()
                else
                    save_dom_current ()->
                        callback(dom)
                editing_now(dom)
    root.save_dom_current = (callback=null)->
        dom_current = $(".dom_current").val()
        dom_owner = $(".dom_owner").val()
        dom_scroll_auto_now = ""
        if $("##{dom_current}").hasClass("dom_scroll_auto")
            dom_scroll_auto_now = "dom_scroll_auto"
        $.ajax
            url:"/api/page/update_dom"
            data:
                block_id: BLOCK_ID
                dom_owner: dom_owner
                dom_current: dom_current
                dom_position_x: $("input.dom_position_x").val()
                dom_position_y: $("input.dom_position_y").val()
                dom_position_w: $("input.dom_position_w").val()
                dom_position_h: $("input.dom_position_h").val()
                dom_position_z: $("input.dom_position_z").val()
                dom_scroll:dom_scroll_auto_now
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
                console.log typeof(callback)
                if callback != null
                    callback()
            error:(data)->
                console.log data
    $("body").on "click",".save",(evt)->
        save_dom_current()


    $("body").on "click",".block_permission_span_btns>span",(evt)->
        dom = $(this)
        permission = dom.attr("data-value")
        $.ajax
            url:"/api/page/set_permission"
            data:
                block_id: BLOCK_ID
                permission: permission
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
                if data.info == "ok"
                    $(".block_permission_span_btns>span").removeClass("selected")
                    $(".block_permission_span_btns>span[data-value=#{permission}]").addClass("selected")
                else
                    alert data.about

            error:(data)->
                console.log data

    $("body").on "click",".block_editors_tool>.add",(evt)->
        aim_user_id = $(".block_editors_tool>.block_editors_search").val()
        $.ajax
            url:"/api/page/add_editor"
            data:
                block_id: BLOCK_ID
                aim_user_id: aim_user_id
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
            error:(data)->
                console.log data
    $("body").on "click",".block_editors_tool>.del",(evt)->
        aim_user_id = $(".block_editors_tool>.block_editors_search").val()
        $.ajax
            url:"/api/page/del_editor"
            data:
                block_id: BLOCK_ID
                aim_user_id: aim_user_id
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
            error:(data)->
                console.log data
    $("body").on "click",".card>.z_index_max",(evt)->
        console.log "zindex update z_index_max"
        dom = $(this)
        card = dom.parents(".card")
        dom_tree = card.attr("data-tree")
        dom_tree_list = dom_tree.split("_")
        dom_owner = dom_tree_list[dom_tree_list.length-1]
        dom_current = dom_tree
        card_zIndex = card.css("zIndex")
        max_zIndx = get_max_zIndex()+1
        $("input.dom_position_z").val(max_zIndx)
        card.find(".z_index_num").text card_zIndex
        card.css("zIndex",max_zIndx)
        page_edit_current(card,true)
    $("body").on "click",".card>.z_index_up",(evt)->
        console.log "zindex update z_index_up"
        dom = $(this)
        card = dom.parents(".card")
        dom_tree = card.attr("data-tree")
        dom_tree_list = dom_tree.split("_")
        dom_owner = dom_tree_list[dom_tree_list.length-1]
        dom_current = dom_tree
        card_zIndex = card.css("zIndex")
        if card_zIndex == "auto"
            card_zIndex = 0
        card_zIndex = parseInt(card_zIndex)
        max_zIndx = get_max_zIndex()
        if card_zIndex <= max_zIndx
            card_zIndex += 1
        $("input.dom_position_z").val(card_zIndex)
        card.find(".z_index_num").text card_zIndex
        card.css("zIndex",card_zIndex)
        page_edit_current(card,true)
    $("body").on "click",".card>.z_index_down",(evt)->
        console.log "zindex update z_index_down"
        dom = $(this)
        card = dom.parents(".card")
        dom_tree = card.attr("data-tree")
        dom_tree_list = dom_tree.split("_")
        dom_owner = dom_tree_list[dom_tree_list.length-1]
        dom_current = dom_tree
        card_zIndex = card.css("zIndex")
        if card_zIndex == "auto"
            card_zIndex = 0
        card_zIndex = parseInt(card_zIndex)
        if card_zIndex > 0
            card_zIndex -= 1
        $("input.dom_position_z").val(card_zIndex)
        card.find(".z_index_num").text card_zIndex
        card.css("zIndex",card_zIndex)
        page_edit_current(card,true)
    get_max_zIndex = ()->
        max_zIndx = 0
        for card in $(".card.dom")
            card_zIndex = $(card).css("zIndex")
            if card_zIndex == "auto"
                card_zIndex = 0
            else
                card_zIndex = parseInt(card_zIndex)
            if max_zIndx<card_zIndex
                max_zIndx = card_zIndex
        return max_zIndx

    $("body").on "click",".section>div>.video_save",(evt)->
        if not send_data_now
            send_data_now = true
            section = $(this).parents(".section")
            dom = $(this).parents(".dom")
            send_data["dom_id"]=dom.attr("id")
            send_data["text"]=
                "src":section.find(".video_src").val()
                "type":section.find(".video_type").val()
                "poster":section.find(".video_poster").val()
            editing_now(dom)
            if editing_now_timeout != null
                clearTimeout update_dom_video_timeout
            update_dom_video_timeout = setTimeout ()->
                    update_dom_video(dom,send_data)
                ,500
    $("body").on "click",".section>div>.iframe_save",(evt)->
        if not send_data_now
            send_data_now = true
            section = $(this).parents(".section")
            dom = $(this).parents(".dom")
            send_data["dom_id"]=dom.attr("id")
            iframe_now = uuid2(6,null)
            $("body").append """
                <iframe id="#{iframe_now}" class="hide"></iframe>
            """
            a= $("##{iframe_now}")[0]
            a.contentDocument.body.innerHTML = section.find(".iframe_html").val()
            if a.contentDocument.body.getInnerHTML == undefined
                _html = a.contentDocument.body.innerHTML
            else
                _html = a.contentDocument.body.getInnerHTML()
            send_data["text"]=
                    "html":_html
            section.find(".iframe_html").val(_html)
            $("#{iframe_now}").remove()
            editing_now(dom)
            if editing_now_timeout != null
                clearTimeout update_dom_iframe_timeout
            update_dom_iframe_timeout = setTimeout ()->
                    update_dom_iframe(dom,send_data)
                ,500
    update_title = (adata)->
        adata["uuid"]=uuid2(6,null)
        uuid2s.push adata["uuid"]
        if typeof(adata["text"]) == "undefined"
            console.log "undefined",adata
            send_data_now = false
            return
        dom_content = JSON.stringify(adata)
        send_data_now = false
        $.ajax
            url:"/api/page/update_title"
            data:
                block_id: BLOCK_ID
                dom_content: dom_content
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
            error:(data)->
                console.log data
    update_desc = (adata)->
        adata["uuid"]=uuid2(6,null)
        uuid2s.push adata["uuid"]
        if typeof(adata["text"]) == "undefined"
            console.log "undefined",adata
            send_data_now = false
            return
        dom_content = JSON.stringify(adata)
        send_data_now = false
        $.ajax
            url:"/api/page/update_desc"
            data:
                block_id: BLOCK_ID
                dom_content: dom_content
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
                if data.info != "ok"
                    $(".block_desc_content_save").text "error"
                    alert data.about
                    return
                $(".block_desc_content_save").text "saved"
                $(".block_desc_content_unsave").addClass "hide"
                block_desc_content_old = $(".block_desc_content").val()
                setTimeout ()->
                        $(".block_desc_content_save").text "save"
                    ,1000

            error:(data)->
                console.log data
    update_main_area = (adata)->
        adata["uuid"]=uuid2(6,null)
        uuid2s.push adata["uuid"]
        if typeof(adata["text"]) == "undefined"
            console.log "undefined",adata
            send_data_now = false
            return
        dom_content = JSON.stringify(adata)
        send_data_now = false
        $.ajax
            url:"/api/page/update_main_area"
            data:
                block_id: BLOCK_ID
                dom_content: dom_content
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
                if data.info != "ok"
                    $(".block_main_area_save").text "error"
                    alert data.about
                    return
                $(".block_main_area_save").text "saved"
                setTimeout ()->
                        $(".block_main_area_save").text "save"
                    ,1000

            error:(data)->
                console.log data
    $("body").on "click",".block_main_area_save",(evt)->
        $(".block_main_area_save").text "saving"
        w = $(".block_main_area_w").val()
        h = $(".block_main_area_h").val()
        send_data["text"]=
            "w":w
            "h":h
        _w = parseInt(w)
        _h = parseInt(h)
        $(".main_area").css
            "width":"#{_w}px"
            "height":"#{_h}px"
        dom = $(this)
        editing_now(dom)
        if editing_now_timeout != null
            clearTimeout update_main_area_timeout
        update_main_area_timeout = setTimeout ()->
                update_main_area(send_data)
            ,500
    update_grid_graph = (adata)->
        adata["uuid"]=uuid2(6,null)
        uuid2s.push adata["uuid"]
        if typeof(adata["text"]) == "undefined"
            console.log "undefined",adata
            send_data_now = false
            return
        dom_content = JSON.stringify(adata)
        send_data_now = false
        $.ajax
            url:"/api/page/update_grid_graph"
            data:
                block_id: BLOCK_ID
                dom_content: dom_content
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
                if data.info != "ok"
                    $(".block_grid_graph_save").text "error"
                    alert data.about
                    return
                $(".block_grid_graph_save").text "saved"
                setTimeout ()->
                        $(".block_grid_graph_save").text "save"
                    ,1000

            error:(data)->
                console.log data
    $("body").on "click",".block_grid_graph_save",(evt)->
        $(".block_grid_graph_save").text "saving"
        w = $(".block_grid_graph_w").val()
        h = $(".block_grid_graph_h").val()
        send_data["text"]=
            "w":w
            "h":h
        _w = parseInt(w)*2
        _h = parseInt(h)*2
        $("body").css
            "backgroundSize":"#{_w}px #{_h}px"
        dom = $(this)
        editing_now(dom)
        if editing_now_timeout != null
            clearTimeout update_grid_graph_timeout
        update_grid_graph_timeout = setTimeout ()->
                update_grid_graph(send_data)
            ,500
    $("body").on "input propertychange",".top_area_info_title",(evt)->
        title = $(this).val()
        send_data["title"] = title
        title_tag = "Page Edit |"
        title_list = window.document.title.split(title_tag)
        title_list[0]="#{title} | "
        window.document.title = title_list.join(title_tag)
        dom = $(this)
        editing_now(dom)
        if editing_now_timeout != null
            clearTimeout update_title_timeout
        update_title_timeout = setTimeout ()->
                update_title(send_data)
            ,500
    $("body").on "click",".add_btn_more",(evt)->
        dom = $(this)
        clc = dom.parents(".page_edit_tools").position().left
        fix_page_edit_tools_list_cover()
        if dom.hasClass("show")
            dom.removeClass("show")
            $(".page_edit_tools_list_cover").addClass("hide")
        else
            dom.addClass("show")
            $(".page_edit_tools_list_cover").removeClass("hide")
    fix_page_edit_tools_list_cover=()->
        if $(".page_edit_tools").position().left > $(window).width()/2 + 60
            $(".page_edit_tools_list_cover").css
                "left":"unset"
                "right":60
        else
            $(".page_edit_tools_list_cover").css
                "right":"unset"
                "left":60

    $("body").on "click",".card>.card_text_align",(evt)->
        dom = $(this)
        card = dom.parents(".card")
        dom_tree = card.attr("data-tree")
        dom_tree_list = dom_tree.split("_")
        dom_owner = dom_tree_list[dom_tree_list.length-1]
        dom_current = dom_tree
        all_section = card.find(".section")
        for item_section in all_section
            if dom.hasClass("text_align_left")
                $(item_section).css("textAlign","left")
            else if dom.hasClass("text_align_center")
                $(item_section).css("textAlign","center")
            else if dom.hasClass("text_align_right")
                $(item_section).css("textAlign","right")

        send_data_now = true
        html = card.find(".sections").html()
        send_data["dom_id"] = card.attr("id")
        send_data["text"] = html
        editing_now(card)
        if editing_now_timeout != null
            clearTimeout update_dom_content_timeout
        if $(".network_now").length < 1
            $("body").append """
            <div class="network_now">正在编辑</div>
            """
        update_dom_content_timeout = setTimeout ()->
                update_dom_content(card,send_data)
            ,500

    $("body").on "click",".card>.card_font_size",(evt)->
        dom = $(this)
        card = dom.parents(".card")
        dom_tree = card.attr("data-tree")
        dom_tree_list = dom_tree.split("_")
        dom_owner = dom_tree_list[dom_tree_list.length-1]
        dom_current = dom_tree
        all_section = card.find(".section")
        if dom.hasClass("font_size_small")
            font_size_action = "small"
        else if dom.hasClass("font_size_big")
            font_size_action = "big"
        else
            return
        for item_section in all_section
            _font_size_now = parseInt($(item_section).css("fontSize"))
            if font_size_action == "small"
                _font_size_now = _font_size_now - 2
                if _font_size_now < 12
                    _font_size_now = 12
            else if font_size_action == "big"
                _font_size_now += 2
            $(item_section).css("fontSize","#{_font_size_now}px")

        send_data_now = true
        send_data["dom_id"] = card.attr("id")
        html = card.find(".sections").html()
        send_data["text"] = html
        editing_now(card)
        if editing_now_timeout != null
            clearTimeout update_dom_content_timeout
        if $(".network_now").length < 1
            $("body").append """
            <div class="network_now">正在编辑</div>
            """
        update_dom_content_timeout = setTimeout ()->
                update_dom_content(card,send_data)
            ,500

    $("body").on "click",".card>.card_scroll_auto",(evt)->
        console.log "card_scroll_auto update card_scroll_auto"
        dom = $(this)
        card = dom.parents(".card")
        dom_tree = card.attr("data-tree")
        dom_tree_list = dom_tree.split("_")
        dom_owner = dom_tree_list[dom_tree_list.length-1]
        dom_current = dom_tree
        if $("##{dom_current}").hasClass("dom_scroll_auto")
            card.removeClass("dom_scroll_auto")
        else
            card.addClass("dom_scroll_auto")
        page_edit_current(card,true)
    load_font_color_html =(dom_now,current_color="#000000",callback)->
        html = """
            <div class="card_font_color_demos">
                <div class="card_font_color_demo_item" 
                        style="background-color: #ffffff;" data-value="#ffffff"
                        data-dark="light"
                        data-name="白">
                        <span>白</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #000000;" data-value="#000000"
                        data-dark="dark"
                        data-name="漆黑">
                        <span>漆黑</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #ff0000;" data-value="#ff0000"
                        data-dark="dark"
                        data-name="赤">
                        <span>赤</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #ff7800;" data-value="#ff7800"
                        data-dark="dark"
                        data-name="橙">
                        <span>橙</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #ffd900;" data-value="#ffd900"
                        data-dark="light"
                        data-name="黄">
                        <span>黄</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #a3e043;" data-value="#a3e043"
                        data-dark="dark"
                        data-name="葱绿">
                        <span>葱绿</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #37d9f0;" data-value="#37d9f0"
                        data-dark="dark"
                        data-name="湖蓝">
                        <span>湖蓝</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #4da8ee;" data-value="#4da8ee"
                        data-dark="dark"
                        data-name="天色">
                        <span>天色</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #956fe7;" data-value="#956fe7"
                        data-dark="dark"
                        data-name="藤紫">
                        <span>藤紫</span>
                </div>

                <div class="card_font_color_demo_item" 
                        style="background-color: #f3f3f4;" data-value="#f3f3f4"
                        data-dark="light"
                        data-name="白练">
                        <span>白练</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #cccccc;" data-value="#cccccc"
                        data-dark="light"
                        data-name="白鼠">
                        <span>白鼠</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #fef2f0;" data-value="#fef2f0"
                        data-dark="light"
                        data-name="樱">
                        <span>樱</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #fef5e7;" data-value="#fef5e7"
                        data-dark="light"
                        data-name="缟">
                        <span>缟</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #fefcd9;" data-value="#fefcd9"
                        data-dark="light"
                        data-name="练">
                        <span>练</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #edf6e8;" data-value="#edf6e8"
                        data-dark="light"
                        data-name="芽">
                        <span>芽</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #e6fafa;" data-value="#e6fafa"
                        data-dark="light"
                        data-name="水">
                        <span>水</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #ebf4fc;" data-value="#ebf4fc"
                        data-dark="light"
                        data-name="缥">
                        <span>缥</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #f0edf6;" data-value="#f0edf6"
                        data-dark="light"
                        data-name="丁香">
                        <span>丁香</span>
                </div>

                <div class="card_font_color_demo_item" 
                        style="background-color: #d7d8d9;" data-value="#d7d8d9"
                        data-dark="light"
                        data-name="灰青">
                        <span>灰青</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #a5a5a5;" data-value="#a5a5a5"
                        data-dark="dark"
                        data-name="鼠">
                        <span>鼠</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #fbd4d0;" data-value="#fbd4d0"
                        data-dark="light"
                        data-name="虹">
                        <span>虹</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #ffd7b9;" data-value="#ffd7b9"
                        data-dark="light"
                        data-name="落柿">
                        <span>落柿</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #f9eda6;" data-value="#f9eda6"
                        data-dark="light"
                        data-name="花叶">
                        <span>花叶</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #d4e9d6;" data-value="#d4e9d6"
                        data-dark="light"
                        data-name="白绿">
                        <span>白绿</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #c7e6ea;" data-value="#c7e6ea"
                        data-dark="light"
                        data-name="天青">
                        <span>天青</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #cce0f1;" data-value="#cce0f1"
                        data-dark="light"
                        data-name="天空">
                        <span>天空</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #dad5e9;" data-value="#dad5e9"
                        data-dark="light"
                        data-name="水晶">
                        <span>水晶</span>
                </div>

                <div class="card_font_color_demo_item" 
                        style="background-color: #7b7f83;" data-value="#7b7f83"
                        data-dark="dark"
                        data-name="薄钝">
                        <span>薄钝</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #41464b;" data-value="#41464b"
                        data-dark="dark"
                        data-name="墨">
                        <span>墨</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #ee7976;" data-value="#ee7976"
                        data-dark="dark"
                        data-name="甚三红">
                        <span>甚三红</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #faa573;" data-value="#faa573"
                        data-dark="dark"
                        data-name="珊瑚">
                        <span>珊瑚</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #e6b322;" data-value="#e6b322"
                        data-dark="dark"
                        data-name="金">
                        <span>金</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #98c091;" data-value="#98c091"
                        data-dark="dark"
                        data-name="薄青">
                        <span>薄青</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #79c6cd;" data-value="#79c6cd"
                        data-dark="dark"
                        data-name="白群">
                        <span>白群</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #6eaad7;" data-value="#6eaad7"
                        data-dark="dark"
                        data-name="薄花">
                        <span>薄花</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #9c8ec1;" data-value="#9c8ec1"
                        data-dark="dark"
                        data-name="紫苑">
                        <span>紫苑</span>
                </div>

                <div class="card_font_color_demo_item" 
                        style="background-color: #41464b;" data-value="#41464b"
                        data-dark="dark"
                        data-name="石墨">
                        <span>石墨</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #333333;" data-value="#333333"
                        data-dark="dark"
                        data-name="黑">
                        <span>黑</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #be1a1d;" data-value="#be1a1d"
                        data-dark="dark"
                        data-name="绯红">
                        <span>绯红</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #b95514;" data-value="#b95514"
                        data-dark="dark"
                        data-name="棕黄">
                        <span>棕黄</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #ad720e;" data-value="#ad720e"
                        data-dark="dark"
                        data-name="土黄">
                        <span>土黄</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #1c7231;" data-value="#1c7231"
                        data-dark="dark"
                        data-name="苍翠">
                        <span>苍翠</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #1c7892;" data-value="#1c7892"
                        data-dark="dark"
                        data-name="孔雀">
                        <span>孔雀</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #19439c;" data-value="#19439c"
                        data-dark="dark"
                        data-name="琉璃">
                        <span>琉璃</span>
                </div>
                <div class="card_font_color_demo_item" 
                        style="background-color: #511b78;" data-value="#511b78"
                        data-dark="dark"
                        data-name="青莲">
                        <span>青莲</span>
                </div>
                <div class="card_font_color_demo_input_area">
                    <div class="card_font_color_demo_item" 
                        style="background-color: #ff9800;" data-value="#ff9800"
                        data-name="自定义">
                    </div>
                    <input class="card_font_color_demo_input">
                </div>
            </div>
        """
        dom_now.empty()
        dom_now.append html
        dom_now.find(".card_font_color_demo_item[data-value=\"#{current_color}\"]").addClass "item_select"
        dom_now.find(".card_font_color_demo_input_area>.card_font_color_demo_item").css
            "backgroundColor":current_color
        dom_now.find(".card_font_color_demo_input_area>.card_font_color_demo_input").val current_color
        return callback
    $("body").on "click",".card>.card_font_color",(evt)->
        dom = $(this)
        dom_now = dom.find(".card_font_color_demo")
        if dom.hasClass("font_color_on")
            dom.removeClass("font_color_on")
        else
            dom.addClass("font_color_on")
            current_color = dom.parents(".dom").find(".sections>.section").css("color")
            console.log current_color
            b=current_color.match(/\((.+?)\)/g)
            b=b[0].replace("(","")
            b=b.replace(")","")
            bl = b.split(",")
            console.log bl,bl.length
            if bl.length == 3
                r = (parseInt(bl[0])<<0).toString(16)
                g = (parseInt(bl[1])<<0).toString(16)
                b = (parseInt(bl[2])<<0).toString(16)
                console.log r,g,b
                console.log r.length,g.length,b.length
                current_color = "#"+r+g+b
                console.log current_color
                if r.length<2
                    r = "0"+r
                if g.length<2
                    g = "0"+g
                if b.length<2
                    b = "0"+b
                console.log r,g,b
                current_color = "#"+r+g+b
                console.log current_color
            else if bl.length == 4
                r = (parseInt(bl[0])<<0).toString(16)
                g = (parseInt(bl[1])<<0).toString(16)
                b = (parseInt(bl[2])<<0).toString(16)
                a = (parseInt(parseFloat(bl[3])*255)<<0).toString(16)
                console.log r.length,g.length,b.length,a.length
                current_color = "#"+r+g+b+a
                console.log current_color
                if r.length<2
                    r = "0"+r
                if g.length<2
                    g = "0"+g
                if b.length<2
                    b = "0"+b
                if a.length<2
                    a = "0"+a
                console.log r,g,b,a
                current_color = "#"+r+g+b+a
                console.log current_color
            load_font_color_html dom_now, current_color ,()->
                console.log "hello color",current_color

    $("body").on "click",".card>.card_font_color>.card_font_color_demo>.card_font_color_demos>.card_font_color_demo_item",(evt)->
        color_now = $(this).attr("data-value")
        dom = $(this)
        card = dom.parents(".card")

        card.find(".card_font_color_demos>.card_font_color_demo_item").removeClass("item_select")
        dom.addClass("item_select")
        card.find(".card_font_color_demo_input_area>.card_font_color_demo_item").css
            "backgroundColor":color_now
        card.find(".card_font_color_demo_input_area>.card_font_color_demo_item").attr "data-value",color_now

        dom_tree = card.attr("data-tree")
        dom_tree_list = dom_tree.split("_")
        dom_owner = dom_tree_list[dom_tree_list.length-1]
        dom_current = dom_tree

        all_section = card.find(".section")
        for item_section in all_section
            $(item_section).css("color",color_now)

        send_data_now = true
        send_data["dom_id"] = card.attr("id")
        html = card.find(".sections").html()
        send_data["text"] = html
        editing_now(card)
        if editing_now_timeout != null
            clearTimeout update_dom_content_timeout
        if $(".network_now").length < 1
            $("body").append """
            <div class="network_now">正在编辑</div>
            """
        update_dom_content_timeout = setTimeout ()->
                update_dom_content(card,send_data)
            ,500
        evt.preventDefault()
        evt.stopPropagation()
        return
    $("body").on "click",".card>.card_font_color>.card_font_color_demo>.card_font_color_demos>.card_font_color_demo_input_area",(evt)->
        evt.preventDefault()
        evt.stopPropagation()
        return
    $("body").on "input propertychange",".card>.card_font_color>.card_font_color_demo>.card_font_color_demos>.card_font_color_demo_input_area>.card_font_color_demo_input",(evt)->
        dom_value = $(this).val()
        $(".card_font_color_demo_input_area>.card_font_color_demo_item").css
            "backgroundColor":dom_value
        $(".card_font_color_demo_input_area>.card_font_color_demo_item").attr "data-value",dom_value
    $("body").on "click",".card>.card_font_color>.card_font_color_demo>.card_font_color_demos>.card_font_color_demo_input_area>.card_font_color_demo_item",(evt)->
        color_now = $(this).attr("data-value")
        dom = $(this)
        card = dom.parents(".card")
        dom_tree = card.attr("data-tree")
        dom_tree_list = dom_tree.split("_")
        dom_owner = dom_tree_list[dom_tree_list.length-1]
        dom_current = dom_tree

        all_section = card.find(".section")
        for item_section in all_section
            $(item_section).css("color",color_now)

        send_data_now = true
        send_data["dom_id"] = card.attr("id")
        html = card.find(".sections").html()
        send_data["text"] = html
        editing_now(card)
        if editing_now_timeout != null
            clearTimeout update_dom_content_timeout
        if $(".network_now").length < 1
            $("body").append """
            <div class="network_now">正在编辑</div>
            """
        update_dom_content_timeout = setTimeout ()->
                update_dom_content(card,send_data)
            ,500
        evt.preventDefault()
        evt.stopPropagation()
        return

    root.block_desc_content_old = $(".block_desc_content").val()
    $("body").on "input propertychange",".block_desc_content",(evt)->
        console.log $(".block_desc_content").val()
        block_desc_content_now = $(".block_desc_content").val()
        if block_desc_content_old==block_desc_content_now
            console.log "same"
            $(".block_desc_content_unsave").addClass("hide")
        else
            console.log "not same"
            $(".block_desc_content_unsave").removeClass("hide")
    $("body").on "click",".block_desc_content_save",(evt)->
        $(this).text "saving"
        desc = $(".block_desc_content").val()
        send_data["desc"] = desc
        dom = $(this)
        editing_now(dom)
        if editing_now_timeout != null
            clearTimeout update_desc_timeout
        update_desc_timeout = setTimeout ()->
                update_desc(send_data)
            ,500

    $("body").on "click",".card>.card_copy",(evt)->
        console.log "card_copy"
        dom = $(this)
        card = dom.parents(".card.dom")
        dom_id = card.attr("id")
        if card.find(".card_more_select").hasClass("card_more_selected")
            current_copy = []
            card_more_selected_items = $(".card_more_selected")
            for card_more_selected_item in card_more_selected_items
                dom_id_item_card = $(card_more_selected_item).parents(".card.dom")
                dom_id_item = dom_id_item_card.attr("id")
                current_copy.push "COPYDOM//#{BLOCK_ID},#{dom_id_item}"
        else
            current_copy = ["COPYDOM//#{BLOCK_ID},#{dom_id}"]
        current_copy = JSON.stringify(current_copy)
        if dom.hasClass("card_copy_select")
            dom.removeClass("card_copy_select")
            localStorage.removeItem("current_copy")
        else
            $(".card_copy").removeClass("card_copy_select")
            dom.addClass("card_copy_select")
            localStorage.setItem("current_copy",current_copy)

    $("body").on "click",".card>.card_more_select",(evt)->
        console.log "card_more_select"
        dom = $(this)
        card = dom.parents(".card.dom")
        if dom.hasClass("card_more_selected")
            dom.removeClass("card_more_selected")
        else
            dom.addClass("card_more_selected")
            $(".terminal_console").val("#{card.attr("id")},#{card.position().left},#{card.position().top}")
        # dom_id = card.attr("id")
        # current_copy = "COPYDOM//#{BLOCK_ID},#{dom_id}"
        # if dom.hasClass("card_copy_select")
        #     dom.removeClass("card_copy_select")
        #     localStorage.removeItem("current_copy")
        # else
        #     $(".card_copy").removeClass("card_copy_select")
        #     dom.addClass("card_copy_select")
        #     localStorage.setItem("current_copy",current_copy)

    latest_current_copy_now_num = "..."
    $(window).on "mousemove touchmove",(evt)->
        current_copy_now = localStorage.getItem("current_copy")
        if current_copy_now != null
            if not $(".add_btn_domcopy").hasClass("has_copy")
                $(".add_btn_domcopy").addClass("has_copy")
            try
                current_copy_now = JSON.parse(current_copy_now)
            catch e
                current_copy_now = [current_copy_now]
                current_copy_save_fix = JSON.stringify(current_copy_now)
                localStorage.setItem("current_copy",current_copy_save_fix)
            current_copy_now_num = current_copy_now.length
            if current_copy_now_num>100
                current_copy_now_num="..."
            if latest_current_copy_now_num != current_copy_now_num
                latest_current_copy_now_num = current_copy_now_num
                $("button.add.add_btn.add_btn_domcopy.has_copy").attr("data-value","#{current_copy_now_num}")
        else
            $(".add_btn_domcopy").removeClass("has_copy")

    get_more_select_base = {}
    root.get_more_select =(nochangedom)->
        if not nochangedom.find(".card_more_select").hasClass("card_more_selected")
            return
        get_more_select_base = {}
        nochangedom_id = nochangedom.attr("id")
        for card_selected in $(".card_more_selected")
            card = $(card_selected).parents(".card.dom")
            card_id = card.attr("id")
            if card_id == nochangedom_id
                continue
            get_more_select_base[card_id]=
                "left":parseInt($(card).css("left"))
                "top":parseInt($(card).css("top"))
    root.set_more_select = (nochangedom,dlt_x,dlt_y)->
        if not nochangedom.find(".card_more_select").hasClass("card_more_selected")
            return
        nochangedom_id = nochangedom.attr("id")
        for card_selected in $(".card_more_selected")
            card = $(card_selected).parents(".card.dom")
            card_id = card.attr("id")
            if card_id == nochangedom_id
                continue
            if get_more_select_base[card_id]==undefined
                continue
            card.css
                "left":"#{get_more_select_base[card_id]["left"]+dlt_x}px"
                "top":"#{get_more_select_base[card_id]["top"]+dlt_y}px"
    root.send_more_select = (nochangedom)->
        console.log nochangedom
        if not nochangedom.find(".card_more_select").hasClass("card_more_selected")
            return
        nochangedom_id = nochangedom.attr("id")
        updates = []
        for card_selected in $(".card_more_selected")
            card = $(card_selected).parents(".card.dom")
            card_id = card.attr("id")
            if card_id == nochangedom_id
                continue
            updates.push
                "dom_id":card_id
                "x":card.position().left
                "y":card.position().top
        uuid_now = uuid2(6,null)
        if updates.length == 0
            return
        uuid2s.push uuid_now
        $.ajax
            url:"/api/page/update_doms"
            data:
                block_id: BLOCK_ID
                updates: JSON.stringify updates
                dom_content: uuid_now
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
            error:(data)->
                console.log data

    block_comment_entities_add_now = false
    $("body").on "click",".block_comment_entities_add",(evt)->
        if block_comment_entities_add_now
            alert "waiting"
            return
        $(".block_comment_entities_add").text "waiting"
        block_comment_entities_add_now = true
        uuid_now = uuid2(6,null)
        uuid2s.push uuid_now
        $.ajax
            url:"/api/page/add_comment"
            data:
                block_id: BLOCK_ID
                dom_content: uuid_now
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
                if data.info == "ok"
                    $(".block_comment_entities_add").text "success"
                    $(".comment_entities").append """
                    <div class="comment_entity_line">
                        <p>block: #{data.comment_entity}</p>
                        <div class="comment_entity_line_tools">
                            <div class="load_card">Load·载入</div>
                            <div class="manager">Manage·管理</div>
                        </div>
                    </div>
                    """
                    setTimeout ()->
                            $(".block_comment_entities_add").text "add"
                            block_comment_entities_add_now = false
                        ,1000
            error:(data)->
                console.log data
    root.symbol_window_select_action = (x1,y1,x2,y2)->
        cards = $(".card.dom")
        select_card_ids = []
        # select_card_ids_has = false
        for card in cards
            x0 = $(card).position().left
            y0 = $(card).position().top
            if x0 >= x1 and x0 <= x2
                if y0 >= y1 and y0 <= y2
                    select_card_ids.push $(card).attr("id")
                    select_card_ids_has = true
        action_ids = "##{select_card_ids.join(",#")}"
        $(".card_more_selected").removeClass("card_more_selected")
        $(action_ids).find(".card_more_select").click()
        root.symbol_window_select_allow = false
        $(".add_btn_area_select").removeClass("show")
        $(".main_area>.test_select").remove()

    $("body").on "keyup",".terminal_console",(evt)->
        if evt.keyCode in [13] and evt.ctrlKey == true
            terminal_console_action = $(this).val()
            terminal_console_action_list = terminal_console_action.split(",")
            terminal_console_action_error = false
            if terminal_console_action_list.length == 3
                dom_id = terminal_console_action_list[0]
                try
                    x = parseInt(terminal_console_action_list[1])
                catch e
                    terminal_console_action_error = true
                try
                    y = parseInt(terminal_console_action_list[2])
                catch e
                    terminal_console_action_error = true 
            if terminal_console_action_error
                alert "error"
                return
            console.log dom_id,x,y
            $("##{dom_id}").animate
                "left":x
                "top":y


            updates = []
            updates.push
                "dom_id":dom_id
                "x":x
                "y":y
            uuid_now = uuid2(6,null)
            if updates.length == 0
                return
            uuid2s.push uuid_now
            $.ajax
                url:"/api/page/update_doms"
                data:
                    block_id: BLOCK_ID
                    updates: JSON.stringify updates
                    dom_content: uuid_now
                dataType: 'json'
                type: 'POST'
                success:(data)->
                    console.log JSON.stringify(data)
                error:(data)->
                    console.log data
    $("body").on "click",".page_agora_btn.join_room",(evt)->
        $.ajax
            url:"/api/page/agora/get_token"
            data:
                aim_id: BLOCK_ID
                device_user: Hs.DEVICE_USER
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
                if data.info == "ok"
                    $("#appid").val(data.appid)
                    $("#token").val(data.token)
                    $("#channel").val(BLOCK_ID);
                    $("#uid").val(Hs.DEVICE_USER);
                    $("#join").click()        
            error:(data)->
                console.log data
        
    $("body").on "click",".page_agora_btn.leave_room",(evt)->
        $("#leave").click()

