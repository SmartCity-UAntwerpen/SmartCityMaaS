package be.uantwerpen.formatter;

import be.uantwerpen.model.Role;
import be.uantwerpen.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Frédéric Melaerts on 27/04/2017.
 */
@Component
public class RoleFormatter implements Formatter<Role> {

    @Autowired
    private RoleRepository roleRepository;

    public Role parse(final String text, final Locale locale) throws ParseException {
        if (text != null && !text.isEmpty())
            return roleRepository.findOne(new Long(text));
        else return null;
    }

    public String print(final Role object, final Locale locale) {
        return (object != null ? object.getId().toString() : "");
    }
}
