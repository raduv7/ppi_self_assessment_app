package sas.model.entity.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {
    private String inputFilePath;
    private String outputFilePath;
}
