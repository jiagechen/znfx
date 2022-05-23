package cn.njust.label.main;

import cn.njust.label.main.dto.Line;
import cn.njust.label.main.service.LoadFileService;
import cn.njust.label.main.service.MongoFindPageService;
import cn.njust.label.main.service.impl.LoadFileServiceImpl;
import cn.njust.label.main.service.impl.MongoFindPageServiceImpl;
import com.mongodb.client.ListIndexesIterable;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class LtMainApplicationTests {
    @Autowired
    MongoTemplate mongoTemplate;
    HashMap<Integer , ArrayList<Line>> cluster;
    @Test
    void contextLoads() {
        ListIndexesIterable<Document> clusterList = mongoTemplate.getCollection("Cluster").listIndexes();
        for(Document id: clusterList){
//            cluster = (HashMap<Integer, ArrayList<Line>>) mongoTemplate.findById(id, );
            System.out.println(cluster.toString());
        }
    }
    @Test
    public void testLoad(){
        LoadFileService loadFileService = new LoadFileServiceImpl();
        loadFileService.loadCivilAviationFile("E:\\workspace\\znfx\\资料文件\\states_2022-04-25", new int[]{1, 2, 3, 12, 0, 5, 4});
    }
    @Test
    public void testFindPage(){
        MongoFindPageService findPageService = new MongoFindPageServiceImpl();
        List<Object> page = findPageService.findPage(null, 1, 5);
        System.out.println(page);
    }
}
