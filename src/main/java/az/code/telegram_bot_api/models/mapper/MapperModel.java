package az.code.telegram_bot_api.models.mapper;

public interface MapperModel {
    <T, Y> T entityToDTO(Y data, Class<T> tClass);
}
