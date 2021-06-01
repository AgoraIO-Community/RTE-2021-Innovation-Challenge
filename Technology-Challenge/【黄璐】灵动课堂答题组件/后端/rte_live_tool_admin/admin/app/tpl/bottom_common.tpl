<div class="footer">
    <div class="footer-inner">
        <div class="footer-content">
                    <span class="bigger-120">
                 
              </span>
        </div>
    </div>
</div>
<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
    <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
</a>
</div>
<script>
    function scDialog(content, title, callback) {
        if (arguments.length < 3) {
            callback = function (){};
        }
        $("#dialog-message").html(content);
        var dialog = $("#dialog-message").removeClass('hide').dialog({
            modal: true,
            title: "<div class='widget-header widget-header-small'><h4 class='smaller'>" + title + "</h4></div>",
            title_html: true,
            buttons: [
                {
                    text: "我知道了",
                    "class": "btn btn-primary btn-xs",
                    click: function () {
                        callback();
                        $(this).dialog("close");
                    }
                }
            ]
        });
    }

    function getVal(ele, isMust) {
        var val = $(ele).val();
        if (isMust == 1 && val === "") {
            scDialog("请" + $(ele).attr("placeholder"), '提示');
            $(ele).focus();
            return false;
        }
        return encodeURIComponent(val);
    }
</script>
</body>

</html>
