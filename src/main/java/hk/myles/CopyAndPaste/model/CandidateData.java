package hk.myles.CopyAndPaste.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@SuperBuilder
@Getter
public class CandidateData {
    @NonNull
    private String candidate;

    private Integer sdpMLineIndex;

    private String sdpMid;

    private String usernameFragment;
}