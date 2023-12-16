package sas.model.entity.assess;

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
