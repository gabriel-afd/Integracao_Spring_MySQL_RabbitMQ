package com.eventos.teste.service;
import com.eventos.teste.entity.Evento;
import com.eventos.teste.repository.EventoRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class EventoService {

    private final EventoRepository repo;
    private final RabbitTemplate rabbitTemplate;
    @Value("${fila.rabbitmq.queue}")
    private final String nomeFila;

    public EventoService(EventoRepository repo, RabbitTemplate rabbitTemplate, @Value("${fila.rabbitmq.queue}") String nomeFila) {
        this.repo = repo;
        this.rabbitTemplate = rabbitTemplate;
        this.nomeFila = nomeFila;
    }


    @Scheduled(fixedDelay = 10000)
    public void processarEventosPendentes(){
        List<Evento> eventos = repo.findByStatus("pendente");

        System.out.println("Buscando eventos com status 'pendente': " + eventos.size());

        for(Evento evento : eventos){
            try {
                System.out.println("Enviando evento " + evento.getId() + " para a fila: " + nomeFila);
                rabbitTemplate.convertAndSend(nomeFila,evento.payload);
                System.out.println("Enviado para RabbitMQ: " + evento.getPayload());

                evento.setStatus("processado");
                evento.setProcessadoEm(Timestamp.from(Instant.now()));
                repo.save(evento);
                System.out.println("Evento enviado " + evento.id);
            }catch (Exception e){
                System.err.println("Falha ao enviar para fila: Evento " + evento.getId() + ", erro: " + e.getMessage());
            }
        }
    }

}
