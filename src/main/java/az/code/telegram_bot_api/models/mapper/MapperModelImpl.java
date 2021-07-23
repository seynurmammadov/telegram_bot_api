package az.code.telegram_bot_api.models.mapper;

import az.code.telegram_bot_api.models.Language;
import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.models.DTOs.RequestDTO;
import az.code.telegram_bot_api.models.enums.AddressType;
import az.code.telegram_bot_api.models.enums.TourType;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Component
public class MapperModelImpl implements MapperModel {
    private final ModelMapper modelMapper;

    public MapperModelImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public <T, Y> T entityToDTO(Y data, Class<T> entityClass) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(data, entityClass);
    }

    @Override
    public <T, Y> T defaultMap(Y data, Class<T> entityClass) {
        return modelMapper.map(data, entityClass);
    }

    @Override
    public Request requestDTOtoRequest(RequestDTO request, Language language, LocalDateTime experationDate) {

        return Request.builder()
                .language(language)
                .isExpired(false)
                .travelStartDate(LocalDate.parse(request.getTravelStartDate()))
                .travelEndDate(LocalDate.parse(request.getTravelEndDate()))
                .UUID(request.getUUID())
                .tourType(TourType.valueOfTour(request.getTourType()))
                .addressFrom(request.getAddressFrom())
                .addressToUser(request.getAddressToUser())
                .addressToType(AddressType.valueOfAddress(request.getAddressToType()))
                .travellerCount(request.getTravellerCount())
                .budget(request.getBudget())
                .createdAt(LocalDateTime.now())
                .experationDate(experationDate)
                .build();
    }
}
