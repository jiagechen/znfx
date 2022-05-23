package cn.njust.label.main.utils;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDBUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBUtil.class);
    private static String mongo_server_address = "localhost";   // 服务器地址
    private static int mongo_server_port = 27017;   // 端口号
//    private static String database_name = "znfx1";   // 数据库名称
    private static MongoClient mongoClient = null;
    private static MongoDatabase mongoDatabase = null;

//    public static MongoDatabase connectMongoDB(String databaseName){
//        try {
//            mongoClient =  new MongoClient(mongo_server_address, mongo_server_port);
//            mongoDatabase =  mongoClient.getDatabase(databaseName);
//            return mongoDatabase;
//        }catch (Exception e){
//            e.printStackTrace();
//            LOGGER.info("连接MongoDB出现异常");
//            LOGGER.info(e.toString());
//            return  null;
//        }
//    }

//    public static void main(String[] args) {
//        MongoDatabase mongoDBUtil = MongoDBUtil.connectMongoDB("znfx");
//        ListIndexesIterable<Document> clusterList = mongoDBUtil.getCollection("Cluster").listIndexes();
//        for(Document id: clusterList){
//            System.out.println(id.toString());
//            break;
//        }
//    }

}
