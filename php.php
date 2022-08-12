<?php
class Sign{

    //签名排序算法
   public function getSignContent($params){
        if(!is_array($params)){
            return false;
        }
        ksort($params);
        $stringToBeSigned = "";
        $i = 0;
        //非空函数
        $checkEmpty = function($value){
            if(!isset($value))
                return true;
            if($value === null)
                return true;
            if(trim(json_encode($value, JSON_UNESCAPED_UNICODE)) === "")
                return true;
            return false;
        };
        //字符集转换函数
        $characet = function($data){
            if(!empty($data)){
                $fileType = "UTF-8";
                if(strcasecmp($fileType, "UTF-8") != 0){
                    $data = mb_convert_encoding($data, "UTF-8", $fileType);
                }
            }
            return $data;
        };
        //参数排序
        foreach($params as $k => $v){
            if(is_array($v)) $v = getSignContent($v);
            if(false === $checkEmpty($v) && "@" != substr($v, 0, 1)){
                // 转换成目标字符集
                $v = $characet($v);
                if($i == 0){
                    $stringToBeSigned .= "$k"."="."$v";
                }else{
                    $stringToBeSigned .= "&"."$k"."="."$v";
                }
                $i++;
            }
        }

        unset ($k, $v);
        return $stringToBeSigned;
    }
}