package az.code.telegram_bot_api.models.mapper;

import az.code.telegram_bot_api.models.Language;
import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.models.DTOs.RequestDTO;

public interface MapperModel {
    <T, Y> T entityToDTO(Y data, Class<T> tClass);
    <T, Y> T defaultMap(Y data, Class<T> tClass);
    Request requestDTOtoRequest(RequestDTO requestDTO, Language language);
}
