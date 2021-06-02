<?php 
//数据库工具类
class DataBaseUtil{
    //MySQL主机地址
    private $_host;
    //MySQL用户名
    private $_user;
    //MySQL用户密码
    private $_password;
    //指定数据库名称
    private $_database;
    //MySQL数据库端口号
    private $_port;
    private $_socket;
    //当前数据库对象
    public $_dbObj;
    //数据库表
    private $_table;
    //数据库表对象
    private $_tableObj;
    // 最近错误信息
    protected $error            =   '';
    // 数据信息
    protected $data             =   array();
    // 查询表达式参数
    protected $options          =   array();
    protected $_validate        =   array();  // 自动验证定义
    protected $_auto            =   array();  // 自动完成定义
    protected $_map             =   array();  // 字段映射定义
    protected $_scope           =   array();  // 命名范围定义
    // 链操作方法列表
    protected $methods          =   array('strict','order','alias','having','group','lock','distinct','auto','filter','validate','result','token','index','force');

    /**
     * Database类初始化函数
     * 取得DB类的实例对象 字段检查
     * @access public
     * @param string $host MySQL数据库主机名
     * @param string $user MySQL数据库用户名
     * @param string $password MySQL数据库密码
     * @param string $database 指定操作的数据库
     * @return mixed  数据库连接信息、错误信息
     */
    public function __construct(){
       		return $this->_initialize();
    }
    /**
     * 错误信息函数
     * 返回数据库操作过程中最后一次执行时的错误信息
     * @access public
     * @return mixed  数据库连接错误信息(正常返回'')
     */
    public function error(){
        return $this->error;
    }
    // 回调方法 初始化模型
    protected function _initialize() {
		$this->_host     = DB_HOST;
		$this->_user     = DB_USER;
		$this->_password = DB_PASSWORD;
		$this->_database = DB_DATABASE;
		$this->_port     = DB_PORT;
		$_dbObj = new mysqli(DB_HOST,DB_USER,DB_PASSWORD,DB_DATABASE,DB_PORT);
		if($_dbObj->connect_errno){
			echo "<br/><font style='color:red'>数据库连接错误,请检查配置文件</font><br/>";
			$this->error = $_dbObj->connect_error;
			return false;
		}else{
            mysqli_query($_dbObj,"set names utf8");                            
			$this->_dbObj = $_dbObj;
			return $this;
		}	
	}
    /**
     * 设置数据对象的值
     * @access public
     * @param string $name 名称
     * @param mixed $value 值
     * @return void
     */
    public function __set($name,$value) {
        // 设置数据对象属性
        $this->data[$name] = $value;
    }

    /**
     * 获取数据对象的值
     * @access public
     * @param string $name 名称
     * @return mixed
     */
    public function __get($name) {
        return isset($this->data[$name])?$this->data[$name]:null;
    }

    /**
     * 检测数据对象的值
     * @access public
     * @param string $name 名称
     * @return boolean
     */
    public function __isset($name) {
        return isset($this->data[$name]);
    }

