<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="icon" href="<?php echo STATICURL?>img/todoen.ico" type="image/x-icon">
    <title>错误 - <?php echo WEBNAME;?></title>
    <style>
        *{ margin:0; padding: 0;}
        h1{text-align: center;
            font-size: 103px;
            color: #a09e9e;
            margin-bottom: 2px;
            margin-top: 10%;}
        .title{text-align: center;
            font-size: 25px;
            color: #a09e9e;}
        #url_box_show_btn{color: #ccc;
            text-align: center;
            width: 90%;
            margin: 0 auto;
            margin-top: 5%;
            word-break: break-all; cursor: pointer}
        #url_box{color: #ccc;
            text-align: center;
            width: 90%;
            margin: 0 auto;
            margin-top: 5%;
            word-break: break-all; display: none;}
        .copyright{color: #ccc;
            text-align: center;
            width: 90%;
            word-break: break-all;
            position: fixed;
            bottom: 10%;
            font-size: 12px;
            left: 5%;}
    </style>
</head>
<body>
<h1>ERROR</h1>
<p class="title" style=''><?php echo $err;?></p>
<p id="url_box_show_btn" onclick="show_now_url()">点击查看当前页面地址</p>
<p id="url_box"><?php echo "https://".$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI'];?></p>
<p class="copyright"><?php echo COPYRIGHT;?></p>
<script>
    function show_now_url(){
        document.getElementById("url_box_show_btn").style.display='none';
        document.getElementById("url_box").style.display='block';
    }
</script>
</body>
</html>


