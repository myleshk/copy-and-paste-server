package hk.myles.CopyAndPaste.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class Message {
    @NonNull
    private UUID id;

    @NonNull
    private String fromId;

    @NonNull
    private String toId;

    @NonNull
    private Date createdAt;

    @NonNull
    private String body;

}
