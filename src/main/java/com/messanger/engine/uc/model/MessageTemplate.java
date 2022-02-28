package com.messanger.engine.uc.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplate {
    private String templateId;
    private String type;
    private String title;
    private String messageBody;
    private String templateName;
    private String useYn;
}
