package cn.njust.label.main.service.impl;

import cn.njust.label.main.service.LoadFileService;
import cn.njust.label.main.utils.PathTypeUtil;
import cn.njust.label.main.utils.ReadFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Service
public class LoadFileServiceImpl implements LoadFileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadFileServiceImpl.class);

    @Autowired
    MongoTemplate mongoTemplate;

    private static LoadFileServiceImpl service;

    @PostConstruct
    public void init(){
        service = this;
        service.mongoTemplate = mongoTemplate;
    }

    public static String loadSingleCivilShipFile(File file, int[] dataIndex){
        ArrayList<String> items = ReadFileUtil.readFile(file);
        String fileName = file.getName();
        // 用HashMap暂存各个航班的item，只取需要的字段
        HashMap<String, ArrayList<ArrayList<String>>> ships;
        if(fileName.endsWith(".txt")){
            ships = ReadFileUtil.parseCivilShipTxt(items);
        }else if(fileName.endsWith(".csv")){
            ships = ReadFileUtil.parseCivilShipCsv(items);
        }
        // 其他类型文件
        else{
            LOGGER.info("文件类型不符合要求，请更正！");
            ships = null;
        }
        LOGGER.info("数据加载完成");
        /*
         * 导入加载数据到MongoDB
         * */
        return ReadFileUtil.importCivilShipToDB(ships, fileName, service.mongoTemplate);
    }

    public static String loadSingleCivilAviationFile(File file, int[] dataIndex){
        ArrayList<String> items = ReadFileUtil.readFile(file);
        String fileName = file.getName();
        // 用HashMap暂存各个航班的item，只取需要的字段
        HashMap<String, ArrayList<ArrayList<String>>> flights;
        flights = ReadFileUtil.parseCivilAviationFile(items, dataIndex, fileName);
        // 其他类型文件
        if(flights == null){
            LOGGER.info(file + "文件为空或文件类型不符合要求");
            return "read success";
        }
        LOGGER.info("数据加载完成");
        /*
         * 导入加载数据到MongoDB
         * */
        return ReadFileUtil.importCivilAviationToDB(flights, fileName, service.mongoTemplate);
    }

    /**
    * @description: 加载单个民航轨迹文件
    * @param file:文件路径
     * @param dataIndex :数据索引点
     * @param collectionName :集合名
    * @return java.lang.String: 导入状态
    * @date: 2022/5/3
    */
    public static String loadSingleCivilAviationFile(File file, int[] dataIndex, String collectionName){
        ArrayList<String> items = ReadFileUtil.readFile(file);
        String fileName = file.getName();
        // 用HashMap暂存各个航班的item，只取需要的字段
        HashMap<String, ArrayList<ArrayList<String>>> flights;
        flights = ReadFileUtil.parseCivilAviationFile(items, dataIndex, fileName);
        // 其他类型文件
        if(flights == null){
            LOGGER.info(file + "文件为空或文件类型不符合要求");
            return "read success";
        }
        LOGGER.info("数据加载完成");
        /*
         * 导入加载数据到MongoDB
         * */
        return ReadFileUtil.importCivilAviationToDB(flights, collectionName, service.mongoTemplate);
    }


    /**
     * 导入民船轨迹文件
     * @param filePath :文件路径
     * @param dataIndex :数据项索引，{id index, lat index, lon index, height index, time_stamp index}
     *                  eg. {0, 1,2,3,4,5,6,7} 表示数据内,第0列表示目标索引，第一列为latitude，第二列为longitude 。。。
     * */
    @Override
    public String loadCivilShipFile(String filePath, int[] dataIndex) {
        File file = new File(filePath);
        String fileType = PathTypeUtil.pathType(file);
        if(fileType.equals("file")){
            LOGGER.info(loadSingleCivilShipFile(file, dataIndex));
        }
        else if(fileType.equals("dir")){
            File[] files = file.listFiles();
            if(files != null && files.length > 0){
                LOGGER.info("文件列表为+" + Arrays.toString(files));
            }else{
                LOGGER.info("文件夹为空");
                return null;
            }
            for(File singleFile: files){
                LOGGER.info(loadSingleCivilShipFile(singleFile, dataIndex));
            }
        }
        LOGGER.info("导入完成。");
        return null;
    }

    /**
    * 导入民航轨迹文件
     * @param filePath :文件路径
     * @param dataIndex :数据项索引，{id index, lat index, lon index, height index, time_stamp index}
     *                  eg. {0, 1,2,3,4,5,6,7} 表示数据内,第0列表示目标索引，第一列为latitude，第二列为longitude 。。。
    * */
    @Override
    public String loadCivilAviationFile(String filePath, int[] dataIndex) {
        File file = new File(filePath);
        String fileType = PathTypeUtil.pathType(file);
        if(fileType.equals("file")){
            LOGGER.info(loadSingleCivilAviationFile(file, dataIndex));
        }
        else if(fileType.equals("dir")){
            File[] files = file.listFiles();
            if(files != null && files.length > 0){
                LOGGER.info("文件列表为+" + Arrays.toString(files));
                for (File singleFile : files){
                    LOGGER.info(loadSingleCivilAviationFile(singleFile, dataIndex, file.getName()));
                }
            }else{
                LOGGER.info("文件夹为空");
                return null;
            }
        }
        LOGGER.info("导入完成。");
        return null;
    }

    public static void main(String[] args) {

    }
}
