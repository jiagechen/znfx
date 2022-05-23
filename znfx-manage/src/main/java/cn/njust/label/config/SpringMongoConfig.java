/*
package cn.njust.label.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoDatabaseFactorySupport;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

*/
/*
* 解决mongotemplate添加数据时，自动添加_class字段的问题
* *//*

@Configuration
public class SpringMongoConfig {
    // 通过自动注入来获取默认的配置类
    @Autowired
    private MongoDatabaseFactorySupport mongoDatabaseFactory;
    @Autowired
    private MappingMongoConverter mappingMongoConverter;
    @Bean
    public MongoTemplate mongoTemplate() {
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDatabaseFactory, mappingMongoConverter);
        return mongoTemplate;
    }
}
*/
