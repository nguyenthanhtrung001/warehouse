package com.example.goodsservice.dto;

public class Bath {
    Long id;
    String nameBath;

    public String getNameBath() {
        return nameBath;
    }

    public void setNameBath(String nameBath) {
        this.nameBath = nameBath;
    }

    public Bath() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
