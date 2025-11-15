package com.flashcards.server.common.utils.http.request;

import com.flashcards.server.common.dtos.ClientInfoDto;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.IOException;

@Service
public class HttpRequestInfo implements IHttpRequestInfo {

    private final Parser uaParser;

    public HttpRequestInfo() throws IOException {
        this.uaParser = new Parser();
    }

    @Override
    public ClientInfoDto getClientInfo(HttpServletRequest request) {
        var ip = extractClientIp(request);
        var userAgentHeader = request.getHeader("User-Agent");

        Client client = uaParser.parse(userAgentHeader != null ? userAgentHeader : "");

        String os = client.os.family + (client.os.major != null ? " " + client.os.major : "");
        String browser = client.userAgent.family +
                (client.userAgent.major != null ? " " + client.userAgent.major : "");
        String device = client.device.family;

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
