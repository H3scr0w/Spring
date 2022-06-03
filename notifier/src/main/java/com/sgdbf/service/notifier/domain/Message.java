package com.sgdbf.service.notifier.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

/**
 * The type Message.
 */
@ToString
public final class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String type;

    private final List<String> recipients = new ArrayList<>();

    @NotNull
    private final Object body;

    /**
     * Instantiates a new Message.
     *
     * @param type       the type
     * @param recipients the recipients
     * @param body       the body
     */
    public Message(@JsonProperty("type") String type,
                   @JsonProperty("recipients") List<String> recipients,
                   @JsonProperty("body") Object body) {
        this.type = type;
        this.recipients.addAll(recipients ==  null ? emptyList() : recipients);
        this.body = body;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets recipients.
     *
     * @return the recipients
     */
    public List<String> getRecipients() {
        return new ArrayList(recipients);
    }

    /**
     * Gets body.
     *
     * @return the body
     */
    public Object getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return Objects.equals(type, message.type) &&
                Objects.equals(recipients, message.recipients) &&
                Objects.equals(body, message.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, recipients, body);
    }

}
