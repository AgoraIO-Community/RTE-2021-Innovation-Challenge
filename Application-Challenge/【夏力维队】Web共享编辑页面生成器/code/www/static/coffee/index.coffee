root = exports ? this
# !!!! Hotpoor root object
root.Hs or= {}
Hs = root.Hs

$ ->
    $(window).on "wheel touchmove",(evt)->
        console.log evt
        comeinpages = $(".comeinpage")
        for comeinpage in comeinpages
            do (comeinpage)->
                points = comeinpage.getBoundingClientRect()
                if points.top>=0 or points.bottom>=0
                    nocomeins = $(comeinpage).find(".nocomein")
                    $(comeinpage).removeClass("comeinpage")
                    for nocomein in nocomeins
                        do (nocomein)->
                            dom = $(nocomein)
                            dom.removeClass("nocomein")
                            dom.addClass("comein")
    $("body").on "click",".wechat_qrcode_show_btn",(evt)->
        if $(".wechat_qrcode").hasClass("hide")
            $(".wechat_qrcode").removeClass("hide")
        else
            $(".wechat_qrcode").addClass("hide")