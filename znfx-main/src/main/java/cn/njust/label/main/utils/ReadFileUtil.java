package cn.njust.label.main.utils;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadFileUtil {

    /**
    *   读取文件
     * @param file :文件路径
    * */
    public static ArrayList<String> readFile(File file){
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String> items = new ArrayList<>();
            // 按行读取字符串
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                items.add(str);
            }
            bufferedReader.close();
            fileReader.close();
            return items;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    * 解析ssr数据txt文件读取的信息
    * */
    public static HashMap<String, ArrayList<ArrayList<String>>> parseCivilAviationTxt(ArrayList<String> items, int[] dataIndex){
        // 用HashMap暂存各个航班的item，只取需要的字段
        HashMap<String, ArrayList<ArrayList<String>>> flights = new HashMap<>();
        if(items != null){
            for(int r = 1; r < items.size(); ++r){
                String cur_item = items.get(r);
                String[] each_col = cur_item.trim().split("\\s+");
                String cur_ssr_track_num = each_col[1];
                ArrayList<String> concise_item = new ArrayList<>();
                concise_item.add(each_col[2]);  // longitude
                concise_item.add(each_col[3]);  // latitude
                concise_item.add(each_col[4]);  // height
                concise_item.add(each_col[0]);  // time_stamp
                if(flights.containsKey(cur_ssr_track_num)){
                    flights.get(cur_ssr_track_num).add(concise_item);
                }
                else{
                    ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
                    temp.add(concise_item);
                    flights.put(cur_ssr_track_num, temp);
                }
            }
            return flights;
        }else return null;
    }

    /**
    * @description: 用于判断是否存在该属性
    * @param index:参数位置
    * @return :是否存在
    * @date: 2022/5/3
    */
    public static boolean isExist(int index){
        return index != -1;
    }
    /*
    * 解析ssr数据，从csv文件中读取的数据
    * */

    public static HashMap<String, ArrayList<ArrayList<String>>> parseCivilAviationFile(ArrayList<String> items, int[] dataIndex, String fileName){
        if(items != null) {
            // 用HashMap暂存各个航班的item，只取需要的字段
            HashMap<String, ArrayList<ArrayList<String>>> flights = new HashMap<>();
            for (int r = 1; r < items.size(); ++r) {
                String cur_item = items.get(r);
                String regex = "";
                if (fileName.endsWith(".txt"))regex = "\\s+";
                else if(fileName.endsWith(".csv")) regex = ",";
                else return null;
                String[] each_col = cur_item.trim().split(regex);
                String cur_ssr_track_num = each_col[dataIndex[0]];
                if(each_col[dataIndex[1]].equals("")||each_col[dataIndex[2]].equals("")||each_col[dataIndex[3]].equals("")||each_col[dataIndex[4]].equals(""))continue;
                ArrayList<String> concise_item = new ArrayList<>();
                concise_item.add(isExist(dataIndex[1]) ? each_col[dataIndex[1]]: null);  // longitude
                concise_item.add(isExist(dataIndex[2]) ? each_col[dataIndex[2]]: null);  // latitude
                concise_item.add(isExist(dataIndex[3]) ? each_col[dataIndex[3]]: null);  // height
                concise_item.add(isExist(dataIndex[4]) ? each_col[dataIndex[4]]: null);  // time_stamp
                concise_item.add(isExist(dataIndex[5]) ? each_col[dataIndex[5]]: null);  // heading
                concise_item.add(isExist(dataIndex[6]) ? each_col[dataIndex[6]]: null);  // speed
                if (flights.containsKey(cur_ssr_track_num)) {
                    flights.get(cur_ssr_track_num).add(concise_item);
                } else {
                    ArrayList<ArrayList<String>> temp = new ArrayList<>();
                    temp.add(concise_item);
                    flights.put(cur_ssr_track_num, temp);
                }
            }
            return flights;
        }else return null;
    }

    public static HashMap<String, ArrayList<ArrayList<String>>> parseCivilShipCsv(ArrayList<String> items){
        if(items != null){
            // 用HashMap暂存各个航班的item，只取需要的字段
            HashMap<String, ArrayList<ArrayList<String>>> ships = new HashMap<String, ArrayList<ArrayList<String>>>();
            for(int r = 1; r < items.size(); ++r){
                String cur_item = items.get(r);
                String[] each_col = cur_item.trim().split(",");
                String cur_MMSI = each_col[0];
                ArrayList<String> concise_item = new ArrayList<>();
                concise_item.add(each_col[3]);  // longitude
                concise_item.add(each_col[2]);  // latitude
                concise_item.add(each_col[6]);  // heading
                concise_item.add(each_col[4]);  // speed
                concise_item.add(each_col[1]);  // time_stamp
                if(ships.containsKey(cur_MMSI)){
                    ships.get(cur_MMSI).add(concise_item);
                }
                else{
                    ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
                    temp.add(concise_item);
                    ships.put(cur_MMSI, temp);
                }
            }
            return ships;
        }else return null;
    }

    public static HashMap<String, ArrayList<ArrayList<String>>> parseCivilShipTxt(ArrayList<String> items) {
        return null;
    }
    /**
    * @Description: 输入导入到MongoDB(轨迹库),民航
    * @Param: [items, collectName]: [待导入项, 集合名]
    * @return: java.lang.String
    * @Date:
    */
    public static String importCivilAviationToDB(HashMap<String, ArrayList<ArrayList<String>>> items, String collectName, MongoTemplate mongoTemplate){
        try {
            for(String trackNum: items.keySet()){
                ArrayList<ArrayList<String>> curItem = items.get(trackNum);
                Document trackStorageTable = new Document();
                ArrayList<Document> trackPointItems = new ArrayList<>();

                trackStorageTable.append("plane_id", trackNum);
                for(int j = 0; j < curItem.size(); ++j ){
                    Document trackPointItem = new Document();
                    trackPointItem.append("itemid", j+1);
                    trackPointItem.append("latitude", curItem.get(j).get(0));
                    trackPointItem.append("longitude", curItem.get(j).get(1));
                    trackPointItem.append("height", curItem.get(j).get(2));
                    trackPointItem.append("heading", curItem.get(j).get(4));
                    trackPointItem.append("speed", curItem.get(j).get(5));
                    trackPointItem.append("time_stamp", curItem.get(j).get(3));

                    trackPointItems.add(trackPointItem);
                }
                trackStorageTable.append("track_point_items", trackPointItems);
                mongoTemplate.insert(trackStorageTable, collectName);
            }
            return "import success";
        }catch (Exception e){
            e.printStackTrace();
            return "import failure";
        }
    }

    /**
     * @Description: 输入导入到MongoDB(轨迹库),民船
     * @Param: [items, collectName]: [待导入项, 集合名]
     * @return: java.lang.String
     * @Date:
     */
    public static String importCivilShipToDB(HashMap<String, ArrayList<ArrayList<String>>> items, String collectName, MongoTemplate mongoTemplate){
        try {
            for(String trackNum: items.keySet()){
                ArrayList<ArrayList<String>> curItem = items.get(trackNum);
                Document trackStorageTable = new Document();
                ArrayList<Document> trackPointItems = new ArrayList<>();

                trackStorageTable.append("ship_id", trackNum);
                for(int j = 0; j < curItem.size(); ++j ){
                    Document track_point_item = new Document();
                    track_point_item.append("itemid", j+1);
                    track_point_item.append("longitude", curItem.get(j).get(0));
                    track_point_item.append("latitude", curItem.get(j).get(1));
                    track_point_item.append("heading", curItem.get(j).get(2));
                    track_point_item.append("speed", curItem.get(j).get(3));
                    track_point_item.append("time_stamp", curItem.get(j).get(4));

                    trackPointItems.add(track_point_item);
                }
                trackStorageTable.append("track_point_items", trackPointItems);
                mongoTemplate.insert(trackStorageTable, collectName);
            }
            return "import success";
        }catch (Exception e){
            e.printStackTrace();
            return "import failure";
        }
    }

}
