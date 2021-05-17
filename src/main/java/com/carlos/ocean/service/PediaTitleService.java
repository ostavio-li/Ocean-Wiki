package com.carlos.ocean.service;

import com.carlos.ocean.pojo.PediaTitle;
import com.carlos.ocean.repository.PediaTitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author EdwardLee
 * @date 2021/5/11
 */

@Service
public class PediaTitleService {

    private PediaTitleRepository repository;

    @Autowired
    public void setRepository(PediaTitleRepository repository) {
        this.repository = repository;
    }

    public PediaTitle savePediaTitle(PediaTitle pediaTitle) {
        return repository.save(pediaTitle);
    }

    public List<PediaTitle> listPediaTitle() {
        return repository.findAll();
    }

    @Transactional
    public void deletePediaTitle(String title) {
        repository.deleteAllByTitle(title);
    }

    @Transactional
    public void clearAll() {
        repository.deleteAll();
    }

}
