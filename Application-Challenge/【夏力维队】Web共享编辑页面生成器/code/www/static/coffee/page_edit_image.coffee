root = exports ? this
root.Hs or= {}
Hs = root.Hs

canvasDataURL = (path, obj, quality=0.7,callback)->
    img = new Image();
    img.src = path;
    img.onload = ()->
        that = this
        #默认按比例压缩
        w = that.width
        h = that.height
        scale = w / h
        w = obj.width || w
        h = obj.height || (w / scale)
        # quality = 0.7;  # 默认图片质量为0.7
        # 生成canvas
        canvas = document.createElement('canvas')
        ctx = canvas.getContext('2d')
        # 创建属性节点
        anw = document.createAttribute("width")
        anw.nodeValue = w
        anh = document.createAttribute("height")
        anh.nodeValue = h
        canvas.setAttributeNode(anw)
        canvas.setAttributeNode(anh)
        ctx.drawImage(that, 0, 0, w, h)
        #图像质量
        if (obj.quality && obj.quality <= 1 && obj.quality > 0)
            quality = obj.quality
        # quality值越小，所绘制出的图像越模糊
        base64 = canvas.toDataURL('image/png', quality)
        #回调函数返回base64的值
        callback(base64)
convertBase64UrlToBlob = (urlData)->
    arr = urlData.split(',')
    mime = arr[0].match(/:(.*?);/)[1]
    bstr = atob(arr[1])
    n = bstr.length
    u8arr = new Uint8Array(n)
    while n--
        u8arr[n] = bstr.charCodeAt(n)
    return new Blob([u8arr], {type:mime})


