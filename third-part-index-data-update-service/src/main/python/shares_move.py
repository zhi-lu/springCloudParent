# -*- coding:utf-8 -*-
"""
# @author  apple
# @version 1.1
# @env     Pycharm Python3
# @desc    股票数据移动脚本
"""
import shutil
import os


def implement_replace():
    path = gain_need_path("")
    source_path: str = ""
    intermediate_path: str = ""
    replace_path: str = ""
    path_split: str = ""
    if os.name == "posix":
        path_split = "/"
        source_path = path + "/static/indexes"
        intermediate_path = path.removesuffix( "third-part-index-data-update-service/src/main/python" )
        replace_path = intermediate_path.__add__( "third-part-index-data-project/src/main/resources/static/indexes" )
    elif os.name == "nt":
        path_split = "\\"
        source_path = path + "\\static\\indexes"
        intermediate_path = path.removesuffix( "third-part-index-data-update-service\\src\\main\\python" )
        replace_path = intermediate_path.__add__( "third-part-index-data-project\\src\\main\\resources\\static\\indexes" )
    else:
        raise OSError( "仅支持POSIX和NT系统谢谢" )

    try:
        shutil.rmtree( replace_path )
    except FileNotFoundError:
        print( "文件夹不存在或者已经被删除" )
    os.mkdir( replace_path )
    file_list: list = os.listdir( source_path )
    for file in file_list:
        file = source_path.__add__( path_split + file )
        shutil.copy( file, replace_path )

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
    implement_replace()
    write_signal_message("moveSuccess")

if __name__ == '__main__':
    main()
