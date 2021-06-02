var canvas, context;
var showTips = null,
    originalPos = null;
var img, imgX = 0,
    imgY = 0,
    imgScale = 20,
    scaleRate = 1;
var MINIMUM_SCALE = 1,
    pos = {},
    posl = {},
    dragging = false;
var initWidth = 0,
    initHeight = 0
var selectedColor = [0,0,0];
//每 delayGetSvg 秒向服务器请求一次图片
let delayGetSvg =60
//每 delayDraw 秒 才能点击一次绘画按钮
let delayDraw = 2
let myURL="http://127.0.0.1:8081"

canvas = document.getElementById('canvas'); //画布对象
context = canvas.getContext('2d'); //画布显示二维图片
loadImg();
canvasEventsInit();


const sleep = (timeout = 2000) => new Promise(resolve => {
    setTimeout(resolve, timeout);
});

async function getSvg() {
    const response = await fetch(URL+"/svg");
    const blob = await response.blob();
    const imgUrl = URL.createObjectURL(blob);
    return imgUrl;
}

setInterval(async function () {

    img.src = await getSvg();

}, 1000*delayGetSvg);//请求最新画布延迟

function loadImg() {
    img = new Image();
    img.width = "400";
    img.height = "400";
    img.src = myURL+"/history/0.svg"
    img.onload = function () {
        initWidth = img.width;
        initHeight = img.height;
        drawImage();
    }

}

async function drawImage() {
    //context.clearRect(0, 0, canvas.width, canvas.height);

    // 保证  imgX  在  [img.width*(1-imgScale),0]   区间内
    ///**
    if (imgX < img.width * (1 - imgScale) / scaleRate) {
        imgX = img.width * (1 - imgScale) / scaleRate;
    } else if (imgX > 0) {
        imgX = 0
    }
    // 保证  imgY   在  [img.height*(1-imgScale),0]   区间内
    if (imgY < img.height * (1 - imgScale) / scaleRate) {
        imgY = img.height * (1 - imgScale) / scaleRate;
    } else if (imgY > 0) {
        imgY = 0
    }
    context.drawImage(
        img, //规定要使用的图像、画布或视频。
        0, 0, //开始剪切的 xy 坐标位置。
        initWidth, initHeight, //被剪切图像的高度。
        //img.width,img.height,

        imgX, imgY, //在画布上放置图像的 x 、y坐标位置。
        img.width * imgScale, img.height * imgScale //要使用的图像的宽度、高度
    );
}
/*事件注册*/
function canvasEventsInit() {
    canvas.onclick = function (event) {
        var x = event.layerX;
        var y = event.layerY;
        // var pixel = context.getIma geData(x, y, 1, 1);
        // var data = pixel.data;
        // console.log(data)
        document.getElementById("selectedX").value = (Math.floor((Math.abs(imgX) + x) / imgScale));
        document.getElementById("selectedY").value = (Math.floor((Math.abs(imgY) + y) / imgScale));
    };

    canvas.onmousedown = function (event) {
        document.getElementById("canvas").style.cursor = "move";
        if (showTips == null) {
            dragging = true;
            pos = windowToCanvas(event.clientX, event.clientY); //坐标转换，将窗口坐标转换成canvas的坐标
        }
        //console.log(pos);
    };
    canvas.onmousemove = async function (evt) { //移动
        var x = evt.layerX;
        var y = evt.layerY;
        var realTimeCoord = document.getElementById("realTimeCoord");
        realTimeCoord.innerText = `X:${(Math.floor((Math.abs(imgX)+x)/imgScale))} Y:${(Math.floor((Math.abs(imgY)+y)/imgScale))} 倍率:${imgScale}`

        if (dragging) {
            posl = windowToCanvas(evt.clientX, evt.clientY);

            var x = posl.x - pos.x,
                y = posl.y - pos.y;
            imgX += x;
            imgY += y;

            pos = JSON.parse(JSON.stringify(posl));

            await drawImage(); //重新绘制图片
        } else {
            pos3 = windowToCanvas(evt.clientX, evt.clientY);
            if (context.isPointInPath(pos3.x, pos3.y)) {
                console.log("进入区域");
            }

        }
    };
    canvas.onmouseup = function (evt) {
        document.getElementById("canvas").style.cursor = "crosshair";

        dragging = false;

    };
    canvas.onwheel = function (event) { //滚轮放大缩小
        var pos = windowToCanvas(event.clientX, event.clientY);
        event.wheelDelta = event.wheelDelta ? event.wheelDelta : (event.deltalY * (-40)); //获取当前鼠标的滚动情况
        var newPos = {
            x: ((pos.x - imgX) / imgScale).toFixed(2),
            y: ((pos.y - imgY) / imgScale).toFixed(2)
        };

        if (event.wheelDelta > 0) { // 放大
            imgScale += 1;
            imgX = (1 - imgScale) * newPos.x + (pos.x - newPos.x);
            imgY = (1 - imgScale) * newPos.y + (pos.y - newPos.y);
        } else { //  缩小
            imgScale -= 1;
            if (imgScale < MINIMUM_SCALE) { //最小缩放1
                imgScale = MINIMUM_SCALE;
            }
            imgX = (1 - imgScale) * newPos.x + (pos.x - newPos.x);
            imgY = (1 - imgScale) * newPos.y + (pos.y - newPos.y);
        }
        drawImage(); //重新绘制图片
        event.preventDefault();
    };
}

