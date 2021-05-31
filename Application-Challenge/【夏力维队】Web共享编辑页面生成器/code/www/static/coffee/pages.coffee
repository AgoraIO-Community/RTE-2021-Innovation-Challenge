root = exports ? this
# !!!! Hotpoor root object
root.Hs or= {}
Hs = root.Hs

$ ->
    console.log "pages"
    $("body").on "click",".add_new_page",(evt)->
        $.ajax
            url:"/api/page/add"
            data:
                title:$(".add_new_page_title").val()
                desc:$(".add_new_page_desc").val()
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
                load_pages_list()
                if data.info == "ok"
                    new_blank = window.open('_blank')
                    if new_blank == null
                        window.location.href = "/home/page/edit/#{data.block_id}"
                    else
                        new_blank.location = "/home/page/edit/#{data.block_id}"
            error:(data)->
                console.log data
    load_pages_list = ()->
        $(".pages_top_list").empty()
        $(".pages_list").empty()
        $.ajax
            url:"/api/pages/list"
            data:
                t:(new Date()).getTime()
            dataType: 'json'
            type: 'GET'
            success:(data)->
                console.log data
                if data.info == "ok"
                    localStorage.setItem("pages",JSON.stringify(data.result))
                    localStorage.setItem("pages_top_ids",JSON.stringify(data.pages_top_ids))
                    for item in data.result
                        fork_from = item["fork_from"]
                        fork_from_html = ""
                        if fork_from != null
                            fork_from_html = """
                                <p class="fork_from">forked from 
                                    <a target="_blank" href="/home/page/#{fork_from}">
                                        <span style="color:#0366d6;">#{fork_from}</span>
                                    </a>
                                </p>
                            """
                        $(".pages_list").append """
                        <div class="page_item" data-block="#{item["block_id"]}">
                            <p class="block">block: #{item["block_id"]}</p>
                            <p class="title">#{item["title"]}</p>
                            #{fork_from_html}
                            <p class="desc">#{item["desc"]}</p>
                            <div class="link_blank">
                                <a target="_blank" href="/home/page/edit/#{item["block_id"]}"><span class="page_edit">Edit·编辑</span></a>
                                <a target="_blank" href="/home/page/#{item["block_id"]}"><span class="page">Page View·预览</span></a>
                                <span class="add_to_top">Add to Top·置顶</span>
                                <span class="remove_to_trash">Remove·移除</span>
                            </div>
                        </div>
                        """
                    if data.pages_top_ids.length > 0
                        $(".pages_top_list").removeClass("hide")
                    for item_top in data.pages_top_ids
                        $(".pages_top_list").append $(".page_item[data-block=#{item_top}]")
                        $(".page_item[data-block=#{item_top}]").find(".add_to_top").after """
                            <span class="remove_from_top">Remove from Top·取消置顶</span>
                        """
                        $(".page_item[data-block=#{item_top}]").find(".add_to_top").remove()

            error:(data)->
                console.log data
    load_pages_list()

    $("body").on "click",".add_to_top",(evt)->
        dom = $(this)
        block_id = dom.parents(".page_item").attr("data-block")
        $.ajax
            url:"/api/page/add_to_top"
            data:
                aim_id:block_id
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
                if data.info == "ok"
                    $(".pages_top_list").removeClass("hide")
                    $(".pages_top_list").prepend $(".page_item[data-block=#{block_id}]")
                    $(".page_item[data-block=#{block_id}]").find(".add_to_top").after """
                        <span class="remove_from_top">Remove from Top·取消置顶</span>
                    """
                    $(".page_item[data-block=#{block_id}]").find(".add_to_top").remove()
            error:(data)->
                console.log data
    $("body").on "click",".remove_from_top",(evt)->
        dom = $(this)
        block_id = dom.parents(".page_item").attr("data-block")
        $.ajax
            url:"/api/page/remove_from_top"
            data:
                aim_id:block_id
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
                if data.info == "ok"
                    $(".pages_list").prepend $(".page_item[data-block=#{block_id}]")
                    $(".page_item[data-block=#{block_id}]").find(".remove_from_top").before """
                        <span class="add_to_top">Add to Top·置顶</span>
                    """
                    $(".page_item[data-block=#{block_id}]").find(".remove_from_top").remove()
                    if $(".pages_top_list>.page_item").length < 1
                        $(".pages_top_list").addClass("hide")
            error:(data)->
                console.log data

    $("body").on "click",".pages_add_new_page_btn",(evt)->
        if $(".create_page_area").hasClass("hide")
            $(".create_page_area").removeClass("hide")
        else
            $(".create_page_area").addClass("hide")

    pages_search_timeout = null
    $("body").on "input propertychange",".pages_search_input",(evt)->
        search = $(this).val()
        clearTimeout pages_search_timeout
        pages_search_timeout = setTimeout ()->
                pages_search(search)
            ,1000
    search_in_items=(from,items)->
        from = from.toLocaleLowerCase()
        for item in items
            if from.indexOf(item)>-1
                return true
        return false
    pages_search=(search)->
        console.log "search: ",search
        search = search.toLocaleLowerCase()
        search_items = search.split(" ")
        if search == ""
            $(".no_has_page").remove()
            $(".page_item").removeClass("hide")
            return
        $(".page_item").addClass("hide")
        pages = JSON.parse(localStorage.getItem("pages"))
        has_page = false
        for page in pages
            _block_id = page["block_id"]
            _title = page["title"]
            _desc = page["desc"]
            _fork_from = page["fork_from"]
            if _fork_from == null
                _fork_from = ""
            if search_in_items(_title,search_items)
                $(".page_item[data-block=#{_block_id}]").removeClass("hide")
                has_page = true
            if search_in_items(_desc,search_items)
                $(".page_item[data-block=#{_block_id}]").removeClass("hide")
                has_page = true
            if search_in_items(_block_id,search_items)
                $(".page_item[data-block=#{_block_id}]").removeClass("hide")
                has_page = true
            if search_in_items(_fork_from,search_items)
                $(".page_item[data-block=#{_block_id}]").removeClass("hide")
                has_page = true
        
        if not has_page
            $(".no_has_page").remove()
            $(".pages_tools_area").after """
            <div class="no_has_page">0 Result here.<span class="remove_search_btn">remove search</span></div>
            """
        else
            $(".no_has_page").remove()

    $("body").on "click",".remove_search_btn",(evt)->
        $(".no_has_page").remove()
        $(".page_item").removeClass("hide")
        $(".pages_search_input").val("")
    $("body").on "click",".remove_to_trash",(evt)->
        dom = $(this)
        block_id = dom.parents(".page_item").attr("data-block")
        $.ajax
            url:"/api/page/remove_to_trash"
            data:
                aim_id:block_id
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
                if data.info == "ok"
                    $(".page_item[data-block=#{block_id}]").animate
                            "opacity":0
                        ,1000 ,()->
                            $(".page_item[data-block=#{block_id}]").remove()
                else if data.info == "error"
                    alert data.about
            error:(data)->
                console.log data


