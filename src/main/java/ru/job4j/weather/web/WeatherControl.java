package ru.job4j.weather.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.job4j.weather.service.WeatherService;
import ru.job4j.weather.model.Weather;

import java.time.Duration;

@RestController
public class WeatherControl {
    @Autowired
    private final WeatherService weathers;

    public WeatherControl(WeatherService weathers) {
        this.weathers = weathers;
    }

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> findAll() {
        return fluxWith(weathers.findAll());
    }

    @GetMapping(value = "/hottest", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> findAllWithMaxTemp() {
        return fluxWith(weathers.findAllWithMaxTemp());
    }

    @GetMapping(value = "/cityGreatThen/{temp}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> findAllByTemp(@PathVariable Integer temp) {
        return fluxWith(weathers.findAllByTemp(temp));
    }

    @GetMapping(value = "/get/{id}")
    public Mono<Weather> get(@PathVariable Integer id) {
        return weathers.findById(id);
    }

    private Flux<Weather> fluxWith(Flux<Weather> data) {
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }
}
