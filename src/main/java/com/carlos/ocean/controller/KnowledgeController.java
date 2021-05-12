package com.carlos.ocean.controller;

import com.carlos.ocean.pojo.PediaTitle;
import com.carlos.ocean.service.KGService;
import com.carlos.ocean.service.PediaTitleService;
import com.carlos.util.kg.KGUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author EdwardLee
 * @date 2021/3/30
 */

@RestController
@RequestMapping("/kg")
public class KnowledgeController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private KGService service;
    private PediaTitleService pediaTitleService;

    @Autowired
    public void setService(KGService service) {
        this.service = service;
    }

    @Autowired
    public void setPediaTitleService(PediaTitleService pediaTitleService) {
        this.pediaTitleService = pediaTitleService;
    }

    @DeleteMapping("")
    public ResponseEntity<Void> clear() {
        KGUtil.getInstance().clear();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Void> buildKg(
            @RequestParam("title") String kgTitle,
            @RequestParam("recurr") int recurrence
    ) {
        logger.info("Recurrence: " + recurrence);
        System.out.println("KGTitle: " + kgTitle);
        KGUtil.getInstance().build(kgTitle, recurrence);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/sim")
    public ResponseEntity<Void> updateSimilarity(
            @RequestParam("similarity") double similarity
    ) {
        if (service.updateSimilarity(similarity)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/sim")
    public ResponseEntity<Double> getSimilarity() {
        return ResponseEntity.ok(service.getSimilarity());
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchForAnswer(
            @RequestParam("question") String question
    ) {
        return ResponseEntity.ok(service.searchForAnswer(question));
    }

    @GetMapping("/title")
    public ResponseEntity<List<PediaTitle>> listTitle() {
        return ResponseEntity.ok(pediaTitleService.listPediaTitle());
    }

    @PostMapping("/title")
    public ResponseEntity<PediaTitle> saveTitle(
            @RequestBody PediaTitle pediaTitle
    ) {
        PediaTitle savedPediaTitle = pediaTitleService.savePediaTitle(pediaTitle);
        return ResponseEntity.ok(savedPediaTitle);
    }

}
