//
// @author apple
// @version 1.1
// @env Clion Clang 12.0.5
// @desc 股票数据JSON格式化.
//

#include <unordered_map>
#include <iostream>
#include <fstream>
#include <vector>

#if _WIN32
#include <windows.h>
#elif __linux__
#include <unistd.h>
#elif __APPLE__

#include <mach-o/dyld.h>

#endif

#include "json/json.h"

#define COUT std::cout
#define END  std::endl

class File {
private:
    std::vector<char> *vectorData = new std::vector<char>;

    std::string *signalFilePath = new std::string;
public:
    File() = default;

    ~File() {
        // 释放分配的空间
        delete this->vectorData;

        delete this->signalFilePath;
    }

    void tuShare_json_replace_quotationMarks(const std::string &code);

    void replace_data(const std::string &code);

    std::unordered_map<std::basic_string<char>, std::basic_string<char>> return_shares_code_to_name_map();

    void writeSignalFile(const std::string &fileName);

private:
    std::string get_path();
};

int main() {
    File executeOtherFunc;
    std::unordered_map<std::basic_string<char>, std::basic_string<char>> map = executeOtherFunc.return_shares_code_to_name_map();
    for (auto &iter: map) {
        File file;
        std::string code = iter.first;
        std::string name = iter.second;
        COUT << "股票:" << name << ",开始进行单到双引号操作." << END;
        file.tuShare_json_replace_quotationMarks(code);
        file.replace_data(code);
    }
    executeOtherFunc.writeSignalFile("signal.txt");
    return 0;
}

/**
 * 打开相关的JSON文件,将单引号转化为双引号.
 *
 * @param code 代码编号
 */
void File::tuShare_json_replace_quotationMarks(const std::string &code) {
    // 创建文件流
    char data;
    std::fstream fileStream;
    std::string path = get_path() + code + ".json";

    fileStream.open(path, std::ios::in | std::ios::out);
    if (!fileStream) {
        COUT << "文件打开失败" << END;
        return;
    }

    while (fileStream >> data) {
        if (data == 39) {
            // 将单引号转换为双引号.
            this->vectorData->emplace_back(34);
        } else {
            this->vectorData->emplace_back(data);
        }
    }
    // 关闭文件流
    fileStream.close();
}

/**
 * 将转化为完的数据覆盖原有的JSON文件.
 *
 * @param code 代码编号
 */
void File::replace_data(const std::string &code) {
    // 文件地址.
    std::string path = get_path() + code + ".json";
    // 打开文件并清除文件原的内容.
    std::fstream fileStream(path, std::ios::in | std::ios::out | std::ios::trunc);
    if (!fileStream) {
        COUT << "打开文件失败" << END;
        return;
    }

    // 写入内容.
    for (auto i: *this->vectorData) {
        fileStream << i;
    }
    COUT << code + ".json, 单引号转化为双引号成功!" << END;
    // 关闭文件流
    fileStream.close();
}

/**
 * 通过Json解析器(cpp),获取相关的股票信息"代码和相关名称"以无序hash容器unordered_map保持信息!
 *
 * @return unordered_map#map
 */
std::unordered_map<std::basic_string<char>, std::basic_string<char>> File::return_shares_code_to_name_map() {
    std::string file_location = get_path() + "codes.json";
    // 创建文件流
    COUT << file_location << END;
    std::fstream fileCodeShareStream(file_location, std::ios::in | std::ios::out);
    std::unordered_map<std::basic_string<char>, std::basic_string<char>> map = {};
    if (!fileCodeShareStream) {
        COUT << "打开文件失败" << END;
        return map;
    }

    Json::Reader reader;
    Json::Value root;
    // 进行解析
    if (reader.parse(fileCodeShareStream, root)) {
        for (int i = 1; i <= root.size(); ++i) {
            Json::Value single_root = root[i - 1];
            std::string code = single_root["code"].asString();
            std::string name = single_root["name"].asString();
            map.emplace(code, name);
        }
    }
    // 关闭文件流
    fileCodeShareStream.close();
    return map;
}


/**
 * 通过可执行文件的地址转换为通用地址获取股票在项目的地址
 *
 * @return string(文件地址)
 */
std::string File::get_path() {
    char path[512];
    unsigned size = 512;
    // 对于MacOS操作系统
#if __APPLE__
    // _NSGetExecutablePath()函数在头文件<mach-o/dyld.h>下
    _NSGetExecutablePath(path, &size);
    path[size] = '\0';
    std::string path_string = path;
    std::string path_end = "cplusplus/main";
    std::string new_path_end = "python/static/indexes/";
    // ends_with()函数在c++20中
    if (path_string.ends_with(path_end)) {
        int new_file_path_size = static_cast<int>(path_string.size() - path_end.size());
        path_string.erase(new_file_path_size,  path_end.size());
        *signalFilePath = path_string;
        path_string = path_string + new_path_end;
    }
    return path_string;
    // 对于linux操作系统
#elif __linux__
    // readlink()函数在头文件<unistd.h>下
    int n = readlink("/proc/self/exe", path, size);
    std::string path_string;
    path[n] = '\0';
    if(n > 0 && n < size){
        path_string = path;
    }
    std::string path_end = "cplusplus/main";
    std::string new_path_end = "python/static/indexes/";
    if (path_string.ends_with(path_end)) {
        int new_file_path_size = static_cast<int>(path_string.size() - path_end.size());
        path_string.erase(new_file_path_size,  path_end.size());
        *signalFilePath = path_string;
        path_string = path_string + new_path_end;
    }
    return path_string;
    // 对于Win操作系统
#elif _WIN32
    // GetModuleFileName()函数在头文件#include <windows.h>下
    GetModuleFileName(nullptr, path, size);
    path[size] = '\0';
    std::string current_path = path;
    std::string ends_path = R"(cplusplus\main.exe)";
    std::string ends_new_path = R"(python\static\indexes\)";
    if (current_path.ends_with(ends_path)) {
        int path_size = static_cast<int>(current_path.size() - ends_path.size());
        current_path.erase(path_size, ends_path.size());
        *signalFilePath = current_path;
        current_path = current_path + ends_new_path;
    }
    return current_path;
#endif
}

void File::writeSignalFile(const std::string &fileName) {
    std::string signalSuccess = "replaceSuccess";
    *signalFilePath = *signalFilePath + fileName;
    std::fstream fileStream(*signalFilePath, std::ios::in | std::ios::out | std::ios::trunc);
    if (!fileStream) {
        COUT << "打开文件失败" << END;
        return;
    }
    for (auto i: signalSuccess) {
        fileStream << i;
    }
    fileStream.close();
}
