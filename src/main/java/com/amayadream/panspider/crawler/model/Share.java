package com.amayadream.panspider.crawler.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 共享实体
 * @author : Amayadream
 * @date :   2017-05-10 11:28
 */
@Document(collection = "share")
public class Share {

    /** 分享id, 主键 */
    @Id
    private String shareId;
    /** 对应短链接 */
    private String shortId;
    /** 名称 */
    private String title;
    /** 描述 */
    private String desc;
    /** 共享时间 */
    private long feedTime;
    /** 分类 */
    private int category;
    /** 文件数 */
    private int fileCount;
    /** 文件夹数 */
    private int dirCount;
    /** 文件列表 */
    private List<ShareFile> files;

    /** uk */
    private String uk;
    /** 用户名 */
    private String userName;
    /** 用户头像 */
    private String avatar;


    public Share(String shareId, String shortId, String title, String desc, long feedTime, int category, int fileCount, int dirCount, String uk, String userName, String avatar) {
        this.shareId = shareId;
        this.shortId = shortId;
        this.title = title;
        this.desc = desc;
        this.feedTime = feedTime;
        this.category = category;
        this.fileCount = fileCount;
        this.dirCount = dirCount;
        this.uk = uk;
        this.userName = userName;
        this.avatar = avatar;
    }


    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getFeedTime() {
        return feedTime;
    }

    public void setFeedTime(long feedTime) {
        this.feedTime = feedTime;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public int getDirCount() {
        return dirCount;
    }

    public void setDirCount(int dirCount) {
        this.dirCount = dirCount;
    }

    public List<ShareFile> getFiles() {
        return files;
    }

    public void setFiles(List<ShareFile> files) {
        this.files = files;
    }

    public String getUk() {
        return uk;
    }

    public void setUk(String uk) {
        this.uk = uk;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
