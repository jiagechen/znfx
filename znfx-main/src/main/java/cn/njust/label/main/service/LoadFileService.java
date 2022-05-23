package cn.njust.label.main.service;

public interface LoadFileService {
    /*导入ssr text类型文件*/
    String loadCivilAviationFile(String filePath, int[] dataIndex);
    String loadCivilShipFile(String filePath, int[] dataIndex);
}
