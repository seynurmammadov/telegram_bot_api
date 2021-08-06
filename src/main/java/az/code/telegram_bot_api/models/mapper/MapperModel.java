package az.code.telegram_bot_api.models.mapper;


public interface MapperModel {
    <T, Y> T entityToDTO(Y data, Class<T> tClass);
    <T, Y> T defaultMap(Y data, Class<T> tClass);
}
