package be.uantwerpen.formatter;

import be.uantwerpen.model.Segment;
import be.uantwerpen.repositories.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import java.text.ParseException;

import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Created by Frédéric Melaerts on 2/05/2017.
 */
@Component
public class SegmentFormatter implements Formatter<Segment> {

    @Autowired
    private SegmentRepository segmentRepository;

    public Segment parse(final String text, final Locale locale) throws ParseException {
        if (text != null && !text.isEmpty())
            return segmentRepository.findOne(new Long(text));
        else return null;
    }

    public String print(final Segment object, final Locale locale) {
        return (object != null ? object.getId().toString() : "");
    }
}
