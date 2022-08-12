# -*- coding:utf-8 -*-
"""
# @author  apple
# @version 1.1
# @env     Pycharm Python3.9
# @desc    股票爬取脚本
"""
import tushare as ts
import time
import json
import sys
import os


# 创建相关的股票编号文件和写入数据
# noinspection DuplicatedCode
def json_transfer_file(key_list: list, value_list: list, share_name: str):
    if not (isinstance( key_list, list ) and isinstance( value_list, list )):
        raise TypeError( "类型出错,都要保证key和value都是数组哦!" )

    if key_list.__len__() != value_list.__len__():
        raise Exception( "key和value的大小长度要相同!" )
    json_file_list = []
    for number_index in range( 0, key_list.__len__() ):
        json_file_list.append(
            operation_func( key_list=key_list, value_list=value_list, number_index=number_index ) )

    file_name = ""
    if os.name == "posix":
        file_name_posix = "/static/indexes/" + share_name + ".json"
        file_name = gain_need_path(file_name_posix)
    elif os.name == "nt":
        file_name_nt = "\\static\\indexes\\" + share_name + ".json"
        file_name = gain_need_path(file_name_nt)
    else:
        raise OSError( "仅支持POSIX和NT系统谢谢" )

    # 将生成的json写入文件中.
    with open( file_name, 'w+' ) as operation_file:
        operation_file.write( str( json_file_list ) )

# 将数据转换为json
def operation_func(key_list, value_list, number_index):
    result_dict = {}
    result_dict.setdefault( "date", key_list[number_index] )
    result_dict.setdefault( "closePoint", value_list[number_index] )
    return result_dict


# 将json转化为dict
def codes_json_transfer_dict():
    codes_path: str = ""
    if os.name == "posix":
        file_codes_posix = "/static/indexes/codes.json"
        codes_path = gain_need_path(file_codes_posix)
    elif os.name == "nt":
        file_codes_nt = "\\static\\indexes\\codes.json"
        codes_path = gain_need_path(file_codes_nt)
    else:
        raise OSError( "仅支持POSIX和NT系统谢谢" )

    with open( codes_path, encoding='utf-8-sig', errors='ignore' ) as file:
        data: list = json.loads( file.read() )
        return list_json_transfer_dict( data )


# 将json.loads()返回的数组中的字典一个个读出来.
def list_json_transfer_dict(data: list):
    dict_message: dict = {}
    for info in data:
        dict_message.setdefault( info['code'], info['name'] )
    return dict_message


# 获取昨天的股票收盘日期.
def get_today_date():
    return time.strftime( "%Y-%m-%d", time.localtime( time.time() - 24 * 60 * 60 ) )


# 删除不在codes.json股票的json数据.
def delete_not_need_json(data: list):
    path = ""
    if os.name == "posix":
        dir_path_posix = "/static/indexes/"
        path = gain_need_path(dir_path_posix)
    elif os.name == "nt":
        dir_path_nt = "\\static\\indexes\\"
        path = gain_need_path(dir_path_nt)
    else:
        raise OSError( "仅支持POSIX和NT系统谢谢" )
    share_dic: list = os.listdir( path )
    share_dic.remove( "codes.json" )
    for code in share_dic:
        if not data.__contains__( code ):
            os.remove( path + code )

# 生成main_cpp 取执行
def generator_main_cpp():
    current_parent_path = gain_need_path("")
    current_work_main = current_parent_path.removesuffix("python")
    current_work_cpp = ""
    if os.name == "posix":
        current_work_cpp = current_work_main + "cplusplus/"
        os.chdir(current_work_cpp)
        if not os.path.exists( "./main" ):
            command: str = ""
            if sys.platform == "darwin":
                command = "c++ -std=c++20 ./script_replace.cpp ./json/lib_json/*.cpp -I ./ -o main"
            elif sys.platform == "linux":
                command = "c++ -std=c++2a ./script_replace.cpp ./json/lib_json/*.cpp -I ./ -o main"
            os.system( command=command )
    elif os.name == "nt":
        current_work_cpp = current_work_main + "cplusplus\\"
        os.chdir(current_work_cpp)
        if not os.path.exists( ".\\main.exe" ):
            command: str = "c++ -std=c++20 .\\script_replace.cpp .\\json\\lib_json\\*.cpp -I .\\ -o main.exe"
            os.system( command=command )
    else:
        raise OSError( "仅支持POSIX和NT系统谢谢" )

# 通过信号量机制来再信号量文件中写入信号量
# noinspection DuplicatedCode
def write_signal_message(signal_message: str):
    execute_file_path: str = gain_need_path("")
    signal_file_path: str = execute_file_path.removesuffix("python")
    signal_file: str = signal_file_path + "signal.txt"
    with open(signal_file, mode='w+') as file:
        file.write(signal_message)


# 获取路径
def gain_need_path(path_: str):
    file_parent_path: str = os.path.split( os.path.realpath( __file__ ) )[0]
    return file_parent_path + path_


def main():
    codes_dict: dict = codes_json_transfer_dict()
    delete_not_need_json( list( codes_dict.keys() ) )
    ts.set_token( "c7c69ac9160c2ea56926e9bff890e837edc5aba4ce13cb736b695f69" )
    for shares_code in codes_dict.keys():
        data_get = ts.get_k_data( code=shares_code, start='2020-01-01', end=get_today_date(), autype='hfq' )
        data_get = data_get.sort_index( ascending=False )
        print( "获取股票: " + codes_dict[shares_code] + "的数据." )
        shares_date: list = data_get['date'].values.tolist()
        shares_close: list = data_get['close'].values.tolist()
        json_transfer_file( shares_date, shares_close, shares_code )
    generator_main_cpp()
    write_signal_message("gainSuccess")

if __name__ == '__main__':
    main()
