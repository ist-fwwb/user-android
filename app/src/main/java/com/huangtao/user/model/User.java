package com.huangtao.user.model;

import com.huangtao.user.model.meta.Type;

import java.util.Arrays;

public class User {
    String _id;
    String enterpriceId;
    String phone;
    String password;
    String name;
    Type type;
    Byte[] faceFile;
    Byte[] featureFile;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public Byte[] getFaceFile() {
        return faceFile;
    }

    public void setFaceFile(Byte[] faceFile) {
        this.faceFile = faceFile;
    }

    public Byte[] getFeatureFile() {
        return featureFile;
    }

    public void setFeatureFile(Byte[] featureFile) {
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
                "_id='" + _id + '\'' +
                ", enterpriceId='" + enterpriceId + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", faceFile=" + Arrays.toString(faceFile) +
                ", featureFile=" + Arrays.toString(featureFile) +
                '}';
    }
}
