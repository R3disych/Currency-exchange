package currencyexchange.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendAsJson(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            objectMapper.writeValue(out, obj);
        }
    }
}