$ ->
    CDN_PREFIX = "http://fm-of-test0.xialiwei.com"

    file_lists = []
    current_dom_img = null
    current_dom_sections = null

    if window.File and window.FileList and window.FileReader and window.Blob and window.Worker
        handleFileSelect = (evt) ->
            img_add = $(this).attr("id")=="img_add_upload" ? true : false
            _room_id = BLOCK_ID
            content_type = "HQWEBIMG//"
            evt.stopPropagation()
            evt.preventDefault()

            files = null
            if evt.target.files
                files = evt.target.files
            else
                files = evt.dataTransfer.files

            file_lists.push(files)
            file_index = 0
            startingByte = 0
            endingByte = 0

            console.log "image coffee"
            console.log BLOCK_ID

            uploadFile = (file) ->
                if file == undefined
                    return
                if file.type == undefined
                    return
                type = if file.type then file.type else 'n/a'
                console.log type
                if not (type in ["image/jpeg","image/jpg","image/webp","image/png","image/gif","image/bmp"])
                    file_index += 1
                    alert "文件格式不支持"
                    return
                reader = new FileReader()
                tempfile = null
                startingByte = 0
                console.log("正在上传第一张图片")

                loading_flag = uuid2(6,null)
                current_dom_img.after """
                    <div id="loading_#{loading_flag}" class="loading_flag" contenteditable="false"></div>
                """
                $("#loading_#{loading_flag}").animate
                        "width":"25%"
                    ,1000

                xhrProvider = () ->
                    xhr = jQuery.ajaxSettings.xhr()
                    if xhr.upload
                        xhr.upload.addEventListener('progress', updateProgress, false)
                    return xhr

                updateProgress = (evt) ->
                    #console.log startingByte, file.size, evt.loaded, evt.total
                    console.log("Uploading file #{file_index+1} of #{files.length} at #{(startingByte + (endingByte-startingByte)*evt.loaded/evt.total)/file.size*100}%")

                uploadNextFile = () ->
                    console.log("正在要上传下一张图片")
                    file_index += 1
                    if file_index < files.length
                        uploadFile(files[file_index])
                        console.log("===")
                        console.log(file_index)
                        console.log("===|||")
                    else
                        file_lists.shift()
                        if file_lists.length > 1
                            file_index = 0
                            files = file_lists[0]
                            uploadFile(files[file_index])
                            console.log("===+++")
                            console.log(file_index)
                            console.log("===|||")
                        else
                            console.log("===>>>")
                            obj = document.getElementById('img_add_upload')
                            obj.outerHTML = obj.outerHTML

                reader.onload = (evt) ->
                    content = evt.target.result.slice evt.target.result.indexOf("base64,")+7
                    bin = atob content

                    re_this = this
                    re = re_this.result
                    w =
                        quality: 0.3

                    worker = new Worker "/static/js/md5.js"
                    worker.onmessage = (event) ->
                        md5 = event.data
                        # console.log "md5", md5, file

                        # Qiniu_UploadUrl_https = "https://up.qbox.me"
                        # if window.location.protocol == "https:"
                        #     Qiniu_UploadUrl = Qiniu_UploadUrl_https
                        # else
                        #     Qiniu_UploadUrl = "http://up.qiniu.com"
                        Qiniu_UploadUrl_https = "https://up-z1.qiniup.com"
                        if window.location.protocol == "https:"
                            Qiniu_UploadUrl = Qiniu_UploadUrl_https
                        else
                            Qiniu_UploadUrl = "http://up-z1.qiniup.com"

                        worker_aim_url = "/api/image/check"
                        $.post worker_aim_url,
                            "block_id": _room_id
                            "md5": md5
                        , (data) ->
                            if files.length == 1
                                console.log("正在上传1张图片")
                                if data["exists"]

                                    result_url = "#{CDN_PREFIX}/#{BLOCK_ID}_#{md5}?imageView2"
                                    current_dom_img.attr("src",result_url)
                                    $("#loading_#{loading_flag}").animate
                                            "width":"100%"
                                        ,500 ,()->
                                            $("#loading_#{loading_flag}").remove()
                                            update_dom_content_img(current_dom_sections)
                                    obj = document.getElementById('img_add_upload')
                                    obj.outerHTML = obj.outerHTML
                                    return

                                    # content = content_type+md5
                                    # if Hs.image_add_type?
                                    #     if Hs.image_add_type == "image_add_list"
                                    #         Hs.image_add_list.push content
                                    #         Hs.image_add_list_action(content)
                                    #         obj = document.getElementById('img_add_upload')
                                    #         obj.outerHTML = obj.outerHTML
                                    #         return
                                    # return $.ajax
                                    #     url: '/api/comment/submit',
                                    #     type: 'POST',
                                    #     dataType: 'json',
                                    #     data: 
                                    #         "app": WX_APP,
                                    #         "aim_id": _room_id,
                                    #         "content": content
                                    #     success: (data) ->
                                    #         console.log(data)
                                    #         obj = document.getElementById('img_add_upload')
                                    #         obj.outerHTML = obj.outerHTML
                                    #         if uploadAction?
                                    #             if uploadAction == "headimgurl"
                                    #                 img_url = content.replace("HQWEBIMG//","")
                                    #                 _img_url = "#{CDN_PREFIX}/#{USER_ID}_#{img_url}?imageView2/1/w/200/h/200"
                                    #                 $.ajax
                                    #                     url: '/api/user/set_headimgurl',
                                    #                     type: 'POST',
                                    #                     dataType: 'json',
                                    #                     data: 
                                    #                         "app": WX_APP,
                                    #                         "aim_id": USER_ID,
                                    #                         "headimgurl": _img_url
                                    #                     success: (data) ->
                                    #                         console.log data
                                    #                         if data.info == "success"
                                    #                             $("#headimgurl").attr("src",_img_url)
                                    #                     error:(error)->
                                    #                         console.log error
                                    #     error: (error) ->
                                    #         console.log(error)
                            else
                                if file_index+1 == files.length
                                    console.log("正在上传最后一张图片")
                                    if data["exists"]
                                        result_url = "#{CDN_PREFIX}/#{BLOCK_ID}_#{md5}?imageView2"
                                        current_dom_sections.append """
                                        <div class="section"><img src="#{result_url}"></div>
                                        """
                                        $("#loading_#{loading_flag}").animate
                                                "width":"100%"
                                            ,500 ,()->
                                                $("#loading_#{loading_flag}").remove()
                                                update_dom_content_img(current_dom_sections)
                                        obj = document.getElementById('img_add_upload')
                                        obj.outerHTML = obj.outerHTML
                                        return
                                        # content = content_type+md5
                                        # if Hs.image_add_type?
                                        #     if Hs.image_add_type == "image_add_list"
                                        #         Hs.image_add_list.push content
                                        #         Hs.image_add_list_action(content)
                                        #         obj = document.getElementById('img_add_upload')
                                        #         obj.outerHTML = obj.outerHTML
                                        #         return
                                        # return $.ajax
                                        #     url: '/api/comment/submit',
                                        #     type: 'POST',
                                        #     dataType: 'json',
                                        #     data: 
                                        #         "app": WX_APP,
                                        #         "aim_id": _room_id,
                                        #         "content": content
                                        #     success: (data) ->
                                        #         console.log(data)
                                        #         obj = document.getElementById('img_add_upload')
                                        #         obj.outerHTML = obj.outerHTML
                                        #     error: (error) ->
                                        #         console.log(error)
                                else
                                    console.log("正在上传"+(file_index+1)+"/"+files.length+"张图片")
                                    if data["exists"]
                                        result_url = "#{CDN_PREFIX}/#{BLOCK_ID}_#{md5}?imageView2"
                                        if file_index == 0
                                            current_dom_img.attr("src",result_url)
                                        else
                                            current_dom_sections.append """
                                            <div class="section"><img src="#{result_url}"></div>
                                            """
                                        $("#loading_#{loading_flag}").animate
                                                "width":"100%"
                                            ,500 ,()->
                                                $("#loading_#{loading_flag}").remove()
                                                update_dom_content_img(current_dom_sections)
                                        # content = content_type+md5
                                        # if Hs.image_add_type?
                                        #     if Hs.image_add_type == "image_add_list"
                                        #         Hs.image_add_list.push content
                                        #         Hs.image_add_list_action(content)
                                        #         uploadNextFile()
                                        #         return
                                        # return $.ajax
                                        #     url: '/api/comment/submit',
                                        #     type: 'POST',
                                        #     dataType: 'json',
                                        #     data: 
                                        #         "app": WX_APP,
                                        #         "aim_id": _room_id,
                                        #         "content": content
                                        #     success: (data) ->
                                        #         console.log(data)
                                        #     error: (error) ->
                                        #         console.log(error)
                                        #     complete:()->
                                        #         uploadNextFile()
                                        return uploadNextFile()
                            upload_token = data["token"]

                            Qiniu_upload = (f, token, key) ->
                                xhr = new XMLHttpRequest()
                                xhr.open('POST', Qiniu_UploadUrl, true)
                                formData = new FormData()
                                if (key != null and key != undefined)
                                    formData.append('key', key)
                                formData.append('token', token)
                                formData.append('file', f)
                                xhr.upload.addEventListener "progress", (evt) ->
                                    if (evt.lengthComputable)
                                        nowDate = new Date().getTime()
                                        taking = nowDate - startDate
                                        x = (evt.loaded) / 1024
                                        y = taking / 1000
                                        uploadSpeed = (x / y)
                                        if (uploadSpeed > 1024)
                                            formatSpeed = (uploadSpeed / 1024).toFixed(2) + "Mb\/s"
                                        else
                                            formatSpeed = uploadSpeed.toFixed(2) + "Kb\/s"

                                        percentComplete = Math.round(evt.loaded * 100 / evt.total)
                                        console.log(percentComplete, ",", formatSpeed)
                                        $("#loading_#{loading_flag}").css
                                            "width":"#{25+percentComplete*0.75}%"
                                        
                                            
                                , false

                                xhr.onreadystatechange = (response) ->
                                    if (xhr.readyState == 4 and xhr.status == 200 and xhr.responseText != "")
                                        blkRet = JSON.parse(xhr.responseText)
                                        $.post "/api/image/add",
                                                "aim_id": _room_id
                                                "md5": md5
                                            , () ->
                                                result_url = "#{CDN_PREFIX}/#{BLOCK_ID}_#{md5}?imageView2"
                                                current_dom_img.attr("src",result_url)
                                                $("#loading_#{loading_flag}").animate
                                                        "width":"100%"
                                                    ,500 ,()->
                                                        $("#loading_#{loading_flag}").remove()
                                                        update_dom_content_img(current_dom_sections)
                                                # content = "HQWEBIMG//"+md5
                                                # if Hs.image_add_type?
                                                #     if Hs.image_add_type == "image_add_list"
                                                #         Hs.image_add_list.push content
                                                #         Hs.image_add_list_action(content)
                                                #         uploadNextFile()
                                                #         return
                                                # else
                                                #     $.ajax
                                                #         url: '/api/comment/submit',
                                                #         type: 'POST',
                                                #         dataType: 'json',
                                                #         data: 
                                                #             "app": WX_APP,
                                                #             "aim_id": _room_id,
                                                #             "content": content
                                                #         success: (data) ->
                                                #             console.log(data)
                                                #             if uploadAction?
                                                #                 if uploadAction == "headimgurl"
                                                #                     img_url = content.replace("HQWEBIMG//","")
                                                #                     _img_url = "#{CDN_PREFIX}/#{USER_ID}_#{img_url}?imageView2/1/w/200/h/200"
                                                #                     $.ajax
                                                #                         url: '/api/user/set_headimgurl',
                                                #                         type: 'POST',
                                                #                         dataType: 'json',
                                                #                         data: 
                                                #                             "app": WX_APP,
                                                #                             "aim_id": USER_ID,
                                                #                             "headimgurl": _img_url
                                                #                         success: (data) ->
                                                #                             console.log data
                                                #                             if data.info == "success"
                                                #                                 $("#headimgurl").attr("src",_img_url)
                                                #                         error:(error)->
                                                #                             console.log error
                                                #         error: (error) ->
                                                #             console.log(error)

                                                uploadNextFile()

                                startDate = new Date().getTime()
                                xhr.send formData


                            size=file.size/1024
                            if size>1025 and (file.type not in ["image/gif"])
                                canvasDataURL re, w, 0.7,(base64Codes)->
                                    bl = convertBase64UrlToBlob(base64Codes);
                                    file = bl
                                    console.log "==="
                                    Qiniu_upload(file, upload_token, _room_id+"_"+md5);
                            else
                                Qiniu_upload(file, upload_token, _room_id+"_"+md5)
                            # Qiniu_upload(file, upload_token, _room_id+"_"+md5)

                    worker.postMessage bin

                reader.readAsDataURL file
            if file_lists.length >= 1
                uploadFile files[file_index]
        # $("#img_add_upload").on "change", handleFileSelect
        $("body").on "change", "#img_add_upload", handleFileSelect
        $("body").on "click",".dom_img>.sections>.section>img",(evt)->
            dom_img = $(this)
            dom_sections = dom_img.parents(".sections")
            # dom_img.attr("data-uuid2",uuid2(6,null))
            current_dom_img = dom_img
            current_dom_sections = dom_sections
            $("#img_add_upload").click()










