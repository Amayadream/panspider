package com.amayadream.panspider.crawler.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author :  Amayadream
 * @date :  2017.04.24 22:37
 */
@Document(collection = "hot_uk")
public class HotUk {

    @Id
    private String uk;
    private Boolean hasUsed;

    public HotUk() {

    }

    public HotUk(Boolean hasUsed) {
        this.hasUsed = hasUsed;
    }

    public HotUk(String uk, Boolean hasUsed) {
        this.uk = uk;
        this.hasUsed = hasUsed;
    }

    public String getUk() {
        return uk;
    }

    public void setUk(String uk) {
        this.uk = uk;
    }

    public Boolean getHasUsed() {
        return hasUsed;
    }

    public void setHasUsed(Boolean hasUsed) {
        this.hasUsed = hasUsed;
    }
}
