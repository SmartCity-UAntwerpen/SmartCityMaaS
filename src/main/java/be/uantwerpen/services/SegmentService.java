package be.uantwerpen.services;

import be.uantwerpen.model.Segment;
import be.uantwerpen.repositories.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Frédéric Melaerts on 2/05/2017.
 */
@Service
public class SegmentService {
    @Autowired
    private SegmentRepository segmentRepository;
    public Iterable<Segment> findAll() {
        return this.segmentRepository.findAll();
    }
    public Segment findOne(Long id) {
        return this.segmentRepository.findOne(id);
    }
}
