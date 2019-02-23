package com.huangtao.user.model;

import com.huangtao.user.helper.ChineseUtils;
import com.huangtao.user.model.meta.Type;

import java.io.Serializable;

public class User implements Comparable<User>, Serializable {
    String id;
    String enterpriceId;
    String phone;
    String password;
    String name;
    Type type;
    String faceFile;
    String featureFile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getFaceFile() {
        return faceFile;
    }

    public void setFaceFile(String faceFile) {
        this.faceFile = faceFile;
    }

    public String getFeatureFile() {
        return featureFile;
    }

    public void setFeatureFile(String featureFile) {
        this.featureFile = featureFile;
    }

    public String getEnterpriceId() {
        return enterpriceId;
    }

    public void setEnterpriceId(String enterpriceId) {
        this.enterpriceId = enterpriceId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", enterpriceId='" + enterpriceId + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", faceFile='" + faceFile + '\'' +
                ", featureFile='" + featureFile + '\'' +
                '}';
    }

    public String getHeaderChar() {
        String header = ChineseUtils.getUpperCase(name, false).substring(0, 1);
        if (header.charAt(0) >= 'A' && header.charAt(0) <= 'Z')
            return header;
        return "#";
    }

    @Override
    public int compareTo(User o) {
        if (getHeaderChar().charAt(0) < o.getHeaderChar().charAt(0)) {
            return -1;
        } else if (getHeaderChar().charAt(0) == o.getHeaderChar().charAt(0)) {
            return 0;
        }
        return 1;
    }
}
