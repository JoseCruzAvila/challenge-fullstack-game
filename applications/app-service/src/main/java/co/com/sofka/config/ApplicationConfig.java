package co.com.sofka.config;

import co.com.sofka.generic.events.DomainEvent;
import co.com.sofka.generic.usecase.UseCase;
import co.com.sofka.model.events.GameStarted;
import co.com.sofka.model.game.Game;
import co.com.sofka.usecase.splitcards.SplitCardsUseCase;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
public class ApplicationConfig {

    @Bean
    public Exchange exchange() {
        return new FanoutExchange("fullstack.game");
    }

    @Bean
    public Flux<UseCase.UseCaseWrap> useCasesForListener(
            SplitCardsUseCase splitCardsUseCase

    ) {
        return Flux.just(new UseCase.UseCaseWrap(splitCardsUseCase, "game.GameStarted"));
    }

    @Bean
    public Flux<DomainEvent> domainEvents() {
        return Flux.just(new GameStarted(new Game()));
    }
}
