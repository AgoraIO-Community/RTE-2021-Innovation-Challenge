<?php

/*
 * This file is part of Flarum.
 *
 * For detailed copyright and license information, please view the
 * LICENSE file that was distributed with this source code.
 */

use Flarum\Extend;
use Flarum\Frontend\Document;

// 自定义移动端页脚的
$customStyle = <<<EOF
<style>
.mobile-app-icon-bar button {
    padding: 0.75rem !important;
    cursor: pointer;
}

.mobile-app-icon-bar button svg,
.mobile-app-icon-bar button i,
.mobile-app-icon-bar button img {}

@media(min-width: 800px) {
    .mobile-app-icon-bar {
        display: none !important;
    }
}

@media(max-width: 500px) {
    .mobile-app-icon-bar {
        display: -webkit-flex;
        display: -ms-flexbox;
        display: flex;
        -webkit-align-items: center;
        -ms-flex-align: center;
        align-items: center;
        -webkit-justify-content: space-around;
        -ms-flex-pack: distribute;
        justify-content: space-around;
        background: none;
        position: sticky;
        bottom: 0;
        width: 100%;
        height: 48px;
        z-index: 2;
        /*border: 1px solid #fff !important;*/
    }
    /* addthis 上移 */
    .at-expanding-share-button[data-position=bottom-right] {
        bottom: 58px !important;
    }
    .scroll-up {
        bottom: 108px;
    }
}


.buttonstyle.active {
    color: #fff !important
}

.buttonstyle {
    width: 100%;
    height: 100%;
    display: block;
    flex-direction: column;
    align-items: center;
    background: #ff8c00;
    color: #ffffff;
    border: none;
    font-size: 18px;
    margin-top: 0px;
    border-radius: 0px
}

.spanstyle {
    font-size: 10px;
    margin-top: 5px
}

.dark .buttonstyle {
    background: #ff8c00;
    color: #ff8c00;
}

.mobile-app-icon-bar button {
    padding: 0.75rem !important;
    cursor: pointer
}

</style>
EOF;

// from: https://github.com/Littlegolden/Site-Backup/blob/061f2a65d005fa42c6bb9d794e1bb1ba463394db/bbs_csur_fun/custom_footer.html
$customFootHTML = <<<EOF
<div class="mobile-app-icon-bar" id="myDIV">
    <button onclick="location.href='/'" class="buttonstyle"><i class="fa fa-home"></i>
        <span class="spanstyle"></span></button>
    <button onclick="location.href='/tags'" class="buttonstyle"><i class="fas fa-tags"></i>
        <span class="spanstyle"></span></button>
    <button onclick="document.querySelector('.IndexPage-newDiscussion').click()" class="buttonstyle"><i class="fas fa-edit"></i>
        <span class="spanstyle"></span></button>
    <button onclick="location.href='/settings'" class="buttonstyle"><i class="fas fa-user-cog"></i>
        <span class="spanstyle"></span></button>
    <button onclick="location.href='/notifications'" class="buttonstyle"><i class="fas fa-bell"></i>
        <span class="spanstyle"></span></button>
</div>
<script>
var btnContainer = document.getElementById("myDIV");
var btns = btnContainer.getElementsByClassName("buttonstyle");
for (var i = 0; i < btns.length; i++) {
    btns[i].addEventListener("click", function() {
        var current = document.getElementsByClassName("active");
        current[0].className = current[0].className.replace(" active", "");
        this.className += " active";
    });
}
</script>

<script>
function deneme() {
    document.querySelector('.IndexPage-newDiscussion').click();
}
</script>
EOF;

return [
    // Register extenders here to customize your forum!
    // 移动端底部栏
    (new Extend\Frontend('forum'))
        ->content(function (Document $document) use ($customFootHTML, $customStyle) {
            $document->head[] = $customStyle;
            $document->foot[] = $customFootHTML;
        })
];
