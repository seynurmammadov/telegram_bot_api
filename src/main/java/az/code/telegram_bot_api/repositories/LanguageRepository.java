package az.code.telegram_bot_api.repositories;

import az.code.telegram_bot_api.models.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Language getByKeyword(String keyword);
}
