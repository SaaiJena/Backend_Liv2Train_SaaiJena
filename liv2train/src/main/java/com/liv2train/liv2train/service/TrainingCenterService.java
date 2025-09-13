package com.liv2train.liv2train.service;

import com.liv2train.liv2train.dto.TrainingCenterRequest;
import com.liv2train.liv2train.dto.TrainingCenterResponse;
import com.liv2train.liv2train.exception.DuplicateResourceException;
import com.liv2train.liv2train.model.TrainingCenter;
import com.liv2train.liv2train.repository.TrainingCenterRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class TrainingCenterService {

    private final TrainingCenterRepository repository;

    public TrainingCenterService(TrainingCenterRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TrainingCenterResponse create(TrainingCenterRequest req) {
        if (repository.existsByCenterCode(req.getCenterCode())) {
            throw new DuplicateResourceException("centerCode already exists: " + req.getCenterCode());
        }

        TrainingCenter entity = new TrainingCenter();
        entity.setCenterName(req.getCenterName());
        entity.setCenterCode(req.getCenterCode());
        entity.setAddress(req.getAddress());
        entity.setStudentCapacity(req.getStudentCapacity());
        entity.setCoursesOffered(req.getCoursesOffered());
        entity.setCreatedOn(Instant.now().toEpochMilli()); // server timestamp
        entity.setContactEmail(req.getContactEmail());
        entity.setContactPhone(req.getContactPhone());

        return toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<TrainingCenterResponse> listAll(String city, String state, String pincode, String course, String code) {
        Specification<TrainingCenter> spec = Specification.where(null);

        if (city != null && !city.isBlank())    spec = spec.and((r, q, cb) -> cb.equal(r.get("address").get("city"), city));
        if (state != null && !state.isBlank())  spec = spec.and((r, q, cb) -> cb.equal(r.get("address").get("state"), state));
        if (pincode != null && !pincode.isBlank()) spec = spec.and((r, q, cb) -> cb.equal(r.get("address").get("pincode"), pincode));
        if (code != null && !code.isBlank())    spec = spec.and((r, q, cb) -> cb.equal(r.get("centerCode"), code));
        if (course != null && !course.isBlank()) spec = spec.and((r, q, cb) -> cb.equal(r.join("coursesOffered"), course));

        return repository.findAll(spec).stream().map(this::toResponse).toList();
    }

    private TrainingCenterResponse toResponse(TrainingCenter tc) {
        return new TrainingCenterResponse(
                tc.getId(), tc.getCenterName(), tc.getCenterCode(),
                tc.getAddress(), tc.getStudentCapacity(), tc.getCoursesOffered(),
                tc.getCreatedOn(), tc.getContactEmail(), tc.getContactPhone()
        );
    }
}
