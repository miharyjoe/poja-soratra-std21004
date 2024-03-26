package hei.school.soratra.service;

import hei.school.soratra.dto.RestSoratra;
import hei.school.soratra.file.BucketComponent;
import hei.school.soratra.repository.model.Soratra;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

@Service
@AllArgsConstructor
public class SoratraFileService {
  BucketComponent bucketComponent;
  SoratraService  soratraService;

  public File processAndSaveTextToFile(String id, String soratraText) {
    if (StringUtils.isEmpty(soratraText)) {
      throw new IllegalArgumentException("Soratra text cannot be empty");
    }

    String transformedText = soratraText.toUpperCase();

    File outputFile = saveTextsToFile(id, soratraText, transformedText);

    Soratra soratra = new Soratra(id, soratraText, transformedText);
    soratraService.save(soratra);

    return outputFile;
  }

  private File saveTextsToFile(String id, String originalText, String transformedText) {
    String filename = id + ".txt";
    try {
      File file = File.createTempFile(filename, null);
      try (FileOutputStream fos = new FileOutputStream(file)) {
        fos.write(("Original Text:\n" + originalText + "\n\nTransformed Text:\n" + transformedText).getBytes());
      }
      return file;
    } catch (IOException e) {
      throw new RuntimeException("Failed to save text to file", e);
    }
  }

  public RestSoratra getSoratraUrlById(String id) {
    Soratra soratra = soratraService.getSoratraById(id);
    if (soratra == null) {
      return null;
    }

    RestSoratra restSoratra = new RestSoratra();
    restSoratra.setOriginal_url(getPresignedUrl(soratra.getOriginalBucketKey()));
    restSoratra.setTransformed_url(getPresignedUrl(soratra.getModifiedBucketKey()));
    return restSoratra;
  }

  private String getPresignedUrl(String bucketKey) {
    return bucketComponent.presign(bucketKey, Duration.ofHours(12)).toString();
  }
}
