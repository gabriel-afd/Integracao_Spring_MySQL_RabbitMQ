package com.eventos.teste.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TesteController {

    private final RabbitTemplate rabbitTemplate;

    @Value("${fila.rabbitmq.queue}")
    private String nomeFila;

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarMsg(){
        String mensagem = "{\"mensagem\":\"teste manual de criacao de fila\"}";
        rabbitTemplate.convertAndSend(nomeFila, mensagem);
        return ResponseEntity.ok("Mensagem enviada para fila: " + nomeFila);
    }
}
