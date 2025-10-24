package com.techpassport.server.auth.core.dtos;

public record ClientInfoDto
(
    String os,
    String device,
    String ip,
    String browser
) {
    public ClientInfoDto(String os, String device, String ip, String browser) {
        this.os = os == null ? "" : os;
        this.device = device == null ? "" : device;
        this.ip = ip == null ? "" : ip;
        this.browser = browser == null ? "" : browser;
    }
}
