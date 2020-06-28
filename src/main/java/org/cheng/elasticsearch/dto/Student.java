package org.cheng.elasticsearch.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import lombok.Data;

@Data
@Document(indexName = "stu", type = "_doc")
public class Student {
 
    @Id
    private Long stuId;
 
    @Field(store = true)
    private String name;
 
    @Field(store = true)
    private Integer age;
    
    @Field(store = true)
    private Float money;
    
    @Field(store = true)
    private String sign;

    @Field(store = true)
    private String description;
}