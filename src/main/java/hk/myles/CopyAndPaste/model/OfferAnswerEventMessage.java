package hk.myles.CopyAndPaste.model;


import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Jacksonized
@Getter
public class OfferAnswerEventMessage extends EventMessage {

    @NonNull
    private OfferAnswerData data;

}
