package org.cheng.elasticsearch;

import java.util.HashMap;
import java.util.Map;

import org.cheng.elasticsearch.dto.Student;
import org.elasticsearch.action.index.IndexRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;

public class ESTest extends  ElasticsearchApplicationTest {
	 
    @Autowired
    private ElasticsearchTemplate esTemplate;
 
    //创建索引
    @Test
    public void createIndex(){
        Student stu = new Student();
        stu.setStuId(1000L);
        stu.setName("Nick");
        stu.setAge(20);
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();
        esTemplate.index(indexQuery);
    }
    
    @Test
    public void deleteIndex(){
        esTemplate.deleteIndex(Student.class);
    }
    
    @Test
    public void updateStuDoc(){
        Map<String,Object> source = new HashMap<>();
        source.put("sign","I am not a english teacher");
        source.put("money",88.8f);
        source.put("age","100");
     
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.source(source);
        UpdateQuery query = new UpdateQueryBuilder().withClass(Student.class)
                .withId("1000")
                .withIndexRequest(indexRequest)
                .build();
        esTemplate.update(query);
    }
    
    @Test
    public void getStuInfo(){
        GetQuery getQuery = new GetQuery();
        getQuery.setId("1000");
        Student student = esTemplate.queryForObject(getQuery, Student.class);
        System.out.println(student);
    }
    
    @Test
    public void deleteStuDoc(){
       esTemplate.delete(Student.class,"1000");
     
    }
}