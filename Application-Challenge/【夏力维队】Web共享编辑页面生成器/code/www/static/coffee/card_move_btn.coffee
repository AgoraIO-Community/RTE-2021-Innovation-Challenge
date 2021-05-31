root = exports ? this
# !!!! Hotpoor root object
root.Hs or= {}
Hs = root.Hs

$ ->
    symbol_window_ismove = false
    symbol_window_ismove_type = null
    symbol_window_dom = null
    symbol_window_dom_left = 0
    symbol_window_dom_top = 0
    symbol_window_dom_width = 0
    symbol_window_dom_height = 0
    symbol_window_dom_x = 0
    symbol_window_dom_y = 0
    symbol_window_dom_scrollTop = 0
    symbol_window_isresize = false
    symbol_window_fix_padding = 0

    symbol_window_isselect = false
    select_area_start_left =0
    select_area_start_top = 0
    select_area_start_x = 0
    select_area_start_y = 0
    select_area_end_x = 0
    select_area_end_y = 0

    $("body").on "mousedown touchstart",".card>.card_move",(evt)->
        if symbol_window_ismove
            return
        dom = $(this)
        if dom.hasClass("card_move_close")
            return
        symbol_window_dom = dom.parents(".card")
        # console.log symbol_window_dom
        symbol_window_dom_left = parseInt(symbol_window_dom.css("left"))
        symbol_window_dom_top = parseInt(symbol_window_dom.css("top"))
        dom.attr("data-position","(#{symbol_window_dom_left},#{symbol_window_dom_top})")
        if evt.type == "touchstart"
            symbol_window_ismove_type = "touch"
            touch = evt.originalEvent.targetTouches[0]
            symbol_window_dom_x = touch.clientX
            symbol_window_dom_y = touch.clientY
        else if evt.type == "mousedown"
            symbol_window_ismove_type = "mouse"
            symbol_window_dom_x = evt.clientX
            symbol_window_dom_y = evt.clientY
        symbol_window_dom_scrollTop = $(window).scrollTop()
        symbol_window_ismove = true
        root.get_more_select(symbol_window_dom)
        evt.preventDefault()
        evt.stopPropagation()

    $("body").on "mousedown touchstart",".card>.resize_btn",(evt)->
        if symbol_window_isresize
            return
        dom = $(this)
        if dom.hasClass("card_move_close")
            return
        symbol_window_dom = dom.parents(".card")
        symbol_window_dom_width = parseInt(symbol_window_dom.width())
        symbol_window_dom_height = parseInt(symbol_window_dom.height())
        if evt.type == "touchstart"
            symbol_window_ismove_type = "touch"
            touch = evt.originalEvent.targetTouches[0]
            symbol_window_dom_x = touch.clientX
            symbol_window_dom_y = touch.clientY
        else if evt.type == "mousedown"
            symbol_window_ismove_type = "mouse"
            symbol_window_dom_x = evt.clientX
            symbol_window_dom_y = evt.clientY
        symbol_window_dom_scrollTop = $(window).scrollTop()
        symbol_window_isresize = true
        evt.preventDefault()
        evt.stopPropagation()

    $(window).on "mousemove touchmove",(evt)->
        if symbol_window_ismove or symbol_window_isresize or symbol_window_isselect
            # console.log evt
            if symbol_window_ismove_type == "touch"
                touch = evt.originalEvent.targetTouches[0]
                _x = touch.clientX
                _y = touch.clientY
                # 判断默认行为是否可以被禁用
                if evt.cancelable
                    # 判断默认行为是否已经被禁用
                    if !evt.defaultPrevented
                        evt.preventDefault()
            else if symbol_window_ismove_type == "mouse"
                _x = evt.clientX
                _y = evt.clientY
                evt.preventDefault()
            _scrollTop = $(window).scrollTop()
            if symbol_window_ismove
                symbol_window_dom_left_update = symbol_window_dom_left + ( _x - symbol_window_dom_x)
                symbol_window_dom_top_update = symbol_window_dom_top + ( _y - symbol_window_dom_y) - (_scrollTop - symbol_window_dom_scrollTop)
                # if symbol_window_dom_left_update < symbol_window_fix_padding
                #    symbol_window_dom_left_update = symbol_window_fix_padding
                if symbol_window_dom_top_update < symbol_window_fix_padding
                   symbol_window_dom_top_update = symbol_window_fix_padding
                symbol_window_dom_left_update = parseInt(symbol_window_dom_left_update)
                symbol_window_dom_top_update = parseInt(symbol_window_dom_top_update)
                symbol_window_dom.css
                    "left":"#{symbol_window_dom_left_update}px"
                    "top":"#{symbol_window_dom_top_update}px"
                symbol_window_dom.find(".card_move").attr("data-position","(#{symbol_window_dom_left_update},#{symbol_window_dom_top_update})")
                root.page_edit_current(symbol_window_dom)
                dlt_x = ( _x - symbol_window_dom_x)
                dlt_y = ( _y - symbol_window_dom_y) - (_scrollTop - symbol_window_dom_scrollTop)
                root.set_more_select(symbol_window_dom,dlt_x,dlt_y)
            else if symbol_window_isresize
                symbol_window_dom_width_update = symbol_window_dom_width + ( _x - symbol_window_dom_x)
                symbol_window_dom_height_update = symbol_window_dom_height + ( _y - symbol_window_dom_y) - (_scrollTop - symbol_window_dom_scrollTop)
                if symbol_window_dom_width_update < 0 #symbol_window_fix_padding
                   symbol_window_dom_width_update = 0 #symbol_window_fix_padding 
                if symbol_window_dom_height_update < 0 #symbol_window_fix_padding
                   symbol_window_dom_height_update = 0 #symbol_window_fix_padding  
                symbol_window_dom.css
                    "width":"#{symbol_window_dom_width_update}px"
                    "height":"#{symbol_window_dom_height_update}px"
                root.page_edit_current(symbol_window_dom)
            else if symbol_window_isselect
                _scrollLeft = $(window).scrollLeft()
                select_area_end_x = _x - select_area_start_left + _scrollLeft
                # select_area_end_y = _y + select_area_start_top - (_scrollTop - symbol_window_dom_scrollTop)
                select_area_end_y = _y - select_area_start_top + _scrollTop
                $(".test_select").css
                    "width":select_area_end_x - select_area_start_x
                    "height":select_area_end_y - select_area_start_y - 22

    $(window).on "mousedown touchstart",(evt)->
        if not symbol_window_ismove and not symbol_window_isresize and not symbol_window_isselect and root.symbol_window_select_allow
            if evt.type == "touchstart"
                symbol_window_ismove_type = "touch"
                touch = evt.originalEvent.targetTouches[0]
                select_area_start_x = touch.clientX
                select_area_start_y = touch.clientY
            else if evt.type == "mousedown"
                symbol_window_ismove_type = "mouse"
                select_area_start_x = evt.clientX
                select_area_start_y = evt.clientY
            select_area_start_left = $(".main_area").position().left
            select_area_start_top = $(".main_area").position().top
            symbol_window_isselect = true
            symbol_window_dom_scrollTop = $(window).scrollTop()
            symbol_window_dom_scrollLeft = $(window).scrollLeft()
            select_area_start_x = select_area_start_x - select_area_start_left + symbol_window_dom_scrollLeft
            select_area_start_y = select_area_start_y - select_area_start_top - 22 + symbol_window_dom_scrollTop
            console.log select_area_start_x,select_area_start_y
            
            evt.preventDefault()
            evt.stopPropagation()
            $(".main_area>.test_select").remove()
            $(".main_area").append """
            <div class="test_select" style="
                position:absolute;
                left:#{select_area_start_x}px;
                top:#{select_area_start_y}px;
                z-index:999999999999;
                border:1px solid #ff9800;
                "></div>
            """

    $(window).on "mouseup touchend",(evt)->
        if symbol_window_ismove
            symbol_window_ismove = false
            if root.page_edit?
                root.page_edit_current symbol_window_dom,true,(symbol_window_dom)->
                    root.send_more_select(symbol_window_dom)
            symbol_window_dom = null
        else if symbol_window_isresize
            symbol_window_isresize = false
            if root.page_edit?
                root.page_edit_current(symbol_window_dom,true)
            symbol_window_dom = null
        else if symbol_window_isselect
            x1="#{select_area_start_x}"
            y1="#{select_area_start_y}"
            x2="#{select_area_end_x}"
            y2="#{select_area_end_y}"
            console.log "(#{x1},#{y1}):(#{x2},#{y2})"
            select_area_start_x = select_area_start_x
            select_area_start_y = select_area_start_y
            select_area_end_x = select_area_end_x
            select_area_end_y = select_area_end_y
            console.log "(#{select_area_start_x},#{select_area_start_y}):(#{select_area_end_x},#{select_area_end_y})"
            symbol_window_isselect = false
            symbol_window_select_action(select_area_start_x,select_area_start_y,select_area_end_x,select_area_end_y)
        # else
        #     if root.page_edit?
        #         root.page_edit_current(symbol_window_dom)

            # resize_timeout = setTimeout ()->
            #         symbol_window_move_auto()
            #         load_tv_demo()
            #     ,500










