package az.code.telegram_bot_api.repositories;

import az.code.telegram_bot_api.models.AcceptedOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcceptedOfferRepository  extends JpaRepository<AcceptedOffer, Long> {
}