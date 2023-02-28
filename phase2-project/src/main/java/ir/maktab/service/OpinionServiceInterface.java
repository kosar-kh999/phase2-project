package ir.maktab.service;

import ir.maktab.data.model.Opinion;

import java.util.List;

public interface OpinionServiceInterface {

    Opinion saveOpinion(Opinion opinion);

    Opinion findById(Long id);

    Opinion update(Opinion opinion);

    void delete(Opinion opinion);

    List<Opinion> showByScore(Long id);
}
