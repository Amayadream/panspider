package com.amayadream.panspider.crawler.model;

/**
 * 共享文件/文件夹实体
 * @author : Amayadream
 * @date :   2017-05-10 11:45
 */
public class ShareFile {

    private String fileName;
    private int category;
    private long size;
    private String md5;
    private String sign;
    private int isDir;
    private long createAt;

    public ShareFile(String fileName, int category, long size, String md5, String sign, int isDir, long createAt) {
        this.fileName = fileName;
        this.category = category;
        this.size = size;
        this.md5 = md5;
        this.sign = sign;
        this.isDir = isDir;
        this.createAt = createAt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getIsDir() {
        return isDir;
    }

    public void setIsDir(int isDir) {
        this.isDir = isDir;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }
}
