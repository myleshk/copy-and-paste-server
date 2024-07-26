package hk.myles.CopyAndPaste.model;


import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
public class UnknownEventMessage extends EventMessage {
    @NonNull
    private Object data;
}
