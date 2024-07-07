package hk.myles.CopyAndPaste.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Table(name = "AppUser")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @NonNull
    private String id;

    private String name;

    private Date lastSeen;
}
