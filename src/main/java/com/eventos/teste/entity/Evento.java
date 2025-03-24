package com.eventos.teste.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fila_eventos")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "tipo_evento")
    public String tipoEvento;

    @Column(columnDefinition = "json")
    public String payload;

    public String status;

    @Column(name = "criado_em")
    public Timestamp criadoEm;

    @Column(name = "processado_em")
    public Timestamp processadoEm;
}
