root = exports ? this
# !!!! Hotpoor root object
root.Hs or= {}
Hs = root.Hs

$ ->
    $("body").on "click",".login",(evt)->
        email = $("input.email").val()
        password = $("input.password").val()
        $.ajax
            url:"/api/login"
            data:
                email:email
                password:password
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
                if data.info == "ok"
                    window.location.href = data.redirect
                else
                    alert data.about
            error:(data)->
                console.log data

    $("body").on "click",".register",(evt)->
        email = $("input.email").val()
        password0 = $("input.password0").val()
        password1 = $("input.password1").val()
        $.ajax
            url:"/api/register"
            data:
                email:email
                password0:password0
                password1:password1
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
            error:(data)->
                console.log data

    $("body").on "click",".reset_password",(evt)->
        email = $("input.email")
        password0 = $("input.password0").val()
        password1 = $("input.password1").val()
        $.ajax
            url:"/api/reset_password"
            data:
                email:email
                password0:password0
                password1:password1
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
            error:(data)->
                console.log data

    $("body").on "click",".get_vcode",(evt)->
        email = $("input.email").val()
        vcode = $("input.vcode").val()
        $.ajax
            url:"/api/get_vcode"
            data:
                email:email
                vcode:vcode
            dataType: 'json'
            type: 'POST'
            success:(data)->
                console.log JSON.stringify(data)
            error:(data)->
                console.log data
