package com.uelisson.cartoesService.dto;

public class AuthResponseDTO {
    private String jwt;
    private String tipoUsuario;
    private Long usuarioId;

    public AuthResponseDTO(String jwt, String tipoUsuario, Long usuarioId) {
        this.jwt = jwt;
        this.tipoUsuario = tipoUsuario;
        this.usuarioId = usuarioId;
    }
    public AuthResponseDTO(String jwt) {
        this.jwt = jwt;
    }



    public String getJwt() { return jwt; }
    public void setJwt(String jwt) { this.jwt = jwt; }
    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
