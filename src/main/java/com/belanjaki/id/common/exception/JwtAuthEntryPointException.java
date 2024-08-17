package com.belanjaki.id.common.exception;

import com.belanjaki.id.common.constant.ReturnCode;
import com.belanjaki.id.common.dto.BaseResponse;
import com.belanjaki.id.common.dto.ErrorObjectDTO;
import com.belanjaki.id.common.dto.Meta;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthEntryPointException implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);

        ErrorObjectDTO dto = new ErrorObjectDTO();
        dto.setMessage(ReturnCode.UNAUTHORIZED.getMessage());
        dto.setStatusCode(ReturnCode.UNAUTHORIZED.getStatusCode());
        dto.setTimeStamp(new Date());

        BaseResponse<ErrorObjectDTO> baseResponse = new BaseResponse<>(dto, new Meta(ReturnCode.UNAUTHORIZED.getStatusCode(), HttpStatus.UNAUTHORIZED.getReasonPhrase()));

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), baseResponse.getCustomizeResponse("unauthorized"));
    }

}
