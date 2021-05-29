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

    $("body").on "mousedown touchstart",".card>.card_move>",(evt)->
        if symbol_window_ismove
            return
        dom = $(this)
        if dom.hasClass("card_move_close")
            return
        if root.page_edit?
            symbol_window_dom = dom.parents(".card_move")
        else
            symbol_window_dom = dom
        symbol_window_dom_left = parseInt(symbol_window_dom.css("left"))
        symbol_window_dom_top = parseInt(symbol_window_dom.css("top"))
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
        evt.preventDefault()
        evt.stopPropagation()

    $("body").on "mousedown touchstart",".card>.card_move>.resize_btn",(evt)->
        if symbol_window_isresize
            return
        dom = $(this)
        if dom.hasClass("card_move_close")
            return
        symbol_window_dom = dom
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
        if symbol_window_ismove or symbol_window_isresize
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
                symbol_window_dom_top_update = symbol_window_dom_top + ( _y - symbol_window_dom_y)-(_scrollTop - symbol_window_dom_scrollTop)
                if symbol_window_dom_left_update < symbol_window_fix_padding
                   symbol_window_dom_left_update = symbol_window_fix_padding 
                if symbol_window_dom_top_update < symbol_window_fix_padding
                   symbol_window_dom_top_update = symbol_window_fix_padding  
                symbol_window_dom.css
                    "left":"#{symbol_window_dom_left_update}px"
                    "top":"#{symbol_window_dom_top_update}px"
            else if symbol_window_isresize
                symbol_window_dom_width_update = symbol_window_dom_width + ( _x - symbol_window_dom_x)
                symbol_window_dom_height_update = symbol_window_dom_height + ( _y - symbol_window_dom_y)-(_scrollTop - symbol_window_dom_scrollTop)
                if symbol_window_dom_width_update < 0 #symbol_window_fix_padding
                   symbol_window_dom_width_update = 0 #symbol_window_fix_padding 
                if symbol_window_dom_height_update < 0 #symbol_window_fix_padding
                   symbol_window_dom_height_update = 0 #symbol_window_fix_padding  
                symbol_window_dom.css
                    "width":"#{symbol_window_dom_width_update}px"
                    "height":"#{symbol_window_dom_height_update}px"


    $(window).on "mouseup touchend",(evt)->
        if symbol_window_ismove
            symbol_window_ismove = false
            if root.page_edit?
                root.page_edit_current(symbol_window_dom)
            symbol_window_dom = null
        else if symbol_window_isresize
            symbol_window_isresize = false
            symbol_window_dom = null
            # resize_timeout = setTimeout ()->
            #         symbol_window_move_auto()
            #         load_tv_demo()
            #     ,500











