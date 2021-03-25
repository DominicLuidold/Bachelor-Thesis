package at.fhv.dlu9576.vaadin.general;

import com.vaadin.flow.i18n.I18NProvider;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Code based on https://vaadin.com/docs/latest/flow/advanced/i18n-localization/#provider-sample-for-translation
 */
@Component
public class TranslationProvider implements I18NProvider {
    private static final Logger LOG = LoggerFactory.getLogger(TranslationProvider.class);
    private static final long serialVersionUID = 2923317579309660156L;

    public static final String BUNDLE_PREFIX = "translation";
    public final Locale LOCALE_DE = new Locale("de", "AT");
    public final Locale LOCALE_EN = new Locale("en", "GB");

    private final List<Locale> locales = List.of(LOCALE_DE, LOCALE_EN);

    @Override
    public List<Locale> getProvidedLocales() {
        return locales;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (null == key) {
            LOG.warn("Received a translation request for key with null value!");
            return "";
        }

        final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale);

        String value;
        try {
            value = bundle.getString(key);
        } catch (final MissingResourceException e) {
            LOG.warn("Missing resource", e);
            return "!" + locale.getLanguage() + ": " + key;
        }
        if (params.length > 0) {
            value = MessageFormat.format(value, params);
        }

        return value;
    }
}
