package com.belanjaki.id.jwt;

import com.belanjaki.id.common.ResourceLabel;
import com.belanjaki.id.common.constant.RoleEnum;
import com.belanjaki.id.common.exception.ResourceNotFoundException;
import com.belanjaki.id.usersmanagement.model.MstRole;
import com.belanjaki.id.usersmanagement.repository.MstRoleRepository;
import com.belanjaki.id.usersmanagement.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final JWTUtils jwtUtils;
    private final MstRoleRepository mstRoleRepository;
    private final ResourceLabel resourceLabel;
    private final Set<String> permittedPathsExceptAdmin;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        String roleID = null;
        String roleName = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtils.extractUsername(jwt);
            roleID = jwtUtils.getAppIdFromJwt(jwt);

            Optional<MstRole> mstRole = mstRoleRepository.findById(UUID.fromString(roleID));
            if (mstRole.isPresent()){
                roleName = mstRole.get().getRoleName();
            }else {
                throw new ResourceNotFoundException(resourceLabel.getBodyLabel("invalid.crediantial"));
            }

            // validasi request auth selain user admin, tidak bisa akses endpoint permittedPathExceptAdmin
            if (permittedPathsExceptAdmin.contains(request.getRequestURI()) && !roleName.contains(RoleEnum.ADMIN.getRoleName())) {
                jwtUtils.generateResponseAccessDenied(response);
                return;
            }


        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.authService.loadUserByUsernameRole(username, roleName);
            if (jwtUtils.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
