package hei.school.soratra.endpoint.rest.controller;

import hei.school.soratra.dto.RestSoratra;
import hei.school.soratra.repository.model.Soratra;
import hei.school.soratra.service.SoratraFileService;
import hei.school.soratra.service.SoratraService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@AllArgsConstructor
public class SoratraController {
  private final SoratraService soratraService;
  private final SoratraFileService service;
  @PutMapping("/soratra/{id}")
  public ResponseEntity<String> uploadSoratra(@PathVariable(name = "id") String id,
                                            @RequestBody String soratraText) {
    File outputFile = service.processAndSaveTextToFile(id, soratraText);
    if (outputFile == null) {
      return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("");
    }
    return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("OK");
  }

  @GetMapping("/soratra/{id}")
  public ResponseEntity<RestSoratra> getSoratra(@PathVariable(name = "id") String id) {
    RestSoratra restSoratra = service.getSoratraUrlById(id);
    if (restSoratra == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(restSoratra);
  }
}
