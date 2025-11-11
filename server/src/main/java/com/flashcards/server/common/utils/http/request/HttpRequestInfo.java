package com.flashcards.server.common.utils.http.request;

import com.flashcards.server.common.dtos.ClientInfoDto;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class HttpRequestInfo implements IHttpRequestInfo {

    private final UserAgentAnalyzer userAgentAnalyzer;

    public HttpRequestInfo() {
        this.userAgentAnalyzer = UserAgentAnalyzer
                .newBuilder()
                .withField("OperatingSystemNameVersion")
                .withField("AgentNameVersion")
                .withField("DeviceClass")
                .build();
    }

    @Override
    public ClientInfoDto getClientInfo(HttpServletRequest request) {
        var ip = extractClientIp(request);
        var userAgentHeader = request.getHeader("User-Agent");

        UserAgent agent = userAgentAnalyzer.parse(userAgentHeader);

        var os = agent.getValue("OperatingSystemNameVersion");
        var browser = agent.getValue("AgentNameVersion");
        var device = agent.getValue("DeviceClass");

        return new ClientInfoDto(os, device, ip, browser);
    }

    private String extractClientIp(HttpServletRequest request) {
        var xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isBlank()) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}