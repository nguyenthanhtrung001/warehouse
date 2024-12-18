package com.devteria.identity.dto.response;

public class WebInfoDTO {
    private String name;
    private String iconUrl;

    // Constructor
    public WebInfoDTO(String name, String iconUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
