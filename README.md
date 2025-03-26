# 📨 Projeto Fila de Eventos com Spring Boot, RabbitMQ e MySQL

Este projeto tem como objetivo consumir eventos pendentes de uma tabela no MySQL (`fila_eventos`), processá-los e enviá-los para uma fila do RabbitMQ. Após o envio, o evento é marcado como processado no banco de dados.

---

## 🚀 Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring JDBC
- Spring AMQP (RabbitMQ)
- RabbitMQ
- MySQL
- Lombok

---

## 🛠️ Funcionalidades

- Leitura de eventos com status `PENDENTE` no banco de dados
- Envio dos eventos para uma fila RabbitMQ (`fila_eventos`)
- Atualização do status para `PROCESSADO` após envio
- Configuração automática da fila com Spring AMQP

---

## 🗃️ Estrutura da Tabela MySQL

### Criação da Tabela `vendas`

```sql
CREATE TABLE vendas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data_venda DATE NOT NULL,
    cpf_cliente VARCHAR(14) NOT NULL,
    matricula_vendedor VARCHAR(5) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (cpf_cliente) REFERENCES clientes(cpf),
    FOREIGN KEY (matricula_vendedor) REFERENCES vendedores(matricula)
);


### Criação da Tabela `fila_eventos`

CREATE TABLE fila_eventos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_evento VARCHAR(255),
    payload TEXT,
    status VARCHAR(50) DEFAULT 'PENDENTE',
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processado_em TIMESTAMP
);


### Criação da Trigger tg_venda_nova_evento
DELIMITER //

CREATE TRIGGER tg_venda_nova_evento 
AFTER INSERT ON vendas
FOR EACH ROW
BEGIN
    INSERT INTO fila_eventos (tipo_evento, payload, status) 
    VALUES (
        'Venda Criada', 
        JSON_OBJECT(
            'id_venda', NEW.id,
            'data_venda', NEW.data_venda,
            'cpf_cliente', NEW.cpf_cliente,
            'matricula_vendedor', NEW.matricula_vendedor,
            'valor_total', NEW.valor_total
        ),
        'pendente'
    );
END//

DELIMITER ;

### Inserindo Valores
INSERT INTO vendas (data_venda, cpf_cliente, matricula_vendedor, valor_total)
VALUES ('2025-04-22', '122.977.174-31', 'V004', 100.90);

SELECT * FROM fila_eventos;

---

## 📦 Configuração da Fila

```java
@Bean
public Queue filaEventos() {
    return new Queue("fila_eventos", true); // Fila durável
}
```
---

## ⚙️ Configurações

### `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/seubanco
spring.datasource.username=root
spring.datasource.password=senha

spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

---

## 🧪 Testando a Fila

1. Insira um registro no banco com status `PENDENTE`
2. Execute a aplicação
3. Verifique se a mensagem chegou no RabbitMQ (fila `fila_eventos`)
4. Confira se o status foi atualizado para `PROCESSADO`

---

## 🎥 Demonstração do Projeto

Confira o vídeo demonstrando o funcionamento do projeto no YouTube:

🔗 [Assista aqui diretamente no YouTube](https://www.youtube.com/watch?v=A6yoq7OvE0I&ab_channel=GabrielM)



## 👨‍💻 Autor

Desenvolvido com 💻 por [Gabriel Medeiros]

---
