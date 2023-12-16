package sas.model.entity.auth;

import sas.model.entity.auth._enum.EOperationType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class Operation {
    @Enumerated(EnumType.STRING)
    private EOperationType operation;
    private String groupName;

    public Operation(EOperationType operation, String groupName) {
        this.operation = operation;
        this.groupName = this.toSentenceCase(groupName);
    }

    public void setGroupName(String groupName) {
        this.groupName = this.toSentenceCase(groupName);
    }

    private String toSentenceCase(String str) {
        StringBuilder result = new StringBuilder();
        String[] components = str.split("\\.");

        for (String component:
                components) {
            result.append(toSentenceCaseWord(component));
            result.append(".");
        }

        return result.substring(0, result.length() - 1);
    }

    private String toSentenceCaseWord(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
