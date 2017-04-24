package com.amayadream.panspider.crawler.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author :  Amayadream
 * @date :  2017.04.24 22:37
 */
@Document(collection = "hot_uk")
public class HotUk {

    /** uk, 唯一标识 */
    @Id
    private String uk;
    /** 创建时间 */
    private Date createdAt;
    /** 最后更新时间 */
    private Date updatedAt;

    public HotUk() {

    }

    public HotUk(Date createdAt, Date updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getUk() {
        return uk;
    }

    public void setUk(String uk) {
        this.uk = uk;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
