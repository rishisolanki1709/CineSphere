package com.cinesphere.main.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import com.cinesphere.main.dto.TheatreResponseDTO;
import com.cinesphere.main.entity.Theatre;
import com.cinesphere.main.repository.TheatreRepository;
import com.cinesphere.main.service.TheatreService;

@Service
public class TheatreServiceImpl implements TheatreService {

	private final TheatreRepository theatreRepository;

	public TheatreServiceImpl(TheatreRepository theatreRepository) {
		this.theatreRepository = theatreRepository;
	}

	@Override
	public TheatreResponseDTO addTheatre(Theatre theatre) {
		Theatre savedTheatre = theatreRepository.save(theatre);
		return mapToDTO(savedTheatre);
	}

	@Override
	public List<TheatreResponseDTO> getTheatresByCity(String city) {
		return theatreRepository.findByCity(city).stream().map(this::mapToDTO).toList();
	}

	private TheatreResponseDTO mapToDTO(Theatre theatre) {
		TheatreResponseDTO dto = new TheatreResponseDTO();
		dto.setId(theatre.getId());
		dto.setName(theatre.getName());
		dto.setCity(theatre.getCity());
		dto.setAddress(theatre.getAddress());
		dto.setActive(theatre.isActive());
		if (theatre.getScreens() != null)
			dto.setScreens(theatre.getScreens().size());
		return dto;
	}

	@Override
	public List<TheatreResponseDTO> getAllTheatres() {
		return theatreRepository.findAll().stream().map(this::mapToDTO).toList();
	}

	@Override
	public void updateStatus(Long id, boolean newStatus) {
		Theatre theatre = theatreRepository.findById(id).orElseThrow(() -> new RuntimeException("Theatre not found with id: " + id));
		theatre.setActive(newStatus);
		theatreRepository.save(theatre);
	}

	@Override
	public void deleteTheatre(Long id) {
		theatreRepository.deleteById(id);
	}

	@Override
	public TheatreResponseDTO updateTheatre(Long id, Theatre theatre) {
		Theatre newTheatre = theatreRepository.findById(id).orElseThrow(() -> new RuntimeException("Theatre not found with id: " + id));
		newTheatre.setAddress(theatre.getAddress());
		newTheatre.setCity(theatre.getCity());
		newTheatre.setName(theatre.getName());
		theatreRepository.save(newTheatre);
		return null;
	}

	@Override
	public TheatreResponseDTO getTheatresById(Long id) {
		Theatre theatre = theatreRepository.findById(id).orElseThrow(() -> new RuntimeException("Theatre not found with id: " + id));;
		TheatreResponseDTO dto = new TheatreResponseDTO();
		dto.setAddress(theatre.getAddress());
		dto.setCity(theatre.getCity());
		dto.setName(theatre.getName());
		return dto;
	}
}
