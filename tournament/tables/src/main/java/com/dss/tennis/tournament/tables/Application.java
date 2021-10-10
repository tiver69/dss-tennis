package com.dss.tennis.tournament.tables;

import com.dss.tennis.tournament.tables.convertor.SequenceConverter;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.request.CreatePlayer;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addConverter(new SequenceConverter<CreatePlayer, PlayerDTO>(modelMapper, PlayerDTO.class));
		return modelMapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
