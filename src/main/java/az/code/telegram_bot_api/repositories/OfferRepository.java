package az.code.telegram_bot_api.repositories;

import az.code.telegram_bot_api.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}