/*坐标转换*/
function windowToCanvas(x, y) {
    var box = canvas.getBoundingClientRect();
    //这个方法返回一个矩形对象，包含四个属性：left、top、right和bottom。分别表示元素各边与页面上边和左边的距离
    return {
        x: x - box.left - (box.width - canvas.width) / 2,
        y: y - box.top - (box.height - canvas.height) / 2
    };
}

function selectColor(btn) {
    var colorButtons = document.getElementsByClassName("ColorPalette__color")
    for (let i = 0; i < colorButtons.length; i++) {
        colorButtons[i].classList.remove("active")
    }
    btn.classList.add("active")
    selectedColor = btn.style.backgroundColor.slice(4, -1).split(", ")
    console.log(selectedColor)
}


async function changePixel() {
    let x = document.getElementById("selectedX").value;
    let y = document.getElementById("selectedY").value;
    try {
        console.log("drawing a pixel")
        const response = await fetch(myURL+"/svg/changePixel?x=" + x + "&y=" + y +
            "&r=" + selectedColor[0] + "&g=" + selectedColor[1] + "&b=" + selectedColor[2], {
                method: "GET"
            })
            if(response.status ==200){
                console.log("draw a pixel success")
                document.getElementById("drawPixelBtn").setAttribute("data-title","稍等一会儿")
                document.getElementById("drawPixelBtn").setAttribute("onclick","");
                //点击后过一段时间才能继续点击, 防脚本
                await sleep(1000*delayDraw)
                document.getElementById("drawPixelBtn").setAttribute("data-title","画下一个像素")
                document.getElementById("drawPixelBtn").setAttribute("onclick","changePixel()");
            }
            else{
                console.log("draw a pixel fail")
            }
    } catch (e) {
        alert("draw a pixel error")
        return;
    }
}



//按钮特效
const docStyle = document.documentElement.style
const aElem = document.querySelector('a')
const boundingClientRect = aElem.getBoundingClientRect()

aElem.onmousemove = function (e) {
    const x = e.clientX - boundingClientRect.left
    const y = e.clientY - boundingClientRect.top

    const xc = boundingClientRect.width / 2
    const yc = boundingClientRect.height / 2

    const dx = x - xc
    const dy = y - yc

    docStyle.setProperty('--rx', `${ dy/-1 }deg`)
    docStyle.setProperty('--ry', `${ dx/10 }deg`)

}

aElem.onmouseleave = function (e) {
    docStyle.setProperty('--ty', '0')
    docStyle.setProperty('--rx', '0')
    docStyle.setProperty('--ry', '0')
}

aElem.onmousedown = function (e) {
    docStyle.setProperty('--tz', '-25px')
}

document.body.onmouseup = function (e) {
    docStyle.setProperty('--tz', '-12px')
}