package com.notification.service.impl;

import com.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendActivationEmail(String email, String name, String subscriptionId) {
        log.info("EmailService: Sending activation email to: {} for subscription: {}", email, subscriptionId);
        log.info("EmailService: Email content: Sayın {}, Aboneliğiniz başarıyla aktif edilmiştir. Abonelik ID: {}", name, subscriptionId);
        // TODO: Gerçek email gönderimi implementasyonu
        log.info("EmailService: Activation email sent successfully to: {} for subscription: {}", email, subscriptionId);
    }

    @Override
    public void sendCancellationByUserEmail(String email, String name, String subscriptionId) {
        log.info("EmailService: Sending cancellation by user email to: {} for subscription: {}", email, subscriptionId);
        log.info("EmailService: Email content: Sayın {}, Abonelik iptal talebiniz alınmıştır. Mevcut abonelik süreniz sonuna kadar hizmetlerimizden yararlanmaya devam edebilirsiniz. Abonelik ID: {}", name, subscriptionId);
        // TODO: Gerçek email gönderimi implementasyonu
        log.info("EmailService: Cancellation by user email sent successfully to: {} for subscription: {}", email, subscriptionId);
    }

    @Override
    public void sendPaymentFailedEmail(String email, String name, String subscriptionId) {
        log.info("EmailService: Sending payment failed email to: {} for subscription: {}", email, subscriptionId);
        log.info("EmailService: Email content: Sayın {}, Ödeme işleminiz başarısız olduğu için aboneliğiniz iptal edilmiştir. Hizmetlerimizden yararlanmaya devam etmek için lütfen ödeme bilgilerinizi güncelleyiniz. Abonelik ID: {}", name, subscriptionId);
        // TODO: Gerçek email gönderimi implementasyonu
        log.info("EmailService: Payment failed email sent successfully to: {} for subscription: {}", email, subscriptionId);
    }

    @Override
    public void sendExpirationEmail(String email, String name, String subscriptionId) {
        log.info("EmailService: Sending expiration email to: {} for subscription: {}", email, subscriptionId);
        log.info("EmailService: Email content: Sayın {}, Aboneliğinizin süresi dolmuştur ve yenilenmemiştir. Aramızdan ayrıldığınız için üzgünüz. Tekrar hizmetlerimizden yararlanmak isterseniz, yeni bir abonelik oluşturabilirsiniz. Abonelik ID: {}", name, subscriptionId);
        // TODO: Gerçek email gönderimi implementasyonu
        log.info("EmailService: Expiration email sent successfully to: {} for subscription: {}", email, subscriptionId);
    }
} 