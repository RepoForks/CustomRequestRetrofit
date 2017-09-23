package com.ahmadrosid.customrequestretrofit.api;

import java.util.List;

/**
 * Created by ocittwo on 9/23/17.
 */

public class ResponseModel {

    /**
     * id : 1
     * key : [{"nama":"Ahmad Rosid","alamat":"Yogyakarta"},{"nama":"Putri Indah Nuraini","alamat":"Lampung Timur"}]
     */

    private String id;
    private List<KeyBean> key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<KeyBean> getKey() {
        return key;
    }

    public void setKey(List<KeyBean> key) {
        this.key = key;
    }

    public static class KeyBean {
        /**
         * nama : Ahmad Rosid
         * alamat : Yogyakarta
         */

        private String nama;
        private String alamat;

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getAlamat() {
            return alamat;
        }

        public void setAlamat(String alamat) {
            this.alamat = alamat;
        }
    }
}
