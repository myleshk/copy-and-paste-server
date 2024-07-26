package hk.myles.CopyAndPaste.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Getter
public class OfferAnswerData {

    @NonNull
    private String sdp;

    @NonNull
    private EventType type;
}
