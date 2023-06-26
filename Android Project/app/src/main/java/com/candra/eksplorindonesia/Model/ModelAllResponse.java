package com.candra.eksplorindonesia.Model;

import java.util.List;

public class ModelAllResponse
{
    private String kode, pesan;
    private List<ModelKuliner> dataKuliner;
    private List<ModelUser> dataUser;
    private List<ModelWisata> dataWisata;
    private List<ModelScanWisata> dataScanWisata;
    private List<ModelScanKuliner> dataScanKuliner;
    private List<ModelScanUser> dataScanUser;

    public String getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public List<ModelKuliner> getDataKuliner() {
        return dataKuliner;
    }

    public List<ModelUser> getDataUser() {
        return dataUser;
    }

    public List<ModelWisata> getDataWisata() {
        return dataWisata;
    }

    public List<ModelScanWisata> getDataScanWisata() {
        return dataScanWisata;
    }

    public List<ModelScanKuliner> getDataScanKuliner() {
        return dataScanKuliner;
    }
    public List<ModelScanUser> getDataScanUser() {
        return dataScanUser;
    }
}
