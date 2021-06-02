<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
<link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/bootstrap.min.css" />
<link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/font-awesome.min.css" />
<link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/ace.min.css" id="main-ace-style" />
<link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/ace-skins.min.css" />
<link rel="stylesheet" href="<?php echo STATICURL; ?>assets/css/ace-rtl.min.css" />
<link rel="shortcut icon" href="./favicon.ico" type="image/x-icon">
<script src="<?php echo STATICURL; ?>assets/js/ace-extra.min.js"></script>
</head>

<body class="no-skin">
<div id="navbar" class="navbar navbar-default">
    <div class="navbar-container" id="navbar-container">
        <button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler">
            <span class="sr-only"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <div class="navbar-header pull-left">
            <a href="index.html" class="navbar-brand">
                <?php echo WEB_ADMIN_NAME; ?>
            </a>
        </div>
        <div class="navbar-buttons navbar-header pull-right" role="navigation">
            <ul class="nav ace-nav">
                <li class="light-blue">
                    <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                        <img class="nav-user-photo" src="<?php echo STATICURL; ?>assets/avatars/user_striper_black.png"
                             alt="Jason's Photo" />
                        <span class="user-info">
				欢迎您<br />
				<?php echo $_SESSION[SYS_SIGN . 'adminNickname'] ?>
			</span>

                        <i class="ace-icon fa fa-caret-down"></i>
                    </a>
                    <ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                        <li>
                            <a href="?c=admin&m=changePassword">
                                <i class="ace-icon fa fa-edit"></i> 修改密码
                            </a>
                        </li>
                        <li>
                            <a href="?c=login&m=loginout">
                                <i class="ace-icon fa fa-power-off"></i> 登出
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>
<div class="main-container" id="main-container">
    <div id="sidebar" class="sidebar responsive">
        <ul class="nav nav-list">
            <li class="" id="adminindexLi">
                <a href="?c=index">
                    <i class="menu-icon fa fa-tachometer"></i>
                    <span class="menu-text">后台管理</span>
                </a>
                <b class="arrow"></b>
            </li>
            <li class="" id="roomLi">
                <a href="#" class="dropdown-toggle">
                    <i class="menu-icon fa fas fa-tv"></i>
                    <span class="menu-text">直播间管理</span>
                    <b class="arrow fa fa-angle-down"></b>
                </a>
                <b class="arrow"></b>
                <ul class="submenu">
                    <li class="" id="room__alllistLi">
                        <a href="?c=room&m=alllist">
                            <i class="menu-icon fa fa-caret-right"></i>直播间列表
                        </a>
                        <b class="arrow"></b>
                    </li>
                    <li class="" id="room__addLi">
                        <a href="?c=room&m=add">
                            <i class="menu-icon fa fa-caret-right"></i>添加直播间
                        </a>
                        <b class="arrow"></b>
                    </li>
                </ul>
            </li>
            <li class="" id="questionLi">
                <a href="#" class="dropdown-toggle">
                    <i class="menu-icon fa fas fa-book"></i>
                    <span class="menu-text">题目管理</span>
                    <b class="arrow fa fa-angle-down"></b>
                </a>
                <b class="arrow"></b>
                <ul class="submenu">
                    <li class="" id="question__alllistLi">
                        <a href="?c=question&m=alllist">
                            <i class="menu-icon fa fa-caret-right"></i>题目列表
                        </a>
                        <b class="arrow"></b>
                    </li>
                    <li class="" id="question__addLi">
                        <a href="?c=question&m=add">
                            <i class="menu-icon fa fa-caret-right"></i>添加题目
                        </a>
                        <b class="arrow"></b>
                    </li>
                </ul>
            </li>

        </ul>
        <div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
            <i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left"
               data-icon2="ace-icon fa fa-angle-double-right"></i>
        </div>
    </div>