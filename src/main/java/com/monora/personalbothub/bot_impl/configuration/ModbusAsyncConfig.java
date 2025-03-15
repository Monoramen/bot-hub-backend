package com.monora.personalbothub.bot_impl.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync // Включаем поддержку асинхронных вызовов
public class ModbusAsyncConfig {

    /**
     * Определяем однопоточный исполнитель для Modbus-операций.
     * @return TaskExecutor с одним потоком.
     */
    @Bean(name = "modbusExecutor")
    public TaskExecutor modbusExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);        // Один поток в пуле
        executor.setMaxPoolSize(1);         // Максимум один поток
        executor.setQueueCapacity(500);     // Очередь для 500 задач
        executor.setThreadNamePrefix("Modbus-Async-");
        executor.initialize();
        return executor;
    }
}