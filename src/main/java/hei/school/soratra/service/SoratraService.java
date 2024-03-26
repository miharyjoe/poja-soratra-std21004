package hei.school.soratra.service;

import hei.school.soratra.repository.SoratraRepository;
import hei.school.soratra.repository.model.Soratra;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SoratraService {
  private final SoratraRepository repository;

  @Transactional
  public Soratra save(Soratra soratra) {
    return repository.save(soratra);
  }

  public Soratra getSoratraById(String id) {
    return repository.findById(id).orElse(null);
  }
}