    /**
     * 销毁数据对象的值
     * @access public
     * @param string $name 名称
     * @return void
     */
    public function __unset($name) {
        unset($this->data[$name]);
    }
    /**
     * 利用__call方法实现一些特殊的方法(对于调用类中不存在方法的解决方案)
     * @access public
     * @param string $method 方法名称
     * @param array $args 调用参数
     * @return mixed
     */
    public function __call($method,$args) {
        /*if(in_array(strtolower($method),$this->methods,true)) {
            // 连贯操作的实现
            $this->options[strtolower($method)] =   $args[0];
            return $this;
        }elseif(in_array(strtolower($method),array('count','sum','min','max','avg'),true)){
            // 统计查询的实现
            $field =  isset($args[0])?$args[0]:'*';
            return ;
        }elseif(strtolower(substr($method,0,5))=='getby') {
            // 根据某个字段获取记录
            $field   =   parse_name(substr($method,5));
            $where[$field] =  $args[0];
            return ;
        }elseif(strtolower(substr($method,0,10))=='getfieldby') {
            // 根据某个字段获取记录的某个值
            $name   =   parse_name(substr($method,10));
            $where[$name] =$args[0];
            return ;
        }elseif(isset($this->_scope[$method])){// 命名范围的单独调用支持
            return ;
        }else{

        }*/
    }
    /*
     * 选择数据库
     * @access public
     * @param string $database 选择的数据库名称
     * @return mixed 数据库连接信息
     * */
    public function select_db($database){
        $select_db = mysqli_select_db($this->_dbObj,$database);
        if($select_db){
            $this->_database = $database;
            $_dbObj = new mysqli($this->_host,$this->_user,$this->_password,$database,$this->_port);
            $this->_dbObj = $_dbObj;
            return $this;
        }else{
            $this->error = mysqli_error($this->_dbObj);
            return false;
        }
    }
    /*
     * 数据库用户更换
     * @access public
     * @param string $user 数据库用户名称
     * @param string $password 数据库用户密码
     * @return mixed 数据库连接信息
     * */
    public function change_user($user,$password){
        $change_user = mysqli_change_user($this->_dbObj,$user,$password,$this->_database);
        if($change_user){
            $this->_user = $user;
            $this->_password = $password;
            $_dbObj = new mysqli($this->_host,$this->_user,$this->_password,$this->_database,$this->_port);
            $this->_dbObj = $_dbObj;
            return $this;
        }else{
            $this->error = mysqli_error($this->_dbObj);
            return false;
        }
    }
    /*
     * 查询数据库中所有的表名
     * @access public
     * @return array 数据表的数量和表名
     * */
    public function tables(){
        $sql = 'show tables';
        $search_res = mysqli_query($this->_dbObj,$sql);
        if($search_res){
            $num_rows = $search_res->num_rows;
            $tables_msg = array(
                'count'=>$num_rows,
                'tables'=>array()
            );
            for($i=0;$i<$num_rows;$i++){
                $row = $search_res->fetch_assoc();
                $key = 'Tables_in_'.$this->_database;
                array_push($tables_msg['tables'],$row[$key]);
            }
            mysqli_free_result($search_res);
            return $tables_msg;
        }else{
            mysqli_free_result($search_res);
            return false;
        }
    }
    /*
     * 获取指定表中所有信息
     * @access public
     * @param string $table 数据表名称
     * @return array 数据表的详细信息
     * */
    public function select_table($table){
        $sql = 'select * from '.$table;
        $search_res = mysqli_query($this->_dbObj,$sql);
        if($search_res){
            $this->_table = $table;
            $table_msg = self::query_handle($search_res);
            $this->_tableObj = $table_msg;
            mysqli_free_result($search_res);
            return $table_msg;
        }else{
            mysqli_free_result($search_res);
            return false;
        }
    }
    /*
     * 获取指定表的字段详细信息
     * @access public
     * @param string $table 数据表名称
     * @return array 数据表的字段详细信息
     * */
    public function select_table_fields($table){
        $sql = 'show fields from '.$table;
        $search_res = mysqli_query($this->_dbObj,$sql);
        if($search_res){
            $this->_table = $table;
            $fields_msg = self::query_handle($search_res);
            mysqli_free_result($search_res);
            return $fields_msg;
        }else{
            mysqli_free_result($search_res);
            return false;
        }
    }
    /*
     * 获取数据表中指定字段信息（允许多字段同时查询）
     * @access public
     * @param mixed $field 指定字段（字符串传入使用，间隔）
     * @return array 数据表中指定字段信息
     * */
    public function getField($field){
        $fields = self::param_handle($field);
        $count = count($fields);
        for($i=0;$i<$count;$i++){
            $index = $fields[$i];
            $sql = 'select '.$index.' from '.$this->_table;
            $res = mysqli_query($this->_dbObj,$sql);
            $field_msg[$index] = self::query_handle($res);
        }
        return $field_msg;
    }
    /*
     * mysqli_query函数结果处理函数
     * @access protected
     * @param object $obj mysqli_query函数结果
     * @return array 数据表中指定字段信息
     * */
    protected function query_handle($obj){
        $res = array();
        for($i=0;$i<$obj->num_rows;$i++){
            $row = $obj->fetch_assoc();
            array_push($res,$row);
        }
        return $res;
    }
    /*
     * 传入参数处理函数
     * @access protected
     * @param mixed $param 传入参数
     * @return array 处理后数组数据
     * */
    public function param_handle($param){
        if(is_string($param)&&!empty($param)){
            $params = explode(',',$param);
        }elseif(is_array($param)&&!empty($param)){
            $params = $param;
        }else{
            return false;
        }
        return $params;
    }
    /*
     * 查询表达式参数处理函数
     * @access protected
     * @param mixed $param 传入参数(where limit order)
     * @return string 处理后字符串数据
     * */
    public function options_handle($param){
        if(is_numeric($param)){
            $option = $param;
        }elseif(is_string($param)&&!empty($param)&&!is_numeric($param)){
            $params = explode(',',$param);
            $count = count($params);
            $option = implode(' and ',$params);
        }elseif(is_array($param)&&!empty($param)){
            $params = $param;
            $count = count($params);
            $arr = array();
            foreach($param as $key=>$value){
                $tip = "$key=$value ";
                array_push($arr,$tip);
            }
            $option = implode(' and ',$arr);
        }else{
            return false;
        }
        return $option;
    }
    /*
     * 查询表达式$options处理函数
     * @access protected
     * @return string 处理后字符串数据
     * */
    protected function option(){
        $options = $this->options;
        $option = '';
        if(isset($options['where'])){
            $option .= 'where '.$options['where'].' ';
        }
        if(isset($options['order'])){
            $option .= 'order by '.$options['order'].' '.$options['order_type'].' ';
        }
        if(isset($options['limit'])){
            $option .= 'limit '.$options['limit'];
        }
        return $option;
    }
    /*
     * 根据查询表达式查询数据(符合条件的所有记录)
     * @access public
     * @return array 满足查询表达式的特定数据
     * */
    public function find(){
        $option = self::option();
        $sql = 'select * from '.$this->_table.' '.$option;
        $search_res = mysqli_query($this->_dbObj,$sql);
        $msg = self::query_handle($search_res);
        return $msg;
    }
    /*
     * 查询表达式 where处理函数
     * @access public
     * @param mixed $where where查询条件
     * @return object $this
     * */
    public function where($where){
        $this->options['where'] = self::options_handle($where);
        return $this;
    }
    /*
     * 查询表达式 limit处理函数
     * @access public
     * @param mixed $limit limit查询条件(数字)
     * @return object $this
     * */
    public function limit($limit){
        $this->options['limit'] = self::options_handle($limit);
        return $this;
    }
    /*
     * 查询表达式 order处理函数
     * @access public
     * @param string $order order查询条件
     * @param string $type order查询条件的顺序（默认降序）
     * @return object $this
     * */
    public function order($order,$type='desc'){
        $this->options['order'] = $order;
        $this->options['order_type'] = $type;
        return $this;
    }
    /*
     * 数据处理函数(最多处理二维数据)
     * @access public
     * @param array $data 需要插入的数据
     * @return object $this
     * */
    public function data(array $data){
        $values = array();
        $fields = array();
        if(is_array($data)){
            foreach($data as $key=>$value){
                if(is_array($value)){       //二维数组
                    $tip = 1;
                    array_push($values,'('.implode(',',array_values($value)).')');
                    array_push($fields,'('.implode(',',array_keys($value)).')');
                }else{      //一维数组
                    $tip = 0;
                }
            }
        }else{
            return false;
        }
        if(!$tip){
            array_push($values,'('.implode(',',array_values($data)).')');
            array_push($fields,'('.implode(',',array_keys($data)).')');
        }
        $this->data['fields'] = $fields[0];
        $this->data['values'] = implode(',',$values);
        return $this;
    }
    /*
     * 数据新增函数
     * @access public
     * @return mixed 数据库新增信息
     * */
    public function add(){
        $fields = $this->data['fields'];
        $values = $this->data['values'];
        $sql = 'INSERT INTO '.$this->_table.$fields.'VALUES'.$values;
        $res = mysqli_query($this->_dbObj,$sql);
        return $res;
    }
    /*
     * 数据更新函数（一维数组）
     * @access public
     * @param array $data 需要更新的数据
     * @return mixed 数据库新增信息
     * */
    function save(array $data){
        $tip = array();
        if(is_array($data)){
            foreach($data as $key=>$value){
                array_push($tip,"$key=$value");
            }
        }else{
            return false;
        }
        $set_msg = implode(',',$tip);
        $sql = 'UPDATE '.$this->_table.' SET '.$set_msg.' WHERE '.$this->options['where'];
        $res = mysqli_query($this->_dbObj,$sql);
        return $res;
    }
    /*
     * 数据删除函数
     * @access public
     * @return mixed 数据库删除信息
     * */
    public function delete(){
        $sql = 'DELETE FROM '.$this->_table.' WHERE '.$this->options['where'];
        $res = mysqli_query($this->_dbObj,$sql);
        return $res;
    }
    /*
     * SQL语句查询
     * */
    public function query($sql){
        $search_res = mysqli_query($this->_dbObj,$sql);
        return $search_res;
    }
    /*
     * mysql中查询语句
     * */
    protected function sql(){
        /*
         * 基本SQL语句
         * 插入数据：INSERT INTO tb_name(id,name,score)VALUES(NULL,'张三',140),(NULL,'张四',178),(NULL,'张五',134);
         * 更新语句：UPDATE tb_name SET score=189 WHERE id=2;
         * 删除数据：DELETE FROM tb_name WHERE id=3;
         * WHERE语句：SELECT * FROM tb_name WHERE id=3;
         * HAVING 语句：SELECT * FROM tb_name GROUP BY score HAVING count(*)>2
         * 相关条件控制符：=、>、<、<>、IN(1,2,3......)、BETWEEN a AND b、NOT AND 、OR Linke()用法中      %  为匹配任意、  _  匹配一个字符（可以是汉字）IS NULL 空值检测
         * MySQL的正则表达式：SELECT * FROM tb_name WHERE name REGEXP '^[A-D]'   //找出以A-D 为开头的name
         * */
    }
    /*
     * 关闭连接
     * */
    public function close(){
        $close = mysqli_close($this->_dbObj);
        if($close){
            return true;
        }else{
            return false;
        }
    }
    public function __destruct(){
        mysqli_close($this->_dbObj);
    }
}
?>