package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.configs.RabbitMQConfig;
import az.code.telegram_bot_api.models.AgencyOffer;
import az.code.telegram_bot_api.models.DTOs.OfferDTO;
import az.code.telegram_bot_api.models.DTOs.UserTokenDTO;
import az.code.telegram_bot_api.models.Offer;
import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import az.code.telegram_bot_api.models.mapper.MapperModel;
import az.code.telegram_bot_api.repositories.OfferRepository;
import az.code.telegram_bot_api.services.interfaces.OfferService;
import az.code.telegram_bot_api.services.interfaces.RequestService;
import az.code.telegram_bot_api.utils.ConverterUtil;
import az.code.telegram_bot_api.utils.TimeUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OfferServiceImpl implements OfferService {

    final
    OfferRepository offerRepository;
    final
    RequestService requestService;
    final
    RabbitTemplate template;
    final
    ConverterUtil converterUtil;
    final
    TimeUtil timeUtil;

    @Value("${offerTemplate.path}")
    String templatePath;

    final
    MapperModel mapperModel;

    public OfferServiceImpl(OfferRepository offerRepository, RequestService requestService,
                            ConverterUtil converterUtil, RabbitTemplate template,
                            MapperModel mapperModel, TimeUtil timeUtil) {
        this.offerRepository = offerRepository;
        this.requestService = requestService;
        this.converterUtil = converterUtil;
        this.template = template;
        this.mapperModel = mapperModel;
        this.timeUtil = timeUtil;
    }

    @Override
    public HttpStatus sendOffer(UserTokenDTO userTokenDTO, Long userRequestId, OfferDTO offerDTO) throws IOException {
        timeUtil.isWorkTime();
        UserRequest userRequest = requestService.getForOffer(userTokenDTO.getUsername(), userRequestId);
        Offer offer = mapperModel.defaultMap(offerDTO, Offer.class);
        offer.setUserRequest(userRequest);
        userRequest.setOffer(offer);
        userRequest.setRequestStatus(RequestStatus.OFFER_MADE);
        offerRepository.save(offer);
        requestService.save(userRequest);
        return sendOffer(userRequest);
    }


    private HttpStatus sendOffer(UserRequest userRequest) throws IOException {
        byte[] offerImage = converterUtil.htmlToImage(templatePath,
                userRequest.getOffer(),
                userRequest.getUser().getCompany_name());
        template.convertAndSend(
                RabbitMQConfig.exchange,
                RabbitMQConfig.offered,
                AgencyOffer.builder()
                        .UUID(userRequest.getRequest().getUUID())
                        .file(offerImage)
                        .username(userRequest.getUser().getUsername())
                        .build());
        return HttpStatus.OK;
    }


}
