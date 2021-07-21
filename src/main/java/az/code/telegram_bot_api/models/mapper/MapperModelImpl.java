package az.code.telegram_bot_api.models.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;


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
}
