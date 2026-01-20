package vn.id.thongdanghoang.user_service.entities.converters;

import vn.id.thongdanghoang.user_service.entities.OidcProvider;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OidcProviderConverter implements AttributeConverter<OidcProvider, String> {

    @Override
    public String convertToDatabaseColumn(OidcProvider provider) {
        if (provider == null) {
            return null;
        }
        return provider.getValue();
    }

    @Override
    public OidcProvider convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return OidcProvider.fromValue(value);
    }
}
