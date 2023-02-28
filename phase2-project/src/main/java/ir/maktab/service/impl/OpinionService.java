package ir.maktab.service.impl;

import ir.maktab.data.model.Expert;
import ir.maktab.data.model.Opinion;
import ir.maktab.data.repository.OpinionRepository;
import ir.maktab.service.OpinionServiceInterface;
import ir.maktab.util.exception.NotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpinionService implements OpinionServiceInterface {
    private final OpinionRepository opinionRepository;
    private final ExpertService expertService;

    public Opinion saveOpinion(Opinion opinion) {
        return opinionRepository.save(opinion);
    }

    public Opinion findById(Long id) {
        return opinionRepository.findById(id).orElseThrow(() -> new NotFound("Not found the opinion"));
    }

    public Opinion update(Opinion opinion) {
        Opinion opinion1 = findById(opinion.getId());
        opinion.setId(opinion1.getId());
        return opinionRepository.save(opinion);
    }

    public void delete(Opinion opinion) {
        Opinion opinion1 = findById(opinion.getId());
        opinion.setId(opinion1.getId());
        opinionRepository.delete(opinion);
    }

    public List<Opinion> showByScore(Long id) {
        Expert expert = expertService.getExpertById(id);
        return opinionRepository.showByScore(expert);
    }
}
