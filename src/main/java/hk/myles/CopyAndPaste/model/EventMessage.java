package hk.myles.CopyAndPaste.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
public class EventMessage {

    @NonNull
    private EventType event;

}
