package hk.myles.CopyAndPaste.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@SuperBuilder
@Getter
public class CandidateEventMessage extends EventMessage {

    @NonNull
    private CandidateData data;

}
