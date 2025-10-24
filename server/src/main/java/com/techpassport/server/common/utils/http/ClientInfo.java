package com.techpassport.server.common.utils.http;

import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

import com.techpassport.server.auth.core.dtos.ClientInfoDto;

@Service
public class ClientInfo implements IClientInfo
{
    @Override
    public ClientInfoDto getClientInfo(HttpServletRequest request) {
        String ip = extractClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String os = extractOS(userAgent);
        String browser = extractBrowser(userAgent);
        String device = extractDevice(userAgent);

        return new ClientInfoDto(os, device, ip, browser);
    }

    private String extractClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isBlank()) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String extractOS(String userAgent) {
        if (userAgent == null) return "";
        if (userAgent.toLowerCase().contains("windows")) return "Windows";
        if (userAgent.toLowerCase().contains("mac")) return "Mac";
        if (userAgent.toLowerCase().contains("x11")) return "Unix";
        if (userAgent.toLowerCase().contains("android")) return "Android";
        if (userAgent.toLowerCase().contains("iphone")) return "iOS";
        return "Unknown";
    }

    private String extractBrowser(String userAgent) {
        if (userAgent == null) return "";
        String ua = userAgent.toLowerCase();
        if (ua.contains("chrome") && !ua.contains("edge")) return "Chrome";
        if (ua.contains("firefox")) return "Firefox";
        if (ua.contains("safari") && !ua.contains("chrome")) return "Safari";
        if (ua.contains("edge")) return "Edge";
        if (ua.contains("msie") || ua.contains("trident")) return "Internet Explorer";
        return "Unknown";
    }

    private String extractDevice(String userAgent) {
        if (userAgent == null) return "";
        String ua = userAgent.toLowerCase();
        if (ua.contains("mobile")) return "Mobile";
        if (ua.contains("tablet")) return "Tablet";
        return "Desktop";
    }
}
